package com.maqautocognita.scene2d.actors;

import com.maqautocognita.bo.Dictionary;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.scene2d.ui.TextCell;
import com.maqautocognita.service.DictionaryService;
import com.maqautocognita.service.IBMWatonTranslateAndSpeechService;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.LoadingIconUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class DictionaryActor extends Table {

    private static final Color WORD_COLOR = Color.valueOf("FFEB3B");
    private static final Color WORD_TYPE_COLOR = Color.valueOf("B8B8B8");
    private static final Color SYNONYMS_COLOR = Color.valueOf("8BC34A");
    private static final Color DEFINITION_COLOR = Color.WHITE;
    private static final Color EXAMPLES_COLOR = Color.valueOf("FFF59D");
    private final float paragraphSpaceHeight;
    private final float lineHeight;
    private TextFontSizeEnum textFontSizeEnum;
    private TextFontSizeEnum wordTextFontSizeEnum;
    private float targetWidth;
    private TextCell speakingTextCell;

    private Table loadingTable;

    public DictionaryActor(float width, TextFontSizeEnum textFontSizeEnum, TextFontSizeEnum wordTextFontSizeEnum) {
        this.textFontSizeEnum = textFontSizeEnum;
        this.wordTextFontSizeEnum = wordTextFontSizeEnum;
        align(Align.top);
        setWidth(width);

        lineHeight = FontGeneratorManager.getFont(textFontSizeEnum).getLineHeight() / 2;
        paragraphSpaceHeight = lineHeight * 2;

        padLeft(lineHeight);
        padRight(lineHeight);
        targetWidth = getWidth() - lineHeight * 2;

    }

    public void setWord(final String word) {
        clearChildren();
        speakingTextCell = null;

        addText(word, wordTextFontSizeEnum, WORD_COLOR, paragraphSpaceHeight, true);

        if (null == loadingTable) {
            loadingTable = LoadingIconUtils.getLoadingIcon();
            loadingTable.setSize(getWidth(), getStage().getHeight());
            loadingTable.setX(getStage().getWidth() - getWidth());
            loadingTable.toFront();
            getStage().addActor(loadingTable);
        }

        loadingTable.setVisible(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadDictionary(word);
                loadingTable.setVisible(false);
            }
        }).start();
    }

    private void addText(final String text, TextFontSizeEnum textFontSizeEnum, Color textColor, float padBottom, boolean isRequiredToPlaySound) {

        if (StringUtils.isNotBlank(text)) {
            final TextCell textCell = new TextCell(text, textFontSizeEnum, targetWidth);
            textCell.setTextFlip(true);
            textCell.setTextAlign(Align.top | Align.left);
            textCell.setTextColor(textColor);
            textCell.setWrap(true);

            if (isRequiredToPlaySound) {
                textCell.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {

                        if (null != speakingTextCell) {
                            speakingTextCell.setHighlighted(false);
                        }

                        speakingTextCell = textCell;

                        textCell.setHighlighted(true);

                        IBMWatonTranslateAndSpeechService.getInstance().textToSpeech(text, new AbstractSoundPlayListener() {
                            @Override
                            public void onComplete() {
                                super.onComplete();
                                textCell.setHighlighted(false);
                            }

                            @Override
                            public void onStop() {
                                super.onStop();
                                textCell.setHighlighted(false);
                            }
                        });

                    }
                });
            }


            add(textCell)
                    .padBottom(padBottom).expandX().fillX().height(
                    LetterUtils.getHeightOfWordWithWrap(text, textFontSizeEnum, targetWidth))
                    .align(Align.top).row();
        }
    }

    private void loadDictionary(String word) {
        List<Dictionary> dictionaryList = DictionaryService.getInstance().getDictionaryByWord(word);
        if (CollectionUtils.isNotEmpty(dictionaryList)) {
            for (Dictionary dictionary : dictionaryList) {
                //draw the word type
                addText(dictionary.wordType, textFontSizeEnum, WORD_TYPE_COLOR, false);
                addText(dictionary.synonyms, textFontSizeEnum, SYNONYMS_COLOR, true);
                if (CollectionUtils.isNotEmpty(dictionary.definitionList)) {
                    for (String definition : dictionary.definitionList) {
                        addText(definition, textFontSizeEnum, DEFINITION_COLOR, paragraphSpaceHeight, true);
                    }
                }
                if (CollectionUtils.isNotEmpty(dictionary.exampleList)) {
                    for (String example : dictionary.exampleList) {
                        if (StringUtils.isNotBlank(example)) {
                            addText(example, textFontSizeEnum, EXAMPLES_COLOR, paragraphSpaceHeight, true);
                        }
                    }
                }
            }
        }
    }

    private void addText(String menuText, TextFontSizeEnum textFontSizeEnum, Color textColor, boolean isRequiredToPlaySound) {
        addText(menuText, textFontSizeEnum, textColor, lineHeight, isRequiredToPlaySound);
    }


}
