package com.maqautocognita.scene2d.actions;

import com.maqautocognita.graphics.CustomCamera;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CameraMoveYAction extends AbstractAction {

    //The time in second of animation will be do
    private static final float DURATION_ANIMATION_IN_SECOND = 1f;
    private final float destinationYPosition;
    private final CustomCamera camera;
    //store the accumulate second which the animation is did
    private float accumulateSecond;

    private float originalCameraYPosition;

    public CameraMoveYAction(float destinationYPosition, CustomCamera camera) {
        this.destinationYPosition = destinationYPosition;
        this.camera = camera;
        originalCameraYPosition = camera.position.y;
    }

    @Override
    public boolean doAct(float delta) {
        if (accumulateSecond > DURATION_ANIMATION_IN_SECOND) {
            return true;
        } else {
            accumulateSecond += delta;
            camera.translate(0, (destinationYPosition - originalCameraYPosition) * delta / DURATION_ANIMATION_IN_SECOND);
        }

        return false;
    }
}
