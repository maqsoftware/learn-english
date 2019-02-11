package com.maqautocognita.scene2d.actions;

import com.maqautocognita.Config;
import com.badlogic.gdx.Gdx;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class JumpAction extends AbstractAction {

    //The time in second of animation will be do
    private static final float DURATION_ANIMATION_IN_SECOND = 2f;
    /**
     * the maximum jump distance(the width) of the object, the unit will be depends on the {@link Config#TABLET_SCREEN_WIDTH}
     */
    private static final float MAXIMUM_JUMP_DISTANCE = 300;
    /**
     * the maximum jump (the height) of the object, the unit will be depends on the {@link Config#TABLET_SCREEN_HEIGHT}
     */
    private static final float MAXIMUM_JUMP_HEIGHT = 200;
    //store the accumulate second which the animation is did
    private float accumulateSecond;
    /**
     * a flag to indicate if the maximum jump height is reach {@link #MAXIMUM_JUMP_HEIGHT}
     * if reached,the object will be drop
     */
    private boolean isJumpMaximumReach;

    /**
     * the accumulation value of the jump height , store the height which current object is jumped.
     */
    private float accumulateJumpHeight;

    private float originalYPosition = -1;

    @Override
    public boolean doAct(float delta) {

        if (originalYPosition == -1) {
            originalYPosition = target.getY();
        }

        Gdx.app.log(getClass().getName(), "jumping height = " + (MAXIMUM_JUMP_HEIGHT * 2 * delta) + " delta = " + delta);

        float jump = MAXIMUM_JUMP_HEIGHT * delta;

        accumulateJumpHeight += jump;

        if (accumulateJumpHeight > MAXIMUM_JUMP_HEIGHT) {
            isJumpMaximumReach = true;
        }

        float y = target.getY() + (isJumpMaximumReach ? -jump : jump);

        target.setPosition(target.getX() + MAXIMUM_JUMP_DISTANCE * delta, y);
        if (accumulateSecond > DURATION_ANIMATION_IN_SECOND) {
            //make sure the y position is same as the original
            target.setY(originalYPosition);
            return true;
        } else {
            accumulateSecond += delta;
        }

        return false;

    }
}
