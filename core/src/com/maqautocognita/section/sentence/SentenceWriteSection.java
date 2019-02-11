package com.maqautocognita.section.sentence;

import com.maqautocognita.Config;
import com.maqautocognita.bo.AbstractSentence;
import com.maqautocognita.constant.ActivityCodeEnum;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.screens.AbstractSentenceScreen;
import com.maqautocognita.service.HandWritingRecognizeScreenService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

/**
 * Created by siu-chun.chi on 5/5/2017.
 */

public class SentenceWriteSection<T extends AbstractSentence> extends AbstractSentenceSection<T> implements HandWritingRecognizeScreenService.IHandWritingRecognizeListener {

    private static final int WRITING_AREA_BOTTOM_MARGIN = ScreenUtils.getScreenHeightWithoutNavigationBar() / 4;
    private static final int WRITING_AREA_PADDING = 20;

    private static final int WRITING_AREA_SIZE_IN_MOBILE = 920;
    /**
     * It is mainly used to check if the sentence draw is finish after below second
     */
    private static final int CHECK_SENTENCE_DRAWING_STOP_TIME_IN_SECOND = 3;
    private HandWritingRecognizeScreenService handWritingRecognizeScreenService;
    private Image writingArea;
    private boolean isRequiredShowCorrectSentence;

    private boolean isAlreadyWriteCorrect;

    public SentenceWriteSection(AbstractSentenceScreen abstractSentenceScreen) {
        super(abstractSentenceScreen);
        handWritingRecognizeScreenService = new HandWritingRecognizeScreenService(this);
        handWritingRecognizeScreenService.setDefaultCheckAlphabetDrawingStopTimeInSecond(CHECK_SENTENCE_DRAWING_STOP_TIME_IN_SECOND);
    }

    @Override
    public void onShow(ActivityCodeEnum activityCodeEnum) {


        String labelText = getLabelText();

        boolean is3LineRequiredForMobile = false;


        if (!ScreenUtils.isTablet &&
                LetterUtils.getHeightOfWordWithWrap(labelText, getWritingAreaStyle(WritingPadFontSize.MOBILE_2_LINE).font, WRITING_AREA_SIZE_IN_MOBILE) >
                        WRITING_AREA_SIZE_IN_MOBILE) {
            writingArea = new Image(AssetManagerUtils.getTextureWithWait(Config.SENTENCE_IMAGE_PATH + "writing area 3-lines smartphone.png"));
            is3LineRequiredForMobile = true;
        } else {
            //draw the writing area
            writingArea = new Image(AssetManagerUtils.getTextureWithWait(Config.SENTENCE_IMAGE_PATH +
                    (ScreenUtils.isTablet ? "writing area.png" : "writing area smartphone.png")));
        }

        writingArea.setAlign(Align.center);
        writingArea.setX(ScreenUtils.getXPositionForCenterObject(writingArea.getWidth()));
        writingArea.setY(WRITING_AREA_BOTTOM_MARGIN);

        stage.addActor(writingArea);


        drawSentenceWithSpeaker(labelText, writingArea.getX());

        if (ActivityCodeEnum.READ_AND_WRITE.equals(activityCodeEnum)) {

            boolean isRequiredShowWritingHints;

            if ("1".equals(abstractSentenceScreen.getUnitCode())) {
                isRequiredShowWritingHints = true;
            } else {
                //From unit 2 and onwards, randomly show the hints
                isRequiredShowWritingHints = Math.random() < 0.5;
            }

            if (isRequiredShowWritingHints) {


                TextFontSizeEnum fontSizeEnum = ScreenUtils.isTablet ? WritingPadFontSize.TABLET : WritingPadFontSize.MOBILE_2_LINE;

                if (ScreenUtils.isTablet) {
                    if (LetterUtils.getTotalWidthOfWord(labelText, fontSizeEnum) > writingArea.getWidth()) {
                        fontSizeEnum = TextFontSizeEnum.FONT_288;
                    }
                }

                Label writingHints = new Label(labelText, getWritingAreaStyle(fontSizeEnum));
                writingHints.setWidth(writingArea.getWidth());
                writingHints.setX(writingArea.getX() + WRITING_AREA_PADDING);

                if (ScreenUtils.isTablet) {
                    writingHints.setAlignment(Align.left);
                    writingHints.setHeight(writingHints.getStyle().font.getCapHeight());
                    writingHints.setY(writingArea.getY() + 130);
                } else {
                    writingHints.setWrap(true);
                    writingHints.setAlignment(Align.topLeft);
                    writingHints.setY(writingArea.getY() + writingArea.getHeight() - writingHints.getHeight());

                    if (is3LineRequiredForMobile) {
                        writingHints.getStyle().font.getData().setLineHeight(LetterUtils.getHeightOfWord(labelText, WritingPadFontSize.MOBILE_2_LINE));
                        writingHints.setY(writingHints.getY() + 100);
                    }

                }

                stage.addActor(writingHints);
            }
        } else {

            isRequiredShowCorrectSentence = true;
            //only show when the correct sentence is wrote
            sentenceLabel.setVisible(false);
            setImageToCenter(speaker);
        }

    }

