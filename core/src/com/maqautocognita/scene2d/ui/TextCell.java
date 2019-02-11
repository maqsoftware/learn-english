package com.maqautocognita.scene2d.ui;

import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class TextCell<ID> extends Actor {
    public ID id;
    private TextFontSizeEnum textFontSize = TextFontSizeEnum.FONT_144;
    private String text;
    private BitmapFont bitmapFont;
    private float textHeight;
    private boolean highlighted;
    private int textAlign = Align.center | Align.top;

    private boolean isNarrowLineHeight;

    private Color textColor = ColorProperties.TEXT;

    private boolean isTextFlip;

    private float alpha = 1f;

    private boolean wrap;

    private float originalLineHeight;

    public TextCell() {

    }

    public TextCell(ID id, String text, TextFontSizeEnum textFontSize, float width) {
        this(id, text, textFontSize, width, 0, 0);
    }

    public TextCell(ID id, String text, TextFontSizeEnum textFontSize, float width, float xPosition, float yPosition) {
        this.id = id;
        this.textFontSize = textFontSize;
        setPosition(xPosition, yPosition);
        setWidth(width);
        setText(text);
    }

    public void setText(String text) {
        this.text = text;
        if (StringUtils.isNotBlank(text)) {
            this.textHeight = LetterUtils.getHeightOfWordWithWrap(text, textFontSize, getWidth());
            setHeight(textHeight);
        }
    }

    public TextCell(String text, TextFontSizeEnum textFontSize, float width) {
        this(null, text, textFontSize, width, 0, 0);
    }

    public TextCell(String text, TextFontSizeEnum textFontSize, float width, float xPosition, float yPosition) {
        this(null, text, textFontSize, width, xPosition, yPosition);
    }

    public TextCell(String text, TextFontSizeEnum textFontSize, float width, float xPosition, float yPosition, int textAlign) {
        this(null, text, textFontSize, width, xPosition, yPosition, textAlign);
    }

    public TextCell(ID id, String text, TextFontSizeEnum textFontSize, float width, float xPosition, float yPosition, int textAlign) {
        this(id, text, textFontSize, width, xPosition, yPosition);
        this.textAlign = textAlign;
    }

    public TextCell(ID id, String displayText, TextFontSizeEnum textFontSizeEnum, float targetWidth, boolean isTextFlip) {
        this(id, displayText, textFontSizeEnum, targetWidth, 0, 0);
        setTextFlip(isTextFlip);
    }

    public void setTextFlip(boolean textFlip) {
        this.isTextFlip = textFlip;
    }

    public ID getId() {
        return id;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {

        initBitmapFont();

        if (null != bitmapFont && StringUtils.isNotBlank(text)) {
            Color displayColor = highlighted ? ColorProperties.HIGHLIGHT : textColor;
            bitmapFont.setColor(displayColor.r, displayColor.g, displayColor.b, displayColor.a * getColor().a);
            GlyphLayout layout =
                    bitmapFont.draw(batch, text, getX(), isTextFlip ? getY() + textHeight : getTextStartYPosition(), getWidth(), textAlign,
                            wrap);
            textHeight = layout.height;
            setHeight(layout.height);
        }
    }

    private void initBitmapFont() {
        if (null == bitmapFont && null != textFontSize) {
            bitmapFont = FontGeneratorManager.getFont(textFontSize);
            float newLineHeight = bitmapFont.getCapHeight() + 10;
            if (newLineHeight != bitmapFont.getData().lineHeight) {
                originalLineHeight = bitmapFont.getData().lineHeight;
            }
            if (isNarrowLineHeight) {
                bitmapFont.getData().setLineHeight(newLineHeight);
            }
        }
    }

    private float getTextStartYPosition() {
        return getY() - (getHeight() - textHeight) / 2;
    }

    @Override
    public boolean remove() {
        if (null != bitmapFont && originalLineHeight > 0) {
            bitmapFont.getData().setLineHeight(originalLineHeight);
        }
        return super.remove();
    }


    @Override
    public Color getColor() {
        return super.getColor();
    }

    public void adjustNarrowLineHeight() {
        isNarrowLineHeight = true;
        initBitmapFont();
        setText(text);
    }

    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
}
