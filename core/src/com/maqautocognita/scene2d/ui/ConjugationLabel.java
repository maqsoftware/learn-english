package com.maqautocognita.scene2d.ui;

import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class ConjugationLabel extends Label {

    public static final String OBJECT_TEXT_COLOR_HEX_CODE = "03A9F4";

    public static final String TENSE_PREFIX_COLOR_HEX_CODE = "9C27B0";

    public static final TextFontSizeEnum NORMAL_FONT_SIZE = TextFontSizeEnum.FONT_54;

    private static final TextFontSizeEnum SMALL_FONT_SIZE = TextFontSizeEnum.FONT_36;

    private String tense;
    private String object;

    public ConjugationLabel(CharSequence text, LabelStyle style, String object, String tense) {
        this(text, style);


        this.tense = tense;
        this.object = object;
    }

    public ConjugationLabel(CharSequence text, LabelStyle style) {
        super(text, style);
        setSize(style.background.getMinWidth(), style.background.getMinHeight());
    }

    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public void setText(CharSequence text) {

        String displayTextWithColor = null;

        if ("kuwa".equalsIgnoreCase(text.toString())) {
            if ("na".equals(tense)) {
                displayTextWithColor = "[#" + OBJECT_TEXT_COLOR_HEX_CODE + "]ni";

            } else if (StringUtils.isBlank(tense)) {
                displayTextWithColor = "[#" + OBJECT_TEXT_COLOR_HEX_CODE + "]si";

            } else {
                displayTextWithColor = getNormalDisplayTextWithColor(text);

            }
        } else if ("kuwa na".equalsIgnoreCase(text.toString())) {
            if (StringUtils.isBlank(tense) || "na".equals(tense)) {
                displayTextWithColor = "[#" + OBJECT_TEXT_COLOR_HEX_CODE + "]" + object + "[#" + TENSE_PREFIX_COLOR_HEX_CODE + "]na";
            } else {
                displayTextWithColor = getNormalDisplayTextWithColor(text);
            }
        } else {
            displayTextWithColor = getNormalDisplayTextWithColor(text);
        }


        if (LetterUtils.getTotalWidthOfWord(text.toString(), NORMAL_FONT_SIZE) >= getWidth() / 7 * 4) {
            changeToSmallFontSize();
        } else {
            changeToNormalFontSize();
        }

        super.setText(displayTextWithColor);
    }

    private String getNormalDisplayTextWithColor(CharSequence text) {

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(object)) {
            sb.append("[#" + OBJECT_TEXT_COLOR_HEX_CODE + "]" + object);
        }

        if (StringUtils.isNotBlank(tense)) {
            sb.append("[#" + TENSE_PREFIX_COLOR_HEX_CODE + "]" + tense);
        }

        sb.append("[#" + ColorProperties.TEXT_COLOR_HEX_CODE + "]" + text);

        return sb.toString();
    }

    private void changeToSmallFontSize() {
        getStyle().font = FontGeneratorManager.getFont(SMALL_FONT_SIZE);
        getStyle().font.getData().markupEnabled = true;
        getStyle().font.getData().padRight = -5;
        setStyle(getStyle());
    }

    private void changeToNormalFontSize() {
        getStyle().font = FontGeneratorManager.getFont(NORMAL_FONT_SIZE);
        getStyle().font.getData().markupEnabled = true;
        getStyle().font.getData().padRight = -5;
        setStyle(getStyle());
    }

    public void clearText() {
        super.setText(null);
    }

}
