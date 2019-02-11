package com.maqautocognita.scene2d.actors;

import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public abstract class AbstractStoryModeObjectActor extends AbstractCameraActor {

    private TextureRegion objectTextureRegion;
    private Texture storyModeObjectTexture;

    private float originalXPosition;
    private float originalYPosition;

    private String imagePath;

    AbstractStoryModeObjectActor(String imagePath) {
        if (StringUtils.isNotBlank(imagePath)) {
            this.imagePath = imagePath;
            if (Gdx.files.internal(imagePath).exists()) {
                storyModeObjectTexture = new Texture(Gdx.files.internal(imagePath));
                objectTextureRegion = new TextureRegion(storyModeObjectTexture, storyModeObjectTexture.getWidth(), storyModeObjectTexture.getHeight());
                setSize(storyModeObjectTexture.getWidth(), storyModeObjectTexture.getHeight());
            }
        }
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setOriginalYPosition(float originalYPosition) {
        this.originalYPosition = originalYPosition;
    }

    public void rollbackToOriginalPosition() {
        setPosition(originalXPosition, originalYPosition);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        float cameraScreenStartPositionX = getCamera().position.x - getCamera().viewportWidth / 2;
        float cameraScreenEndPositionX = getCamera().position.x + getCamera().viewportWidth / 2;

        if (isInsideCamera()) {
            drawActor(batch, getDrawTexture(), getX(), getY(), getWidthAfterScale(), getHeightAfterScale(), parentAlpha);
        } else {
            if ((cameraScreenEndPositionX > 0 && cameraScreenEndPositionX > getX()) ||
                    isObjectNeedToShowInPreviousWorld(cameraScreenStartPositionX)) {
                int repeatedWorld = (int) (cameraScreenEndPositionX / getCamera().getWorldWidth());
                if (isObjectNeedToShowInPreviousWorld(cameraScreenStartPositionX)) {
                    repeatedWorld--;
                }
                setX(repeatedWorld);
            } else if (cameraScreenStartPositionX < 0 || cameraScreenEndPositionX < 0) {

                int repeatedWorld = (int) (cameraScreenEndPositionX / getCamera().getWorldWidth()) - 1;

                if (cameraScreenStartPositionX <
                        //check if the end x position of the object included in the current camera, the current camera may include part of next world screen in left sides
                        repeatedWorld * getCamera().getWorldWidth() - (getCamera().getWorldWidth() - originalXPosition) + getWidthAfterScale()) {
                    repeatedWorld -= 1;
                }


                setX(repeatedWorld);
            }
        }

    }

    protected void drawActor(Batch batch, TextureRegion textureRegion, float x, float y, float width, float height, float parentAlpha) {
        if (null != textureRegion) { // add by Pazu on 3 Oct 2016, for no image object
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            batch.draw(textureRegion, x, y, width, height);
        }
    }

    protected TextureRegion getDrawTexture() {
        return objectTextureRegion;
    }

    public float getWidthAfterScale() {
        return getWidth() * getScaleX();
    }

    public float getHeightAfterScale() {
        return getHeight() * getScaleX();
    }

    /**
     * Check if the end x position of this object is shown on the screen, the user will swipe the screen to move the camera to right,
     * and the object will be reposition to the right, in order to make it visible when the camera is reach the suitable position, so when the user swipe back,
     * which mean move the camera to left, the object position is required minus 1 width of the world , which mean get the previous position in the previous world to check if the object is required to show in the current camera
     *
     * @param cameraScreenStartPositionX
     * @return
     */
    private boolean isObjectNeedToShowInPreviousWorld(float cameraScreenStartPositionX) {
        return (cameraScreenStartPositionX > 0 && cameraScreenStartPositionX < getX() - getCamera().getWorldWidth() + getWidthAfterScale());
    }

    /**
     * set the x position of the object by the given number of world which already repeated.
     *
     * @param numberOfWorldRepeated in the begining , the number of world should be 0,
     *                              because the user has no move the camera and no world repeated in the camera
     *                              For example: if the camera is moved to right and
     *                              out of the maximum width of the world ({@link CustomCamera#getWorldWidth()}), the given numberOfWorldRepeated will be 1
     */
    protected void setX(int numberOfWorldRepeated) {
        float xPosition = numberOfWorldRepeated * getCamera().getWorldWidth() + originalXPosition;
        if (getX() != xPosition) {
            setX(xPosition);
        }
    }

    public float getOriginalXPosition() {
        return originalXPosition;
    }

    public void setOriginalXPosition(float originalXPosition) {
        this.originalXPosition = originalXPosition;
    }

    public void dispose() {
        if (null != storyModeObjectTexture) {
            objectTextureRegion = null;
            storyModeObjectTexture.dispose();
            storyModeObjectTexture = null;
            remove();
        }
    }

    public boolean isOverlap(AbstractStoryModeObjectActor overlapActor) {
        return getX() < overlapActor.getX() + overlapActor.getWidthAfterScale()
                && getX() + getWidthAfterScale() > overlapActor.getX() &&
                getY() < overlapActor.getY() + overlapActor.getHeightAfterScale()
                && getY() + getHeightAfterScale() > overlapActor.getY();

    }

}
