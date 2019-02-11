package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ObjectActor extends Actor {

    public boolean isDroppedToContainer;
    protected CustomCamera camera;
    protected TextureRegion objectTextureRegion;
    protected float currentX;
    protected boolean isTurnLeft = true;
    private String imagePath;
    private float originalRatio;
    private Vector2 positionInContainer;

    public ObjectActor(String imagePath, float x, float y) {
        this.currentX = x;
        if (StringUtils.isNotBlank(imagePath)) {
            this.imagePath = Config.STORY_HDPI_IMAGE_FOLDER_NAME + "/" + imagePath + ".png";
            Texture containerTexture = new Texture(Gdx.files.internal(this.imagePath));
            this.objectTextureRegion = new TextureRegion(containerTexture, containerTexture.getWidth(), containerTexture.getHeight());
            setSize(objectTextureRegion.getRegionWidth(), objectTextureRegion.getRegionHeight());
            setPosition(x, y);
        }
        setScale(ScreenUtils.getSceneRatio());
        originalRatio = getScaleX();

        final Actor target = this;

        addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!camera.isLocked()) {
                    Gdx.app.log(getClass().getName(), "focus on " + getImagePath());
                    camera.setFocusTarget(target);
                }
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (count == 1) {
                    flipX();
                }

            }

        });
    }

    public String getImagePath() {
        return imagePath;
    }

    protected void flipX() {
        isTurnLeft = !isTurnLeft;
        objectTextureRegion.flip(true, false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        initCamera();

        if (camera.isObjectInsideCamera(getX(), getWidthAfterScale())) {
            drawActor(batch, getDrawTexture(), getX(), getY(), getWidthAfterScale(), getHeightAfterScale());
        }

        float cameraScreenEndPositionX = camera.position.x + camera.viewportWidth / 2;
        float cameraScreenStartPositionX = camera.position.x - camera.viewportWidth / 2;

        // if camera is viewing the right side of the original world
        if (cameraScreenEndPositionX - (cameraScreenEndPositionX % camera.getWorldWidth()) >= camera.getWorldWidth()) {
            int repeatedWorld = (int) (cameraScreenEndPositionX / camera.getWorldWidth());

            onCameraMoveRightAndOutOfTheWorld(repeatedWorld, batch);
        }
        //else if camera is viewing the left side of the original world
        else if (cameraScreenStartPositionX < 0) {
            int repeatedWorld = (int) (Math.abs(cameraScreenStartPositionX) / camera.getWorldWidth()) + 1;
            onCameraMoveLeftAndOutOfTheWorld(repeatedWorld, batch);
        }
    }

    protected void initCamera() {
        if (null == camera) {
            camera = (CustomCamera) getStage().getCamera();
        }
    }

    public float getWidthAfterScale() {
        return getWidth() * getScaleX();
    }

    protected void drawActor(Batch batch, TextureRegion textureRegion, float x, float y, float width, float height) {
        if (null != textureRegion) {
            batch.draw(textureRegion, x, y, width, height);
        }
    }

    protected TextureRegion getDrawTexture() {
        return objectTextureRegion;
    }

    public float getHeightAfterScale() {
        return getHeight() * getScaleX();
    }

    protected void onCameraMoveRightAndOutOfTheWorld(int numberOfWorldOut, Batch batch) {
        if (!camera.isObjectInsideCamera(getX(), getWidthAfterScale())) {
            float xPosition = numberOfWorldOut * camera.getWorldWidth() + currentX;
            if (getX() != xPosition) {
                setX(xPosition);
            }
        }
    }

    protected void onCameraMoveLeftAndOutOfTheWorld(int numberOfWorldOut, Batch batch) {
        if (!camera.isObjectInsideCamera(getX(), getWidthAfterScale())) {

            if (camera.position.x > currentX) {
                //if the camera is still viewing the images.actor original position, so update the position to original position
                numberOfWorldOut = 0;
            } else if (camera.isMovingRight()) {
                numberOfWorldOut = numberOfWorldOut - 1;
            }
            float xPosition = -numberOfWorldOut * camera.getWorldWidth() + currentX;
            if (getX() != xPosition) {
                Gdx.app.log(getClass().getName(), "image path = " + getImagePath() + " originalXPosition = " + currentX);
                setX(xPosition);
            }
        }
    }

    public void setCurrentX(float xPosition) {
        currentX = getOriginalXInActualWorld(xPosition);
    }

    protected float getOriginalXInActualWorld(float xPosition) {
        return camera.getWorldWidth() - Math.abs(xPosition % camera.getWorldWidth());
    }

    public void restoreToOriginalRatio() {
        setScale(originalRatio);
    }

    public void setPositionInContainer(float x, float y) {
        if (null == positionInContainer) {
            positionInContainer = new Vector2();
        }

        positionInContainer.x = x;
        positionInContainer.y = y;
    }

    public Vector2 getPositionInContainer() {
        return positionInContainer;
    }
}
