package com.maqautocognita.section.sentence;

import com.maqautocognita.Config;
import com.maqautocognita.bo.AbstractSentence;
import com.maqautocognita.constant.ActivityCodeEnum;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.screens.AbstractSentenceScreen;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StageUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public abstract class AbstractSentenceSection<T extends AbstractSentence> {

    protected static final float SENTENCE_START_Y_POSITION = ScreenUtils.getNavigationBarStartYPosition() - 150;
    protected static final TextFontSizeEnum SENTENCE_FONT_SIZE = TextFontSizeEnum.FONT_72;
    private static final int SPACE_BETWEEN_SENTENCE_AND_SPEAKER = 50;
    protected final AbstractSentenceScreen abstractSentenceScreen;
    protected Stage stage;
    protected T sentence;
    protected int numberOfFails;
    protected Label sentenceLabel;
    protected Image speaker;
    private CustomCamera camera;

    public AbstractSentenceSection(AbstractSentenceScreen abstractSentenceScreen) {
        this.abstractSentenceScreen = abstractSentenceScreen;
        camera = new CustomCamera();
        camera.setWorldWidth(ScreenUtils.getScreenWidth());
        camera.setToOrtho(false, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
    }

    public void show(T sentence, ActivityCodeEnum activityCodeEnum) {
        this.sentence = sentence;
        camera.update();
        if (null == stage) {
            ScreenViewport screenViewport = new ScreenViewport(camera);
            screenViewport.setUnitsPerPixel(ScreenUtils.widthRatio);
            stage = new Stage(screenViewport);
        }

        onShow(activityCodeEnum);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if (null != Gdx.input.getInputProcessor()) {
            inputMultiplexer.addProcessor(0, Gdx.input.getInputProcessor());
        }

        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        if (null != sentence) {
            //it is always play the introduction audio
            abstractSentenceScreen.playSound(sentence.audioFileName);
        }
    }

    public abstract void onShow(ActivityCodeEnum activityCodeEnum);

    protected Image drawSentenceWithSpeaker(String sentence, float x) {
        sentenceLabel = drawSentence(sentence, x);
        //draw the speaker
        speaker = drawSpeaker(sentenceLabel.getX() + sentenceLabel.getWidth() + SPACE_BETWEEN_SENTENCE_AND_SPEAKER,
                getScreenStartYPosition() - sentenceLabel.getHeight());

        speaker.setY(sentenceLabel.getY() + (sentenceLabel.getHeight() - speaker.getHeight()) / 2);

        return speaker;
    }

    protected Label drawSentence(String sentence, float x) {
        sentenceLabel = new Label(sentence, getSentenceStyle());
        sentenceLabel.setX(x);
        adjustSentenceWidth();
        sentenceLabel.setHeight(sentenceLabel.getPrefHeight());
        sentenceLabel.setY(getScreenStartYPosition() - sentenceLabel.getPrefHeight());

        addSpeakSentenceAction(sentence, sentenceLabel, sentenceLabel);

        stage.addActor(sentenceLabel);

        return sentenceLabel;
    }

    protected Image drawSpeaker(float x, float y) {
        speaker = new Image(AssetManagerUtils.getTextureWithWait(Config.SENTENCE_IMAGE_PATH + "speaker.png"));
        addSpeaker(speaker, x, y);
        return speaker;
    }

    protected Label.LabelStyle getSentenceStyle() {
        return getSentenceStyle(getSentenceFontSize());
    }

    protected Label.LabelStyle getSentenceStyle(TextFontSizeEnum fontSize) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(fontSize);
        labelStyle.fontColor = ColorProperties.TEXT;
        labelStyle.font.getData().setLineHeight(labelStyle.font.getCapHeight() + 20);

        return labelStyle;
    }

    private void addSpeaker(Image speaker, float x, float y) {
        speaker.setPosition(x, y);
        addSpeaker(speaker);
    }

    /**
     * set the given text in the {@link #sentenceLabel} and adjust the x position {@link #speaker} at the end of the label
     *
     * @param text
     */
    protected void setSentenceLabelText(String text) {
        if (null != sentenceLabel) {
            sentenceLabel.setText(text);

            adjustSentenceWidth();

            if (null != speaker) {
                speaker.setX(sentenceLabel.getX() +
                        sentenceLabel.getWidth() + SPACE_BETWEEN_SENTENCE_AND_SPEAKER);
            }
        }
    }

    private void adjustSentenceWidth() {
        float speakerWidth = null == speaker ? 0 : speaker.getWidth();
        float sentenceMaximumWidth = ScreenUtils.getScreenWidth() - sentenceLabel.getX() - SPACE_BETWEEN_SENTENCE_AND_SPEAKER * 2 - speakerWidth;

        float textWidth = LetterUtils.getTotalWidthOfWord(sentenceLabel.getText().toString(), getSentenceFontSize());

        if (sentenceLabel.getX() +
                textWidth
                > sentenceMaximumWidth) {
            sentenceLabel.setWidth(sentenceMaximumWidth);
            sentenceLabel.setWrap(true);
        } else {
            sentenceLabel.setWidth(textWidth);
        }

    }

    protected TextFontSizeEnum getSentenceFontSize() {
        return SENTENCE_FONT_SIZE;
    }

    protected Image drawSpeakerInCenterX() {
        Image speaker = new Image(AssetManagerUtils.getTextureWithWait(Config.SENTENCE_IMAGE_PATH + "speaker.png"));
        setImageToCenter(speaker);
        addSpeaker(speaker);
        return speaker;
    }

    protected void setImageToCenter(Image image) {
        image.setPosition(ScreenUtils.getXPositionForCenterObject(image.getWidth()),
                getScreenStartYPosition() - image.getHeight());
    }

    private void addSpeaker(Image speaker) {
        stage.addActor(speaker);
        addSpeakSentenceAction(getSpeakSentence(), speaker, sentenceLabel);

    }

    protected float getScreenStartYPosition() {
        return SENTENCE_START_Y_POSITION;
    }

    protected void addSpeakSentenceAction(final String sentence, final Actor listener, final Label displayLabel) {
        listener.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {

                if (StringUtils.isNotBlank(sentence)) {

                    final Color originalColor = (null == displayLabel) ? null : displayLabel.getStyle().fontColor;

                    if (null != displayLabel) {
                        displayLabel.getStyle().fontColor = ColorProperties.HIGHLIGHT;
                    }
                    abstractSentenceScreen.playSound(getAudioFileName(sentence), new AbstractSoundPlayListener() {
                        @Override
                        public void onComplete() {
                            super.onComplete();
                            onFinish();
                        }

                        @Override
                        public void onStop() {
                            super.onStop();
                            onFinish();
                        }

                        private void onFinish() {
                            if (null != displayLabel && null != originalColor) {
                                displayLabel.getStyle().fontColor = originalColor;
                            }
                        }
                    });
                }
            }
        });
    }

    protected String getSpeakSentence() {
        if (null != sentenceLabel && StringUtils.isNotBlank(sentenceLabel.getText().toString())) {
            return sentenceLabel.getText().toString();
        }
        return null;
    }

    private String getAudioFileName(String word) {
        if (StringUtils.isNotBlank(word)) {
            return word.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
        }
        return null;
    }

    protected void onComplete() {
        abstractSentenceScreen.showNextSection(numberOfFails);
        numberOfFails = 0;
    }

    public void hide() {
        StageUtils.dispose(stage);
        stage = null;
        AssetManagerUtils.unloadAllTexture();
        sentenceLabel = null;
        speaker = null;
        sentence = null;
    }

    public void render() {
        if (null != stage) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }


    protected Texture getTextureFromSentenceFolder(String imageName) {
        return AssetManagerUtils.getTextureWithWait(Config.SENTENCE_IMAGE_PATH + imageName);
    }
}
