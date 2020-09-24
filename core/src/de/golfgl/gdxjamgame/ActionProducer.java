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
                setSwingAroundAction(upperRotator, 70, 1.3f);
                break;
            case 2:
                setSwingAroundAction(upperRotator, 60, 1f);
                break;
            case 3:
                setSwingAroundAction(upperRotator, 30, .7f);
                break;
            case 4:
                setSwingAroundAction(upperRotator, 60, 1f);
                setTurnAroundAction(lowerRotater, 1f, 1f);
                break;
            case 5:
                setSwingAroundAction(upperRotator, 60, .8f);
                setTurnAroundAction(lowerRotater, 1f, .5f);
                break;
            case 6:
                setSwingAroundAction(upperRotator, 40, 1f);
                setSwingAroundAction(lowerRotater, -40, 1f);
                break;
            case 7:
                setTurnAroundAction(upperRotator, 1f, .7f);
                setTurnAroundAction(lowerRotater, 1f, .5f);
                break;
            case 8:
                setTurnAroundAction(upperRotator, 1f, false);
                setTurnAroundAction(lowerRotater, 1f, true);
                break;
            case 9:
                setTurnAroundAction(upperRotator, 1f, true);
                setSwingAroundAction(lowerRotater, 30, .7f);
                break;
            default:
                return false;
        }
        return true;
    }

    private void setTurnAroundAction(Rotator actor, float turnAroundTime, float waitTime) {
        actor.addAction(Actions.sequence(Actions.rotateTo(0, .2f),
                Actions.forever(Actions.sequence(Actions.delay(waitTime),
                        Actions.rotateBy(-360, turnAroundTime, Interpolation.fade)))));
    }

    private void setTurnAroundAction(Rotator actor, float turnAroundTime, boolean clockwise) {
        actor.addAction(Actions.sequence(Actions.rotateTo(0, .2f),
                Actions.forever(Actions.rotateBy(clockwise ? -360 : 360, turnAroundTime))));
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
            case 0:
                return "Remember, this is about PARALLEL worlds!\nLet's begin!";
            case 2:
                return "Too easy?\nLet's see if you can do this, too!";
            case 4:
                return "Okay, still to easy for you. We will spice this up now!";
            case 6:
                return "Congratulations, that one was hard!\nSit back and relax now...";
            case 9:
                return "Okay, you made it to the End boss!\n\nHave fun!";
            default:
                return null;
        }
    }
}