    protected String getLabelText() {
        String labelText = sentence.writingSentence;
        if (StringUtils.isBlank(labelText)) {
            labelText = sentence.sentence;
            if (StringUtils.isBlank(labelText)) {
                labelText = sentence.words;
            }
        }
        return labelText;
    }

    private Label.LabelStyle getWritingAreaStyle(TextFontSizeEnum fontSize) {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = getLabelFont(fontSize);
        labelStyle.fontColor = ColorProperties.TEXT;
        return labelStyle;
    }

    private BitmapFont getLabelFont(TextFontSizeEnum fontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nationfd.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize.getFontSize();
        parameter.incremental = true;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.spaceX = 15;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }

    @Override
    public void hide() {
        super.hide();
        isRequiredShowCorrectSentence = false;
        handWritingRecognizeScreenService.clearDrawPoints();
        handWritingRecognizeScreenService.clearCorrectDrawPoints();
        if (null != writingArea) {
            writingArea.remove();
            writingArea = null;
        }
        isAlreadyWriteCorrect = false;
    }

    @Override
    public void render() {
        super.render();
        if (null != handWritingRecognizeScreenService) {
            handWritingRecognizeScreenService.drawLine();
        }
    }

    public void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        handWritingRecognizeScreenService.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
    }

    public void touchUp() {
        handWritingRecognizeScreenService.touchUp();
    }

    public void touchDown(int screenX, int screenY) {
        handWritingRecognizeScreenService.
                touchDown(getTouchingScreenX(screenX), getTouchingScreenY(screenY), screenX, screenY);
    }

    private int getTouchingScreenX(int screenX) {
        float widthRatio = ScreenUtils.getScreenWidth() / ScreenUtils.viewport.width;
        return (int) (screenX * widthRatio - ScreenUtils.viewport.x * widthRatio);
    }

    private int getTouchingScreenY(int screenY) {
        float heightRatio = ScreenUtils.getScreenHeight() / ScreenUtils.viewport.height;
        return (int) (ScreenUtils.getScreenHeight() - screenY * heightRatio + ScreenUtils.viewport.y * heightRatio);
    }

    @Override
    public boolean isDrawAllow(int screenX, int screenY) {
        return TouchUtils.isTouched(writingArea, screenX, screenY);
    }

    @Override
    public boolean isSaveCorrectDrawPointsRequired() {
        return false;
//        return true;
    }

    @Override
    public void whenCorrectLetterWrite() {
        if (isRequiredShowCorrectSentence) {

            if (null != sentenceLabel) {
                setSentenceLabelText(sentenceLabel.getText().toString());
                sentenceLabel.setVisible(true);

            }
        }

        if (!isAlreadyWriteCorrect) {
            abstractSentenceScreen.showNextSection(0);
            isAlreadyWriteCorrect = true;
        }
    }

    @Override
    public void whenWrongLetterWrite() {
        if (isRequiredShowCorrectSentence) {

            if (null != sentenceLabel) {
                setSentenceLabelText(sentenceLabel.getText().toString());
                sentenceLabel.setVisible(true);

            }
        }
    }

    @Override
    public void whenLetterWriteFails() {

    }

    @Override
    public boolean isWriteCorrect() {
        return true;
    }

    @Override
    public void afterDrawPointAdded(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

    }

    @Override
    public boolean isRequiredClearDrawPointsAfterTimesUp() {
        return true;
    }


    private static class WritingPadFontSize {
        static final TextFontSizeEnum TABLET = TextFontSizeEnum.FONT_414;
        static final TextFontSizeEnum MOBILE_2_LINE = TextFontSizeEnum.FONT_360;
    }
}
