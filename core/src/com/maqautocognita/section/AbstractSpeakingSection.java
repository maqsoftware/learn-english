package com.maqautocognita.section;

import com.maqautocognita.Config;
import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.MicrophoneService;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.ScreenUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractSpeakingSection extends AbstractAutoCognitaSection implements IActivityChangeListener, MicrophoneService.IMicrophoneListener {


    private static final int GAP_BETWEEN_WORD = 157;
    private static final int ROW_GAP_BETWEEN_WORD_VERTICAL_MODE = 80;
    private static final int MOBILE_GAP_BETWEEN_WORD = 80;
    private final MicrophoneService microphoneService;
    protected Activity selectedActivity;
    protected List<ScreenObject<String, ScreenObjectType>> screenWordList;
    private String[] playingWords;
    private int totalNumberOfWord;
    private int currentPlayWordEndIndex;
    private TimerService timerService;
    private float timerDuration = 0.5f; // seconds

    /**
     * It is mainly used to indicate if the speech RecognizeFile is required to change, when {@link #setSelectedActivity(Activity, boolean)} is called,
     * it will be set to true, in order to reload a new recognize file
     */
    private boolean isRequiredToChangeRecognizeFile = true;

    public AbstractSpeakingSection(AbstractAutoCognitaScreen alphabetScreen, AbstractAutoCognitaSection.IOnHelpListener onHelpListener) {
        super(alphabetScreen, onHelpListener);
        this.microphoneService = new MicrophoneService(this);
    }

    @Override
    public void render(boolean isShowing) {
        super.render(isShowing);
        if (isShowing) {
            if (isRequiredToChangeRecognizeFile) {
                microphoneService.changeRecognizeFile();
                isRequiredToChangeRecognizeFile = false;
            }
        }
    }

    @Override
    public void render() {

        initScreenObject();

        batch.begin();

        ScreenObjectUtils.draw(batch, screenWordList);

        ScreenObjectUtils.draw(batch, microphoneService.getMicrophone());

        batch.end();

    }

    private void initScreenObject() {

        if (null == screenWordList && ArrayUtils.isNotEmpty(getWords())) {
            initScreenWordList();
        }

    }

    protected abstract String[] getWords();

    protected void initScreenWordList() {
        totalNumberOfWord = getWords().length;


        int numberOfWordToBeShow = getNumberOfWordToBeDisplay();

        int numberOfWordShow = Math.min(numberOfWordToBeShow, totalNumberOfWord - currentPlayWordEndIndex);

        //if there is only 1 word remain after this round , try to display the last word together in this round
        if (1 == totalNumberOfWord - currentPlayWordEndIndex - numberOfWordShow) {
            numberOfWordShow++;
        }

        float wordsTotalWidth = 0;
        float wordsTotalHeight = 0;

        //calculate the total word width in the screen per line
        for (int i = currentPlayWordEndIndex; i < currentPlayWordEndIndex + numberOfWordShow; i++) {
            String word = getWords()[i];

            float wordSize[] = LetterUtils.getSizeOfWord(word, getLetterFontSize());

            if (0 == wordsTotalHeight) {
                wordsTotalHeight = wordSize[1];
            }

            if (wordsTotalWidth + wordSize[0] > Config.SCREEN_CENTER_WIDTH || isVerticalAlign()) {
                //which mean the word will go to next line
                wordsTotalHeight += wordSize[1];

                if (isVerticalAlign()) {
                    wordsTotalHeight += ROW_GAP_BETWEEN_WORD_VERTICAL_MODE;
                }
            } else {
                wordsTotalWidth += wordSize[0] + getGapBetweenWord();
            }
        }

        //remove the last gap for the last word
        wordsTotalWidth -= getGapBetweenWord();

        final float originalXPosition = ScreenUtils.getXPositionForCenterObject(wordsTotalWidth);

        float startXPosition = originalXPosition;
        float startYPosition = getWordStartYPosition(wordsTotalHeight);

        screenWordList = new ArrayList<ScreenObject<String, ScreenObjectType>>(getWords().length);

        playingWords = new String[numberOfWordShow];

        int playingWordIndex = 0;
        for (int i = currentPlayWordEndIndex; i < currentPlayWordEndIndex + numberOfWordShow; i++) {
            String word = getWords()[i];

            playingWords[playingWordIndex] = word;

            if (isVerticalAlign()) {
                startXPosition = ScreenUtils.getXPositionForCenterObject(LetterUtils.getTotalWidthOfWord(word, getLetterFontSize()));
            }

            ScreenObject<String, ScreenObjectType> wordScreenObject = LetterUtils.getTextScreenObjectWithYPositionNotIncludeHeight(word, ScreenObjectType.WORD,
                    startXPosition, startYPosition, word, getLetterFontSize());

            float wordWidth = LetterUtils.getTotalWidthOfWord(word, getLetterFontSize());

            if (startXPosition + wordWidth > Config.SCREEN_CENTER_WIDTH + originalXPosition || isVerticalAlign()) {


                if (isVerticalAlign()) {
                    startYPosition += wordScreenObject.height + ROW_GAP_BETWEEN_WORD_VERTICAL_MODE;
                } else {
                    //append the next line
                    startYPosition -=
                            //because the bitfont will be draw in in lower down position , so the start y position
                            // will be need reduce add the previous line height and the current line height
                            wordScreenObject.height * 2
                                    //gap between each line
                                    - 20;


                    //rollback the x position
                    startXPosition = originalXPosition;
                    //relocate the position for next line
                    wordScreenObject.xPositionInScreen = startXPosition;
                    wordScreenObject.yPositionInScreen = startYPosition + wordScreenObject.height;
                }

            }

            startXPosition += wordWidth + getGapBetweenWord();

            screenWordList.add(wordScreenObject);

            playingWordIndex++;

        }

        currentPlayWordEndIndex += numberOfWordShow;

    }

    /**
     * This is used to indicate maximum number of word should be display per one screen, if there is less than the number of given words {@link #getWords()},
     * the remain words will be display in the next screen after the user played correctly for the current showing words.
     *
     * @return -1 mean showing all words
     */
    protected abstract int getNumberOfWordToBeDisplay();

    protected abstract TextFontSizeEnum getLetterFontSize();

    protected boolean isVerticalAlign() {
        return false;
    }

    private float getGapBetweenWord() {
        return ScreenUtils.isLandscapeMode ? GAP_BETWEEN_WORD : MOBILE_GAP_BETWEEN_WORD;
    }

    protected abstract float getWordStartYPosition(float totalWordHeight);

    @Override
    protected AbstractAudioFile getAudioFile() {
        return selectedActivity;
    }

    @Override
    public void dispose() {
        super.dispose();
        reset();
    }

    @Override
    public void reset() {
        playingWords = null;
        totalNumberOfWord = 0;
        currentPlayWordEndIndex = 0;
        clearScreen();
    }

    private void clearScreen() {
        if (null != screenWordList) {
            screenWordList.clear();
            screenWordList = null;
        }
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        microphoneService.touchUp();
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

//        microphoneService.touchDown(screenX, screenY);

        if (microphoneService.touchDown(screenX, screenY))
        {
            final AudioDevice playbackDevice = Gdx.audio.newAudioDevice(44100, true);
            final AudioRecorder recordingDevice = Gdx.audio.newAudioRecorder(44100, true);
            final short[] samples = new short[44100 * selectedActivity.getAudioDuration()]; // mono audio


            // Record player's audio
//            recordingDevice.read(samples, 0, samples.length);

            // Delay
            new TimerService(new TimerService.ITimerListener() {
                @Override
                public void beforeStartTimer() {
                    Gdx.app.log(getClass().getName(), "touchDown.beforeStartTimer()");
                }

                @Override
                public void onTimerComplete(Object threadIndicator) {
                    Gdx.app.log(getClass().getName(), "touchDown.onTimerComplete()");
                    recordingDevice.dispose();
                    playbackDevice.dispose();
//                    playTheRightAndPlayerAudio(playbackDevice, samples, recordingDevice);
                }
            }).startTimer(null, timerDuration);
        }
    }

    private void playTheRightAndPlayerAudio(final AudioDevice playbackDevice, final short[] samples, final AudioRecorder recordingDevice) {

        abstractAutoCognitaScreen.playSound(selectedActivity.getAudioFileName());

        // Delay
        new TimerService(new TimerService.ITimerListener() {
            @Override
            public void beforeStartTimer() {
                Gdx.app.log(getClass().getName(), "playTheRightAndPlayerAudio.beforeStartTimer()");
            }

            @Override
            public void onTimerComplete(Object threadIndicator) {
                Gdx.app.log(getClass().getName(), "playTheRightAndPlayerAudio.onTimerComplete()");

//                playbackDevice.writeSamples(samples, 0, samples.length);
                recordingDevice.dispose();
                playbackDevice.dispose();
//                playCorrectSound();
            }
        }).startTimer(null, selectedActivity.getAudioDuration());
    }

    private void playCorrectSound() {

        // Delay
        new TimerService(new TimerService.ITimerListener() {
            @Override
            public void beforeStartTimer() {
                Gdx.app.log(getClass().getName(), "playCorrectSound.beforeStartTimer()");
            }

            @Override
            public void onTimerComplete(Object threadIndicator) {
                Gdx.app.log(getClass().getName(), "playCorrectSound.onTimerComplete()");

                abstractAutoCognitaScreen.playSound(abstractAutoCognitaScreen.getCorrectSoundFilename());
            }
        }).startTimer(null, timerDuration);
    }

    @Override
    public void setSelectedActivity(Activity selectedActivity, boolean isMasterTest) {
        isRequiredToChangeRecognizeFile = true;
        this.selectedActivity = selectedActivity;
    }

    @Override
    public boolean isSoundRecognizeRequired() {
        return true;
    }

    @Override
    public void onCorrectAnswerPlayed() {
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {
                if (currentPlayWordEndIndex < totalNumberOfWord) {
                    //if there are word still need to play
                    clearScreen();
                } else {
                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
                }
            }
        });

    }

    @Override
    public boolean onSpeechRecognizeResult(String[] results) {
        if (ArrayUtils.isNotEmpty(results)) {
            Gdx.app.log(this.getClass().getName(), "recogniate result = " + ArrayUtils.toString(results));
            return isAnswerCorrect(results);
        }

        return false;

    }

    /**
     * Check if the given results are matched all playing words
     *
     * @param results
     * @return true if all playing word are speak correctly
     */
    private boolean isAnswerCorrect(String[] results) {
        if (ArrayUtils.isNotEmpty(playingWords)) {
            for (String playingWord : playingWords) {
                if (!ArrayUtils.isContainIgnoreCase(results, playingWord)) {
                    addNumberOfFails();
                    abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                        @Override
                        public void onCorrectSoundPlayed() {

                        }
                    });
                    return false;
                }
            }

            return true;
        }

        return false;
    }

}
