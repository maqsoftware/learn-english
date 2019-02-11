package com.maqautocognita.section.sentence;

import com.maqautocognita.bo.SwahiliSentence;
import com.maqautocognita.bo.WordWithSpeech;
import com.maqautocognita.constant.ActivityCodeEnum;
import com.maqautocognita.constant.SentenceWordType;
import com.maqautocognita.scene2d.actors.VerbObjectPrefixWordBlockActor;
import com.maqautocognita.scene2d.actors.VerbRootFor2PrefixWordBlockActor;
import com.maqautocognita.scene2d.actors.VerbRootFor3PrefixWordBlockActor;
import com.maqautocognita.scene2d.actors.VerbSubjectPrefixWordBlockActor;
import com.maqautocognita.scene2d.actors.VerbTensePrefixWordBlockActor;
import com.maqautocognita.scene2d.actors.WordBlockActor;
import com.maqautocognita.screens.AbstractSentenceScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by siu-chun.chi on 5/5/2017.
 */

public class SwahiliSentenceArrangeSection extends SentenceArrangeSection<SwahiliSentence> {


    private List<WordWithSpeech> wordWithSpeechList;

    private VerbRootFor2PrefixWordBlockActor verbRootFor2PrefixWordBlockActor;

    private VerbRootFor3PrefixWordBlockActor verbRootFor3PrefixWordBlockActor;

    public SwahiliSentenceArrangeSection(AbstractSentenceScreen abstractSentenceScreen) {
        super(abstractSentenceScreen);
        maximumNumberOfUnderlinePerRow = 3;
    }

    @Override
    protected float generateWordsInWordTray(final Image wordTray, final ActivityCodeEnum activityCodeEnum) {
        float wordBlockHeight = 0;

        wordWithSpeechList = new ArrayList<WordWithSpeech>();
        String[] words = getSentence().split(" ");
        Gdx.app.log(getClass().getName(), "words " + ArrayUtils.printArray(words));
        String[] speeches = sentence.sentencePartOfSpeech.split(",");
        for (int i = 0; i < words.length; i++) {
            WordWithSpeech wordWithSpeech = new WordWithSpeech();
            wordWithSpeech.word = words[i];
            wordWithSpeech.speech = speeches[i];
            wordWithSpeechList.add(wordWithSpeech);
        }

        List<WordBlockActor> wordBlockActorList = new ArrayList<WordBlockActor>();

        //check if there is any verb here
        for (int index = 0; index < wordWithSpeechList.size(); index++) {
            WordWithSpeech wordWithSpeech = wordWithSpeechList.get(index);
            if (SentenceWordType.isVerb(wordWithSpeech.speech)) {
                if (StringUtils.isNotBlank(sentence.verbSubjectPrefix)) {
                    //suppose the verb subject prefix is not empty
                    WordBlockActor verbSubjectPrefixBlock = new VerbSubjectPrefixWordBlockActor(sentence.verbSubjectPrefix, abstractSentenceScreen);
                    verbSubjectPrefixBlock.setWordIndexInSentence(index);
                    wordBlockActorList.add(verbSubjectPrefixBlock);
                }
                if (StringUtils.isNotBlank(sentence.verbTensePrefix)) {
                    //suppose the verb tense prefix is not empty
                    WordBlockActor verbTensePrefixBlock = new VerbTensePrefixWordBlockActor(sentence.verbTensePrefix, abstractSentenceScreen);
                    verbTensePrefixBlock.setWordIndexInSentence(index);
                    wordBlockActorList.add(verbTensePrefixBlock);
                }
                if (StringUtils.isNotBlank(sentence.verbObjectPrefix)) {
                    //suppose the verb object prefix is not empty
                    WordBlockActor verbObjectPrefixBlock = new VerbObjectPrefixWordBlockActor(sentence.verbObjectPrefix, abstractSentenceScreen);
                    wordBlockActorList.add(verbObjectPrefixBlock);

                    verbRootFor3PrefixWordBlockActor = new VerbRootFor3PrefixWordBlockActor(sentence.verbRoot, abstractSentenceScreen);
                    verbRootFor3PrefixWordBlockActor.setWordIndexInSentence(index);
                    wordBlockActorList.add(verbRootFor3PrefixWordBlockActor);

                } else {
                    verbRootFor2PrefixWordBlockActor = new VerbRootFor2PrefixWordBlockActor(sentence.verbRoot, abstractSentenceScreen);
                    verbRootFor2PrefixWordBlockActor.setWordIndexInSentence(index);
                    wordBlockActorList.add(verbRootFor2PrefixWordBlockActor);
                }
            } else {
                WordBlockActor wordBlockActor = new WordBlockActor(wordWithSpeech.word.toLowerCase(), wordWithSpeech.speech, abstractSentenceScreen);
                wordBlockActor.setWordIndexInSentence(index);
                wordBlockActorList.add(wordBlockActor);
            }
        }

        Collections.shuffle(wordBlockActorList);

        final float originalStartXPosition = wordTray.getX() + WORD_TRAY_PADDING;

        float startX = originalStartXPosition;
        float startY = wordTray.getY() + wordTray.getHeight() - WORD_TRAY_PADDING;

        for (WordBlockActor wordBlock : wordBlockActorList) {

            if (startX + wordBlock.getWidth() + WORD_TRAY_PADDING > wordTray.getX() + wordTray.getWidth()) {
                //next row
                startY -= wordBlock.getHeight() + WORD_TRAY_PADDING;
                startX = originalStartXPosition;
            }

            wordBlock.setPosition(startX, startY - wordBlock.getHeight());

            addWordBlockDragListener(wordBlock, activityCodeEnum, wordBlock.getWordIndexInSentence(), wordTray);

            stage.addActor(wordBlock);
            wordBlockHeight = wordBlock.getHeight();

            startX += wordBlock.getWidth() + WORD_TRAY_PADDING;

        }

        return wordBlockHeight;
    }

