package com.maqautocognita.section.sentence;

import com.maqautocognita.Config;
import com.maqautocognita.bo.ReadingComprehension;
import com.maqautocognita.constant.ActivityCodeEnum;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.scene2d.actors.UnderlineActor;
import com.maqautocognita.scene2d.actors.WordBlockActor;
import com.maqautocognita.screens.AbstractSentenceScreen;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;

/**
 * Created by siu-chun.chi on 7/2/2017.
 */

public class ReadingComprehensionSection extends SentenceArrangeSection<ReadingComprehension> {

    private static final int SPACE_BETWEEN_READING_PASSAGE_AND_QUESTION = 20;

    private static final Color QUESTION_FONT_COLOR = Color.valueOf("1565C0");

    private static final int LABEL_MARGIN = 20;

    /**
     * smaller the font size if the reading passage is too long
     */
    private static float SMALLER_FONT_SCALE = 0.5f;

    private Label readingPassageLabel;
    private Label questionLabel;

    private Image readingPassageSpeaker;
    private Image questionSpeaker;
    private Label questionMark;

    public ReadingComprehensionSection(AbstractSentenceScreen abstractSentenceScreen) {
        super(abstractSentenceScreen);
    }

    @Override
    public void hide() {
        super.hide();
        readingPassageLabel = null;
        questionLabel = null;
        readingPassageSpeaker = null;
        questionSpeaker = null;
        questionMark = null;
    }


    @Override
    protected void afterDrawWordTrayAndWordBlocksInTheTray(ActivityCodeEnum activityCodeEnum, Image wordTray, float wordBlockHeight) {

        boolean isReadingPassageSmaller = false;

        if (StringUtils.isNotBlank(sentence.readingPassage)) {
            readingPassageLabel = drawSentence(sentence.readingPassage, wordTray.getX());

            if (readingPassageLabel.getHeight() > wordBlockHeight * 3) {
                readingPassageLabel.setFontScale(SMALLER_FONT_SCALE);
                isReadingPassageSmaller = true;
            }

            readingPassageLabel.setHeight(readingPassageLabel.getPrefHeight());
            readingPassageLabel.setY(getScreenStartYPosition() - readingPassageLabel.getPrefHeight());

        }

        if (StringUtils.isNotBlank(sentence.question)) {

            if (null == readingPassageLabel) {
                questionLabel = drawSentence(sentence.question, wordTray.getX());
            } else {
                Label.LabelStyle style = getSentenceStyle();
                style.fontColor = QUESTION_FONT_COLOR;
                questionLabel = new Label(sentence.question, style);
                questionLabel.setWidth(wordTray.getWidth());
                questionLabel.setWrap(true);

                if (isReadingPassageSmaller) {
                    questionLabel.setFontScale(SMALLER_FONT_SCALE);
                }

                questionLabel.setX(wordTray.getX());
                questionLabel.setY(readingPassageLabel.getY() - questionLabel.getPrefHeight() - SPACE_BETWEEN_READING_PASSAGE_AND_QUESTION);
                addSpeakSentenceAction(sentence.question, questionLabel, questionLabel);
                stage.addActor(questionLabel);
            }

        }

        if (ActivityCodeEnum.COMPREHENSION_LISTEN.equals(activityCodeEnum)) {
            if (null != readingPassageLabel) {
                readingPassageLabel.setVisible(false);
                readingPassageSpeaker = new Image(AssetManagerUtils.getTextureWithWait(Config.SENTENCE_IMAGE_PATH + "speaker.png"));
                setImageToCenter(readingPassageSpeaker);
                stage.addActor(readingPassageSpeaker);
                addSpeakSentenceAction(sentence.readingPassage, readingPassageSpeaker, readingPassageLabel);
            }

        }

        generateUnderLine(wordTray, wordBlockHeight);

    }

    @Override
    protected void whenFirstWordDraggingOnUnderLine(WordBlockActor wordBlock) {
        if (StringUtils.isBlank(sentence.beforeAnswer)) {
            super.whenFirstWordDraggingOnUnderLine(wordBlock);
        }
    }

    @Override
    protected void whenLastWordDraggedToUnderLine(WordBlockActor wordBlock, String word) {

    }

    @Override
    protected void whenFirstWordDraggedToUnderLine(WordBlockActor wordBlock, String word) {
        if (StringUtils.isBlank(sentence.beforeAnswer)) {
            super.whenFirstWordDraggedToUnderLine(wordBlock, word);
        }
    }

    @Override
    protected void showSentence() {
        super.showSentence();
        if (null != readingPassageLabel) {
            readingPassageLabel.setVisible(true);
        }

        if (null != questionLabel) {
            questionLabel.setVisible(true);
        }

        if (null != readingPassageSpeaker) {
            readingPassageSpeaker.setVisible(false);
        }

        if (null != questionSpeaker) {
            questionSpeaker.setVisible(false);
        }

        if (null != questionMark) {
            questionMark.setVisible(false);
        }
    }

