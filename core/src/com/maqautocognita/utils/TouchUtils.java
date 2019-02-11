package com.maqautocognita.utils;

import com.maqautocognita.graphics.AbstractBitmapFontScreenObject;
import com.maqautocognita.graphics.ScreenObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class TouchUtils {

    public static boolean isTouched(float x1, float y1, float width, float height) {

        if (Gdx.input.isTouched()) {
            float touchedXPosition = ScreenUtils.toViewPosition(Gdx.input.getX());

            float touchedYPosition = ScreenUtils.getExactYPositionOnScreen(Gdx.input.getY());

            return touchedXPosition >= x1 && touchedXPosition <= x1 + width && touchedYPosition >= y1 && touchedYPosition <= y1 + height;
        }
        return false;
    }

    public static boolean isTouched(IconPosition iconPosition, int touchedX, int touchedY) {

        if (null == iconPosition) {
            return false;
        }

        return isTouched(iconPosition.x, iconPosition.y, iconPosition.width, iconPosition.height, touchedX, touchedY);

    }

    public static boolean isTouched(float x1, float y1, float width, float height, float touchedX, float touchedY) {

        return isTouchedX(x1, width, touchedX) && touchedY >= y1 && touchedY <= y1 + height;
    }

    public static boolean isTouchedX(float x1, float width, float touchedX) {

        return touchedX >= x1 && touchedX <= x1 + width;
    }

    public static boolean isTouched(ScreenObject screenObject, int touchedX, int touchedY) {

        if (null == screenObject) {
            return false;
        }

        return isTouched(screenObject.xPositionInScreen, screenObject.yPositionInScreen,
                screenObject.width, screenObject.height, touchedX, touchedY);

    }

    public static boolean isTouched(AbstractBitmapFontScreenObject screenObject, int touchedX, int touchedY) {

        if (null == screenObject) {
            return false;
        }

        return isTouched(screenObject.xPositionInScreen, screenObject.isNotIncludeHeight ? screenObject.yPositionInScreen - screenObject.height : screenObject.yPositionInScreen,
                screenObject.width, screenObject.height, touchedX, touchedY);

    }


    public static boolean isTouched(Rectangle area, int touchedX, int touchedY) {

        if (null == area) {
            return false;
        }

        return isTouched(area.x, area.y, area.width, area.height, touchedX, touchedY);

    }

    public static boolean isTouched(Actor actor, int touchedX, int touchedY) {
        if (null == actor) {
            return false;
        }

        return actor.isVisible() && isTouched(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight(), touchedX, touchedY);

    }

}
