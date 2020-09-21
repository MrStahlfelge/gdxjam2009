package de.golfgl.gdxjamgame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class PlayScreen extends Table {
    private final GdxJamGame game;
    private final Rotator firstRotator;
    private final Rotator secondRotator;
    private final ProgressBar progressBar;
    private float timePassed;

    public PlayScreen(GdxJamGame game) {
        setFillParent(true);
        setBackground(game.white);

        firstRotator = new Rotator(game);
        secondRotator = new Rotator(game);

        add(firstRotator).expand();
        row();
        add(secondRotator).expand();

        row();
        ProgressBar.ProgressBarStyle pbs = new ProgressBar.ProgressBarStyle();
        pbs.knobBefore = game.white;
        progressBar = new ProgressBar(0, getTimeAvail(), .05f, false, pbs);
        add(progressBar).expandX().fillX().height(20);

        setSwingAroundAction(firstRotator,45, 1f);

        addAction(Actions.color(new Color(0, 0, 0, 1f), 2f));

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
    }

    private float getTimeAvail() {
        return 7.5f;
    }

    private void setSwingAroundAction(Image actor, int maxDegrees, float rockAroundTime) {
        actor.setRotation(maxDegrees);
        actor.addAction(Actions.forever(Actions.sequence(
                Actions.rotateBy(-2f * maxDegrees, rockAroundTime / 2f, Interpolation.fade),
                Actions.rotateBy(2 * maxDegrees, rockAroundTime / 2f, Interpolation.fade))));
    }

    private void inputDone() {
        firstRotator.clearActions();
        secondRotator.clearActions();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timePassed += delta;

        progressBar.setValue(timePassed);
        if (timePassed >= getTimeAvail()) {
            // do something
        }
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage != null) {
            stage.setKeyboardFocus(this);
        }
    }
}
