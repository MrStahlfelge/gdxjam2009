package de.golfgl.gdxjamgame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class PlayScreen extends Table {
    private final GdxJamGame game;
    private final Rotator firstRotator;
    private final Rotator secondRotator;
    private final ProgressBar progressBar;
    private float timePassed;
    private int currentLevel;
    private boolean inputDone;
    private final Label level;
    private final Label scoreLabel;
    private int score;

    public PlayScreen(GdxJamGame game) {
        setFillParent(true);
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
        ProgressBar.ProgressBarStyle pbs = new ProgressBar.ProgressBarStyle();
        pbs.knobBefore = game.white;
        progressBar = new ProgressBar(0, getTimeAvail(), .05f, false, pbs);
        add(progressBar).expandX().fillX().height(20);

        this.game = game;

        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.SPACE:
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
        level.setText("Level " + currentLevel);
        new ActionProducer().addSwingActions(currentLevel, firstRotator, secondRotator);
        timePassed = 0;
        inputDone = false;
        clearActions();
        addAction(Actions.color(new Color(0, 0, 0, 1f), timeBeforeStart()));
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
            updateScoreLabel();
            if (score < getMinScoreNeeded())
                setGameOver();
        }
    }

    @Override
    public void act(float delta) {
        if (!game.isPaused) {
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

    private void setGameOver() {
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
        scoreLabel.setColor(score < getMinScoreNeeded() ? Color.RED : Color.WHITE);
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
