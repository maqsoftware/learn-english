package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.scene2d.actions.IAdvanceActionListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.SentenceWordTypeUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

/**
 * Created by siu-chun.chi on 5/5/2017.
 */
public class WordBlockActor extends Group {

    private static final Color WORD_COLOR = Color.WHITE;
    private static final Color HIGHLIGHTED_TEXT_COLOR = Color.valueOf("FFEB3B");
    private final String word;
    private String speechImageName;
    private Label label;
    private TextFontSizeEnum WORD_FONT_SIZE = TextFontSizeEnum.FONT_72;

    private IAdvanceActionListener<String> onWordAudioPlayedListener;

    private UnderlineActor underline;

    private Image block;

    private int wordIndexInSentence;

    public WordBlockActor(String givenWord, String speech, final AbstractAutoCognitaScreen screen) {
        this(givenWord);
        this.speechImageName = SentenceWordTypeUtils.getImageNameByWordType(speech);
        setWordBlock(getWordBlockTexture(word), screen);
    }

    public WordBlockActor(String word) {
        this.word = word.trim();
        Gdx.app.log(getClass().getName(), "building the word = " + word);
    }

    protected void setWordBlock(Texture texture, final AbstractAutoCognitaScreen screen) {
        block = new Image(texture);
        addActor(block);
        label = new Label(word, getWordLabelStyle());
        addActor(label);
        if (null == block) {
            setSize(label.getWidth(), label.getHeight());
        } else {
            configLabelText(label, block);
            setSize(block.getWidth(), block.getHeight());
        }

        addBlockClickListener(screen);
    }

    private Texture getWordBlockTexture(String text) {
        Texture texture = AssetManagerUtils.getTextureWithWait(Config.SENTENCE_IMAGE_PATH + "/" +
                speechImageName + getImageLengthByWord(text) + ".png");

        float wordWidth = LetterUtils.getTotalWidthOfWord(text, WORD_FONT_SIZE);
        if (wordWidth >= texture.getWidth() ||
                texture.getWidth() - wordWidth <= 15) {
            texture = AssetManagerUtils.getTextureWithWait(Config.SENTENCE_IMAGE_PATH + "/" +
                    speechImageName + getImageLengthByWord(text + " " + " ") + ".png");
        }

        return texture;
    }

    private Label.LabelStyle getWordLabelStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(WORD_FONT_SIZE);
        labelStyle.fontColor = WORD_COLOR;

        return labelStyle;
    }

    protected void configLabelText(Label label, Image wordBlock) {
        label.setSize(wordBlock.getWidth(), wordBlock.getHeight());
        label.setAlignment(Align.center);
    }

    private void addBlockClickListener(final AbstractAutoCognitaScreen screen) {
        addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                label.getStyle().fontColor = HIGHLIGHTED_TEXT_COLOR;
                screen.playSound(word, new AbstractSoundPlayListener() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        doComplete();
                    }

                    @Override
                    public void onStop() {
                        super.onStop();
                        doComplete();
                    }

                    private void doComplete() {
                        label.getStyle().fontColor = WORD_COLOR;
                        if (null != onWordAudioPlayedListener) {
                            onWordAudioPlayedListener.onComplete(word);
                        }
                    }
                });
            }
        });
    }

    private int getImageLengthByWord(String word) {
        int wordLength = word.length();
        if (wordLength % 2 == 0) {
            wordLength++;
        }

        if (wordLength > 11) {
            wordLength = 11;
        }

        return wordLength;
    }

    @Override
    public boolean remove() {
        AssetManagerUtils.unloadAllTexture();
        return super.remove();
    }

    public void setUnderline(UnderlineActor underline) {
        this.underline = underline;
    }

    public void clearUnderline() {
        if (null != underline) {
            underline.setContainWord(null);
            underline = null;
        }
    }

    public void setOnWordAudioPlayedListener(IAdvanceActionListener<String> onWordAudioPlayedListener) {
        this.onWordAudioPlayedListener = onWordAudioPlayedListener;
    }

    public String getWord() {
        return word;
    }

    public int getWordIndexInSentence() {
        return wordIndexInSentence;
    }

    public void setWordIndexInSentence(int wordIndexInSentence) {
        this.wordIndexInSentence = wordIndexInSentence;
    }

    public void setText(String text) {
        text = text.trim();
        label.setText(text);

        if (null != block && text.length() != word.length()) {

            int originalImageLength = getImageLengthByWord(word);
            int futureImageLength = getImageLengthByWord(text);

            if (StringUtils.isNotBlank(speechImageName) && originalImageLength != futureImageLength) {
                block.setDrawable(new SpriteDrawable(new Sprite(getWordBlockTexture(text))));
            }
        }
    }

    public boolean isAllowDragToUnderLine() {
        return true;
    }

}
