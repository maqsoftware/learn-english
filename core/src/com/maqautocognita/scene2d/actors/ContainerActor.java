package com.maqautocognita.scene2d.actors;

import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ContainerActor extends ObjectActor {
    protected static final int MAXIMUM_SPEED = 20;
    protected static final int MOVE_DISTANCE_PER_SECOND = 300;
    //The time for deceleration,when the time reach, speed will be minus or add 1 depends on the moving direction
    private static final float DURATION_FOR_DECELERATE_SPEED = 1f;
    private final boolean isMoveAble;
    /**
     * The speed of the moving container, if the speed is less than 0, which mean the container is moving to the left, else which mean moving to the right
     */
    protected int speed;
    TextureRegion containerWithSomethingTextureRegion;
    /**
     * the area which is used to store the object
     */
    private Rectangle availableSpace;
    private List<ObjectActor> things;
    private float keepDistance;
    //store the second which to indicate if the {@link #DURATION_FOR_DECELERATE_SPEED} is reach
    private float accumulateSecond;

    public ContainerActor(String imagePath, String imagePathWithSomething, float x, float y, final boolean isMoveAble) {
        super(imagePath, x, y);
        this.isMoveAble = isMoveAble;

        Texture containerWithSomethingTexture = new Texture(Gdx.files.internal(imagePathWithSomething));
        containerWithSomethingTextureRegion =
                new TextureRegion(containerWithSomethingTexture, containerWithSomethingTexture.getWidth(), containerWithSomethingTexture.getHeight());

        addListener(new ActorGestureListener() {

            /**
             * store the touch down x position in the scene, it is mainly used to check if the user has drag the point and the drag direction by compare it to the touch up point
             */
            private float touchDownStartX;

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //convert the given touch point to the point located in the scene
                touchDownStartX = Math.abs(x) % camera.getWorldWidth();
                Gdx.app.log(this.getClass().getName(), "touchDownStartX =" + touchDownStartX);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                float touchUpStartX = Math.abs(x) % camera.getWorldWidth();

                Gdx.app.log(this.getClass().getName(), "touchUpStartX =" + touchDownStartX);

                if (touchDownStartX != touchUpStartX) {
                    if (touchDownStartX > touchUpStartX) {
                        //if the user fling to left hand side
                        speed = Math.max(-MAXIMUM_SPEED, speed - 1);
                    } else {
                        //if the user fling to right hand side
                        speed = Math.min(MAXIMUM_SPEED, speed + 1);
                    }

                    Gdx.app.log(this.getClass().getName(), "speed =" + speed);

                    accumulateSecond = 0;
                }
            }

        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        accumulateSecond += delta;
        if (0 != speed) {
            onMoving(delta);
        }
    }

    protected void onMoving(float delta) {
        float increaseDistance = delta * MOVE_DISTANCE_PER_SECOND * speed;

        setXPosition(getX() + increaseDistance);
        if (accumulateSecond > DURATION_FOR_DECELERATE_SPEED) {
            //decelerate the images.actor
            if (speed < 0) {
                speed++;
            } else {
                speed--;
            }

            //restart the time calculation for next decelerate
            accumulateSecond = 0;
        }
        //lock the camera to focus this container if it is moving
        camera.setLocked(speed != 0);
    }

    public void setXPosition(float x) {
        setX(x);
        if (CollectionUtils.isNotEmpty(things)) {
            for (ObjectActor thing : things) {
                thing.setX(x + thing.getPositionInContainer().x);
            }
        }
        setCurrentX(x);
    }

    protected void flipX() {
        super.flipX();
        containerWithSomethingTextureRegion.flip(true, false);

        if (CollectionUtils.isNotEmpty(things)) {
            for (ObjectActor thing : things) {
                thing.setPositionInContainer(getWidthAfterScale() - thing.getPositionInContainer().x - thing.getWidthAfterScale(), thing.getPositionInContainer().y);
                thing.setX(getX() + thing.getPositionInContainer().x);
            }
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {

        initCamera();

        if (isMoveAble && camera.isFocusTarget(this)) {
            if (0 == keepDistance) {
                keepDistance = getX() - camera.getOriginalXPosition();
            }

            if (camera.position.x == camera.getOriginalXPosition() && getX() > camera.getWorldWidth()) {
                setX(getX() - camera.getWorldWidth());
            } else if (camera.position.x != getX()) {
                float moveCameraX = 0;
                if (Math.abs(getX() - camera.position.x) != keepDistance) {
                    //make sure the camera is keep a constant distance with the images.actor
                    moveCameraX = getX();
                }
                if (0 != moveCameraX && moveCameraX != camera.position.x) {
                    camera.updateCameraXPosition(moveCameraX);
                }
            }
        }
        super.draw(batch, alpha);

    }

    @Override
    public void setCurrentX(float xPosition) {
        super.setCurrentX(xPosition);
        if (CollectionUtils.isNotEmpty(things)) {
            for (ObjectActor thing : things) {
                thing.currentX = currentX + thing.getPositionInContainer().x;
            }
        }
    }

    protected TextureRegion getDrawTexture() {
        return CollectionUtils.isNotEmpty(things) ? containerWithSomethingTextureRegion : objectTextureRegion;
    }

    protected void onCameraMoveRightAndOutOfTheWorld(int numberOfWorldOut, Batch batch) {
        if (!camera.isFocusTarget(this)) {
            super.onCameraMoveRightAndOutOfTheWorld(numberOfWorldOut, batch);
        }
    }

    protected void onCameraMoveLeftAndOutOfTheWorld(int numberOfWorldOut, Batch batch) {
        if (!camera.isFocusTarget(this)) {
            super.onCameraMoveLeftAndOutOfTheWorld(numberOfWorldOut, batch);
        }
    }

    public void addThing(ObjectActor thing) {
        if (null == things) {
            things = new ArrayList<ObjectActor>();
        }
        things.add(thing);
    }

    private void removeAllThing() {
        if (CollectionUtils.isNotEmpty(things)) {
            float xPositionForRemovedThing = getX();
            for (ObjectActor thing : things) {
                thing.isDroppedToContainer = false;
                thing.setPositionInContainer(0, 0);
                thing.restoreToOriginalRatio();
                thing.setPosition(xPositionForRemovedThing, getY());
                Gdx.app.log(getClass().getName(), "xPositionForRemovedThing = " + xPositionForRemovedThing);
                xPositionForRemovedThing += thing.getWidthAfterScale();
            }
            things.clear();
        }
    }

    public boolean isFlipped() {
        return !isTurnLeft;
    }

    public Rectangle getAvailableSpace() {
        return availableSpace;
    }

    public void setAvailableSpace(Rectangle availableSpace) {
        if (null != availableSpace) {
            availableSpace.setHeight(availableSpace.getHeight() * ScreenUtils.getSceneRatio());
            availableSpace.setWidth(availableSpace.getWidth() * ScreenUtils.getSceneRatio());
            availableSpace.setX(availableSpace.getX() * ScreenUtils.getSceneRatio());
            availableSpace.setY(availableSpace.getY() * ScreenUtils.getSceneRatio());
        }
        this.availableSpace = availableSpace;
    }
}
