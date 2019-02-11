package com.maqautocognita.section.sentence;

import com.maqautocognita.Config;
import com.maqautocognita.bo.AbstractSentence;
import com.maqautocognita.constant.ActivityCodeEnum;
import com.maqautocognita.scene2d.actors.UnderlineActor;
import com.maqautocognita.scene2d.actors.WordBlockActor;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.screens.AbstractSentenceScreen;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by siu-chun.chi on 5/5/2017.
 */

public class SentenceArrangeSection<T extends AbstractSentence> extends AbstractSentenceSection<T> {

    protected static final int WORD_TRAY_PADDING = 15;
    protected static final int WORD_TRAY_TOP_MARGIN = 50;
    /**
     * The second will show the sentence for just arrange and listen arrange when the user drag word in wrong 3 times
     */
    private static final float SECOND_TO_SHOW_SENTENCE_FOR_JUST_ARRANGE_ACTIVITY = 3;
    private static final int WORD_TRAY_BOTTOM_MARGIN = 20;
    private static final int WORD_TO_UNDERLINE_BOTTOM_PADDING = 20;
    private static final int MAXIMUM_NUMBER_OF_UNDERLINE_PER_ROW_IN_TABLET = 4;

    private static final int MAXIMUM_NUMBER_OF_UNDERLINE_PER_ROW_IN_MOBILE = 3;

    protected int maximumNumberOfUnderlinePerRow;

    protected List<UnderlineActor> underlineActorList;

    private Image speaker;

    private boolean IsSentenceWrong = false ;

    public SentenceArrangeSection(AbstractSentenceScreen abstractSentenceScreen) {
        super(abstractSentenceScreen);
        maximumNumberOfUnderlinePerRow = ScreenUtils.isTablet ? MAXIMUM_NUMBER_OF_UNDERLINE_PER_ROW_IN_TABLET : MAXIMUM_NUMBER_OF_UNDERLINE_PER_ROW_IN_MOBILE;
    }

    @Override
    public void onShow(ActivityCodeEnum activityCodeEnum) {

        //draw the word tray
        Image wordTray = new Image(AssetManagerUtils.getTextureWithWait(Config.SENTENCE_IMAGE_PATH +
                (ScreenUtils.isTablet ? "word tray.png" : "word tray smartphone.png")));
        wordTray.setAlign(Align.center);
        wordTray.setX(ScreenUtils.getXPositionForCenterObject(wordTray.getWidth()));
        wordTray.setY(WORD_TRAY_BOTTOM_MARGIN);
        stage.addActor(wordTray);

        float wordBlockHeight = generateWordsInWordTray(wordTray, activityCodeEnum);

        afterDrawWordTrayAndWordBlocksInTheTray(activityCodeEnum, wordTray, wordBlockHeight);

    }

    @Override
    protected String getSpeakSentence() {
        return sentence.sentence;
    }

