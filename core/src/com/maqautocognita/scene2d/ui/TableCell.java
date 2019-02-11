package com.maqautocognita.scene2d.ui;

import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class TableCell extends Actor {

    private static final int PADDING_RIGHT = 10;

    protected final String text;
    protected TextFontSizeEnum textFontSize = TextFontSizeEnum.FONT_36;
    protected boolean highlighted;

    private String textWhenHighlighted;

    private TextFontSizeEnum textFontSizeWhenHighlighted;

    private Color textColor = ColorProperties.TEXT;
    private int textAlign = Align.right;

    private float textHeight;

    private float maximumWidthOfDigit;
    private float maximumWidthOfSymbol;

    public TableCell(String text, int width, int height, Color textColor) {
        this(text, width, height);
        this.textColor = textColor;
    }

    public TableCell(String text, int width, int height) {
        this.text = text;
        this.textWhenHighlighted = text;
        setSize(width, height);
        initWidth();
        if (StringUtils.isNotBlank(text)) {
            textHeight = LetterUtils.getMaximumHeight(textFontSize);
        }
    }

    private void initWidth() {
        final String textToDisplay = highlighted ? textWhenHighlighted : text;
        if (StringUtils.isNotBlank(textToDisplay)) {
            maximumWidthOfDigit = LetterUtils.getTotalWidthOfWord("10", textFontSize);
            maximumWidthOfSymbol = LetterUtils.getTotalWidthOfWord("=", textFontSize);
        }
    }

    public TableCell(String text, int width, int height, int textAlign) {
        this(text, width, height);
        this.textAlign = textAlign;
    }

    public TableCell(String text, int width, int height, TextFontSizeEnum textFontSize, int textAlign) {
        this(text, width, height);
        this.textFontSize = textFontSize;
        this.textAlign = textAlign;
    }

    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
    }

    public void setTextColor(Color color) {
        this.textColor = color;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        drawText(batch);
    }

    protected void drawText(Batch batch) {
        if (StringUtils.isNotBlank(text)) {
            BitmapFont bitmapFont = FontGeneratorManager.getFont(highlighted ? textFontSizeWhenHighlighted : textFontSize);
            bitmapFont.setColor(highlighted ? ColorProperties.HIGHLIGHT : textColor);

            final String textToDisplay = highlighted ? textWhenHighlighted : text;
            float startXPosition = getX();
            int i = 1;
            int numberOfSubText = textToDisplay.split(" ").length;
            for (String subText : textToDisplay.split(" ")) {
                float width = maximumWidthOfSymbol;
                if (numberOfSubText == 1) {
                    width = getWidth();
                } else if (isDigitsOnly(subText)) {
                    if (i == numberOfSubText) {
                        width = maximumWidthOfDigit + PADDING_RIGHT;
                    } else {
                        width = maximumWidthOfDigit;
                    }
                }
                bitmapFont.draw(batch, subText, startXPosition,
                        getStartYPositionForText(), width, textAlign, false);
                startXPosition += width;
                i++;
            }


        }

    }

    private boolean isDigitsOnly(CharSequence str) {
        final int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private float getStartYPositionForText() {
        return getY() + getHeight() - (getHeight() - textHeight) / 2;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        this.textFontSizeWhenHighlighted = textFontSize;
    }

    public void setHighlighted(boolean highlighted, TextFontSizeEnum fontSizeWhenHighlighted, String text) {
        this.highlighted = highlighted;
        this.textFontSizeWhenHighlighted = fontSizeWhenHighlighted;
        this.textWhenHighlighted = text;
    }

    public String getText() {
        return text;
    }
}
