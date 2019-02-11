package com.maqautocognita.section.Math;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.graphics.NumberWritingScreenObject;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.scene2d.ui.NumberCell;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.HandWritingRecognizeScreenService;
import com.maqautocognita.utils.AnimationUtils;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractLargeNumberSpeakingAndWritingSection extends AbstractMathMicrophoneSection implements HandWritingRecognizeScreenService.IHandWritingRecognizeListener {


    private static final int BORDER_WIDTH = 5;
    private static final int DOTTED_RADIUS = BORDER_WIDTH / 2;
    private static final int PADDING_FOR_WRITING_PAD = 50;
    private static final float SPACE_BETWEEN_WRITING_PAD_AND_NUMBER = 30;
    protected final int maximumNumber;
    protected final int minimumNumber;
    private final HandWritingRecognizeScreenService handWritingRecognizeScreenService;
    protected int displayNumber;
    protected NumberWritingScreenObject numberHundredScreenObject;
    protected NumberWritingScreenObject numberTenScreenObject;
    protected NumberWritingScreenObject numberDigitScreenObject;

    private NumberCell answerNumberHundredCell;
    private NumberCell answerNumberTenCell;
    private NumberCell answerNumberDigitCell;

    private ShapeRenderer borderShapeRenderer;
    private ShapeRenderer dottedLineShapeRenderer;
    private Rectangle writingPadNumberArea;
    private Rectangle writingPadNumberHundredDigitArea;
    private Rectangle writingPadNumberTenDigitArea;
    private Rectangle writingPadNumberDigitArea;
    private int numberOfTimeFailed;

    public AbstractLargeNumberSpeakingAndWritingSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int minimumNumber, int maximumNumber, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.maximumNumber = maximumNumber;
        this.minimumNumber = minimumNumber;
        this.handWritingRecognizeScreenService = new HandWritingRecognizeScreenService(this);

    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        microphoneService.changeRecognizeFile();
    }

    @Override
    public void render() {

        initNumberScreenObjectList();

        super.render();

        batch.begin();

        ScreenObjectUtils.draw(batch, numberHundredScreenObject);
        ScreenObjectUtils.draw(batch, numberTenScreenObject);
        ScreenObjectUtils.draw(batch, numberDigitScreenObject);
        batch.end();

        if (null == borderShapeRenderer) {
            borderShapeRenderer = new ShapeRenderer();
            borderShapeRenderer.setColor(ColorProperties.BORDER);
            borderShapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        }
        //draw blue border
        borderShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //draw the left line
        borderShapeRenderer.rectLine(writingPadNumberArea.x, writingPadNumberArea.y,
                writingPadNumberArea.x, writingPadNumberArea.y + writingPadNumberArea.height, BORDER_WIDTH);

        //draw the top line
        borderShapeRenderer.rectLine(writingPadNumberArea.x, writingPadNumberArea.y + writingPadNumberArea.height,
                writingPadNumberArea.x + writingPadNumberArea.width, writingPadNumberArea.y + writingPadNumberArea.height, BORDER_WIDTH);

        //draw the right line
        borderShapeRenderer.rectLine(writingPadNumberArea.x + writingPadNumberArea.width,
                writingPadNumberArea.y,
                writingPadNumberArea.x + writingPadNumberArea.width,
                writingPadNumberArea.y + writingPadNumberArea.height, BORDER_WIDTH);

        //draw the bottom line
        borderShapeRenderer.rectLine(writingPadNumberArea.x, writingPadNumberArea.y,
                writingPadNumberArea.x + writingPadNumberArea.width, writingPadNumberArea.y, BORDER_WIDTH);


        borderShapeRenderer.end();

        if (null == dottedLineShapeRenderer) {
            dottedLineShapeRenderer = new ShapeRenderer();
            dottedLineShapeRenderer.setColor(Color.BLACK);
            dottedLineShapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        }

        //draw a vertical dotted line in the left side of the second writing pad
        drawDottedLine(dottedLineShapeRenderer,
                writingPadNumberTenDigitArea.x,
                writingPadNumberTenDigitArea.y, writingPadNumberTenDigitArea.height);

        //draw a vertical dotted line in the left side of the third writing pad
        drawDottedLine(dottedLineShapeRenderer,
                writingPadNumberDigitArea.x,
                writingPadNumberDigitArea.y, writingPadNumberDigitArea.height);

        if (isMicrophoneRequired()) {
            //draw microphone
            super.render();
        }

        //draw the line if user has writing something in the pad
        handWritingRecognizeScreenService.drawLine();

    }

    private void initNumberScreenObjectList() {
        if (null == numberDigitScreenObject) {
            //here use the number 9 to determine the maximum size of the number
            float[] writingPadSize = LetterUtils.getSizeOfWord("9", TextFontSizeEnum.FONT_288);
            float writingPadWidth = writingPadSize[0] + PADDING_FOR_WRITING_PAD * 2;
            float writingPadHeight = writingPadSize[1] + PADDING_FOR_WRITING_PAD * 2;

            boolean isHundredDigitRequired = maximumNumber >= 100;

            int numberOfWritingPadRequired = //which mean only 2 number is required
                    isHundredDigitRequired ?
                            //there are 3 writing pad for 3 digits
                            3 :
                            //there are 2 writing pad for 2 digits
                            2;

            float startXPositionOfWritingPad = ScreenUtils.getXPositionForCenterObject(
                    //there are 2 writing pad for 2 digits
                    writingPadWidth * numberOfWritingPadRequired);

            float startYPositionOfWritingPad = ScreenUtils.getBottomYPositionForCenterObject(writingPadHeight);

            writingPadNumberArea = new Rectangle(startXPositionOfWritingPad, startYPositionOfWritingPad,
                    writingPadWidth * numberOfWritingPadRequired, writingPadHeight);

            if (isHundredDigitRequired) {
                writingPadNumberHundredDigitArea = new Rectangle(startXPositionOfWritingPad, startYPositionOfWritingPad, writingPadWidth, writingPadHeight);
                numberHundredScreenObject = new NumberWritingScreenObject(0, writingPadWidth, writingPadNumberHundredDigitArea.x, 0, TextFontSizeEnum.FONT_288, true);

                answerNumberHundredCell = new NumberCell();
                answerNumberHundredCell.setSize(writingPadNumberHundredDigitArea.width, writingPadNumberHundredDigitArea.height);
                answerNumberHundredCell.setPosition(writingPadNumberHundredDigitArea.x, writingPadNumberHundredDigitArea.y);
                answerNumberHundredCell.setVisible(false);
                stage.addActor(answerNumberHundredCell);
            }

            writingPadNumberTenDigitArea = new Rectangle(
                    null == writingPadNumberHundredDigitArea ? startXPositionOfWritingPad :
                            (writingPadNumberHundredDigitArea.x + writingPadNumberHundredDigitArea.width),
                    startYPositionOfWritingPad, writingPadWidth, writingPadHeight);
            numberTenScreenObject = new NumberWritingScreenObject(0, writingPadWidth, writingPadNumberTenDigitArea.x, 0, TextFontSizeEnum.FONT_288, false);
            answerNumberTenCell = new NumberCell();
            answerNumberTenCell.setSize(writingPadNumberTenDigitArea.width, writingPadNumberTenDigitArea.height);
            answerNumberTenCell.setPosition(writingPadNumberTenDigitArea.x, writingPadNumberTenDigitArea.y);
            answerNumberTenCell.setVisible(false);
            stage.addActor(answerNumberTenCell);

            writingPadNumberDigitArea = new Rectangle(writingPadNumberTenDigitArea.x + writingPadWidth, startYPositionOfWritingPad,
                    writingPadWidth, writingPadHeight);
            numberDigitScreenObject = new NumberWritingScreenObject(0, writingPadWidth, writingPadNumberDigitArea.x, 0, TextFontSizeEnum.FONT_288, false);
            answerNumberDigitCell = new NumberCell();
            answerNumberDigitCell.setSize(writingPadNumberDigitArea.width, writingPadNumberDigitArea.height);
            answerNumberDigitCell.setPosition(writingPadNumberDigitArea.x, writingPadNumberDigitArea.y);
            answerNumberDigitCell.setVisible(false);
            stage.addActor(answerNumberDigitCell);

            regenerateDisplayNumber();

            if (null != numberHundredScreenObject) {
                setDisplayNumberYPosition(numberHundredScreenObject);
            }

            setDisplayNumberYPosition(numberTenScreenObject);

            setDisplayNumberYPosition(numberDigitScreenObject);

            regenerateDisplayNumber();
        }
    }

    /**
     * Draw a vertical dotted line from bottom to top
     *
     * @param shapeRenderer
     * @param startXPosition
     * @param startYPosition
     * @param height
     */
    private void drawDottedLine(ShapeRenderer shapeRenderer, float startXPosition, float startYPosition, float height) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        final float endYPosition = startYPosition + height;

        while (startYPosition < endYPosition) {
            shapeRenderer.circle(startXPosition, startYPosition, DOTTED_RADIUS, 100);
            startYPosition += DOTTED_RADIUS * 2 + 5;
        }

        shapeRenderer.end();
    }

    protected void regenerateDisplayNumber() {
        displayNumber = RandomUtils.getRandomWithExclusion(minimumNumber, maximumNumber);
        setNumberScreenObjectDisplayText();
    }

    private void setDisplayNumberYPosition(NumberScreenObject numberScreenObject) {
        numberScreenObject.setYPositionInScreen(writingPadNumberDigitArea.y - SPACE_BETWEEN_WRITING_PAD_AND_NUMBER, false);
    }

    private void setNumberScreenObjectDisplayText() {
        if (null != numberHundredScreenObject) {
            numberHundredScreenObject.setDisplayText(displayNumber / 100);
            answerNumberHundredCell.setNumber(displayNumber / 100);
        }
        numberTenScreenObject.setDisplayText(displayNumber / 10 % 10);
        answerNumberTenCell.setNumber(displayNumber / 10 % 10);
        numberDigitScreenObject.setDisplayText(displayNumber % 10);
        answerNumberDigitCell.setNumber(displayNumber % 10);
    }

    @Override
    protected boolean isMicrophoneRequired() {
        return true;
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        microphoneService.touchUp();
        handWritingRecognizeScreenService.touchUp();
    }

    @Override
    protected int getNumberRequiredForSpeechRecognize() {
        return displayNumber;
    }

    private float getCenterX(Rectangle area, float objectWidth) {
        return area.getX() + (area.width - objectWidth) / 2;
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        numberHundredScreenObject = null;
        numberTenScreenObject = null;
        numberDigitScreenObject = null;

        if (null != borderShapeRenderer) {
            borderShapeRenderer.dispose();
            borderShapeRenderer = null;
        }

        if (null != dottedLineShapeRenderer) {
            dottedLineShapeRenderer.dispose();
            dottedLineShapeRenderer = null;
        }


    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        handWritingRecognizeScreenService.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (!microphoneService.touchDown(screenX, screenY)) {
            handWritingRecognizeScreenService.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        }
    }

    @Override
    public boolean isDrawAllow(int screenX, int screenY) {
        return isTouchingWritingPadNumberDigit(screenX, screenY)
                || isTouchingWritingPadNumberTenDigit(screenX, screenY)
                || isTouchingWritingPadNumberHundredDigit(screenX, screenY);
    }

    private boolean isTouchingWritingPadNumberDigit(int screenX, int screenY) {
        return TouchUtils.isTouched(writingPadNumberDigitArea, screenX, screenY);
    }

    private boolean isTouchingWritingPadNumberTenDigit(int screenX, int screenY) {
        return TouchUtils.isTouched(writingPadNumberTenDigitArea, screenX, screenY);
    }

    private boolean isTouchingWritingPadNumberHundredDigit(int screenX, int screenY) {
        return TouchUtils.isTouched(writingPadNumberHundredDigitArea, screenX, screenY);
    }

    @Override
    public boolean isSaveCorrectDrawPointsRequired() {
        return false;
//        return true;
    }

    @Override
    public void whenCorrectLetterWrite() {
        numberOfTimeFailed = 0;
    }

    @Override
    public void whenWrongLetterWrite() {
        numberOfTimeFailed = 0;
    }

    @Override
    public void whenLetterWriteFails() {
        numberOfTimeFailed++;
        if (numberOfTimeFailed == 3) {
            numberOfTimeFailed = 0;
            if (null != answerNumberHundredCell) {
                answerNumberHundredCell.setVisible(true);
            }
            if (null != answerNumberTenCell) {
                answerNumberTenCell.setVisible(true);
            }
            if (null != answerNumberDigitCell) {
                answerNumberDigitCell.setVisible(true);
            }
            AnimationUtils.doFlashAndFadeOut(answerNumberHundredCell);
            AnimationUtils.doFlashAndFadeOut(answerNumberTenCell);
            AnimationUtils.doFlashAndFadeOut(answerNumberDigitCell);
        }
    }

    @Override
    public boolean isWriteCorrect() {
        if (null != AutoCognitaGame.handWritingRecognizeService) {
            if ((null == numberHundredScreenObject || numberHundredScreenObject.isNumberWritingCorrect()) &&
                    numberTenScreenObject.isNumberWritingCorrect() && numberDigitScreenObject.isNumberWritingCorrect()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void afterDrawPointAdded(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        if (isTouchingWritingPadNumberHundredDigit(screenX, screenY) && null != numberHundredScreenObject) {
            numberHundredScreenObject.addDrawingPoints(systemDetectXPosition, systemDetectYPosition);
        } else if (isTouchingWritingPadNumberTenDigit(screenX, screenY)) {
            numberTenScreenObject.addDrawingPoints(systemDetectXPosition, systemDetectYPosition);
        } else if (isTouchingWritingPadNumberDigit(screenX, screenY)) {
            numberDigitScreenObject.addDrawingPoints(systemDetectXPosition, systemDetectYPosition);
        }
    }

    protected void clearDrawPoints() {
        if (null != numberHundredScreenObject) {
            numberHundredScreenObject.clearDrawingPoints();
        }
        numberTenScreenObject.clearDrawingPoints();
        numberDigitScreenObject.clearDrawingPoints();
    }


}