    @Override
    protected void onComplete() {
        super.onComplete();
        showSentence();
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

    protected float generateWordsInWordTray(final Image wordTray, final ActivityCodeEnum activityCodeEnum) {
        float wordBlockHeight = 0;


        String[] words = sentence.words.split(",");
        List<Integer> wordIndexList = new ArrayList<Integer>(words.length);
        for (int i = 0; i < words.length; i++) {
            wordIndexList.add(i);
        }

        Collections.shuffle(wordIndexList);

        final float originalStartXPosition = wordTray.getX() + WORD_TRAY_PADDING;

        float startX = originalStartXPosition;
        float startY = wordTray.getY() + wordTray.getHeight() - WORD_TRAY_PADDING;
        for (final int index : wordIndexList) {

            final String word = words[index];

            String speech = null;

            if (StringUtils.isNotBlank(sentence.wordsPartOfSpeech) && sentence.wordsPartOfSpeech.split(",").length > index) {
                speech = sentence.wordsPartOfSpeech.split(",")[index];
            }

            Gdx.app.log(getClass().getName(), "going to generate words in word block " + word + " and the speech " + speech);

            final WordBlockActor wordBlock = new WordBlockActor(word, speech, abstractSentenceScreen);

            if (startX + wordBlock.getWidth() + WORD_TRAY_PADDING > wordTray.getX() + wordTray.getWidth()) {
                //next row
                startY -= wordBlock.getHeight() + WORD_TRAY_PADDING;
                startX = originalStartXPosition;
            }

            wordBlock.setPosition(startX, startY - wordBlock.getHeight());


            stage.addActor(wordBlock);
            startX += wordBlock.getWidth() + WORD_TRAY_PADDING;

            wordBlockHeight = wordBlock.getHeight();

            addWordBlockDragListener(wordBlock, activityCodeEnum, index, wordTray);
        }

        return wordBlockHeight;
    }

    protected void afterDrawWordTrayAndWordBlocksInTheTray(ActivityCodeEnum activityCodeEnum, Image wordTray, float wordBlockHeight) {
        if (ActivityCodeEnum.LISTEN_ARRANGE.equals(activityCodeEnum)) {
            speaker = drawSpeakerInCenterX();
            speaker.setOrigin(speaker.getX(), speaker.getY());
        } else if (ActivityCodeEnum.READ_ARRANGE.equals(activityCodeEnum)) {
            drawSentenceWithSpeaker(sentence.sentence, wordTray.getX());
        }

        if (ActivityCodeEnum.JUST_ARRANGE.equals(activityCodeEnum) || ActivityCodeEnum.LISTEN_ARRANGE.equals(activityCodeEnum)) {
            drawSentence(sentence.sentence, wordTray.getX());
            sentenceLabel.toFront();
            sentenceLabel.setVisible(false);
        }

        if (ArrayUtils.isNotEmpty(getSentenceWords())) {
            generateUnderLine(getSentenceWords(),
                    getSentenceWordTypes(),
                    wordTray, wordBlockHeight);
        }
    }

    protected void addWordBlockDragListener(final WordBlockActor wordBlock,
                                            final ActivityCodeEnum activityCodeEnum, final int index, final Image wordTray) {
        wordBlock.setOrigin(wordBlock.getX(), wordBlock.getY());

        wordBlock.addListener(new DragListener() {
            /**
             * This is used to store the touch position in the actor
             */
            private float startTouchXPosition = -1;
            private float startTouchYPosition = -1;

            private float originalX;
            private float originalY;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                originalX = wordBlock.getX();
                originalY = wordBlock.getY();

                startTouchXPosition = event.getStageX() - wordBlock.getX();
                startTouchYPosition = event.getStageY() - wordBlock.getY();
                wordBlock.toFront();
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                wordBlock.setPosition(event.getStageX() - startTouchXPosition, event.getStageY() - startTouchYPosition);

                if (CollectionUtils.isNotEmpty(underlineActorList)) {
                    if (TouchUtils.isTouched(underlineActorList.get(0), (int) event.getStageX(), (int) event.getStageY()) &&
                            !underlineActorList.get(0).isContainWord()) {
                        whenFirstWordDraggingOnUnderLine(wordBlock);
                    } else {
                        wordBlock.setText(wordBlock.getWord());
                    }
                }

            }


            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                boolean isDragToUnderLine = false;

                //clear the previous dragged underline
                wordBlock.clearUnderline();

                int touchX = (int) event.getStageX();

                int touchY = (int) event.getStageY();

                if (wordBlock.isAllowDragToUnderLine()) {
                    for (UnderlineActor underlineActor : underlineActorList) {
                        if (TouchUtils.isTouched(underlineActor, touchX, touchY) && !underlineActor.isContainWord()) {

                            isDragToUnderLine = true;

                            wordBlock.setX(underlineActor.getX() +
                                    ScreenUtils.getXPositionForCenterObject(wordBlock.getWidth(), underlineActor.getWidth()));
                            wordBlock.setY(underlineActor.getY() + underlineActor.getLineHeight() + WORD_TO_UNDERLINE_BOTTOM_PADDING);

                            wordBlock.setUnderline(underlineActor);

                            underlineActor.setContainWord(wordBlock.getWord());
                            if (underlineActor.isCorrect()) {
                                if (index == underlineActorList.size() - 1) {
                                    //if this is the last word, add the punctuation by get it from the sentence last character
                                    whenLastWordDraggedToUnderLine(wordBlock, underlineActor.getWord());
                                } else if (0 == index) {
                                    whenFirstWordDraggedToUnderLine(wordBlock, underlineActor.getWord());
                                }

                            } else {
                                if (null != sentenceLabel && !ActivityCodeEnum.READ_ARRANGE.equals(activityCodeEnum) && (numberOfFails + 1) % 3 == 0) {

                                    showSentence();

                                    new TimerService(new TimerService.ITimerListener() {
                                        @Override
                                        public void beforeStartTimer() {

                                        }

                                        @Override
                                        public void onTimerComplete(Object threadIndicator) {
                                            hideSentence();
                                        }
                                    }).startTimer(null, SECOND_TO_SHOW_SENTENCE_FOR_JUST_ARRANGE_ACTIVITY);
                                }
                                numberOfFails++;
                            }


                            //check if all word match to the belongs underline
                            if (isAllWordMatch()) {
                                onComplete();
                                IsSentenceWrong = false;
                            }
                            else if(IsSentenceWrong){
                                abstractSentenceScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                                    @Override
                                    public void onCorrectSoundPlayed() {

                                    }
                                });
                            }

                            break;
                        }
                    }
                }

                if (!isDragToUnderLine) {
                    whenWordBlockDragOutsideUnderline(wordTray, wordBlock, originalX, originalY, touchX, touchY);
                }

            }

        });
    }

    protected String[] getSentenceWords() {
        return sentence.words.split(",");
    }

    protected float generateUnderLine(String[] words, String[] partsOfSpeech, Image wordTray,
                                      float wordBlockHeight) {


        float underLineWidth = getUnderLineMaximumWidth(wordTray.getWidth());

        float startYPosition = wordTray.getY() + wordTray.getHeight() + WORD_TRAY_TOP_MARGIN;

        float startXPosition = wordTray.getX();

        float maximumWidth = wordTray.getWidth();

        final float originalStartXPosition = startXPosition;
        final float originalStartYPosition = startYPosition;

        float totalHeightForAllUnderlines = 0;

        for (int i = 0; i < words.length; i++) {
            UnderlineActor underlineActor = new UnderlineActor(words[i], partsOfSpeech[i],
                    underLineWidth, wordBlockHeight * 2);
            underlineActor.setPosition(startXPosition, originalStartYPosition);
            totalHeightForAllUnderlines = underlineActor.getHeight();
            stage.addActor(underlineActor);
            startXPosition += underlineActor.getWidth();

            if (null == underlineActorList) {
                underlineActorList = new ArrayList<UnderlineActor>();
            }

            underlineActorList.add(underlineActor);
        }

        if (underLineWidth * words.length > maximumWidth) {
            totalHeightForAllUnderlines = (underlineActorList.size() / maximumNumberOfUnderlinePerRow + 1) * underlineActorList.get(0).getHeight();
            startYPosition += totalHeightForAllUnderlines;
            startXPosition = originalStartXPosition;
            for (UnderlineActor underlineActor : underlineActorList) {
                if (startXPosition + underlineActor.getWidth() > originalStartXPosition + maximumWidth) {
                    startYPosition -= underlineActor.getHeight();
                    startXPosition = originalStartXPosition;
                }
                underlineActor.setPosition(startXPosition, startYPosition - underlineActor.getHeight());
                startXPosition += underlineActor.getWidth();
            }
        }

        return originalStartYPosition + totalHeightForAllUnderlines;
    }

    protected String[] getSentenceWordTypes() {
        return sentence.wordsPartOfSpeech.split(",");
    }

    protected void whenFirstWordDraggingOnUnderLine(WordBlockActor wordBlock) {
        wordBlock.setText(upperCaseTheFirstLetter(wordBlock.getWord()));
    }

    protected void whenLastWordDraggedToUnderLine(WordBlockActor wordBlock, String word) {
        //if this is the last word, add the sentence last character to the word such as ".?"
        wordBlock.setText(word + sentence.sentence.substring(sentence.sentence.length() - 1));
    }

    protected void whenFirstWordDraggedToUnderLine(WordBlockActor wordBlock, String word) {
        //if this is the first word, make the first letter to upper case
        wordBlock.setText(upperCaseTheFirstLetter(word));
    }

    protected void showSentence() {
        if (null != speaker) {
            speaker.setX(sentenceLabel.getX() + sentenceLabel.getWidth() + 20);
        }

        if (null != sentenceLabel) {
            sentenceLabel.setVisible(true);
        }
    }

    private void hideSentence() {
        sentenceLabel.setVisible(false);
        if (null != speaker) {
            speaker.setX(speaker.getOriginX());
        }
    }

    protected boolean isAllWordMatch() {
        int numberOfWords = underlineActorList.size();
        int correctStatus;
        int Counter1=0,Counter2=0,Counter0=0;
        boolean status = false;
        IsSentenceWrong = false;
        for (UnderlineActor underlineActor : underlineActorList) {
            correctStatus = underlineActor.isCorrectEnglishComprehension();
            if (correctStatus == 1) {
                Counter1++;
            }
            else if (correctStatus == 2){
                Counter2++;
            }
            else{
                Counter0++;
            }
        }

        if(Counter0 > 0 && Counter1 == 0){
            IsSentenceWrong = true;
            status = false;
        }
        else if(Counter2 == numberOfWords){
            IsSentenceWrong = false;
            status = true;
        }

        return status;
    }

    protected void whenWordBlockDragOutsideUnderline(Image wordTray, WordBlockActor wordBlock, float originalX, float originalY, int touchX, int touchY) {
        //check if dragged to the wordTray
        if (TouchUtils.isTouched(wordTray, (int) wordBlock.getX(), (int) wordBlock.getY())) {
            //roll back to original text,because when the word block dragged to the underline,
            // the text may change such as if it is the first word of the sentence,
            // the first letter in the word will become upper
            wordBlock.setText(wordBlock.getWord());
            wordBlock.setPosition(wordBlock.getOriginX(), wordBlock.getOriginY());
        } else {
            //rollback to start drag position
            wordBlock.setPosition(originalX, originalY);
        }
    }

    protected float getUnderLineMaximumWidth(float screenWidth) {

        return screenWidth / maximumNumberOfUnderlinePerRow;
    }

    protected String upperCaseTheFirstLetter(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }
}
