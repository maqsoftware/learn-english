package com.maqautocognita.section.Math;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AbstractBitmapFontScreenObject;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.graphics.NumberWritingScreenObject;
import com.maqautocognita.graphics.TextScreenObject;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.HandWritingRecognizeScreenService;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractLongFormSection extends AbstractMathSection implements HandWritingRecognizeScreenService.IHandWritingRecognizeListener {


    /**
     * the space between each row in the straight formula
     */
    protected static final int GAP_BETWEEN_NUMBER_IN_STRAIGHT_FORMULA = 70;
    /**
     * The height of the horizontal line in the straight formula
     */
    protected static final int HORIZONTAL_LINE_HEIGHT = 5;
    private static final TextFontSizeEnum LONG_FORM_TEXT_SIZE = TextFontSizeEnum.FONT_288;
    private static final TextFontSizeEnum CARRY_DIGIT_TEXT_SIZE = TextFontSizeEnum.FONT_72;
    private static final int GAP_BETWEEN_HORIZONTAL_LINE = 40;
    private static final int MAXIMUM_NUMBER_OF_DIGIT_IN_STRAIGHT_FORMULA = 2;
    protected final HandWritingRecognizeScreenService handWritingRecognizeScreenService;
    protected int upperNumber;
    protected int lowerNumber;
    //store the fixed size for each digit which show in the formula
    protected float[] maximumSizeOfNumber;
    protected float straightFormulaStartXPosition;
    protected float straightFormulaStartYPosition;

    //the number which will be shown in the upper of the long formula
    protected List<NumberWritingScreenObject> upperNumberScreenObjectList;
    //the number which will be shown in the lower of the long formula
    protected List<NumberWritingScreenObject> lowerNumberScreenObjectList;

    protected NumberScreenObject carryDigitNumberScreenObject;

    protected Rectangle carryDigitNumberContainerArea;
    /**
     * store the answer which is the result of the {@link #upperNumber} and {@link #lowerNumber}, which will also store in the {@link #abstractBitmapFontScreenObjectList}
     */
    protected List<NumberWritingScreenObject> answerScreenObjectList;
    protected int numberOfRoundPlayed;
    protected List<List<Vector3>> carrayDigitDrawPoints;
    private boolean isInitializated;
    /**
     * store the long formula object without answer
     */
    private List<AbstractBitmapFontScreenObject> abstractBitmapFontScreenObjectList;
    /**
     * The total width of the straight formula
     */
    private float formulaWidth;
    private float straightFormulaHorizontalLineStartYPosition;
    private ShapeRenderer shapeRenderer;
    /**
     * Store the list of upper  and lower value which will be used to play, it will use the "_" to separate the x and y value
     */
    private List<String> upperLowerList;

    public AbstractLongFormSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        handWritingRecognizeScreenService = new HandWritingRecognizeScreenService(this);
        maximumSizeOfNumber = LetterUtils.getSizeOfWord(String.valueOf(9), LONG_FORM_TEXT_SIZE);
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (isAnswerWrote()) {
            handWritingRecognizeScreenService.touchUp();
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        handWritingRecognizeScreenService.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);

        if (null == carrayDigitDrawPoints) {
            carrayDigitDrawPoints = new ArrayList<List<Vector3>>();
        }

        carrayDigitDrawPoints.add(new ArrayList<Vector3>());
        handWritingRecognizeScreenService.addDrawPoint(carrayDigitDrawPoints, screenX, screenY, systemDetectXPosition, systemDetectYPosition);
    }

    private boolean isAnswerWrote() {
        if (CollectionUtils.isNotEmpty(answerScreenObjectList)) {
            for (NumberWritingScreenObject numberWritingScreenObject : answerScreenObjectList) {
                if (!numberWritingScreenObject.hasDrawingPoints()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        numberOfRoundPlayed = 0;
        resetScreen();
    }

    @Override
    protected void render() {
        super.render();

        if (!isInitializated) {

            shapeRenderer = new ShapeRenderer();


            formulaWidth =
                    //width * ({@link #MAXIMUM_NUMBER_OF_DIGIT_IN_STRAIGHT_FORMULA}+1) , because count with the formula symbol
                    maximumSizeOfNumber[0] * (MAXIMUM_NUMBER_OF_DIGIT_IN_STRAIGHT_FORMULA + 1);
            /**
             * The start x for straightFormula is start from the third part of the screen
             */
            straightFormulaStartXPosition = ScreenUtils.getXPositionForCenterObject(formulaWidth);
            straightFormulaStartYPosition = ScreenUtils.getBottomYPositionForCenterObject(maximumSizeOfNumber[1] * 3);

            resetFormula();

            isInitializated = true;
        }


        //draw horizontal line for the straight formula
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(straightFormulaStartXPosition, straightFormulaHorizontalLineStartYPosition, formulaWidth, HORIZONTAL_LINE_HEIGHT);
        shapeRenderer.end();

        drawAnswerContainer();

        batch.begin();

        ScreenObjectUtils.draw(batch, abstractBitmapFontScreenObjectList);

        batch.end();

        handWritingRecognizeScreenService.drawLine();
        handWritingRecognizeScreenService.drawExtraLine(carrayDigitDrawPoints);

    }

    @Override
    public void dispose() {
        super.dispose();
        if (null != answerScreenObjectList) {
            answerScreenObjectList.clear();
        }

        if (null != upperNumberScreenObjectList) {
            upperNumberScreenObjectList.clear();
        }
        if (null != lowerNumberScreenObjectList) {
            lowerNumberScreenObjectList.clear();
        }
        if (null != carryDigitNumberScreenObject) {
            carryDigitNumberScreenObject = null;
        }
        if (null != abstractBitmapFontScreenObjectList) {
            abstractBitmapFontScreenObjectList.clear();
        }
        isInitializated = false;
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        handWritingRecognizeScreenService.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (TouchUtils.isTouched(carryDigitNumberContainerArea, screenX, screenY)) {
            handWritingRecognizeScreenService.addDrawPoint(carrayDigitDrawPoints, screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        }
    }

    @Override
    protected boolean isTrashRequired() {
        return super.isTrashRequired();
    }

    @Override
    protected boolean isNumberBlocksRequired() {
        return false;
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
        if (null != carrayDigitDrawPoints) {
            carrayDigitDrawPoints.clear();
            carrayDigitDrawPoints = null;
        }
        handWritingRecognizeScreenService.clearCorrectDrawPoints();
        handWritingRecognizeScreenService.clearDrawPoints();
    }

    /**
     * In the formula section, it will separate into 4 rows
     * for example:
     * 28
     * +10
     * ---
     * 38
     */
    protected void resetFormula() {

        generateUpperAndLowerNumber();

        if (null == abstractBitmapFontScreenObjectList) {
            abstractBitmapFontScreenObjectList = new ArrayList<AbstractBitmapFontScreenObject>();
        } else {
            abstractBitmapFontScreenObjectList.clear();
        }

        int theFirstDigitInUpperNumber = upperNumber % 10;
        int theSecondDigitInLowerNumber = lowerNumber % 10;

        int carryDigit = getCarryDigitToTensDigit(theFirstDigitInUpperNumber, theSecondDigitInLowerNumber);

        upperNumberScreenObjectList = getNumberScreenObjectList(upperNumber, maximumSizeOfNumber[0], straightFormulaStartYPosition + maximumSizeOfNumber[1] * 2, carryDigit, 1);
        //init the number which show in the upper of the formula
        abstractBitmapFontScreenObjectList.addAll(upperNumberScreenObjectList);


        float secondRowStartYPosition = straightFormulaStartYPosition + maximumSizeOfNumber[1] - GAP_BETWEEN_NUMBER_IN_STRAIGHT_FORMULA;
        lowerNumberScreenObjectList = getNumberScreenObjectList(lowerNumber, maximumSizeOfNumber[0], secondRowStartYPosition, 0, 0);
        //init the number which show in the lower of the formula
        abstractBitmapFontScreenObjectList.addAll(lowerNumberScreenObjectList);

        //draw the icon for the formula e.g: +-x/
        TextScreenObject icon = new TextScreenObject(getFormulaSymbol(), straightFormulaStartXPosition, secondRowStartYPosition, LONG_FORM_TEXT_SIZE, true);
        icon.setAlign(Align.bottom | Align.center);
        abstractBitmapFontScreenObjectList.add(icon);

        straightFormulaHorizontalLineStartYPosition = secondRowStartYPosition - GAP_BETWEEN_HORIZONTAL_LINE;

        //draw the answer below the horizontal line
        answerScreenObjectList = getNumberScreenObjectList(getAnswer(upperNumber, lowerNumber), maximumSizeOfNumber[0],
                straightFormulaHorizontalLineStartYPosition - maximumSizeOfNumber[1] - GAP_BETWEEN_HORIZONTAL_LINE, 0, 0);
        for (NumberWritingScreenObject numberWritingScreenObject : answerScreenObjectList) {
            numberWritingScreenObject.setClearWritingAfterCheckCorrect(false);
        }
        abstractBitmapFontScreenObjectList.addAll(answerScreenObjectList);

        afterStraightFormulaInitialized(abstractBitmapFontScreenObjectList, carryDigit);

    }

    private void drawAnswerContainer() {

        if (isAnswerContainerRequired()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            //draw a border for each of the  digit of the answer, it act as a indicator to indicate the user that the answer must be write inside the border
            for (NumberWritingScreenObject numberWritingScreenObject : answerScreenObjectList) {
                numberWritingScreenObject.isVisible = false;
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.rect(numberWritingScreenObject.xPositionInScreen, numberWritingScreenObject.yPositionInScreen - numberWritingScreenObject.height, numberWritingScreenObject.width, numberWritingScreenObject.height);
            }

            //draw container for carry digit
            if (null != carryDigitNumberScreenObject) {
                int padding = 50;
                shapeRenderer.setColor(Color.BLACK);
                carryDigitNumberContainerArea = new Rectangle(carryDigitNumberScreenObject.xPositionInScreen - padding,
                        carryDigitNumberScreenObject.yPositionInScreen - carryDigitNumberScreenObject.height,
                        carryDigitNumberScreenObject.width + padding * 2, carryDigitNumberScreenObject.height + padding * 2);

                if (isCarryDigitContainerRequired()) {
                    drawCarryDigitContainer(shapeRenderer, carryDigitNumberContainerArea);
                }

            }

            shapeRenderer.end();
        }
    }

    /**
     * generate the {@link #upperNumber} and  {@link #lowerNumber} randomly
     */
    private void generateUpperAndLowerNumber() {
        if (CollectionUtils.isEmpty(upperLowerList)) {
            upperLowerList = generateRandomList();
        }
        if (numberOfRoundPlayed < upperLowerList.size()) {
            String values[] = upperLowerList.get(numberOfRoundPlayed).split("_");
            upperNumber = Integer.valueOf(values[0]);
            lowerNumber = Integer.valueOf(values[1]);
        }
    }

    protected abstract int getCarryDigitToTensDigit(int theFirstDigitInUpperNumber, int theSecondDigitInLowerNumber);

    /**
     * create a list of {@link NumberWritingScreenObject} by given number, for example, if the given number is 123, the size of list will be 3
     * <p/>
     * only the carry digit will be add to {@link #abstractBitmapFontScreenObjectList}
     * <p/>
     *
     * @param number
     * @param numberWidth
     * @param startYPosition
     * @param carryDigit         the carry digit need to show, it must be larger than 0, if not no carry digit will be drawn
     * @param carryDigitPosition the digit position, the given carry digit position should be shown,
     *                           the position is the carry digit which will carry to the position of the given number, the position will be start from left to right
     * @return the list of {@link NumberScreenObject}
     */
    private List<NumberWritingScreenObject> getNumberScreenObjectList(int number, float numberWidth, float startYPosition, int carryDigit, int carryDigitPosition) {

        List<NumberWritingScreenObject> numberScreenObjectList = null;

        int positionOfDigit = MAXIMUM_NUMBER_OF_DIGIT_IN_STRAIGHT_FORMULA;
        while (number > 0) {
            int d = number / 10;
            int digit = number - d * 10;
            number = d;

            float startXPosition = straightFormulaStartXPosition + numberWidth * positionOfDigit - 1;

            NumberWritingScreenObject numberScreenObject = new NumberWritingScreenObject(digit, numberWidth, startXPosition, startYPosition, LONG_FORM_TEXT_SIZE, true);

            if (null == numberScreenObjectList) {
                numberScreenObjectList = new ArrayList<NumberWritingScreenObject>();
            }

            if (isCarryDigitNeedToShow() && carryDigitPosition == positionOfDigit && carryDigit > 0) {
                carryDigitNumberScreenObject = new NumberScreenObject(carryDigit, startXPosition,
                        startYPosition + numberScreenObject.height + GAP_BETWEEN_NUMBER_IN_STRAIGHT_FORMULA
                        , CARRY_DIGIT_TEXT_SIZE, false);
                carryDigitNumberScreenObject.xPositionInScreen = getCarryDigitXPosition(numberScreenObject, carryDigitNumberScreenObject.width, numberWidth);

                abstractBitmapFontScreenObjectList.add(carryDigitNumberScreenObject);
            }

            numberScreenObjectList.add(numberScreenObject);
            positionOfDigit--;
        }

        return numberScreenObjectList;
    }

    protected abstract String getFormulaSymbol();

    protected abstract int getAnswer(int upper, int lower);

    /**
     * It will be call after the long formula is initialized
     *
     * @param abstractBitmapFontScreenObjectList
     * @param carryDigit
     */
    protected void afterStraightFormulaInitialized(List<AbstractBitmapFontScreenObject> abstractBitmapFontScreenObjectList, int carryDigit) {

    }

    /**
     * If it is return true, the answer container will be draw below the displaying formula and the answer will be invisible, otherwise, the answer will be shown below the formula
     *
     * @return
     */
    protected abstract boolean isAnswerContainerRequired();

    protected boolean isCarryDigitContainerRequired() {
        return true;
    }

    protected void drawCarryDigitContainer(ShapeRenderer shapeRenderer, Rectangle carryDigitNumberContainerArea) {
        shapeRenderer.rect(carryDigitNumberContainerArea.x, carryDigitNumberContainerArea.y, carryDigitNumberContainerArea.width, carryDigitNumberContainerArea.height);
    }

    protected abstract List<String> generateRandomList();

    protected boolean isCarryDigitNeedToShow() {
        return true;
    }

    protected abstract float getCarryDigitXPosition(NumberScreenObject tensDigitNumberScreenObject, float carryDigitWith, float numberTargetWidth);

    @Override
    public boolean isDrawAllow(int screenX, int screenY) {
        if (isAnswerContainerRequired() && CollectionUtils.isNotEmpty(answerScreenObjectList)) {
            for (NumberWritingScreenObject numberWritingScreenObject : answerScreenObjectList) {
                if (TouchUtils.isTouched(numberWritingScreenObject, screenX, screenY)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isSaveCorrectDrawPointsRequired() {
        return numberOfRoundPlayed + 1 >= getNumberOfRoundRequiredToPlay();
    }

    protected abstract int getNumberOfRoundRequiredToPlay();

    @Override
    public void whenCorrectLetterWrite() {
        //reset the formula

        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {

                clearAllDrawingPoints();

                numberOfRoundPlayed++;
                next();
            }

        });
    }

    @Override
    public void whenWrongLetterWrite() {
        //reset the formula

        abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {

            }

        });
    }

    private void clearAllDrawingPoints() {
        for (NumberWritingScreenObject numberWritingScreenObject : answerScreenObjectList) {
            numberWritingScreenObject.clearDrawingPoints();
        }
        if (null != carrayDigitDrawPoints) {
            carrayDigitDrawPoints.clear();
            carrayDigitDrawPoints = null;
        }

        handWritingRecognizeScreenService.clearDrawPoints();
    }

    /**
     * It will be call when the correct answer is write in the formula and the handwriting on the screen has cleared
     */
    protected void next() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                resetFormula();
            }
        });

    }

    @Override
    public void whenLetterWriteFails() {
        if (isAnswerWrote()) {
            clearAllDrawingPoints();
        }
    }

    @Override
    public boolean isWriteCorrect() {
        if (null != AutoCognitaGame.handWritingRecognizeService) {
            if (CollectionUtils.isNotEmpty(answerScreenObjectList)) {
                for (NumberWritingScreenObject numberWritingScreenObject : answerScreenObjectList) {
                    if (!numberWritingScreenObject.isNumberWritingCorrect()) {
                        return false;
                    }
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public void afterDrawPointAdded(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (CollectionUtils.isNotEmpty(answerScreenObjectList)) {
            for (NumberWritingScreenObject numberWritingScreenObject : answerScreenObjectList) {
                if (TouchUtils.isTouched(numberWritingScreenObject, screenX, screenY)) {
                    numberWritingScreenObject.addDrawingPoints(systemDetectXPosition, systemDetectYPosition);
                }
            }
        }

    }

    @Override
    public boolean isRequiredClearDrawPointsAfterTimesUp() {
        return false;
    }
}
