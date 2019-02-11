package com.maqautocognita.graphics;

import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.maqautocognita.constant.TextFontSizeEnum.FONT_36;

/**
 * @param <DISPLAY> the type of the drawing text, for example Integer,Double or String
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractBitmapFontScreenObject<DISPLAY, ID> extends ScreenObject<ID, ScreenObjectType> {

    public DISPLAY displayText;
    public boolean isNotIncludeHeight;
    protected float targetWidth = 0;
    private BitmapFont bitmapFont;
    private Color color = ColorProperties.TEXT;
    private TextFontSizeEnum textFontSizeEnum;
    /**
     * The text alignment in the area, if {@link #targetWidth>0}, because the size of text maybe smaller than the area
     */
    private int align = Align.center;

    protected AbstractBitmapFontScreenObject() {

    }

    public AbstractBitmapFontScreenObject(DISPLAY displayText, float xPositionInScreen,
                                          float yPositionInScreen, TextFontSizeEnum textFontSizeEnum, boolean isNotIncludeHeight) {
        this(displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum);

        setYPositionInScreen(yPositionInScreen, isNotIncludeHeight);
    }

    public AbstractBitmapFontScreenObject(DISPLAY displayText, float xPositionInScreen,
                                          float yPositionInScreen, TextFontSizeEnum textFontSizeEnum) {
        this.displayText = displayText;
        this.xPositionInScreen = xPositionInScreen;
        this.yPositionInScreen = yPositionInScreen;
        this.textFontSizeEnum = textFontSizeEnum;

        bitmapFont = FontGeneratorManager.getFont(textFontSizeEnum);

        setDisplayTextSize();
    }

    public void setYPositionInScreen(float yPositionInScreen, boolean isNotIncludeHeight) {
        this.isNotIncludeHeight = isNotIncludeHeight;
        this.yPositionInScreen = yPositionInScreen;
        if (isNotIncludeHeight) {
            //because the bitmap font will be draw the text from screen upper to lower, not from the y-position to upper screen
            this.yPositionInScreen += height;
        }
    }

    private void setDisplayTextSize() {
        String drawText = String.valueOf(displayText);
        if (StringUtils.isNotBlank(drawText)) {

            int numberOfLine = 1;
            if (drawText.indexOf("\n") >= 0) {
                Pattern p = Pattern.compile("\n");
                Matcher m = p.matcher(drawText);
                while (m.find()) {
                    numberOfLine++;
                }
            }

            float[] size = LetterUtils.getSizeOfWord(drawText, textFontSizeEnum);
            width = size[0];
            height = size[1] * numberOfLine;
        }
    }


    public AbstractBitmapFontScreenObject(ID id, ScreenObjectType objectType, DISPLAY displayText, float xPositionInScreen,
                                          float yPositionInScreen, TextFontSizeEnum textFontSizeEnum, boolean isNotIncludeHeight) {

        this(displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum);
        this.id = id;
        this.objectType = objectType;

        setYPositionInScreen(yPositionInScreen, isNotIncludeHeight);
    }

    public void setDisplayText(DISPLAY displayText) {
        this.displayText = displayText;
        //reset the width because it may change by the given displayText
        setDisplayTextSize();
    }

    public void setTargetWidth(float targetWidth) {
        this.targetWidth = targetWidth;
        if (targetWidth > 0) {
            bitmapFont.getData().setLineHeight(
                    LetterUtils.getMaximumHeight(FONT_36) + 15);
            this.width = targetWidth;
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    @Override
    public void onDraw(Batch batch) {

        if (null != displayText) {
            String drawText = String.valueOf(displayText);

            if (StringUtils.isNotBlank(drawText)) {
                if (isDisabled) {
                    bitmapFont.setColor(ColorProperties.DISABLE_TEXT);
                } else if (isHighlighted) {
                    bitmapFont.setColor(Color.RED);
                } else {
                    bitmapFont.setColor(color);
                }

                if (0 == targetWidth) {
                    bitmapFont.draw(batch, drawText, xPositionInScreen, yPositionInScreen);
                } else {
                    bitmapFont.draw(batch, drawText, xPositionInScreen, yPositionInScreen, targetWidth, align, false);
                }
            }
        }
    }

}
