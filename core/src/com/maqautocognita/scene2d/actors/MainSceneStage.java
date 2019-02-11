package com.maqautocognita.scene2d.actors;

import com.maqautocognita.graphics.CustomCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class MainSceneStage extends Stage {

    protected static final int MOVE_DISTANCE_PER_SECOND = 100;
    //The time for deceleration,when the time reach, speed will be minus or add 1 depends on the moving direction
    private static final float DURATION_FOR_DECELERATE_SPEED = 1f;
    private static final int MAXIMUM_SPEED = 15;
    private final CustomCamera mainCamera;
    //store the second which to indicate if the {@link #DURATION_FOR_DECELERATE_SPEED} is reach
    private float accumulateSecond;
    /**
     * The speed of the moving container, if the speed is less than 0, which mean the container is moving to the left, else which mean moving to the right
     */
    private int speed;

    public MainSceneStage(Viewport viewport) {
        super(viewport);

        mainCamera = (CustomCamera) getCamera();

        addListener(new ActorGestureListener() {

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                if (//make sure the camera is not locked by other target
                        !mainCamera.isLocked() &&
                                //make sure there is a movement on x
                                deltaX != 0) {
                    //make sure there is no target focusing for the camera
                    mainCamera.setFocusTarget(null);

                    if (deltaX > 0) {
                        //move left
                        speed = Math.max(-MAXIMUM_SPEED, speed - 1);
                    } else {
                        //move right
                        speed = Math.min(MAXIMUM_SPEED, speed + 1);
                    }

                    accumulateSecond = 0;
                }

            }

        });
    }

    public void draw() {
        super.draw();
        if (0 != speed) {
            final CustomCamera mainCamera = (CustomCamera) getCamera();
            float increaseDistance = Gdx.graphics.getDeltaTime() * MOVE_DISTANCE_PER_SECOND * speed;
            mainCamera.translateCameraXPosition(increaseDistance);
            accumulateSecond += Gdx.graphics.getDeltaTime();
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
        }

    }

}
