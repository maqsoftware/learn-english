package com.maqautocognita.scene2d.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class BackgroundActor extends ObjectActor {

    public BackgroundActor(String imagePath, float x, float y) {
        super(imagePath, x, y);
        setTouchable(Touchable.disabled);
        //the image for the background is no required to scale as it is fit to the screen
        setScale(1);
    }

    @Override
    protected void onCameraMoveRightAndOutOfTheWorld(int numberOfWorldOut, Batch batch) {
        drawRepeatedObject(getDrawTexture(), numberOfWorldOut, batch, false);
    }

    @Override
    protected void onCameraMoveLeftAndOutOfTheWorld(int numberOfWorldOut, Batch batch) {
        drawRepeatedObject(getDrawTexture(), numberOfWorldOut, batch, true);
    }

    private void drawRepeatedObject(TextureRegion textureRegion, int repeatedWorld, Batch batch, boolean isTurnLeft) {
        for (int i = 1; i <= repeatedWorld; i++) {
            float worldWidth = isTurnLeft ? -camera.getWorldWidth() : camera.getWorldWidth();

            if (camera.isObjectInsideCamera(worldWidth * i + getX(), getWidthAfterScale())) {
                //repeat the end part of background append in the start of the current background
                drawActor(batch, textureRegion, (worldWidth * i + getX()), 0, getWidthAfterScale(), getHeightAfterScale());
            }
        }
    }
}
