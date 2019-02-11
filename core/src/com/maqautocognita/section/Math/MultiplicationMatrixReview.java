package com.maqautocognita.section.Math;


import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.Config;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.RoundCornerRectangleScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.HandWritingRecognizeScreenService;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * User is asked to answer multiplication questions up to 10x10.  User can use the multiplication matrix to help.  Repeat 8 times.
 *
 * @author sc.chi csc19840914@gmail.com
 */

public class MultiplicationMatrixReview extends MultiplicationMatrixSection implements HandWritingRecognizeScreenService.IHandWritingRecognizeListener {

    private static final int NUMBER_OF_ROUND_REQUIRED_TO_PLAY = 8;

    private static final int WRITING_PAD_START_Y_POSITION = 535;
    private final HandWritingRecognizeScreenService handWritingRecognizeScreenService;
    protected RoundCornerRectangleScreenObject writingPad;
    /**
     * Store the drawing point of the hundreds column digit
     */
    private List<Vector2> drawingPointsForHundredsDigit;
    /**
     * Store the drawing point of the ten column digit
     */
    private List<Vector2> drawingPointsForTensDigit;
    /**
     * Store the drawing point of the one column digit
     */
    private List<Vector2> drawingPointsForNumberDigit;
    private int numberOfRoundPlayed;

    private ShapeRenderer shapeRenderer;

    public MultiplicationMatrixReview(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.handWritingRecognizeScreenService = new HandWritingRecognizeScreenService(this);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (null != writingPad) {
            writingPad.dispose();
            writingPad = null;
        }
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        handWritingRecognizeScreenService.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
    }

