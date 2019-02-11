package com.maqautocognita.section.sentence;

import com.maqautocognita.bo.WordWithSpeech;
import com.maqautocognita.constant.ActivityCodeEnum;
import com.maqautocognita.scene2d.actions.IAdvanceActionListener;
import com.maqautocognita.scene2d.actors.UnderlineActor;
import com.maqautocognita.scene2d.actors.WordBlockActor;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.screens.AbstractSentenceScreen;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by siu-chun.chi on 5/5/2017.
 */

public class SentenceMultipleChoiceSection extends AbstractSentenceSection {

    private static final int WORD_BLOCK_RIGHT_MARGIN = 50;

    private static final int WORD_BLOCK_BOTTOM_MARGIN_FOR_TABLET = 100;

    private static final int WORD_BLOCK_BOTTOM_MARGIN_FOR_MOBILE = 50;

    private List<UnderlineActor> underlineActorList;

    /**
     * store the list of word which has not been played
     */
    private List<WordWithSpeech> playWordList;

    /**
     * store the word currently playing for multiple choice
     */
    private WordWithSpeech playingWord;

    public SentenceMultipleChoiceSection(AbstractSentenceScreen abstractSentenceScreen) {
        super(abstractSentenceScreen);
    }

    @Override
    public void onShow(ActivityCodeEnum activityCodeEnum) {

        String words[] = sentence.words.split(",");
        String speechs[] = sentence.wordsPartOfSpeech.split(",");

        List<WordWithSpeech> wordList = new ArrayList<WordWithSpeech>(words.length);
        playWordList = new ArrayList<WordWithSpeech>(words.length);
        int index = 0;
        for (String word : words) {
            WordWithSpeech wordWithSpeech = new WordWithSpeech();
            wordWithSpeech.word = word.trim();
            wordWithSpeech.speech = speechs[index];
            playWordList.add(wordWithSpeech);
            wordList.add(wordWithSpeech);
            index++;
        }

        showWordBlock(wordList);

        drawSpeakerInCenterX();

    }

    @Override
    public void hide() {
        super.hide();
        numberOfFails = 0;
        if (null != underlineActorList) {
            underlineActorList.clear();
            underlineActorList = null;
        }

    }

    private void showWordBlock(final List<WordWithSpeech> wordList) {
        Collections.shuffle(playWordList);
        playingWord = playWordList.get(0);

        Gdx.app.log(getClass().getName(), "going to play the word = " + playingWord.word);

        //init random words which will be show in the screen
        int numberOfChoice = wordList.size() / 2;
        final List<WordBlockActor> wordBlockList = new ArrayList<WordBlockActor>(numberOfChoice);
        final List<WordWithSpeech> randomWordList = new ArrayList<WordWithSpeech>(numberOfChoice - 1);

        for (WordWithSpeech wordWithSpeech : wordList) {
            //the list will only store the word which is not playing
            if (!playingWord.equals(wordWithSpeech)) {
                randomWordList.add(wordWithSpeech);
            }
        }

        //random the word list
        Collections.shuffle(randomWordList);
        for (int i = 0; i < numberOfChoice; i++) {
            if (0 == i) {
                //add the playing word (must be)
                wordBlockList.add(new WordBlockActor(playingWord.word, playingWord.speech, abstractSentenceScreen));
            } else {
                // add other words as choice for user to select
                wordBlockList.add(new WordBlockActor(randomWordList.get(i).word, randomWordList.get(i).speech, abstractSentenceScreen));
            }
        }

        Collections.shuffle(wordBlockList);

        float totalWidth = 0;
        float totalHeight = 0;
        float startX = ScreenUtils.getNavigationBarStartXPosition();
        for (WordBlockActor wordBlock : wordBlockList) {
            if (ScreenUtils.isTablet && startX + totalWidth + wordBlock.getWidth() < ScreenUtils.getNavigationRightArrowStartXPosition()) {
                totalWidth += wordBlock.getWidth() + WORD_BLOCK_RIGHT_MARGIN;
            } else {
                totalHeight += wordBlock.getHeight() + getWordBlockBottomMargin();
                startX = 0;
            }
        }

        float startWordBlockXPosition = ScreenUtils.getXPositionForCenterObject(totalWidth);
        float startWordBlockYPosition = ScreenUtils.getStartYPositionForCenterObjectWithoutNavigationBar(totalHeight);

        if (ScreenUtils.isTablet) {
            initWordBlockPositionForTablet(wordBlockList, startWordBlockXPosition, startWordBlockYPosition);
        } else {
            initWordBlockPositionForMobile(wordBlockList, startWordBlockYPosition);
        }

        for (WordBlockActor wordBlock : wordBlockList) {
            wordBlock.setOnWordAudioPlayedListener(new IAdvanceActionListener<String>() {
                @Override
                public void onComplete(String word) {

                    if (playWordList.size() > 0) {
                        boolean isMatch = false;
                        String toPlayWrongSound = "true";
                        if (word.equals(playingWord.word)) {
                            playWordList.remove(playingWord);
                            isMatch = true;
                            toPlayWrongSound = "false";
                        }


                        if (0 == playWordList.size()) {
                            //which mean completed
                            abstractSentenceScreen.showNextSection(0);
                        } else if (isMatch) {
                            abstractSentenceScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                                @Override
                                public void onCorrectSoundPlayed() {
                                    Gdx.app.postRunnable(new Runnable() {
                                        @Override
                                        public void run() {
                                            //remove all wordblock
                                            for (WordBlockActor wordBlockActor : wordBlockList) {
                                                wordBlockActor.remove();
                                            }
                                            //regenerate the word block
                                            showWordBlock(wordList);
                                        }
                                    });

                                }
                            });
                        }else if(!isMatch){
                            abstractSentenceScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                                @Override
                                public void onCorrectSoundPlayed() {

                                }
                            });
                        }
                    }
                }
            });

            stage.addActor(wordBlock);

        }
    }

    private float getWordBlockBottomMargin() {
        return ScreenUtils.isTablet ? WORD_BLOCK_BOTTOM_MARGIN_FOR_TABLET : WORD_BLOCK_BOTTOM_MARGIN_FOR_MOBILE;
    }

    private void initWordBlockPositionForTablet(List<WordBlockActor> wordBlockList, float startWordBlockXPosition,
                                                float startWordBlockYPosition) {

        final float originalStartWordBlockXPosition = startWordBlockXPosition;

        for (WordBlockActor wordBlock : wordBlockList) {
            if (startWordBlockXPosition + wordBlock.getWidth() > ScreenUtils.getNavigationRightArrowStartXPosition()) {
                //change to next row
                startWordBlockXPosition = originalStartWordBlockXPosition;
                startWordBlockYPosition = startWordBlockYPosition - (wordBlock.getHeight() + getWordBlockBottomMargin());
            }
            wordBlock.setPosition(startWordBlockXPosition, startWordBlockYPosition - wordBlock.getHeight());

            startWordBlockXPosition += wordBlock.getWidth() + WORD_BLOCK_RIGHT_MARGIN;
        }
    }

    private void initWordBlockPositionForMobile(List<WordBlockActor> wordBlockList, float startWordBlockYPosition) {
        for (WordBlockActor wordBlock : wordBlockList) {
            wordBlock.setPosition(ScreenUtils.getXPositionForCenterObject(wordBlock.getWidth()),
                    startWordBlockYPosition - wordBlock.getHeight());
            startWordBlockYPosition -= wordBlock.getHeight() + getWordBlockBottomMargin();
        }
    }


}
