package com.maqautocognita.scene2d.actions;

import com.maqautocognita.graphics.CustomCamera;
import com.badlogic.gdx.Gdx;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ZoomAction extends AbstractAction {


    //The time in second of animation will be do
    private static final float DURATION_ANIMATION_IN_SECOND = 2f;
    private final CustomCamera customCamera;
    private float targetZoomSize = 0.5f;
    //store the accumulate second which the animation is did
    private float accumulateSecond;
    private float originalZoom = -1;

    public ZoomAction(CustomCamera customCamera) {
        this.customCamera = customCamera;
    }

    public ZoomAction(CustomCamera customCamera, float targetZoomSize) {
        this(customCamera);
        this.targetZoomSize = targetZoomSize;
    }


    @Override
    public boolean doAct(float delta) {

        if (originalZoom == -1) {
            originalZoom = customCamera.zoom;
        }

        if (accumulateSecond > DURATION_ANIMATION_IN_SECOND) {
            Gdx.app.log("", "zoom = " + customCamera.zoom + "");
            return true;
        } else {
            accumulateSecond += delta;
            float zoom = customCamera.zoom - (targetZoomSize * delta / DURATION_ANIMATION_IN_SECOND);

            if (zoom <= 1) {
                customCamera.zoom = zoom;
            }
        }

        return false;
    }
}
