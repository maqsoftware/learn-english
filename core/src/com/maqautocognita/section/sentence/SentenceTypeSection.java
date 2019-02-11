package com.maqautocognita.section.sentence;

import com.maqautocognita.constant.ActivityCodeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.screens.AbstractSentenceScreen;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by siu-chun.chi on 5/14/2017.
 */

public class SentenceTypeSection extends AbstractSentenceSection {

    private static final Color TYPE_COLOR = ColorProperties.BORDER;

    private static final Color UNDERLINE_COLOR = ColorProperties.DISABLE_TEXT;

    private TextArea textField;

    /**
     * This is the line which draw under the text field {@link #textField}
     */
    private ShapeRenderer textFieldUnderline;

    private int numberOfTextAreaLine;

    public SentenceTypeSection(AbstractSentenceScreen abstractSentenceScreen) {
        super(abstractSentenceScreen);
    }

    @Override
    public void onShow(ActivityCodeEnum activityCodeEnum) {

        final String sentenceText = sentence.sentence;

        float startXPosition = ScreenUtils.getNavigationBarStartXPosition();

        drawSentenceWithSpeaker(sentenceText, startXPosition);
        if (!ActivityCodeEnum.READ_AND_TYPE.equals(activityCodeEnum)) {
            sentenceLabel.setVisible(false);
            setImageToCenter(speaker);

        }

        textField = new TextArea(null, getWritingAreaStyle());

        textField.setWidth(ScreenUtils.getNavigationBarWidth());

        float lineHeight = textField.getStyle().font.getLineHeight();

        numberOfTextAreaLine = (int) Math.ceil(LetterUtils.getTotalWidthOfWord(sentenceText, getSentenceFontSize()) / textField.getWidth());

        textField.setHeight(numberOfTextAreaLine * lineHeight);
        textField.setPosition(startXPosition, speaker.getY() - speaker.getHeight() - textField.getHeight() - 100);

        textField.setMaxLength(sentenceText.length());

        textField.setTextFieldListener(new TextField.TextFieldListener() {

            @Override
            public void keyTyped(final TextField textField, char key) {
                if (StringUtils.isNotBlank(textField.getText()) &&
                        removeAllSpecialCharacter(textField.getText()).equalsIgnoreCase(removeAllSpecialCharacter(sentenceText))) {
                    //make sure the correct sentence is show
                    sentenceLabel.setVisible(true);
                    setSentenceLabelText(sentenceText);
                    abstractSentenceScreen.showNextSection(0);
                }
            }
        });

        stage.addActor(textField);
        stage.setKeyboardFocus(textField);
        Gdx.input.setOnscreenKeyboardVisible(true);
    }

    @Override
    protected float getScreenStartYPosition() {
        return ScreenUtils.getNavigationBarStartYPosition() - 50;
    }

    @Override
    public void hide() {
        Gdx.input.setOnscreenKeyboardVisible(false);
        textField = null;
        if (null != textFieldUnderline) {
            textFieldUnderline.dispose();
            textFieldUnderline = null;
        }
        super.hide();
    }

    @Override
    public void render() {

        if (null != textField) {
            if (null == textFieldUnderline) {
                textFieldUnderline = new ShapeRenderer();
                textFieldUnderline.setColor(UNDERLINE_COLOR);
                textFieldUnderline.getProjectionMatrix().setToOrtho2D(0, 0, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
            }

            textFieldUnderline.begin(ShapeRenderer.ShapeType.Filled);
            for (int i = 0; i < numberOfTextAreaLine; i++) {
                textFieldUnderline.rect(textField.getX(),
                        textField.getY(), textField.getWidth(), 4f);
            }
            textFieldUnderline.end();
        }
        super.render();

    }

    private TextField.TextFieldStyle getWritingAreaStyle() {

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = FontGeneratorManager.getFont(getSentenceFontSize());
        textFieldStyle.fontColor = TYPE_COLOR;

        Pixmap cursor = new Pixmap(3, 1, Pixmap.Format.RGB565);
        cursor.setColor(TYPE_COLOR);
        cursor.fill();
        textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(cursor)));

        cursor.dispose();

        return textFieldStyle;
    }

    private String removeAllSpecialCharacter(String text) {
        return text.trim().replaceAll("\\.", "").replaceAll("\\,", "").replaceAll("\\?", "");
    }
}
