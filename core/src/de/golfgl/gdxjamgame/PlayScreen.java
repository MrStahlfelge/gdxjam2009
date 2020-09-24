package de.golfgl.gdxjamgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.IntArray;

public class PlayScreen extends Table {
    private final GdxJamGame game;
    private final Rotator firstRotator;
    private final Rotator secondRotator;
    private final ProgressBar progressBar;
    private final Label level;
    private final Label scoreLabel;
    private final IntArray levelScores = new IntArray();
    private final ActionProducer actionProducer;
    private float timePassed;
    private int currentLevel;
    private boolean inputDone;
    private int score;
    boolean isPaused;

    public PlayScreen(GdxJamGame game) {
        actionProducer = new ActionProducer();

        setFillParent(true);
        pad(20);
        setBackground(game.white);

        Table hud = new Table(game.skin);
        level = new Label("", game.skin);
        hud.add(level).expandX().left();
        scoreLabel = new Label("", game.skin);
        hud.add(scoreLabel).expandX().right();

        add(hud).expandX().fillX();

        firstRotator = new Rotator(game);
        secondRotator = new Rotator(game);

        row();
        add(firstRotator).expand();
        row();
        add(secondRotator).expand();

        row();
        progressBar = new ProgressBar(0, getTimeAvail(), .05f, false, game.skin);
        add(progressBar).expandX().fillX().height(20);

        this.game = game;

        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.SPACE:
                    case Input.Keys.CENTER:
                    case Input.Keys.ENTER:
                        inputDone();
                        return true;
                }

                return super.keyDown(event, keycode);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                inputDone();
                return true;
            }
        });

        setTouchable(Touchable.enabled);

        prepareNextLevel();
    }

    private void prepareNextLevel() {

        boolean nextLevel = actionProducer.addSwingActions(currentLevel, firstRotator, secondRotator);

        if (!nextLevel)
            setGameOver();
        else {

            level.setText("Level " + (1 + currentLevel));
            inputDone = false;
            clearActions();
            String motivationText = actionProducer.getLevelMotivationText(currentLevel);

            if (motivationText != null) {
                timePassed = -2f;
                final Label motivationLabel = new Label(motivationText, game.skin) {
                    @Override
                    public void draw(Batch batch, float parentAlpha) {
                        super.draw(batch, parentAlpha);
                    }
                };
                // dirty hack to get this to work on the very first level
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.stage.addActor(motivationLabel);
                        motivationLabel.setColor(1, 1, 1, 0);
                        motivationLabel.addAction(Actions.sequence(Actions.fadeIn(.5f, Interpolation.fade),
                                Actions.delay(1.5f), Actions.fadeOut(.3f, Interpolation.fade),
                                Actions.removeActor()));
                    }
                });
                motivationLabel.setAlignment(Align.center);
                motivationLabel.setWrap(true);
                motivationLabel.setWidth(game.stage.getWidth() - 40);
                motivationLabel.setHeight(motivationLabel.getPrefHeight());
                motivationLabel.setPosition(game.stage.getWidth() / 2,
                        game.stage.getHeight() / 2, Align.center);
                scoreLabel.addAction(fadeOutAndInAgainAction());
                level.addAction(fadeOutAndInAgainAction());
                firstRotator.addAction(fadeOutAndInAgainAction());
                secondRotator.addAction(fadeOutAndInAgainAction());
            } else {
                timePassed = 0;
            }

            addAction(Actions.sequence(Actions.color(new Color(0, 0, 0, 1f), timeBeforeStart()),
                    Actions.delay(getTimeAvail() / 2f),
                    Actions.color(Color.RED, getTimeAvail() / 2f)));
        }
    }

    private SequenceAction fadeOutAndInAgainAction() {
        return Actions.sequence(Actions.fadeOut(0f),
                Actions.delay(2f), Actions.fadeIn(.3f, Interpolation.fade));
    }

    private float timeBeforeStart() {
        return 2f;
    }

    private float getTimeAvail() {
        return 7.5f;
    }

    private int getMinScoreNeeded() {
        return 70;
    }

    private void inputDone() {
        if (!inputDone && timePassed > timeBeforeStart()) {
            inputDone = true;
            firstRotator.clearActions();
            secondRotator.clearActions();
            clearActions();
            updateScoreLabel();
            if (score < getMinScoreNeeded())
                setGameOver();
            else {
                levelScores.add(score);
                currentLevel++;
                addAction(Actions.sequence(Actions.color(Color.FOREST, .5f, Interpolation.fade),
                        Actions.delay(1f), Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                prepareNextLevel();
                            }
                        })));
            }
        }
    }

    @Override
    public void act(float delta) {
        if (!isPaused) {
            super.act(delta);

            if (!inputDone) {
                timePassed += delta;
                progressBar.setValue(timePassed - timeBeforeStart());
                if (timePassed >= timeBeforeStart() + getTimeAvail()) {
                    setGameOver();
                }
                updateScoreLabel();
            }
        }
    }

    public void setGameOver() {
        clearActions();
        inputDone = true;
        firstRotator.clearActions();
        secondRotator.clearActions();
        addAction(Actions.sequence(Actions.fadeOut(2f, Interpolation.fade),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        // go to leader board
                        MainMenuScreen mainMenuScreen = new MainMenuScreen(game);
                        mainMenuScreen.getColor().a = 0;
                        mainMenuScreen.addAction(Actions.fadeIn(1f, Interpolation.fade));
                        game.stage.addActor(mainMenuScreen);
                    }
                }), Actions.removeActor()));
    }

    private void updateScoreLabel() {
        updateScore();
        scoreLabel.setText(score);
        Color color = new Color(score < getMinScoreNeeded() ? Color.RED : Color.WHITE);
        color.a = scoreLabel.getColor().a;
        scoreLabel.setColor(color);
    }

    private void updateScore() {
        float rotation1 = normalizeRotation(firstRotator.getRotation());
        float rotation2 = normalizeRotation(secondRotator.getRotation());

        float absDegree = Math.abs(rotation1 - rotation2);
        // it is now from 0 to 180, with 90 being the worst
        // get it from -90 to 90 with 0 being the worst
        absDegree -= 90;
        absDegree = Math.abs(absDegree);
        score = (int) ((absDegree) * (10 / 9f));
    }

    private float normalizeRotation(float rotation) {
        while (rotation < 0) {
            rotation += 180;
        }
        while (rotation >= 180) {
            rotation -= 180;
        }
        return rotation;
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage != null) {
            stage.setKeyboardFocus(this);
        }
    }
}
