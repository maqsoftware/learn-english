package com.maqautocognita.scene2d.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class Car extends ContainerActor {

    private Sprite frontWheelTextureRegion;
    private Sprite rearWheelTextureRegion;

    private Vector2 frontWheelPositionInCar;
    private Vector2 rearWheelPositionInCar;

    public Car(String imagePath, String imagePathWithSomething, float x, float y, boolean isMoveable) {
        super(imagePath, imagePathWithSomething, x, y, isMoveable);
        Texture texture = new Texture(Gdx.files.internal("images.actor/Wheel_001.png"));
        frontWheelTextureRegion = new Sprite(texture);
        frontWheelTextureRegion.setSize(186 * getScaleX(), 186 * getScaleX());
        frontWheelPositionInCar = new Vector2(104 * getScaleX(), 0);

        rearWheelTextureRegion = new Sprite(texture);
        rearWheelTextureRegion.setSize(186 * getScaleX(), 186 * getScaleX());
        rearWheelPositionInCar = new Vector2(683 * getScaleX(), 0);

    }

    @Override
    protected void onMoving(float delta) {
        rotateWheel(frontWheelTextureRegion, delta);
        rotateWheel(rearWheelTextureRegion, delta);

        super.onMoving(delta);
    }

    private void rotateWheel(Sprite wheelSprite, float delta) {
        wheelSprite.setOriginCenter();

        float rotateAngle = delta * 360 / MAXIMUM_SPEED * speed * (float) Math.PI;

        wheelSprite.rotate(-rotateAngle);
    }

    @Override
    protected void flipX() {
        super.flipX();
        frontWheelTextureRegion.flip(true, false);
        rearWheelTextureRegion.flip(true, false);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        super.draw(batch, alpha);
        //draw wheel

        drawWheel(batch, frontWheelTextureRegion, frontWheelPositionInCar);
        drawWheel(batch, rearWheelTextureRegion, rearWheelPositionInCar);

    }

    private void drawWheel(Batch batch, Sprite wheelSprite, Vector2 wheelPositionInCar) {
        wheelSprite.setPosition(
                getX() + (isTurnLeft ? wheelPositionInCar.x : getWidthAfterScale() - wheelPositionInCar.x - wheelSprite.getWidth()),
                getY() + wheelPositionInCar.y);
        wheelSprite.draw(batch);
    }
}
