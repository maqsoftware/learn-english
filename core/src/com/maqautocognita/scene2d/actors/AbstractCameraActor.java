package com.maqautocognita.scene2d.actors;

import com.maqautocognita.graphics.CustomCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractCameraActor extends Actor implements IStoryModeActor {

    private CustomCamera camera;

    protected boolean isInsideCamera() {
        if (null == getCamera()) {
            return false;
        }
        return getCamera().isObjectInsideCamera(getX(), getWidth());
    }

    public CustomCamera getCamera() {
        if (null == camera && null != getStage() && getStage().getCamera() instanceof CustomCamera) {
            camera = (CustomCamera) getStage().getCamera();
        }

        return camera;
    }


}