    private void generateUnderLine(Image wordTray,
                                   float wordBlockHeight) {

        float startYPosition = wordTray.getY() + wordTray.getHeight() + WORD_TRAY_TOP_MARGIN;
        float startXPosition = wordTray.getX();
        float maximumWidth = wordTray.getWidth();

        String[] words = sentence.answerWords.split(",");
        String[] partsOfSpeech = sentence.sentencePartOfSpeech.split(",");


        float totalWidth = 0;

        Label beforeAnswerLabel = null;
        if (StringUtils.isNotBlank(sentence.beforeAnswer)) {
            beforeAnswerLabel = new Label(sentence.beforeAnswer, getSentenceStyle(super.getSentenceFontSize()));

            addSpeakSentenceAction(sentence.beforeAnswer, beforeAnswerLabel, beforeAnswerLabel);

            beforeAnswerLabel.setPosition(startXPosition, startYPosition + LABEL_MARGIN);
            beforeAnswerLabel.setWrap(true);
            stage.addActor(beforeAnswerLabel);
            if (beforeAnswerLabel.getWidth() > maximumWidth) {
                beforeAnswerLabel.setWidth(maximumWidth);
            }
            startXPosition = beforeAnswerLabel.getX() + beforeAnswerLabel.getWidth() + LABEL_MARGIN;
        }

        final float originalStartXPosition = startXPosition;
        final float originalStartYPosition = startYPosition;

        float underLineWidth = getUnderLineMaximumWidth(wordTray.getWidth());


        for (int i = 0; i < words.length; i++) {
            UnderlineActor underlineActor = new UnderlineActor(words[i], partsOfSpeech[i],
                    underLineWidth, wordBlockHeight * 2);
            underlineActor.setPosition(startXPosition, originalStartYPosition);
            stage.addActor(underlineActor);
            startXPosition += underlineActor.getWidth();
            totalWidth += underlineActor.getWidth();

            if (null == underlineActorList) {
                underlineActorList = new ArrayList<UnderlineActor>();
            }

            underlineActorList.add(underlineActor);

        }

        UnderlineActor lastUnderlineActor = underlineActorList.get(underlineActorList.size() - 1);

        Label afterAnswerLabel = null;
        if (StringUtils.isNotBlank(sentence.afterAnswer)) {
            afterAnswerLabel = new Label(sentence.afterAnswer, getSentenceStyle(super.getSentenceFontSize()));
            afterAnswerLabel.setPosition(startXPosition + LABEL_MARGIN, startYPosition + LABEL_MARGIN);
            afterAnswerLabel.setWrap(true);
            addSpeakSentenceAction(sentence.beforeAnswer, afterAnswerLabel, afterAnswerLabel);

            if (afterAnswerLabel.getWidth() > maximumWidth) {
                afterAnswerLabel.setWidth(maximumWidth);
            }
            stage.addActor(afterAnswerLabel);
        }


        if (null != beforeAnswerLabel) {
            totalWidth += Math.max(1, Math.ceil(beforeAnswerLabel.getWidth() / lastUnderlineActor.getWidth())) * lastUnderlineActor.getWidth();
        }
        if (null != afterAnswerLabel) {
            totalWidth += Math.max(1, Math.ceil(afterAnswerLabel.getWidth() / lastUnderlineActor.getWidth())) * lastUnderlineActor.getWidth();
        }


        final float maximumXPosition = wordTray.getX() + maximumWidth;

        if (totalWidth > maximumWidth) {
            float totalHeightForAllUnderlines = ((int) Math.ceil(totalWidth / maximumWidth)) *
                    lastUnderlineActor.getHeight();

            Gdx.app.log(getClass().getName(), "totalWidth = " + totalWidth + "maximumWidth = " + maximumWidth + "totalHeightForAllUnderlines = " + totalHeightForAllUnderlines);

            startYPosition += totalHeightForAllUnderlines;
            startXPosition = originalStartXPosition;

            if (null != beforeAnswerLabel) {
                beforeAnswerLabel.setY(startYPosition - lastUnderlineActor.getHeight() + LABEL_MARGIN);
            }

            for (UnderlineActor underlineActor : underlineActorList) {
                if (startXPosition + underlineActor.getWidth() > maximumXPosition) {
                    startYPosition -= underlineActor.getHeight();
                    startXPosition = wordTray.getX();
                }
                underlineActor.setPosition(startXPosition, startYPosition - underlineActor.getHeight());
                startXPosition += underlineActor.getWidth();
            }


            if (null != afterAnswerLabel) {
                if (startXPosition + afterAnswerLabel.getWidth() > maximumXPosition) {
                    startXPosition = wordTray.getX();
                    startYPosition -= lastUnderlineActor.getHeight();
                }
                afterAnswerLabel.setPosition(startXPosition, startYPosition - lastUnderlineActor.getHeight() + LABEL_MARGIN);
            }
        }
    }

    @Override
    protected TextFontSizeEnum getSentenceFontSize() {
        return ScreenUtils.isTablet ? super.getSentenceFontSize() : TextFontSizeEnum.FONT_108;
    }

    @Override
    protected float getScreenStartYPosition() {
        return ScreenUtils.getNavigationBarStartYPosition() - 50;
    }
}
