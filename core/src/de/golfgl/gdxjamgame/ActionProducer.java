package de.golfgl.gdxjamgame;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ActionProducer {
    void addSwingActions(int level, Rotator upperRotator, Rotator lowerRotater) {
        setSwingAroundAction(upperRotator, 45, 1f);
    }

    private void setSwingAroundAction(Image actor, int maxDegrees, float rockAroundTime) {
        actor.setRotation(maxDegrees);
        actor.addAction(Actions.forever(Actions.sequence(
                Actions.rotateBy(-2f * maxDegrees, rockAroundTime / 2f, Interpolation.fade),
                Actions.rotateBy(2 * maxDegrees, rockAroundTime / 2f, Interpolation.fade))));
    }

    public void addMainMenuSwingActions(Rotator firstRotator, Rotator secondRotator) {
        setSwingAroundAction(firstRotator, 20, 1f);
        setSwingAroundAction(secondRotator, 20, 1f);
    }
}
