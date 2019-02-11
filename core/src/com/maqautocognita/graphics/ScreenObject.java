package com.maqautocognita.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class ScreenObject<ID, T> {

    public ID id;

    public T objectType;
    public float xPositionInScreen;
    public float yPositionInScreen;
    /**
     * Store the audio file name which will be play when the user single tag this object
     */
    public String audioFileName;
    /**
     * It is mainly used to indicate if touch is allow for the user, if false is set, this object will not trigger any touch event
     */
    public boolean isTouchAllow = true;
    /**
     * Store other screen object which is in the same group with this screen object
     */
    public ScreenObject sameGroupObject;
    public boolean isHighlighted;
    public float width, height;
    public boolean isDisabled;
    public boolean isRotatingToRight;
    public boolean isVisible = true;
    protected Vector2 originalPosition;
    /**
     * It is used to store the touch position in this Object which the user is going to drag
     */
    private float touchingXPosition;
    private float touchingYPosition;

    public final void draw(Batch batch) {
        if (isVisible) {
            onDraw(batch);
        }
    }

    public abstract void onDraw(Batch batch);

    public boolean isOverlap(ScreenObject screenObject) {
        return isOverlap(screenObject.xPositionInScreen, screenObject.yPositionInScreen, screenObject.width, screenObject.height);
    }

    public boolean isOverlap(float x, float y, float width, float height) {
        return xPositionInScreen < x + width
                && xPositionInScreen + this.width > x &&
                yPositionInScreen < y + height
                && yPositionInScreen + this.height > y;

    }

    /**
     * set the touching position on the object which the user touched
     *
     * @param touchingXPosition
     * @param touchingYPosition
     */
    public void touchingPosition(float touchingXPosition, float touchingYPosition) {
        this.touchingXPosition = touchingXPosition - xPositionInScreen;
        this.touchingYPosition = touchingYPosition - yPositionInScreen;
    }

    public void dragPosition(float x, float y) {
        xPositionInScreen = x - touchingXPosition;
        yPositionInScreen = y - touchingYPosition;
    }

    public boolean isDragging() {
        if (null == originalPosition) {
            return false;
        }
        return xPositionInScreen != originalPosition.x || yPositionInScreen != originalPosition.y;
    }

    public void rollbackToOriginalPosition() {
        if (null != originalPosition) {
            xPositionInScreen = originalPosition.x;
            yPositionInScreen = originalPosition.y;
        }
    }


    public void makeNewPosition(float x, float y) {
        xPositionInScreen = x;
        yPositionInScreen = y;
        if (null == originalPosition) {
            originalPosition = new Vector2(x, y);
        } else {
            originalPosition.x = x;
            originalPosition.y = y;
        }
    }

    /**
     * Set the current position to current {@link #xPositionInScreen} and {@link #yPositionInScreen}
     */
    public void setOriginalPositionToCurrent() {
        if (null == originalPosition) {
            originalPosition = new Vector2();
        }
        originalPosition.x = xPositionInScreen;
        originalPosition.y = yPositionInScreen;
    }
}
