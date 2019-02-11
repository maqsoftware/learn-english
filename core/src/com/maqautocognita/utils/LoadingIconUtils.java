package com.maqautocognita.utils;

import com.maqautocognita.Config;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class LoadingIconUtils {

    public static Table getLoadingIcon() {
        Table table = new Table();

        Stack stack = new Stack();
        Image leftIcon = new Image(AssetManagerUtils.getTextureWithWait(Config.COMMON_IMAGE_XDPI_PATH + "wait_left.png"));
        leftIcon.setScaling(Scaling.fit);
        Image rightIcon = new Image(AssetManagerUtils.getTextureWithWait(Config.COMMON_IMAGE_XDPI_PATH + "wait_right.png"));
        rightIcon.setScaling(Scaling.fit);

        leftIcon.addAction(getFlashAction(false));
        stack.add(leftIcon);

        rightIcon.addAction(getFlashAction(true));
        stack.add(rightIcon);

        table.add(stack);
        return table;
    }

    private static Action getFlashAction(boolean isRequiredDelayAtBeginning) {
        SequenceAction sequenceAction = new SequenceAction();

        if (isRequiredDelayAtBeginning) {
            DelayAction delayAction = new DelayAction();
            delayAction.setDuration(0.5f);
            sequenceAction.addAction(delayAction);
        }

        VisibleAction visibleAction = new VisibleAction();
        visibleAction.setVisible(true);
        sequenceAction.addAction(visibleAction);

        DelayAction delayAction = new DelayAction();
        delayAction.setDuration(0.5f);
        sequenceAction.addAction(delayAction);

        VisibleAction invisibleAction = new VisibleAction();
        invisibleAction.setVisible(false);
        sequenceAction.addAction(invisibleAction);

        if (!isRequiredDelayAtBeginning) {
            delayAction = new DelayAction();
            delayAction.setDuration(0.5f);
            sequenceAction.addAction(delayAction);
        }

        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setAction(sequenceAction);
        repeatAction.setCount(RepeatAction.FOREVER);

        return repeatAction;
    }
}
