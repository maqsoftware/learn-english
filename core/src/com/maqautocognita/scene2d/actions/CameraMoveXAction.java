package com.maqautocognita.scene2d.actions;

import com.maqautocognita.graphics.CustomCamera;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CameraMoveXAction extends AbstractAction {

    private final float moveXPosition;
    private final CustomCamera camera;
    //The time in second of animation will be do
    private float durationAnimationInSecond = 2f;
    //store the accumulate second which the animation is did
    private float accumulateSecond;

    private float originalCameraX;

    private boolean isMoveStartFromCameraXPosition;

    public CameraMoveXAction(float moveXPosition, CustomCamera camera) {
        this.moveXPosition = moveXPosition;
        this.camera = camera;
    }

    public CameraMoveXAction(float moveXPosition, CustomCamera camera, boolean isMoveStartFromCameraXPosition) {
        this.moveXPosition = moveXPosition;
        this.camera = camera;
        this.isMoveStartFromCameraXPosition = isMoveStartFromCameraXPosition;
        if (isMoveStartFromCameraXPosition) {
            originalCameraX = camera.position.x;
        }
    }

    public CameraMoveXAction(float moveXPosition, CustomCamera camera, float durationAnimationInSecond, IActionListener actionListener) {
        this(moveXPosition, camera, durationAnimationInSecond);
        setActionListener(actionListener);

    }

    public CameraMoveXAction(float moveXPosition, CustomCamera camera, float durationAnimationInSecond) {
        this.moveXPosition = moveXPosition;
        this.camera = camera;
        this.durationAnimationInSecond = durationAnimationInSecond;
        originalCameraX = camera.position.x;
    }

    @Override
    public boolean doAct(float delta) {
        if (accumulateSecond > durationAnimationInSecond) {
            //make sure the camera is exactly in the expected distance
            camera.position.x = moveXPosition + originalCameraX;
            return true;
        } else {
            if (null != getTarget()) {
                accumulateSecond += delta;
                float fromXPosition = getTarget().getX();
                if (isMoveStartFromCameraXPosition) {
                    fromXPosition = originalCameraX;
                }
                camera.translateCameraXPosition((moveXPosition - fromXPosition) * delta / durationAnimationInSecond);
            }
        }

        return false;
    }
}
