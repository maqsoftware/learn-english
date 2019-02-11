package com.maqautocognita.utils;

import com.maqautocognita.scene2d.actors.ImageActor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class AnimationUtils {

    private static final int DEFAULT_REPEAT_TIMES = 3;

    private static final int HINT_TOUCH_SIZE = 200;

    public static void doFlash(Actor actor, int repeatTimes, Runnable completeListener) {

        doFlash(actor, false, 0.5f, repeatTimes, completeListener);
    }

    public static void doFlash(Actor actor, boolean isFadeOutFinal, float durationInSecond, int repeatTimes, Runnable completeListener) {

        if (null != actor) {
            AlphaAction actionFadeOut = new AlphaAction();
            actionFadeOut.setAlpha(0f);
            actionFadeOut.setDuration(durationInSecond);

            AlphaAction actionFadeIn = new AlphaAction();
            actionFadeIn.setAlpha(1f);
            actionFadeIn.setDuration(durationInSecond);

            RepeatAction repeatAction = new RepeatAction();
            SequenceAction sequenceAction;
            if (isFadeOutFinal) {
                sequenceAction = new SequenceAction(actionFadeIn, actionFadeOut);
            } else {
                sequenceAction = new SequenceAction(actionFadeOut, actionFadeIn);
            }
            repeatAction.setAction(sequenceAction);
            repeatAction.setCount(repeatTimes);

            SequenceAction finalSequenceAction = new SequenceAction(repeatAction, actionFadeIn);
            if (null != completeListener) {
                RunnableAction runnableAction = new RunnableAction();
                runnableAction.setRunnable(completeListener);
                finalSequenceAction.addAction(runnableAction);
            }

            actor.addAction(finalSequenceAction);
        }
    }

    public static void doFadeOut(Actor actor, float duration) {
        if (null != actor) {
            AlphaAction actionFadeIn = new AlphaAction();
            actionFadeIn.setAlpha(1f);
            actionFadeIn.setDuration(0);

            AlphaAction actionFadeOut = new AlphaAction();
            actionFadeOut.setAlpha(0f);
            actionFadeOut.setDuration(duration);

            SequenceAction sequenceAction = new SequenceAction(actionFadeIn, actionFadeOut);

            actor.addAction(sequenceAction);
        }
    }

    public static void doFlash(Actor actor) {

        doFlash(actor, false, 0.5f, DEFAULT_REPEAT_TIMES, null);
    }

    public static void doFlashAndFadeOut(Actor actor) {

        actor.setVisible(true);
        doFlash(actor, true, 0.5f, DEFAULT_REPEAT_TIMES, null);
    }

    public static void doObjectIndication(ImageActor indicateImage, Actor focusingActor) {

        indicateImage.setTouchable(Touchable.childrenOnly);

        SequenceAction sequenceAction = new SequenceAction();

        addScaleAction(sequenceAction, 0.25f);
        addScaleAction(sequenceAction, 0.5f);
        addScaleAction(sequenceAction, 0.75f);
        addScaleAction(sequenceAction, 1);

        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setAction(sequenceAction);
        repeatAction.setCount(3);

        float focusingActorXPositionInTheScene = focusingActor.getX();
        float focusingActorYPositionInTheScene = focusingActor.getY();

        //in order to make the indicate image in the center of the focusing object
        if (focusingActor.getWidth() > HINT_TOUCH_SIZE) {
            focusingActorXPositionInTheScene += (focusingActor.getWidth() - HINT_TOUCH_SIZE) / 2;
        } else {
            focusingActorXPositionInTheScene -= (HINT_TOUCH_SIZE - focusingActor.getWidth()) / 2;
        }

        if (focusingActor.getHeight() > HINT_TOUCH_SIZE) {
            focusingActorYPositionInTheScene += (focusingActor.getHeight() - HINT_TOUCH_SIZE) / 2;
        } else {
            focusingActorYPositionInTheScene -= (HINT_TOUCH_SIZE - focusingActor.getHeight()) / 2;
        }

        indicateImage.setPosition(focusingActorXPositionInTheScene, focusingActorYPositionInTheScene);

        indicateImage.setVisible(true);

        indicateImage.addAction(repeatAction);
    }

    private static void addScaleAction(SequenceAction sequenceAction, float scale) {
        ScaleToAction scaleToAction = new ScaleToAction();
        scaleToAction.setScale(scale);
        DelayAction delayAction = new DelayAction();
        delayAction.setDuration(0.3f);
        sequenceAction.addAction(scaleToAction);
        sequenceAction.addAction(delayAction);
    }


}
