package com.maqautocognita.scene2d.actors;

import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * The actor cannot be scale by the user,the size will be fixed, and it will be render depends if it is inside the camera.
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class ImageActor<T> extends AbstractCameraActor {


    private T id;
    private String imagePath;
    private AutoCognitaTextureRegion autoCognitaTextureRegion;
    private IconPosition iconPosition;
    private boolean isRequiredToCheckCameraBeforeDraw = true;
    /**
     * It is used to store the touch position in this object, for calculate  the future position  which the user is going to drag
     */
    private float touchingXPosition;
    private float touchingYPosition;

    private int times;

    private float alpha = 1f;

    public ImageActor(float width, float height) {
        setSize(width, height);
    }

    public ImageActor(T id, String imagePath, float screenX, float screenY, float width, float height) {
        this(id, imagePath, null, 1, screenX, screenY, width, height);
    }

    public ImageActor(T id, String imagePath, IconPosition iconPosition, int times, float screenX, float screenY, float width, float height) {
        if (StringUtils.isNotBlank(imagePath)) {
            this.imagePath = imagePath;
            //loadImage(imagePath);
            this.iconPosition = iconPosition;
            setPosition(screenX, screenY);
            setSize(width, height);
        }
        this.times = times;
        setId(id);
    }


    public ImageActor(T id, String imagePath) {
        this(id, imagePath, null, 1, 0, 0, 0, 0);
    }

    public ImageActor(T id, String imagePath, IconPosition iconPosition, int times) {
        this(id, imagePath, iconPosition, 0, 0, times);
    }


    public ImageActor(T id, String imagePath, IconPosition iconPosition, float screenX, float screenY, int times) {
        this(id, imagePath, iconPosition, 1, screenX, screenY, iconPosition.width, iconPosition.height);
        this.times = times;
    }

    public ImageActor(String imagePath, IconPosition iconPosition) {
        this(imagePath, iconPosition, 0, 0);
    }

    /**
     * It is mainly used for create a {@link #autoCognitaTextureRegion}, the given iconPosition is used to indicuate the region area in the given imagePath
     *
     * @param imagePath
     * @param iconPosition
     * @param screenX
     * @param screenY
     */
    public ImageActor(String imagePath, IconPosition iconPosition, float screenX, float screenY) {
        this(null, imagePath, iconPosition, 1, screenX, screenY, iconPosition.width, iconPosition.height);
    }

    public ImageActor(String imagePath) {
        this(imagePath, 0, 0, 0, 0);
    }

    public ImageActor(String imagePath, float screenX, float screenY, float width, float height) {
        this(null, imagePath, null, 1, screenX, screenY, width, height);
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public void setIconPosition(IconPosition iconPosition) {
        this.iconPosition = iconPosition;
        setSize(iconPosition.width, iconPosition.height);
        autoCognitaTextureRegion = null;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        loadImage(imagePath);
        autoCognitaTextureRegion = null;
    }

    protected void loadImage(String imagePath) {
        if (StringUtils.isNotBlank(imagePath)) {
            AssetManagerUtils.loadTexture(imagePath);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible() && (!isRequiredToCheckCameraBeforeDraw || isInsideCamera())) {
            drawActor(batch);
        }
    }

    protected void drawActor(Batch batch) {
        if (null != getAutoCognitaTextureRegion() &&
                //make sure the image is not disposed by other object
                AssetManagerUtils.isImageLoaded(imagePath)) {
            draw(batch, getAutoCognitaTextureRegion());
        }
    }

    public AutoCognitaTextureRegion getAutoCognitaTextureRegion() {
        if (null == autoCognitaTextureRegion && StringUtils.isNotBlank(imagePath)) {
            if (null == AssetManagerUtils.getTexture(imagePath)) {
                loadImage(imagePath);
                AssetManagerUtils.isFinishLoading();
            } else {
                if (null == iconPosition) {
                    autoCognitaTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(imagePath));
                    if (0 == getWidth() || 0 == getHeight()) {
                        setSize(autoCognitaTextureRegion.getRegionWidth(), autoCognitaTextureRegion.getRegionHeight());
                    }
                } else {
                    autoCognitaTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(imagePath),
                            iconPosition, times);
                }
            }
        }

        return autoCognitaTextureRegion;
    }

    public void setAutoCognitaTextureRegion(AutoCognitaTextureRegion autoCognitaTextureRegion) {
        this.autoCognitaTextureRegion = autoCognitaTextureRegion;
        setSize(autoCognitaTextureRegion.getRegionWidth(), autoCognitaTextureRegion.getRegionHeight());
    }

    protected void draw(Batch batch, AutoCognitaTextureRegion autoCognitaTextureRegion) {
        Color originalColor = batch.getColor();
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * alpha);
        batch.draw(autoCognitaTextureRegion,
                //in order to make the image in the center position of the original even if it is scaled
                getX() + (getWidth() - getWidth() * getScaleX()) / 2,
                getY() + (getHeight() - getHeight() * getScaleY()) / 2,
                getWidth() * getScaleX(), getHeight() * getScaleY());
        batch.setColor(originalColor);
    }

    @Override
    public void setX(float x) {
        super.setX(x - touchingXPosition);
    }

    @Override
    public void setY(float y) {
        super.setY(y - touchingYPosition);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x - touchingXPosition, y - touchingYPosition);
    }

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

    public void dispose() {
        disposeWithoutRemove();
        remove();
    }

    private void disposeWithoutRemove() {
        if (null != autoCognitaTextureRegion) {
            autoCognitaTextureRegion = null;
            AssetManagerUtils.unloadTexture(imagePath);
        }
    }

    public boolean isMoved() {
        return getOriginX() != getX() || getOriginY() != getY();
    }

    public ImageActor<T> clone(float positionX, float positionY) {
        ImageActor<T> imageActor = new ImageActor<T>(imagePath, iconPosition);
        imageActor.id = id;
        imageActor.setSize(getWidth(), getHeight());
        imageActor.setOrigin(positionX, positionY);
        imageActor.setPosition(imageActor.getOriginX(), imageActor.getOriginY());
        return imageActor;
    }

    public void removeTouch() {
        this.touchingXPosition = 0;
        this.touchingYPosition = 0;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setTouchingPosition(float x, float y) {
        this.touchingXPosition = x - getOriginX();
        this.touchingYPosition = y - getOriginY();
    }
}
