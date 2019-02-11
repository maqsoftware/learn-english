package com.maqautocognita.section.Phonic;

import com.maqautocognita.Config;
import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.bo.Question;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ImageUtils;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.AbstractDrawWordPictureSection;
import com.maqautocognita.section.IActivityChangeListener;
import com.maqautocognita.service.SoundService;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicFillInTheBlankSection extends AbstractDrawWordPictureSection implements IActivityChangeListener {

    private static final int GAP_WIDTH_BETWEEN_HINT = 50;
    /**
     * The fixed width for the phonic sound which show in the center of the screen as a choice for user to select.
     * The phonic sound which only contain 1 letter
     */
    private static final float SINGLE_PHONIC_SOUND_WIDTH = 100;
    /**
     * The fixed width for the phonic sound which show in the center of the screen as a choice for user to select.
     * The phonic sound which contain more than 1 letter
     */
    private static final float DOUBLE_PHONIC_SOUND_WIDTH = 150;
    private final TextFontSizeEnum textFontSize;
    private float hintStartYPosition;
    private List<ScreenObject<String, ScreenObjectType>> screenObjectList;
    private Question question;
    private String[] questions;
    private String[] answers;
    private String[] choices;
    private Activity selectedActivity;
    private List<TextureScreenObject<String, ScreenObjectType>> pictureScreenObjectList;
    //it is used to indicate the current playing word in the list of {@link selectedQuestion}
    private int currentPlayingQuestionIndex;
    private ScreenObject<String, ScreenObjectType> touchingScreenObject;
    /**
     * Store the list of selected phonic sound, maximum is 2
     * There is some case required 2 phonic sounds for the answer
     */
    private List<String> selectedAnswerList;
    private List<ScreenObject<String, ScreenObjectType>> wordList;

    public PhonicFillInTheBlankSection(AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(abstractAutoCognitaScreen, onHelpListener);
        if (ScreenUtils.isTablet) {
            textFontSize = TextFontSizeEnum.FONT_72;
        } else {
            textFontSize = TextFontSizeEnum.FONT_108;
        }
    }

    @Override
    protected float getHeightForPictureSection() {
        return hintStartYPosition;
    }

    @Override
    protected String getAudioFileName(String word, int index) {
        return "word_" + getAnswer(index) + ".m4a";
    }

    @Override
    protected void afterPictureInitialized(TextureScreenObject border, int index) {

        if (null == pictureScreenObjectList) {
            pictureScreenObjectList = new ArrayList<TextureScreenObject<String, ScreenObjectType>>();
        }

        pictureScreenObjectList.add(border);

        if (currentPlayingQuestionIndex == index) {
            border.isHighlighted = true;
        }
    }

    @Override
    protected void afterWordCreated(List<ScreenObject<String, ScreenObjectType>> wordList) {

        if (null == this.wordList) {
            this.wordList = new ArrayList<ScreenObject<String, ScreenObjectType>>();
        }

        this.wordList.addAll(wordList);

        for (ScreenObject<String, ScreenObjectType> screenObject : wordList) {
            if (screenObject.id.equals("_")) {
                screenObject.isHighlighted = true;
            }
        }
    }

    @Override
    public void setSelectedActivity(Activity selectedActivity, boolean isMasterTest) {
        this.selectedActivity = selectedActivity;
        currentPlayingQuestionIndex = 0;
        question = selectedActivity.getQuestion();
    }

    @Override
    protected void render() {
        initQuestionAndAnswer();
        if (null == screenObjectList) {
            initGraphic();
        }
        render(screenObjectList);
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return null;
    }

    @Override
    protected List<String> getIntroductionAudioFileName() {
        return getAudioFile().getShortInstructionAudioFilenameList();
    }

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
        clearScreenObjects();
        answers = null;
        questions = null;
        choices = null;
    }

    private void clearScreenObjects() {
        if (null != screenObjectList) {
            screenObjectList.clear();
            screenObjectList = null;
        }
        if (null != pictureScreenObjectList) {
            pictureScreenObjectList.clear();
            pictureScreenObjectList = null;
        }

        if (null != wordList) {
            wordList.clear();
            wordList = null;
        }
        touchingScreenObject = null;
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (null != touchingScreenObject) {


            if (ScreenObjectType.HINTS.equals(touchingScreenObject.objectType)) {
                if (isCorrectAnswer(touchingScreenObject.id)) {
                    //prevent the user to touch other key or object before finish all audio playing, such as the correct sound, the playing word
                    abstractAutoCognitaScreen.setTouchAllow(false);
                }
                final List<ScreenObject<String, ScreenObjectType>> touchingPhonicSoundSymbolList = ScreenObjectUtils.getScreenObjectListById(screenObjectList, touchingScreenObject.id);

                if (StringUtils.isBlank(touchingScreenObject.audioFileName)) {
                    Gdx.app.error(getClass().getName(), "No sound audio found for " + touchingScreenObject.id);
                    doCheckCorrectAnswer();
                } else {
                    //play the phonic sound
                    abstractAutoCognitaScreen.playSound(touchingScreenObject.audioFileName, new AbstractSoundPlayListener() {

                        private final ScreenObject<String, ScreenObjectType> touchingPhonicSound = touchingScreenObject;

                        @Override
                        public void onPlay(int audioListIndex, long millisecond) {
                            if (isShowing) {
                                for (ScreenObject phonicSoundSymbol : touchingPhonicSoundSymbolList) {

                                    if (phonicSoundSymbol instanceof TextureScreenObject) {
                                        TextureScreenObject textureScreenObjectPhonicSoundSymbol = (TextureScreenObject) phonicSoundSymbol;

                                        if (ScreenObjectType.PHONIC_SOUND_SYMBOL_LEFT.equals(phonicSoundSymbol.objectType)) {
                                            if (millisecond < 300) {
                                                textureScreenObjectPhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolOpenIconWave75Percent();
                                            } else if (millisecond < 600) {
                                                textureScreenObjectPhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolOpenIconWave50Percent();
                                            } else {
                                                textureScreenObjectPhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolOpenIconWave25Percent();
                                            }
                                        } else if (ScreenObjectType.PHONIC_SOUND_SYMBOL_RIGHT.equals(phonicSoundSymbol.objectType)) {
                                            if (millisecond < 300) {
                                                textureScreenObjectPhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolCloseIconWave75Percent();
                                            } else if (millisecond < 600) {
                                                textureScreenObjectPhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolCloseIconWave50Percent();
                                            } else {
                                                textureScreenObjectPhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolCloseIconWave25Percent();
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onComplete() {
                            resumeNormal();
                            doCheckCorrectAnswer();
                        }

                        private void resumeNormal() {
                            if (isShowing) {
                                //make sure the phonic symbol is rollback to the original image
                                for (ScreenObject phonicSoundSymbol : touchingPhonicSoundSymbolList) {
                                    if (ScreenObjectType.PHONIC_SOUND_SYMBOL_LEFT.equals(phonicSoundSymbol.objectType)) {
                                        TextureScreenObject textureScreenObjectPhonicSoundSymbol = (TextureScreenObject) phonicSoundSymbol;
                                        textureScreenObjectPhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolOpenIcon();
                                    } else if (ScreenObjectType.PHONIC_SOUND_SYMBOL_RIGHT.equals(phonicSoundSymbol.objectType)) {
                                        TextureScreenObject textureScreenObjectPhonicSoundSymbol = (TextureScreenObject) phonicSoundSymbol;
                                        textureScreenObjectPhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolCloseIcon();
                                    }
                                }

                                //add the selected phonic sound to the selected answer list
                                if (null == selectedAnswerList) {
                                    selectedAnswerList = new ArrayList<String>(2);
                                }

                                if (2 == selectedAnswerList.size()) {
                                    //remove the first one
                                    selectedAnswerList.remove(0);
                                }
                                selectedAnswerList.add(touchingPhonicSound.id);
                            }

                        }

                        @Override
                        public void onStop() {
                            resumeNormal();
                            touchingPhonicSound.isHighlighted = false;
                        }


                    });
                }

            } else {
                //if it is touching other object beside of the choice
                //try to play the audio
                abstractAutoCognitaScreen.playSound(touchingScreenObject.audioFileName);
            }
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        ScreenObject<String, ScreenObjectType> currentTouchingScreenObject = ScreenObjectUtils.getTouchingScreenObject(screenObjectList, screenX, screenY);

        if (null != currentTouchingScreenObject) {

            if (null != touchingScreenObject) {
                if (ScreenObjectType.HINTS.equals(touchingScreenObject.objectType) && !touchingScreenObject.equals(currentTouchingScreenObject)) {
                    //if previous selection is still playing
                    touchingScreenObject.isHighlighted = false;
                }
            }

            if (ScreenObjectType.HINTS.equals(currentTouchingScreenObject.objectType)) {
                currentTouchingScreenObject.isHighlighted = true;
            }

            touchingScreenObject = currentTouchingScreenObject;

        }

    }

    private boolean isCorrectAnswer(String selectedAnswer) {

        String correctAnswer = answers[currentPlayingQuestionIndex];
        boolean isCorrect = false;
        isCorrect = isCorrectAnswer(correctAnswer, selectedAnswer);
        if (isCorrect) {
            return true;
        } else {
            StringBuilder joinSelectedAnswer = new StringBuilder();
            if (CollectionUtils.isNotEmpty(selectedAnswerList)) {
                for (String selectedAns : selectedAnswerList) {
                    joinSelectedAnswer.append(selectedAns);
                }

                isCorrect = isCorrectAnswer(correctAnswer, joinSelectedAnswer.toString());
            }
        }

        if (!isCorrect) {
            addNumberOfFails();
        }

        return isCorrect;

    }

    private void doCheckCorrectAnswer() {
        if (isShowing && null != touchingScreenObject) {
            //check if the selected answer is correct
            if (isCorrectAnswer(touchingScreenObject.id)) {
                //show the answer in the word instead of underscore
                float xPositionInScreen = 0;
                for (ScreenObject screenObject : ScreenObjectUtils.getScreenObjectListById(wordList, questions[currentPlayingQuestionIndex])) {
                    TextScreenObject textScreenObject = (TextScreenObject) screenObject;
                    if (0 == xPositionInScreen) {
                        xPositionInScreen = textScreenObject.xPositionInScreen;
                    }
                    if ("_".equals(textScreenObject.displayText)) {
                        textScreenObject.setDisplayText(touchingScreenObject.id);
                        textScreenObject.isHighlighted = true;
                    }
                    textScreenObject.xPositionInScreen = xPositionInScreen;
                    xPositionInScreen += textScreenObject.width;
                }


                //play the current playing word and then play the correct sound
                abstractAutoCognitaScreen.playSound(getAudioFileName(null, currentPlayingQuestionIndex), new AbstractSoundPlayListener() {
                    @Override
                    public void onComplete() {
                        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                            @Override
                            public void onCorrectSoundPlayed() {
                                if (null != answers) {
                                    if (currentPlayingQuestionIndex + 1 == answers.length) {
                                        //which mean all question are answered correctly
                                        abstractAutoCognitaScreen.showNextSection(numberOfFails);
                                    } else {
                                        pictureScreenObjectList.get(currentPlayingQuestionIndex).isHighlighted = false;

                                        //move to next question
                                        currentPlayingQuestionIndex++;
                                        //select the current playing border
                                        pictureScreenObjectList.get(currentPlayingQuestionIndex).isHighlighted = true;
                                    }
                                }

                                if (CollectionUtils.isNotEmpty(selectedAnswerList)) {
                                    selectedAnswerList.clear();
                                }

                                //make sure the touch input is enabled after the correct sound is play
                                abstractAutoCognitaScreen.setTouchAllow(true);

                            }
                        });
                    }
                });


            }
            else{
                abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                    @Override
                    public void onCorrectSoundPlayed() {

                    }
                });
            }

            if (ScreenObjectType.HINTS.equals(touchingScreenObject.objectType)) {
                //only choice will be unhighlighted, the picture will only be unhighlighted when the user selected correct answer
                touchingScreenObject.isHighlighted = false;
            }

            touchingScreenObject = null;

        }
    }

    private boolean isCorrectAnswer(String correctAnswer, String selectedAnswer) {
        return correctAnswer.equals(questions[currentPlayingQuestionIndex].replaceAll("_", selectedAnswer));
    }

    /**
     * Get the answer for given questionIndex
     *
     * @return
     */
    private String getAnswer(int questionIndex) {
        return StringUtils.removeAllSpace(answers[questionIndex]);
    }

    private void initQuestionAndAnswer() {
        if (null != question && StringUtils.isNotBlank(question.getAnswers()) && null == answers) {
            answers = question.getAnswers().split(",");
            questions = question.getQuestions().split(",");
            if (StringUtils.isNotBlank(question.getChoices())) {
                choices = question.getChoices().split(",");
            }
        }
    }

    private float getCenterXPositionOfHints(int numberOfHints) {
        float totalHintsWidth = numberOfHints * getPhonicSoundWidth() + numberOfHints * GAP_WIDTH_BETWEEN_HINT;
        totalHintsWidth += numberOfHints * ImageUtils.SMALL_PHONIC_SYMBOL_OPEN.width * 2;

        return ScreenUtils.getXPositionForCenterObject(totalHintsWidth);
    }

    private float getPhonicSoundWidth() {
        return isContainMoreOneLetter() ? DOUBLE_PHONIC_SOUND_WIDTH : SINGLE_PHONIC_SOUND_WIDTH;
    }

    private void initGraphic() {
        if (ArrayUtils.isNotEmpty(choices)) {


            float hintHeight = getHintHeight(choices.length);

            float totalContentHeight = hintHeight +
                    getTotalHeightOfWordPictures(questions.length);

            hintStartYPosition = ScreenUtils.getStartYPositionForCenterObjectWithoutNavigationBar(totalContentHeight);

            float startXPosition = getCenterXPositionOfHints(Math.min(choices.length, getNumberOfChoicePerRow()));

            for (int i = 0; i < choices.length; i++) {
                String choice = choices[i];
                if (null == screenObjectList) {
                    screenObjectList = new ArrayList<ScreenObject<String, ScreenObjectType>>();
                }

                if (i >= getNumberOfChoicePerRow()) {
                    hintStartYPosition -= LetterUtils.getMaximumHeight(textFontSize) * 2;
                    startXPosition = getCenterXPositionOfHints(Math.min(choices.length - i, getNumberOfChoicePerRow()));
                }

                //draw phonic open symbol
                TextureScreenObject openSymbol = new TextureScreenObject<String, ScreenObjectType>(choice, ScreenObjectType.PHONIC_SOUND_SYMBOL_LEFT, startXPosition,
                        hintStartYPosition, ImageUtils.getSmallPhonicSymbolOpenIcon(), null);
                openSymbol.isTouchAllow = false;
                screenObjectList.add(openSymbol);

                startXPosition += ImageUtils.SMALL_PHONIC_SYMBOL_OPEN.width;

                //draw the phonic sound
                ScreenObject<String, ScreenObjectType> phonicSoundScreenObject = LetterUtils.getTextScreenObject(choice, ScreenObjectType.HINTS, startXPosition,
                        hintStartYPosition, choice, textFontSize, getPhonicSoundWidth());
                screenObjectList.add(phonicSoundScreenObject);
                //because the bitmap font will be draw the text from screen upper to lower, not from the y-position to upper screen
                //TODO should be recaculate the y position
                phonicSoundScreenObject.yPositionInScreen = hintStartYPosition + phonicSoundScreenObject.height + (openSymbol.height - phonicSoundScreenObject.height) / 2;

                startXPosition += getPhonicSoundWidth();
                phonicSoundScreenObject.audioFileName = SoundService.getInstance().getPhonicAudioFileNameByUnitCodeAndElementCode(selectedActivity.getUnitCode(), choice);

                //draw phonic close symbol
                TextureScreenObject closeSymbol = new TextureScreenObject<String, ScreenObjectType>(choice, ScreenObjectType.PHONIC_SOUND_SYMBOL_RIGHT, startXPosition, hintStartYPosition, ImageUtils.getSmallPhonicSymbolCloseIcon(), null);
                closeSymbol.isTouchAllow = false;
                screenObjectList.add(closeSymbol);

                startXPosition += ImageUtils.SMALL_PHONIC_SYMBOL_CLOSE.width + GAP_WIDTH_BETWEEN_HINT;

            }

            String[] questions = question.getQuestions().split(",");

            String picturePaths[] = new String[answers.length];
            for (int i = 0; i < answers.length; i++) {
                picturePaths[i] = Config.LESSON_IMAGE_FOLDER_NAME + "/" + abstractAutoCognitaScreen.getLessonService().getEnglishWordBySwahili(getAnswer(i)) + ".png";
            }

            screenObjectList.addAll(getWordPictureScreenObjectList(picturePaths, questions, selectedActivity.getUnitCode()));
        }
    }

    private int getNumberOfChoicePerRow() {
        return ScreenUtils.isLandscapeMode ? 6 : 3;
    }


    private float getHintHeight(int numberOfChoice) {
        return Math.max(ImageUtils.SMALL_PHONIC_SYMBOL_CLOSE.height, LetterUtils.getMaximumHeight(textFontSize)) *
                Math.min(1, numberOfChoice / getNumberOfChoicePerRow());
    }

    private boolean isContainMoreOneLetter() {
        if (ArrayUtils.isNotEmpty(choices)) {
            for (String choice : choices) {
                if (choice.length() > 1) {
                    return true;
                }
            }
        }

        return false;
    }

}
