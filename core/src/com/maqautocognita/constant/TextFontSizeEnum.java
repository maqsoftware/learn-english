package com.maqautocognita.constant;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public enum TextFontSizeEnum {
    FONT_36(36), FONT_48(48), FONT_54(54), FONT_72(72), FONT_84(84), FONT_108(108),
    FONT_144(144), FONT_252(252), FONT_276(276), FONT_288(288),
    FONT_360(360), FONT_414(414);

    private final int fontSize;

    TextFontSizeEnum(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontSize() {
        return fontSize;
    }
}
