package com.maqautocognita.section.Math;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.Config;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.RoundCornerRectangleScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.scene2d.ui.NumberCell;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.HandWritingRecognizeScreenService;
import com.maqautocognita.utils.AnimationUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * This is similar to the class {@link Counting10WithKeyboardSection}. but it will not render the number keyboard,
 * and user is required to write the correct answer in the {@link #writingPad}
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class CountingNumberWritingSection extends Counting10WithKeyboardSection implements HandWritingRecognizeScreenService.IHandWritingRecognizeListener {

    private static final int NUMBER_OF_FAILS_TIME_REQUIRED_TO_SHOW_ANSWER = 3;
    private HandWritingRecognizeScreenService handWritingRecognizeScreenService;
    private int numberOfFailsForTheCurrentLetter;
    private RoundCornerRectangleScreenObject writingPad;

    /**
     * Store the drawing point of the ten column digit
     */
    private List<Vector2> drawingPointsForNumber1;

    /**
     * Store the drawing point of the one column digit
     */
    private List<Vector2> drawingPointsForNumber0;


    private NumberCell numberCell;

    public CountingNumberWritingSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener, false);

    }

    @Override
    public void onResize() {
        super.onResize();
        if (null == handWritingRecognizeScreenService) {
            this.handWritingRecognizeScreenService = new HandWritingRecognizeScreenService(this);
        }
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        handWritingRecognizeScreenService.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
    }

    @Override
    public void render() {
        initNumberWritingPad();
        batch.begin();
        ScreenObjectUtils.draw(batch, writingPad);
        batch.end();
        super.render();
        handWritingRecognizeScreenService.drawLine();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (null != writingPad) {
            writingPad.dispose();
            writingPad = null;
        }
        if (null != drawingPointsForNumber1) {
            drawingPointsForNumber1.clear();
        }
        if (null != drawingPointsForNumber0) {
            drawingPointsForNumber0.clear();
        }

    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        handWritingRecognizeScreenService.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
    }

    @Override
    protected boolean isNumberScreenObjectRequired() {
        return false;
    }

    @Override
    protected void setPlayingNumber(int playingNumber) {
        super.setPlayingNumber(playingNumber);
        if (null != numberCell) {
            numberCell.setNumber(playingNumber);
            numberCell.setVisible(false);
        }
    }

    @Override
    protected Rectangle getObjectDrawWithinArea() {
        if (null != writingPad) {
            return new Rectangle(Config.SCREEN_CENTER_START_X_POSITION, getStartYPositionOfNumberKeyboard(),
                    Config.SCREEN_CENTER_WIDTH - writingPad.width, ScreenUtils.getNavigationBarStartYPosition() - getStartYPositionOfNumberKeyboard());
        }
        return null;
    }

    private void initNumberWritingPad() {
        if (null == writingPad) {
            int writingPadWidth = Config.SCREEN_CENTER_WIDTH / 6;
            int writingPadHeight = Config.TABLET_SCREEN_HEIGHT / 5;

            float startYPosition = ScreenUtils.getBottomYPositionForCenterObject(writingPadHeight);
            float startXPosition = Config.SCREEN_CENTER_START_X_POSITION + Config.SCREEN_CENTER_WIDTH - writingPadWidth;

            writingPad =
                    new RoundCornerRectangleScreenObject(startXPosition, startYPosition, writingPadWidth, writingPadHeight, 5);

            numberCell = new NumberCell();
            numberCell.setSize(writingPadWidth, writingPadHeight);
            numberCell.setPosition(startXPosition, startYPosition);
            stage.addActor(numberCell);
        }
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        handWritingRecognizeScreenService.touchUp();
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
        numberOfFailsForTheCurrentLetter = 0;
        afterPlayCorrectly();
    }

    @Override
    public void whenWrongLetterWrite() {
        numberOfFailsForTheCurrentLetter = 0;
        afterPlayCorrectly();
    }

    @Override
    public void whenLetterWriteFails() {
        numberOfFailsForTheCurrentLetter++;

        if (numberOfFailsForTheCurrentLetter >= NUMBER_OF_FAILS_TIME_REQUIRED_TO_SHOW_ANSWER) {
            AnimationUtils.doFlashAndFadeOut(numberCell);
            //numberCell.setVisible(false);
            numberOfFailsForTheCurrentLetter = 0;
        }

    }

    @Override
    public boolean isWriteCorrect() {
        if (null != AutoCognitaGame.handWritingRecognizeService) {
            if (isWritingNumber10()) {
                boolean isCorrect = false;

                AutoCognitaGame.handWritingRecognizeService.clearPoints();
                //check if number 1 write correctly
                for (Vector2 vector2 : drawingPointsForNumber1) {
                    AutoCognitaGame.handWritingRecognizeService.addPoint(vector2.x, vector2.y);
                }

                if (AutoCognitaGame.handWritingRecognizeService.isCorrect("1")) {
                    AutoCognitaGame.handWritingRecognizeService.clearPoints();
                    //check if number 0 write correctly
                    for (Vector2 vector2 : drawingPointsForNumber0) {
                        AutoCognitaGame.handWritingRecognizeService.addPoint(vector2.x, vector2.y);
                    }

                    isCorrect = AutoCognitaGame.handWritingRecognizeService.isCorrect("0");
                }

                drawingPointsForNumber0.clear();
                drawingPointsForNumber1.clear();

                return isCorrect;

            } else {
                return AutoCognitaGame.handWritingRecognizeService.isCorrect(String.valueOf(playingNumber));
            }
        }

        return false;
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
        if (isWritingNumber10()) {
            //check if the  touching position is within the number "1" area
            if (screenX < writingPad.xPositionInScreen + writingPad.width / 2) {
                if (null == drawingPointsForNumber1) {
                    drawingPointsForNumber1 = new ArrayList<Vector2>();
                }

                drawingPointsForNumber1.add(new Vector2(systemDetectXPosition, systemDetectYPosition));
            } else {
                if (null == drawingPointsForNumber0) {
                    drawingPointsForNumber0 = new ArrayList<Vector2>();
                }

                drawingPointsForNumber0.add(new Vector2(systemDetectXPosition, systemDetectYPosition));
            }

        }
    }

    @Override
    public boolean isRequiredClearDrawPointsAfterTimesUp() {
        return true;
    }

    /**
     * Check if the current playing number is 10
     *
     * @return
     */
    private boolean isWritingNumber10() {
        return 10 == playingNumber;
    }
}