    private String getSentence() {
        return sentence.sentence.replaceAll("\"", "");
    }

    @Override
    protected String[] getSentenceWords() {
        List<String> wordList = new ArrayList<String>();

        for (WordWithSpeech wordWithSpeech : wordWithSpeechList) {
            if (SentenceWordType.isVerb(wordWithSpeech.speech)) {
                wordList.add(sentence.verbRoot);
            } else {
                wordList.add(wordWithSpeech.word);
            }
        }
        return wordList.toArray(new String[]{});
    }

    @Override
    protected void whenLastWordDraggedToUnderLine(WordBlockActor wordBlock, String word) {
        //if this is the last word, add the sentence last character to the word such as ".?"
        wordBlock.setText(word + getSentence().substring(getSentence().length() - 1));
    }

    @Override
    protected boolean isAllWordMatch() {
        if (super.isAllWordMatch()) {
            if (null != verbRootFor2PrefixWordBlockActor) {
                return verbRootFor2PrefixWordBlockActor.isAddCompleted();
            }
        }

        return false;
    }

    protected void whenWordBlockDragOutsideUnderline(Image wordTray, WordBlockActor wordBlock, float originalX, float originalY, int touchX, int touchY) {
        boolean isDragged = false;
        if (!wordBlock.isAllowDragToUnderLine()) {
            if (wordBlock instanceof VerbSubjectPrefixWordBlockActor) {
                if (null != verbRootFor2PrefixWordBlockActor) {
                    if (verbRootFor2PrefixWordBlockActor.isTouchSubject(touchX, touchY)) {
                        if (0 == wordBlock.getWordIndexInSentence()) {
                            //which mean it is the first word in the sentence
                            wordBlock.setText(upperCaseTheFirstLetter(wordBlock.getWord()));
                        }
                        verbRootFor2PrefixWordBlockActor.addSubjectBlock(wordBlock);
                        isDragged = true;
                    }
                }
            }

            if (wordBlock instanceof VerbTensePrefixWordBlockActor) {
                if (null != verbRootFor2PrefixWordBlockActor) {
                    if (verbRootFor2PrefixWordBlockActor.isTouchTense(touchX, touchY)) {
                        verbRootFor2PrefixWordBlockActor.addTenseBlock(wordBlock);
                        isDragged = true;
                    }
                }
            }
        }

        if (isDragged) {
            //check if all word match
            if (isAllWordMatch()) {
                onComplete();
            }
        } else {
            super.whenWordBlockDragOutsideUnderline(wordTray, wordBlock, originalX, originalY, touchX, touchY);
        }
    }
}
