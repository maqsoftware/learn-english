package com.maqautocognita.scene2d.actors;

import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.utils.ArrayUtils;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractGroup extends Group implements IStoryModeActor {

    private CustomCamera customCamera;
    private boolean isRequiredToCheckCameraBeforeDraw = true;

    /**
     * indicate if it is required to check if the object is inside the camera before render
     * <p/>
     * It is useful when the object rendering is depends on the parent
     *
     * @param isRequiredToCheckCameraBeforeDraw
     */
    public void setIsRequiredToCheckCameraBeforeDraw(boolean isRequiredToCheckCameraBeforeDraw) {
        this.isRequiredToCheckCameraBeforeDraw = isRequiredToCheckCameraBeforeDraw;
    }

    @Override
    public void dispose() {
        if (null != getChildren() && ArrayUtils.isNotEmpty(getChildren().items)) {
            for (int i = 0, n = getChildren().size; i < n; i++) {
                Actor actor = getChildren().items[i];
                if (actor instanceof IStoryModeActor) {
                    ((IStoryModeActor) actor).dispose();
                }

            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isRequiredToCheckCameraBeforeDraw && null == customCamera) {
            customCamera = (CustomCamera) getStage().getCamera();
        }
        if (!isRequiredToCheckCameraBeforeDraw || customCamera.isObjectInsideCamera(getX(), getWidth())) {
            super.draw(batch, parentAlpha);
        }
    }
}
