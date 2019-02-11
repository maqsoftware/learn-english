package com.maqautocognita.section.Phonic;

import com.maqautocognita.Config;
import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.bo.Question;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ImageProperties;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ImageUtils;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.AbstractAutoCognitaSection;
import com.maqautocognita.section.AbstractDrawWordPictureSection;
import com.maqautocognita.section.IActivityChangeListener;
import com.maqautocognita.service.SoundService;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.ApplicationUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.SizeUtils;
import com.maqautocognita.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicGroupingSection extends AbstractDrawWordPictureSection implements TimerService.ITimerListener, IActivityChangeListener {

    private static final String TWO_SECOND_CALLBACK_INDICATOR = "2";
    private static final String FIVE_SECOND_CALLBACK_INDICATOR = "5";

    private static final float GAP_BETWEEN_PHONIC_SYMBOL_AND_PHONIC_LETTER = 10;

    private static final float GAP_BETWEEN_WORD_PICTURE = 20;

    private static final float GAP_BETWEEN_ANSWER_CONTAINER = 40;

    private static final float GAP_BETWEEN_WORD_AND_ANSWER_CONTAINER = 150;

    /**
     * Store the width between answers in the answer tray
     */
    private static final int GAP_WIDTH_BETWEEN_ANSWER = 20;
    protected final TimerService timerService;
    protected List<ScreenObject<QuestionAndAnswer, ScreenObjectType>> questionScreenObjectList;
    protected Question question;
    private Activity selectedActivity;
    private List<ScreenObject<QuestionAndAnswer, ScreenObjectType>> touchingQuestionList;
    private List<TextureScreenObject<AnswerContainer, ScreenObjectType>> answerContainerList;
    private List<ScreenObject<String, ScreenObjectType>> phonicSoundList;

    private TextFontSizeEnum textFontSize;

    /**
     * below is store number of answer which the user answered correctly
     */
    private int numberOfAnswerCorrected;
    /**
     * below is store the number of answer that the user should be answer.
     * It is not equals to the number of answer,because there are question maybe not need to the right hand side
     */
    private int numberOfAnswer;

    public PhonicGroupingSection(AbstractAutoCognitaScreen abstractAutoCognitaScreen, AbstractAutoCognitaSection.IOnHelpListener onHelpListener) {
        super(abstractAutoCognitaScreen, onHelpListener);
        timerService = new TimerService(this);

        if (ScreenUtils.isTablet) {
            textFontSize = TextFontSizeEnum.FONT_72;
        } else {
            textFontSize = TextFontSizeEnum.FONT_108;
        }

    }

    @Override
    protected float getHeightForPictureSection() {
        return 0;
    }

    @Override
    public void reset() {
        clearScreenObjects();
        question = null;
        numberOfAnswer = 0;
        numberOfAnswerCorrected = 0;
    }

    private void clearScreenObjects() {
        if (null != questionScreenObjectList) {
            questionScreenObjectList.clear();
            questionScreenObjectList = null;
        }
        touchingQuestionList = null;
        if (null != answerContainerList) {
            answerContainerList.clear();
            answerContainerList = null;
        }
        if (null != phonicSoundList) {
            phonicSoundList.clear();
            phonicSoundList = null;
        }
    }

    @Override
    public void setSelectedActivity(Activity selectedActivity, boolean isMasterTest) {
        this.selectedActivity = selectedActivity;
        this.question = selectedActivity.getQuestion();
    }

    @Override
    protected void render() {
        if (null == questionScreenObjectList) {
            initGraphic();
        }

        render(questionScreenObjectList);

        batch.begin();
        ScreenObjectUtils.draw(batch, answerContainerList);
        ScreenObjectUtils.draw(batch, phonicSoundList);
        batch.end();


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
        clearScreenObjects();
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);

        if (null != touchingQuestionList) {
            for (ScreenObject touchingQuestion : touchingQuestionList) {
                touchingQuestion.dragPosition(screenX, screenY);
                if (ScreenObjectType.PICTURE.equals(touchingQuestion.objectType)) {
                    touchingQuestion.isHighlighted = false;
                }
            }
        }
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        final TextureScreenObject<AnswerContainer, ScreenObjectType> touchingAnswerContainer = ScreenObjectUtils.getTouchingScreenObject(answerContainerList, screenX, screenY);

        if (null != touchingAnswerContainer) {

            if (null != touchingQuestionList) {
                String answer = touchingQuestionList.get(0).id.answer;
                if (touchingAnswerContainer.id.hint.equals(answer)) {

                    numberOfAnswerCorrected++;
                    //draw the selected question in the answer container
                    ScreenObject<QuestionAndAnswer, ScreenObjectType> answeredQuestionLetter = LetterUtils.getTextScreenObjectWithYPositionNotIncludeHeight(
                            touchingAnswerContainer.id.nextQuestionsStartXPosition,
                            touchingAnswerContainer.id.hintStartYPosition,
                            touchingQuestionList.get(0).id.question,
                            textFontSize);

                    touchingAnswerContainer.id.nextQuestionsStartXPosition += touchingQuestionList.get(0).id.questionScreenWidth + GAP_WIDTH_BETWEEN_ANSWER;
                    //the answer is not allow touch
                    answeredQuestionLetter.isTouchAllow = false;


                    questionScreenObjectList.add(answeredQuestionLetter);

                    for (ScreenObject screenObject : touchingQuestionList) {
                        //make the question disable and non selectable after the question is answered
                        screenObject.isHighlighted = false;
                        screenObject.isTouchAllow = false;
                        screenObject.isDisabled = true;
                        if (ScreenObjectType.QUESTION.equals(screenObject.objectType)) {
                            //if it is word, make the word in gray color
                            screenObject.isDisabled = true;
                        } else if (ScreenObjectType.BORDER.equals(screenObject.objectType)) {
                            //if it is border
                            ((TextureScreenObject) screenObject).textureRegionInDisableState = ImageUtils.getSmallGreyBorder();
                        }
                        screenObject.rollbackToOriginalPosition();
                    }
                    touchingQuestionList = null;


                    abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                        @Override
                        public void onCorrectSoundPlayed() {

                            if (numberOfAnswerCorrected == numberOfAnswer) {
                                abstractAutoCognitaScreen.showNextSection(numberOfFails);
                            }

                        }
                    });
                } else {
                    addNumberOfFails();
                    abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                        @Override
                        public void onCorrectSoundPlayed() {

                        }
                    });
                }

            }
        }

        //make sure the position of the selected question is rollback if the user is dragging the question
        if (null != touchingQuestionList) {
            boolean isDragging = false;
            for (ScreenObject screenObject : touchingQuestionList) {
                if (screenObject.isDragging()) {
                    screenObject.isHighlighted = false;
                    isDragging = true;
                }
                screenObject.rollbackToOriginalPosition();
            }
            if (isDragging) {
                touchingQuestionList = null;
            }
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        ScreenObject<QuestionAndAnswer, ScreenObjectType> touchingScreenObject = ScreenObjectUtils.getTouchingScreenObject(questionScreenObjectList, screenX, screenY);

        if (null != touchingScreenObject) {

            if (null != touchingQuestionList) {
                //make sure the previous touching question is unselected
                for (ScreenObject previousTouchingQuestion : touchingQuestionList) {
                    previousTouchingQuestion.isHighlighted = false;
                }
            }

            touchingQuestionList = ScreenObjectUtils.getScreenObjectListById(questionScreenObjectList, touchingScreenObject.id);
            if (CollectionUtils.isNotEmpty(touchingQuestionList)) {
                //if the user is touching the question
                for (ScreenObject screenObject : touchingQuestionList) {
                    //make sure the touching question include border and the word is changed to highlight state, they should be in same id
                    screenObject.isHighlighted = !screenObject.isHighlighted;
                    screenObject.touchingPosition(screenX, screenY);
                    timerService.startTimer(TWO_SECOND_CALLBACK_INDICATOR);
                }
            }

            //play the sound of the selected word
            abstractAutoCognitaScreen.playSound(touchingScreenObject.audioFileName);
            return;
        }

        final ScreenObject<String, ScreenObjectType> touchingPhonicSound = ScreenObjectUtils.getTouchingScreenObject(phonicSoundList, screenX, screenY);

        if (null != touchingPhonicSound && ScreenObjectType.SOUND.equals(touchingPhonicSound.objectType)) {

            touchingPhonicSound.isHighlighted = true;

            final List<ScreenObject<String, ScreenObjectType>> touchingPhonicSoundSymbolList = ScreenObjectUtils.getScreenObjectListById(phonicSoundList, touchingPhonicSound.id);

            //play the phonic sound
            abstractAutoCognitaScreen.playSound(touchingPhonicSound.audioFileName, new AbstractSoundPlayListener() {

                @Override
                public void onPlay(int audioListIndex, long millisecond) {
                    for (ScreenObject phonicSoundSymbol : touchingPhonicSoundSymbolList) {
                        if (ScreenObjectType.PHONIC_SOUND_SYMBOL_LEFT.equals(phonicSoundSymbol.objectType)) {

                            TextureScreenObject texturePhonicSoundSymbol = (TextureScreenObject) phonicSoundSymbol;

                            if (millisecond < 300) {
                                texturePhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolOpenIconWave75Percent();
                            } else if (millisecond < 600) {
                                texturePhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolOpenIconWave50Percent();
                            } else {
                                texturePhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolOpenIconWave25Percent();
                            }
                        } else if (ScreenObjectType.PHONIC_SOUND_SYMBOL_RIGHT.equals(phonicSoundSymbol.objectType)) {

                            TextureScreenObject texturePhonicSoundSymbol = (TextureScreenObject) phonicSoundSymbol;

                            if (millisecond < 300) {
                                texturePhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolCloseIconWave75Percent();
                            } else if (millisecond < 600) {
                                texturePhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolCloseIconWave50Percent();
                            } else {
                                texturePhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolCloseIconWave25Percent();
                            }
                        }
                    }
                }

                @Override
                public void onComplete() {
                    resumeNormal();
                }

                @Override
                public void onStop() {
                    resumeNormal();
                }

                @Override
                public boolean isWaveChangeListenerRequired() {
                    return true;
                }

                private void resumeNormal() {

                    touchingPhonicSound.isHighlighted = false;

                    //make sure the phonic symbol is rollback to the original image
                    for (ScreenObject phonicSoundSymbol : touchingPhonicSoundSymbolList) {
                        if (ScreenObjectType.PHONIC_SOUND_SYMBOL_LEFT.equals(phonicSoundSymbol.objectType)) {
                            TextureScreenObject texturePhonicSoundSymbol = (TextureScreenObject) phonicSoundSymbol;
                            texturePhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolOpenIcon();
                        } else if (ScreenObjectType.PHONIC_SOUND_SYMBOL_RIGHT.equals(phonicSoundSymbol.objectType)) {
                            TextureScreenObject texturePhonicSoundSymbol = (TextureScreenObject) phonicSoundSymbol;
                            texturePhonicSoundSymbol.textureRegion = ImageUtils.getSmallPhonicSymbolCloseIcon();
                        }
                    }
                }
            });
        }
    }

    private void initGraphic() {

        if (null != question && StringUtils.isNotBlank(question.getQuestions())) {
            String[] questions = question.getQuestions().split(",");
            String[] answers = new String[questions.length];
            if (StringUtils.isNotBlank(question.getAnswers())) {
                answers = question.getAnswers().split(",");
            }

            String[] phonicSounds = new HashSet<String>(Arrays.asList(answers)).toArray(new String[]{});

            int numberOfWordColumn = ScreenUtils.isLandscapeMode ? 1 : 2;

            int numberOfWordRow = questions.length / numberOfWordColumn;

            float estimationQuestionWidth = numberOfWordColumn * ImageProperties.getSmallImageWordSize() +
                    (numberOfWordColumn - 1) * GAP_BETWEEN_WORD_PICTURE;

            float estimationAnswerWidth = ImageUtils.BLUE_ROUND_RECTANGLE.width;

            float estimationQuestionHeight = numberOfWordRow * ImageProperties.getSmallImageWordSize() + (numberOfWordRow - 1) * GAP_BETWEEN_WORD_PICTURE;

            float estimationAnswerHeight = phonicSounds.length * ImageUtils.BLUE_ROUND_RECTANGLE.height + (phonicSounds.length - 1) * 40;

            float estimationTotalHeight = 0;
            float estimationTotalWidth = 0;

            if (ScreenUtils.isLandscapeMode) {

                String maxLengthQuestion = questions[0];
                //get the max length of the question
                for (String question : questions) {
                    if (null == maxLengthQuestion) {
                        maxLengthQuestion = question;
                    } else if (maxLengthQuestion.length() < question.length()) {
                        maxLengthQuestion = question;
                    }
                }

                estimationTotalHeight = Math.max(estimationQuestionHeight, estimationAnswerHeight);
                estimationTotalWidth = estimationAnswerWidth + estimationQuestionWidth + LetterUtils.getTotalWidthOfWord(maxLengthQuestion, textFontSize);
            } else {
                estimationTotalHeight = estimationQuestionHeight + estimationAnswerHeight;
                estimationTotalWidth = Math.max(estimationQuestionWidth, estimationAnswerWidth);
            }

            final float startXPosition = ScreenUtils.getXPositionForCenterObject(estimationTotalWidth);

            final float startYPosition = ScreenUtils.getStartYPositionForCenterObjectWithoutNavigationBar(estimationTotalHeight);

            float maximumWidthOfWord = 0;

            float maximumBottomYPosition = Float.MAX_VALUE;

            boolean isImageRequired = ApplicationUtils.isImageRequired(selectedActivity.getUnitCode());

            //draw the word selection list
            for (int i = 0; i < questions.length; i++) {

                String word = questions[i];

                if (null == questionScreenObjectList) {
                    questionScreenObjectList = new ArrayList<ScreenObject<QuestionAndAnswer, ScreenObjectType>>();
                }

                QuestionAndAnswer questionAndAnswer = new QuestionAndAnswer(word, answers[i]);

                TextureScreenObject picture = null;

                String picturePath = Config.LESSON_IMAGE_FOLDER_NAME + "/" + abstractAutoCognitaScreen.getLessonService().getEnglishWordBySwahili(word) + ".png";

                float[] pictureSize = SizeUtils.getExpectSize(picturePath, ImageProperties.getSmallImageWordSize(), ImageProperties.getSmallImageWordSize());

                float pictureXPosition = startXPosition + (estimationAnswerWidth / numberOfWordColumn) * (i % numberOfWordColumn);
                float pictureYPosition = startYPosition - i / numberOfWordColumn * ImageProperties.getSmallImageWordSize() - i / numberOfWordColumn * GAP_BETWEEN_WORD_PICTURE;

                if (isImageRequired) {

                    picture = new TextureScreenObject<QuestionAndAnswer, ScreenObjectType>(
                            questionAndAnswer,
                            ScreenObjectType.PICTURE,
                            pictureSize[0] < ImageProperties.getSmallImageWordSize() ? pictureXPosition + (ImageProperties.getSmallImageWordSize() - pictureSize[0]) / 2 : pictureXPosition,
                            pictureSize[1] < ImageProperties.getSmallImageWordSize() ? pictureYPosition + (ImageProperties.getSmallImageWordSize() - pictureSize[1]) / 2 : pictureYPosition,
                            pictureSize[0],
                            pictureSize[1],
                            picturePath);
                    picture.setOriginalPositionToCurrent();
                    //draw the picture
                    questionScreenObjectList.add(picture);
                }

                //draw word near the picture
                ScreenObject<QuestionAndAnswer, ScreenObjectType> wordScreenObject =
                        LetterUtils.getTextScreenObjectWithYPositionNotIncludeHeight(questionAndAnswer, ScreenObjectType.QUESTION,
                                pictureXPosition + (isImageRequired ? ImageProperties.getSmallImageWordSize() + GAP_BETWEEN_WORD_PICTURE : 0),
                                pictureYPosition + (ImageProperties.getSmallImageWordSize() - LetterUtils.getMaximumHeight(textFontSize)) / 2,
                                word, textFontSize);
                wordScreenObject.setOriginalPositionToCurrent();

                int questionScreenWidth = 0;
                questionScreenWidth += wordScreenObject.width;
                if (null != picture) {
                    wordScreenObject.sameGroupObject = picture;
                    picture.sameGroupObject = wordScreenObject;
                }
                wordScreenObject.audioFileName = SoundService.getInstance().getWordAudioFilename(word);

                questionAndAnswer.questionScreenWidth = questionScreenWidth;

                questionScreenObjectList.add(wordScreenObject);

                maximumWidthOfWord = Math.max(wordScreenObject.width, maximumWidthOfWord);

                if (null != picture) {
                    maximumBottomYPosition = Math.min(picture.yPositionInScreen - picture.height, maximumBottomYPosition);
                }
            }


            int i = 0;
            for (String phonicSound : phonicSounds) {
                if (!phonicSound.equals(LetterUtils.UNDERSCORE)) {
                    if (null == answerContainerList) {
                        answerContainerList = new ArrayList<TextureScreenObject<AnswerContainer, ScreenObjectType>>();
                    }

                    float answerContainerXPosition = 0;
                    float answerContainerYPosition = 0;

                    if (ScreenUtils.isLandscapeMode) {
                        answerContainerXPosition = startXPosition +
                                (isImageRequired ? ImageProperties.getSmallImageWordSize() + GAP_BETWEEN_WORD_PICTURE : 0)
                                + maximumWidthOfWord + GAP_BETWEEN_WORD_AND_ANSWER_CONTAINER;
                        answerContainerYPosition = startYPosition -
                                (ImageUtils.BLUE_ROUND_RECTANGLE.height - ImageProperties.getSmallImageWordSize());
                    } else {
                        answerContainerXPosition = startXPosition;
                        answerContainerYPosition = maximumBottomYPosition - GAP_BETWEEN_WORD_AND_ANSWER_CONTAINER;
                    }

                    answerContainerYPosition -= (i * ImageUtils.BLUE_ROUND_RECTANGLE.height + i * GAP_BETWEEN_ANSWER_CONTAINER);

                    float[] phonicSoundSize = LetterUtils.getSizeOfWord(phonicSound, textFontSize);

                    float hintXPosition = answerContainerXPosition + GAP_BETWEEN_WORD_PICTURE;


                    float phonicSymbolWidth = ImageUtils.SMALL_PHONIC_SYMBOL_OPEN.width;

                    if (null == phonicSoundList) {
                        phonicSoundList = new ArrayList<ScreenObject<String, ScreenObjectType>>();
                    }


                    float phonicSoundYPosition = answerContainerYPosition + (ImageUtils.BLUE_ROUND_RECTANGLE.height -
                            phonicSoundSize[1]) / 2 + 5;
                    //draw phonic Sound which is location in the answer container
                    ScreenObject<String, ScreenObjectType> phonicSoundScreenObject =
                            LetterUtils.getTextScreenObjectWithYPositionNotIncludeHeight(phonicSound, ScreenObjectType.SOUND,
                                    hintXPosition + phonicSymbolWidth + GAP_BETWEEN_PHONIC_SYMBOL_AND_PHONIC_LETTER,
                                    phonicSoundYPosition,
                                    phonicSound, textFontSize);

                    phonicSoundScreenObject.audioFileName = SoundService.getInstance().getPhonicAudioFileName(phonicSound);

                    phonicSoundList.add(phonicSoundScreenObject);

                    float phonicSymbolYPosition = answerContainerYPosition + (ImageUtils.BLUE_ROUND_RECTANGLE.height -
                            Math.max(ImageUtils.SMALL_PHONIC_SYMBOL_OPEN.height, phonicSoundSize[1])) / 2;
                    //draw the phonic open symbol
                    phonicSoundList.add(new TextureScreenObject<String, ScreenObjectType>(phonicSound,
                            ScreenObjectType.PHONIC_SOUND_SYMBOL_LEFT,
                            hintXPosition, phonicSymbolYPosition, ImageUtils.getSmallPhonicSymbolOpenIcon(), null));
                    //draw the phonic close symbol
                    hintXPosition += phonicSymbolWidth + GAP_BETWEEN_PHONIC_SYMBOL_AND_PHONIC_LETTER * 2 + phonicSoundSize[0];
                    phonicSoundList.add(new TextureScreenObject<String, ScreenObjectType>(phonicSound, ScreenObjectType.PHONIC_SOUND_SYMBOL_RIGHT,
                            hintXPosition, phonicSymbolYPosition, ImageUtils.getSmallPhonicSymbolCloseIcon(), null));


                    //draw answer container
                    hintXPosition += ImageUtils.SMALL_PHONIC_SYMBOL_CLOSE.width + 50;

                    TextureScreenObject answerContainer = new TextureScreenObject<AnswerContainer, ScreenObjectType>(new AnswerContainer(phonicSound, hintXPosition, phonicSoundYPosition),
                            ScreenObjectType.ANSWER_CONTAINER,
                            answerContainerXPosition,
                            answerContainerYPosition,
                            ImageUtils.getBlueRoundRectangle(), null);

                    answerContainerList.add(answerContainer);
                    i++;
                }

            }

            numberOfAnswer = 0;
            for (String answer : answers) {
                if (!answer.equals(LetterUtils.UNDERSCORE)) {
                    numberOfAnswer++;
                }
            }
        }

    }

    @Override
    public void beforeStartTimer() {

    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        if (CollectionUtils.isNotEmpty(touchingQuestionList)) {
            for (ScreenObject<QuestionAndAnswer, ScreenObjectType> touchingPicture : touchingQuestionList) {
                if (ScreenObjectType.PICTURE.equals(touchingPicture.objectType)) {
                    if (TWO_SECOND_CALLBACK_INDICATOR.equals(threadIndicator)) {
                        touchingPicture.isHighlighted = false;
                        //the image will be animated for 2 seconds, then pause for 5 seconds, and then animated for 2 seconds, and so on in that pattern?
                        timerService.startTimer(FIVE_SECOND_CALLBACK_INDICATOR, 5);
                    } else if (FIVE_SECOND_CALLBACK_INDICATOR.equals(threadIndicator)) {
                        touchingPicture.isHighlighted = true;
                        timerService.startTimer(TWO_SECOND_CALLBACK_INDICATOR);
                    }
                    //suppose only 1 image is highlighted
                    break;
                }
            }
        }
    }

    private class AnswerContainer {
        public String hint;
        public float nextQuestionsStartXPosition;
        public float hintStartYPosition;

        public AnswerContainer(String hint, float nextQuestionsStartXPosition, float hintStartYPosition) {
            this.hint = hint;
            this.nextQuestionsStartXPosition = nextQuestionsStartXPosition;
            this.hintStartYPosition = hintStartYPosition;
        }
    }

    private class QuestionAndAnswer {
        public String question;
        public String answer;
        public int questionScreenWidth;

        public QuestionAndAnswer(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }
}
