package com.maqautocognita.graphics;

import com.maqautocognita.Config;
import com.maqautocognita.listener.ICameraMoveListener;
import com.maqautocognita.utils.CollectionUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CustomCamera extends OrthographicCamera {
    private static final float PAN_CAMERA_MOVE_RATIO = 0.4f;
    private float originalXPosition;
    private float originalYPosition;
    private float worldWidth;
    /**
     * the list of camera which the x position will be update if the x position of this camera is updated.
     */
    private List<CustomCamera> relationCameraList;

    /**
     * when the speed of the camera when the focus target is target is moving, it is normally 1, but in order to apply the "parallax effect" for different stage, the main stage will be move the camera in speed 1,
     * but in other layer, such as sky stage, the moving speed of camera will be slower
     */
    private float movingSpeed;

    private Actor focusTarget;

    private float previousPositionX;

    /**
     * indicate if the camera is locked , if yes, the camera will only focus on the current target{@link #focusTarget}, no other target will be focus even the user tap on other object
     */
    private boolean locked;

    private ICameraMoveListener cameraMoveListener;

    public CustomCamera() {
        this(1);
    }


    public CustomCamera(float movingSpeed) {
        this.movingSpeed = movingSpeed;
    }


    public float getOriginalXPosition() {
        return originalXPosition;
    }


    public void setOriginalPosition(float originalXPosition, float originalYPosition) {
        this.originalXPosition = originalXPosition;
        this.originalYPosition = originalYPosition;
    }

    public float getWorldWidth() {
        return worldWidth;
    }

    public void setWorldWidth(float worldWidth) {
        this.worldWidth = worldWidth;
    }

    public void move(float moveX, float moveY) {

        moveY = moveY * PAN_CAMERA_MOVE_RATIO;
        moveX = moveX * PAN_CAMERA_MOVE_RATIO;

        if (isAllowCameraMove(moveY)) {
            translateCameraXYPosition(moveX, moveY);

        } else {
            //move the camera x position only
            translateCameraXPosition(moveX);
        }
    }

    private boolean isAllowCameraMove(float moveY) {
        if (moveY < 0) {
            //if camera can see the bottom point of the background
            if (frustum.pointInFrustum(position.x, 0, 0)) {
                return false;
            }
        } else {
            if (
                //if the camera can see the top point of the background
                    frustum.pointInFrustum(position.x, Config.TABLET_SCREEN_HEIGHT, 0)
                    ) {
                return false;
            }
        }

        return true;
    }

    public void translateCameraXYPosition(float cameraXPosition, float cameraYPosition) {
        translateCameraXYPositionWithoutUpdateOtherCamera(cameraXPosition, cameraYPosition);

        if (CollectionUtils.isNotEmpty(relationCameraList)) {
            for (CustomCamera camera : relationCameraList) {
                camera.translateCameraXYPositionWithoutUpdateOtherCamera(cameraXPosition, cameraYPosition);
            }
        }
    }

    public void translateCameraXPosition(float cameraXPosition) {
        translateCameraXPositionWithoutUpdateOtherCamera(cameraXPosition);

        if (CollectionUtils.isNotEmpty(relationCameraList)) {
            for (CustomCamera camera : relationCameraList) {
                camera.translateCameraXPositionWithoutUpdateOtherCamera(cameraXPosition);
            }
        }
    }

    public void translateCameraXYPositionWithoutUpdateOtherCamera(float cameraXPosition, float cameraYPosition) {
        previousPositionX = position.x;
        position.add(cameraXPosition * movingSpeed, getYPositionToBeMove(cameraYPosition), 0);
        update();
    }

    public void translateCameraXPositionWithoutUpdateOtherCamera(float cameraXPosition) {
        previousPositionX = position.x;
        position.add(cameraXPosition * movingSpeed, 0, 0);
        update();

        if (null != cameraMoveListener) {
            cameraMoveListener.onMove(this);
        }
    }

    public float getYPositionToBeMove(float yPosition) {
        return yPosition / movingSpeed;
    }

    public void setCameraMoveListener(ICameraMoveListener cameraMoveListener) {
        this.cameraMoveListener = cameraMoveListener;
    }

    public void restoreYPosition() {
        restoreYPositionWithoutUpdateOtherCamera();
        if (CollectionUtils.isNotEmpty(relationCameraList)) {
            for (CustomCamera camera : relationCameraList) {
                camera.restoreYPositionWithoutUpdateOtherCamera();
            }
        }
    }

    private void restoreYPositionWithoutUpdateOtherCamera() {
        position.y = getOriginalYPosition();
    }

    public float getOriginalYPosition() {
        return originalYPosition;
    }

    /**
     * Check if the given object is inside the current camera view
     *
     * @param objectStartXPosition
     * @return
     */
    public boolean isObjectInsideCamera(float objectStartXPosition, float objectWidth) {
        //the start x position of the camera in the screen
        float cameraStartPositionX = position.x - viewportWidth / 2;
        //the end x position of the camera in the screen
        float cameraEndPositionX = cameraStartPositionX + viewportWidth;
        float objectEndXPosition = objectStartXPosition + objectWidth;

        return
                //check if the area of the given object is overlapped in the camera
                (cameraStartPositionX <= objectEndXPosition && cameraEndPositionX >= objectStartXPosition);
    }

    public void updateCameraXPosition(float cameraXPosition) {
        updateCameraXPositionWithoutUpdateOtherCamera(cameraXPosition);

        if (CollectionUtils.isNotEmpty(relationCameraList)) {
            for (CustomCamera camera : relationCameraList) {
                camera.updateCameraXPositionWithoutUpdateOtherCamera(cameraXPosition);
            }
        }
    }

    public void updateCameraXPositionWithoutUpdateOtherCamera(float cameraXPosition) {

        if (position.x != cameraXPosition) {
            previousPositionX = position.x;
            position.x = cameraXPosition * movingSpeed;
            update();
        }
    }

    public boolean isMovingRight() {
        return position.x > previousPositionX;
    }

    public boolean isMovingLeft() {
        return position.x < previousPositionX;
    }

    /**
     * add the camera, so that if the position is updated, it will also ask the given camera to update their position
     *
     * @param customCamera
     */
    public void addCamera(CustomCamera customCamera) {
        if (null != customCamera) {

            if (null == relationCameraList) {
                relationCameraList = new ArrayList<CustomCamera>();
            }

            if (!relationCameraList.contains(customCamera)) {

                relationCameraList.add(customCamera);

                customCamera.addCamera(this);

                //make sure other camera also has add the given camera, build a network
                for (CustomCamera camera : relationCameraList) {
                    camera.addCamera(customCamera);
                }


            }
        }
    }

    public boolean isFocusTarget(Actor actor) {
        return null != focusTarget && focusTarget.equals(actor);
    }

    public void setFocusTarget(Actor focusTarget) {
        setFocusTargetWithoutUpdateOtherCamera(focusTarget);
        if (CollectionUtils.isNotEmpty(relationCameraList)) {
            for (CustomCamera camera : relationCameraList) {
                camera.setFocusTargetWithoutUpdateOtherCamera(focusTarget);
            }
        }
    }

    public void setFocusTargetWithoutUpdateOtherCamera(Actor focusTarget) {
        this.focusTarget = focusTarget;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        setLockedWithoutUpdateOtherCamera(locked);
        if (CollectionUtils.isNotEmpty(relationCameraList)) {
            for (CustomCamera camera : relationCameraList) {
                camera.setLockedWithoutUpdateOtherCamera(locked);
            }
        }
    }

    private void setLockedWithoutUpdateOtherCamera(boolean locked) {
        this.locked = locked;
    }
}
