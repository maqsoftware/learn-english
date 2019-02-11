package com.maqautocognita.graphics;

import com.maqautocognita.constant.TextFontSizeEnum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * This is mainly used for a texture which required to do a motion
 * from the start position {@link #xPositionInScreen}, {@link #yPositionInScreen} to a destination {@link #targetXPosition} and {@link #targetYPosition}
 * It also include the the animation for changing size from {@link #height} and {@link #width} to {@link #finalHeight} and {@link #finalWidth}
 * <p/>
 * For the object which required to display a number in the center of the object include a numberTextScreenObject in the center of the object
 * can init the object by calling {@link #AnimateTextureScreenObject(Integer, TextureScreenObject, float, float, float, float)}
 * <p/>
 * It will copy the source information {@link TextScreenObject},such as texture, size and alpha
 * and the caller required to pass the target size and position,
 * and it will do the animation from the original size and position which is from copying source in 1 second.
 * <p/>
 * It also contain a numberTextScreenObject {@link TextScreenObject} in white color which is located in the center of this object
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class AnimateTextureScreenObject extends TextureScreenObject<Integer, Object> {

    public float finalWidth;
    public float finalHeight;
    /**
     * A flag to indicate if the animation is completed, and it will be mark to true after the {@link #animationListener} onComplete is called。
     * Assume the animation listener will be call once
     */
    public boolean isAnimationCompleted;
    private float targetXPosition;
    private float targetYPosition;
    private IAnimationListener animationListener;
    //the number which will be shown in the center of this object
    private NumberScreenObject numberTextScreenObject;
    private float originalXPositionInScreen;
    private float originalYPositionInScreen;
    private float originalWidth;
    private float originalHeight;
    private float originalAlpha;
    private AnimationCompleteDependsAttribute animationCompleteDependsAttribute = AnimationCompleteDependsAttribute.ALPHA;

    public AnimateTextureScreenObject(AutoCognitaTextureRegion autoCognitaTextureRegion,
                                      AutoCognitaTextureRegion textureRegionInHighlightState, AutoCognitaTextureRegion textureRegionInDisabledState,
                                      float xPositionInScreen, float yPositionInScreen) {
        super(xPositionInScreen, yPositionInScreen, autoCognitaTextureRegion, textureRegionInHighlightState,
                textureRegionInDisabledState);
    }

    public AnimateTextureScreenObject(Integer id, Texture texture, float xPositionInScreen, float yPositionInScreen, float width, float height) {
        super(id, null, texture,
                xPositionInScreen, yPositionInScreen,
                width, height);
        //make the final size and position same as the given, so no animation will be happen
        this.targetXPosition = xPositionInScreen;
        this.targetYPosition = yPositionInScreen;
        this.finalWidth = width;
        this.finalHeight = height;
    }

    public AnimateTextureScreenObject(Integer id, TextureScreenObject sourceTextureScreenObject,
                                      float targetXPosition, float targetYPosition, float finalWidth, float finalHeight

    ) {
        super(id, null, sourceTextureScreenObject.getTexture(),
                sourceTextureScreenObject.xPositionInScreen, sourceTextureScreenObject.yPositionInScreen,
                sourceTextureScreenObject.width, sourceTextureScreenObject.height);
        alpha = sourceTextureScreenObject.alpha;
        this.targetXPosition = targetXPosition;
        this.targetYPosition = targetYPosition;
        this.finalWidth = finalWidth;
        this.finalHeight = finalHeight;

        initNumberText();

        this.originalAlpha = alpha;
        this.originalWidth = width;
        this.originalHeight = height;
        this.originalXPositionInScreen = xPositionInScreen;
        this.originalYPositionInScreen = yPositionInScreen;
    }

    private void initNumberText() {
        numberTextScreenObject = new NumberScreenObject(id, targetXPosition, targetYPosition, TextFontSizeEnum.FONT_144, true);
        numberTextScreenObject.setColor(Color.WHITE);

        //make sure the numberTextScreenObject is show in vertical center of this object
        numberTextScreenObject.yPositionInScreen += (finalHeight - numberTextScreenObject.height
                //in order to make sure the numberTextScreenObject is display in the object content part, there is a slightly lower
                - 70)
                / 2;
        setNumberXPosition();

        numberTextScreenObject.isVisible = false;
    }

    private void setNumberXPosition() {
        if (null != numberTextScreenObject) {
            numberTextScreenObject.xPositionInScreen = targetXPosition + (finalWidth - numberTextScreenObject.width) / 2;
        }
    }

    public void setSize(float width, float height) {
        this.originalWidth = this.finalWidth;
        this.originalHeight = this.finalHeight;
        this.finalWidth = width;
        this.finalHeight = height;
        setNumberXPosition();
    }

    public void setTargetPosition(float targetXPosition, float targetYPosition) {
        this.originalXPositionInScreen = this.targetXPosition;
        this.originalYPositionInScreen = this.targetYPosition;
        this.targetXPosition = targetXPosition;
        this.targetYPosition = targetYPosition;
        setNumberXPosition();
    }

    public void setAnimationListener(IAnimationListener animationListener) {
        this.animationListener = animationListener;
    }

    public int getNumber() {
        if (null != numberTextScreenObject) {
            return numberTextScreenObject.displayText;
        }

        return 0;
    }

    public void setNumber(int number) {
        numberTextScreenObject.setDisplayText(number);
        //reset the x position of the number becasue the size for the given number may change
        setNumberXPosition();
    }

    public void showNumber() {
        numberTextScreenObject.isVisible = true;
    }

    public void hideNumber() {
        numberTextScreenObject.isVisible = false;
    }

    public void setAnimationCompleteDependsAttribute(AnimationCompleteDependsAttribute animationCompleteDependsAttribute) {
        this.animationCompleteDependsAttribute = animationCompleteDependsAttribute;
    }

    @Override
    public void onDraw(Batch batch) {

        //in order to make the animation to complete in 1 second (increase/decrease the image size)
        float deltaTime = Gdx.graphics.getDeltaTime() / 1f;

        if (alpha < 1) {
            alpha += (1 - originalAlpha) * deltaTime;
        } else if (AnimationCompleteDependsAttribute.ALPHA.equals(animationCompleteDependsAttribute)) {
            //we will use the alpha value to check if the animation is complete, becasue the alpha value cannot be sum to 1,
            fireCompleteListener();
        }

        if (originalHeight > finalHeight ? height > finalHeight : height < finalHeight) {
            height += (finalHeight - originalHeight) * deltaTime;
        } else if (AnimationCompleteDependsAttribute.SIZE.equals(animationCompleteDependsAttribute)) {
            fireCompleteListener();
        }

        if (originalWidth > finalWidth ? width > finalWidth : width < finalWidth) {
            width += (finalWidth - originalWidth) * deltaTime;
        } else if (AnimationCompleteDependsAttribute.SIZE.equals(animationCompleteDependsAttribute)) {
            fireCompleteListener();
        }

        if (originalXPositionInScreen > targetXPosition ? xPositionInScreen > targetXPosition : xPositionInScreen < targetXPosition) {
            xPositionInScreen += (targetXPosition - originalXPositionInScreen) * deltaTime;
        } else if (AnimationCompleteDependsAttribute.POSITION.equals(animationCompleteDependsAttribute)) {
            fireCompleteListener();
        }

        if (originalYPositionInScreen > targetYPosition ? yPositionInScreen > targetYPosition : yPositionInScreen < targetYPosition) {
            yPositionInScreen += (targetYPosition - originalYPositionInScreen) * deltaTime;
        } else if (AnimationCompleteDependsAttribute.POSITION.equals(animationCompleteDependsAttribute)) {
            fireCompleteListener();
        }

        super.onDraw(batch);

        if (null != numberTextScreenObject) {
            numberTextScreenObject.draw(batch);
        }

    }

    private void fireCompleteListener() {
        if (!isAnimationCompleted && null != animationListener) {
            isAnimationCompleted = true;
            //we will use the alpha value to check if the animation is complete, becasue the alpha value cannot be sum to 1,
            animationListener.onComplete();

        }
    }

    @Override
    public boolean isOverlap(float x, float y, float width, float height) {
        return targetXPosition < x + width
                && targetXPosition + this.width > x &&
                targetYPosition < y + height
                && targetYPosition + this.height > y;

    }

    public enum AnimationCompleteDependsAttribute {
        ALPHA, SIZE, POSITION
    }

    public interface IAnimationListener {
        void onComplete();
    }

}

