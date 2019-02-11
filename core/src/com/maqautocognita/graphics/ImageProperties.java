package com.maqautocognita.graphics;

import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author sc.chi csc19840914@gmail.com
 *         <p/>
 *         This is mainly used to store the common image size
 */
public class ImageProperties {

    public static final int BORDER_IMAGE_SIZE = 270;

    public static final float WORD_IMAGE_WIDTH = 250;
    public static final int BORDER_GAP_WIDTH = 30;
    public static final int MICROPHONE_SIZE = 250;
    public static final Vector2 NOT_PRESSED_MICROPHONE = new Vector2(0, 0);
    public static final Vector2 PRESSED_MICROPHONE = new Vector2(300, 0);
    public static final Vector2 PRESSED_20_40_MICROPHONE = new Vector2(600, 0);
    public static final Vector2 PRESSED_40_60_MICROPHONE = new Vector2(900, 0);
    public static final Vector2 PRESSED_60_80_MICROPHONE = new Vector2(1200, 0);
    public static final Vector2 PRESSED_80_100_MICROPHONE = new Vector2(1500, 0);
    public static final Vector2 SPEECH_ICON_SCREEN_POSITION = new Vector2(1750, 250);
    private static final int SMALL_WORD_IMAGE_SIZE_IN_LANDSCAPE = 100;
    private static final int SMALL_WORD_IMAGE_SIZE = 150;

    public static int getSmallImageWordSize() {
        return ScreenUtils.isLandscapeMode ? SMALL_WORD_IMAGE_SIZE_IN_LANDSCAPE : SMALL_WORD_IMAGE_SIZE;
    }

}
