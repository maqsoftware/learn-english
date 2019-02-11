package com.maqautocognita.graphics;

import com.maqautocognita.constant.TextFontSizeEnum;

/**
 * Created by sc.chi on 31/7/16.
 */
public class NumberScreenObject extends AbstractBitmapFontScreenObject<Integer, Integer> {

    protected NumberScreenObject() {

    }

    public NumberScreenObject(Integer displayText, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum, boolean isNotIncludeHeight) {
        super(displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum, isNotIncludeHeight);
    }


}
