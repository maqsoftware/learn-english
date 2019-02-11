package com.maqautocognita.graphics;

import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class TextScreenObject<ID> extends AbstractBitmapFontScreenObject<String, ID> {


    public TextScreenObject(String displayText, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum) {
        super(displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum);
    }

    public TextScreenObject(String displayText, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum, boolean isNotIncludeHeight, float targetWidth) {
        this(displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum, isNotIncludeHeight);
        setTargetWidth(targetWidth);
    }

    public TextScreenObject(String displayText, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum, boolean isNotIncludeHeight) {
        super(displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum, isNotIncludeHeight);
    }

    public TextScreenObject(ID id, String displayText, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum, boolean isNotIncludeHeight, float targetWidth) {
        this(id, displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum, isNotIncludeHeight);
        setTargetWidth(targetWidth);
    }

    public TextScreenObject(ID id, String displayText, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum, boolean isNotIncludeHeight) {
        this(id, null, displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum, isNotIncludeHeight);
    }

    public TextScreenObject(ID id, ScreenObjectType screenObjectType, String displayText, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum, boolean isNotIncludeHeight) {
        super(id, screenObjectType, displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum, isNotIncludeHeight);
    }

    public TextScreenObject(ScreenObjectType screenObjectType, String displayText, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum) {
        this(null, screenObjectType, displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum, false);
    }

    public TextScreenObject(ScreenObjectType screenObjectType, String displayText, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum, boolean isNotIncludeHeight) {
        this(null, screenObjectType, displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum, isNotIncludeHeight);
    }

    public TextScreenObject(ID id, ScreenObjectType screenObjectType, String displayText, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum) {
        this(id, screenObjectType, displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum, false);
    }

    public TextScreenObject(ID id, ScreenObjectType screenObjectType, String displayText, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum, float targetWidth) {
        this(id, screenObjectType, displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum, false);
        setTargetWidth(targetWidth);
    }

    public TextScreenObject(ID id, ScreenObjectType screenObjectType, String displayText, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum, float targetWidth, boolean isNotIncludeHeight) {
        this(id, screenObjectType, displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum, isNotIncludeHeight);
        setTargetWidth(targetWidth);
    }
}