    @Override
    protected boolean isNumberBlockInNumberPadAllowToDrag() {
        return false;
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
        handWritingRecognizeScreenService.clearDrawPoints();
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (isAnswerWrote()) {
            handWritingRecognizeScreenService.touchUp();
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        handWritingRecognizeScreenService.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
    }

    @Override
    public void render() {
        super.render();
        initNumberWritingPad();

        batch.begin();
        ScreenObjectUtils.draw(batch, writingPad);
        batch.end();

        if (null == shapeRenderer) {
            shapeRenderer = new ShapeRenderer();
        }
        if (null != writingPad) {
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(ColorProperties.DISABLE_TEXT);
            int lengthOfAnswer = String.valueOf(result).length();
            if (lengthOfAnswer > 1) {
                float startXPosition = writingPad.xPositionInScreen;
                for (int i = 1; i < lengthOfAnswer; i++) {
                    startXPosition += writingPad.width / lengthOfAnswer;
                    //draw the line to indicate the digit write in the cell area
                    shapeRenderer.rect(startXPosition, writingPad.yPositionInScreen, 2, writingPad.height);
                }
            }
            shapeRenderer.end();
        }

        handWritingRecognizeScreenService.drawLine();
    }

    protected void initNumberWritingPad() {
        if (null != formulaTextScreenObject) {
            float writingPadXPosition = formulaTextScreenObject.xPositionInScreen + formulaTextScreenObject.width;
            if (null == writingPad) {
                int writingPadWidth = Config.SCREEN_CENTER_WIDTH / 5;
                int writingPadHeight = Config.TABLET_SCREEN_HEIGHT / 5;
                writingPad =
                        new RoundCornerRectangleScreenObject(writingPadXPosition,
                                WRITING_PAD_START_Y_POSITION, writingPadWidth, writingPadHeight, 5);
            } else {
                writingPad.xPositionInScreen = writingPadXPosition;
            }
        }


    }

    @Override
    protected boolean isSwitchModeButtonRequiredToShow() {
        return false;
    }

    @Override
    protected String getFormula() {
        StringBuilder formulaBuilder = new StringBuilder();
        formulaBuilder.append(yValue);
        formulaBuilder.append(" x ");
        formulaBuilder.append(xValue);
        formulaBuilder.append(" = ");
        return formulaBuilder.toString();
    }

    public boolean isAnswerWrote() {
        if (isWriting3DigitAnswer()) {
            return CollectionUtils.isNotEmpty(drawingPointsForHundredsDigit) &&
                    CollectionUtils.isNotEmpty(drawingPointsForTensDigit) &&
                    CollectionUtils.isNotEmpty(drawingPointsForNumberDigit);
        } else if (isWriting2DigitAnswer()) {
            return CollectionUtils.isNotEmpty(drawingPointsForTensDigit) &&
                    CollectionUtils.isNotEmpty(drawingPointsForNumberDigit);
        } else {
            return CollectionUtils.isNotEmpty(drawingPointsForNumberDigit);
        }
    }

    /**
     * Check if the current answer is larger or equals than 10
     *
     * @return
     */
    private boolean isWriting3DigitAnswer() {
        return result >= 100;
    }

    /**
     * Check if the current answer is larger or equals than 10
     *
     * @return
     */
    private boolean isWriting2DigitAnswer() {
        return result >= 10;
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        if (numberOfRoundPlayed >= NUMBER_OF_ROUND_REQUIRED_TO_PLAY) {
            numberOfRoundPlayed = 0;
        }
        if (xValue == 0 || yValue == 0) {
            nextRound();
        }
    }

    protected void nextRound() {
        resetScreen();
        int randomYValue = RandomUtils.getRandomWithExclusion(1, 10);
        int randomXValue = RandomUtils.getRandomWithExclusion(1, 10);
        for (int i = 0; i < randomYValue; i++) {
            addNumberBlockUpInNumberPad(randomXValue);
        }
    }

    @Override
    protected boolean isTrashRequired() {
        return false;
    }

    @Override
    protected boolean isTouchingNumberBlock(int screenX, int screenY) {
        //in order to make sure the user cannot drag the number block in the bottom of the screen
        return false;
    }

    @Override
    public boolean isDrawAllow(int screenX, int screenY) {
        return TouchUtils.isTouched(writingPad, screenX, screenY);
    }

    @Override
    public boolean isSaveCorrectDrawPointsRequired() {
        return false;
//        return true;
    }

    @Override
    public void whenCorrectLetterWrite() {
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {
                numberOfRoundPlayed++;
                if (numberOfRoundPlayed >= NUMBER_OF_ROUND_REQUIRED_TO_PLAY) {
                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
                } else {
                    nextRound();
                }
            }
        });
    }

    @Override
    public void whenWrongLetterWrite() {
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {

            }
        });
    }

    @Override
    public void whenLetterWriteFails() {
        if (null != drawingPointsForHundredsDigit) {
            drawingPointsForHundredsDigit.clear();
        }
        if (null != drawingPointsForTensDigit) {
            drawingPointsForTensDigit.clear();
        }
        if (null != drawingPointsForNumberDigit) {
            drawingPointsForNumberDigit.clear();
        }
    }

    @Override
    public boolean isWriteCorrect() {
        if (null != AutoCognitaGame.handWritingRecognizeService) {
            if (isWriting3DigitAnswer()) {
                return isDrawingPointsCorrect(drawingPointsForHundredsDigit, result / 100) &&
                        isDrawingPointsCorrect(drawingPointsForTensDigit, (result - 100) / 10) && isDrawingPointsCorrect(drawingPointsForNumberDigit, result % 10);
            } else if (isWriting2DigitAnswer()) {
                return
                        isDrawingPointsCorrect(drawingPointsForTensDigit, result / 10) && isDrawingPointsCorrect(drawingPointsForNumberDigit, result % 10);

            } else {
                return isDrawingPointsCorrect(drawingPointsForNumberDigit, result);
            }
        }

        return false;
    }

    private boolean isDrawingPointsCorrect(List<Vector2> drawingPoints, int expectedResult) {
        boolean isCorrect = false;
        if (CollectionUtils.isNotEmpty(drawingPoints)) {
            AutoCognitaGame.handWritingRecognizeService.clearPoints();
            for (Vector2 vector2 : drawingPoints) {
                AutoCognitaGame.handWritingRecognizeService.addPoint(vector2.x, vector2.y);
            }

            isCorrect = AutoCognitaGame.handWritingRecognizeService.isCorrect(String.valueOf(expectedResult));
            drawingPoints.clear();
        }
        return isCorrect;
    }

    /**
     * The current handWritingRecognizeService is only support reognize 1 letter in the writing pad, so if there is a case which are required to write 2 digit such as 10,
     * here is required to store the drawing point separately in the writing pad.
     * <p/>
     * In the left side of the writing pad will be store the ten column number digit
     * In the right side of the writing pad will be store the one column number digit
     *
     * @param screenX
     * @param screenY
     * @param systemDetectXPosition
     * @param systemDetectYPosition
     */
    @Override
    public void afterDrawPointAdded(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (isWriting3DigitAnswer()) {
            if (screenX < writingPad.xPositionInScreen + writingPad.width / 3) {
                if (null == drawingPointsForHundredsDigit) {
                    drawingPointsForHundredsDigit = new ArrayList<Vector2>();
                }
                drawingPointsForHundredsDigit.add(new Vector2(systemDetectXPosition, systemDetectYPosition));
            } else if (screenX < writingPad.xPositionInScreen + writingPad.width / 3 * 2) {
                if (null == drawingPointsForTensDigit) {
                    drawingPointsForTensDigit = new ArrayList<Vector2>();
                }
                drawingPointsForTensDigit.add(new Vector2(systemDetectXPosition, systemDetectYPosition));
            } else {
                if (null == drawingPointsForNumberDigit) {
                    drawingPointsForNumberDigit = new ArrayList<Vector2>();
                }

                drawingPointsForNumberDigit.add(new Vector2(systemDetectXPosition, systemDetectYPosition));
            }
        } else if (isWriting2DigitAnswer()) {

            //check if the  touching position is within the tens digit  area
            if (screenX < writingPad.xPositionInScreen + writingPad.width / 2) {
                if (null == drawingPointsForTensDigit) {
                    drawingPointsForTensDigit = new ArrayList<Vector2>();
                }
                drawingPointsForTensDigit.add(new Vector2(systemDetectXPosition, systemDetectYPosition));
            } else {
                if (null == drawingPointsForNumberDigit) {
                    drawingPointsForNumberDigit = new ArrayList<Vector2>();
                }

                drawingPointsForNumberDigit.add(new Vector2(systemDetectXPosition, systemDetectYPosition));
            }
        } else {
            if (null == drawingPointsForNumberDigit) {
                drawingPointsForNumberDigit = new ArrayList<Vector2>();
            }

            drawingPointsForNumberDigit.add(new Vector2(systemDetectXPosition, systemDetectYPosition));
        }
    }

    @Override
    public boolean isRequiredClearDrawPointsAfterTimesUp() {
        return true;
    }
}
