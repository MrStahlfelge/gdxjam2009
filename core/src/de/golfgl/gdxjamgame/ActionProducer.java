package de.golfgl.gdxjamgame;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ActionProducer {
    boolean addSwingActions(int level, Rotator upperRotator, Rotator lowerRotater) {
        switch (level) {
            case 0:
                setSwingAroundAction(upperRotator, 45, 1f);
                break;
            case 1:
                setSwingAroundAction(upperRotator, 30, .7f);
                break;
            case 2:
                setSwingAroundAction(upperRotator, 70, 1.3f);
                break;
            default:
                return false;
        }
        return true;
    }

    private void setSwingAroundAction(Image actor, int maxDegrees, float rockAroundTime) {
        actor.addAction(Actions.sequence(Actions.rotateTo(maxDegrees, .2f),
                Actions.forever(Actions.sequence(
                        Actions.rotateBy(-2f * maxDegrees, rockAroundTime / 2f, Interpolation.fade),
                        Actions.rotateBy(2 * maxDegrees, rockAroundTime / 2f, Interpolation.fade)))));
    }

    public void addMainMenuSwingActions(Rotator firstRotator, Rotator secondRotator) {
        setSwingAroundAction(firstRotator, 20, 1f);
        setSwingAroundAction(secondRotator, 20, 1f);
    }

    public String getLevelMotivationText(int currentLevel) {
        switch (currentLevel) {
            case 1:
                return "Too easy?\nLet's see if you can do this, too!";
            default:
                return null;
        }
    }
}
