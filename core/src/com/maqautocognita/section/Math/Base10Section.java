package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.Timer;
import java.util.TimerTask;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class Base10Section extends AbstractNumberPadSection implements TimerService.ITimerListener {

    private static final int NUMBER_BLOCK_START_X_POSITION_IN_NUMBER_TRAY = 210;
    private static final int START_COUNTING_NUMBER = 10;
    private static final int MAXIMUM_COUNTING_NUMBER = 30;

    private static final IconPosition ADD_1_BUTTON_POSITION = new IconPosition(1600, 820, 98, 105);
    private int countingNumber = 0;
    private boolean isShow1BlockInTheTray;
    private boolean isAddOneButtonRequiredToShow;

    private Timer timer;
    private TimerService timerService;

    public Base10Section(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.timerService = new TimerService(this);
    }

    @Override
    public void render() {

        startAddNumberAnimation();

        super.render();

        batch.begin();

        //draw the result number in the center of the screen
        drawNumberInScreenCenter(countingNumber);


        if (isAddOneButtonRequiredToShow) {
            //draw the add 1 button
            batch.draw(new AutoCognitaTextureRegion(smallBlockTrayTexture, 0, 200, (int) ADD_1_BUTTON_POSITION.width, (int) ADD_1_BUTTON_POSITION.height), ADD_1_BUTTON_POSITION.x, ADD_1_BUTTON_POSITION.y);

        }
        //if (countingNumber > START_COUNTING_NUMBER) {
        int startNumberBlockXPosition = NUMBER_BLOCK_START_X_POSITION_IN_NUMBER_TRAY;
        //draw the number block in the tray
        if (isShow1BlockInTheTray) {
            for (int i = 0; i < countingNumber / 10; i++) {
                //draw number block 10 tray
                batch.draw(numberBlocksAutoCognitaTextureRegions[9], startNumberBlockXPosition, 663);
                startNumberBlockXPosition += numberBlocksAutoCognitaTextureRegions[9].getRegionWidth() + NUMBER_BLOCK_GAP;
            }

            if (countingNumber % 10 > 0) {
                batch.draw(numberBlocksAutoCognitaTextureRegions[countingNumber % 10 - 1], startNumberBlockXPosition, 663);
            }

        } else {
            //draw the one number block in the block
            for (int i = 0; i < countingNumber; i++) {
                batch.draw(numberBlocksAutoCognitaTextureRegions[0], startNumberBlockXPosition + i * 50, 663);
            }
        }


        batch.end();

    }

    /**
     * make a quick intro to this screen by starting from 0 (instead of 10),
     * then automatically add one block at a time and showing the corresponding number (about 0.5 seconds each),
     * until it reaches 10, then all the 10 “1” blocks turn into one “10” block.  We don’t show the “+1” at the beginning, and only show it once the “10” block is displayed.
     * Do not allow any user input while the screen shows the automatic addition of blocks from 0 to 10.
     * Then show the “+1” button and allow the user to press that, to go from 10 all the way to 30.
     */
    private void startAddNumberAnimation() {
        if (null == timer) {
            timer = new Timer();
            final TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    countingNumber++;
                    if (countingNumber == START_COUNTING_NUMBER) {
                        //then all the 10 “1” blocks turn into one “10” block
                        isShow1BlockInTheTray = true;
                        //show the “+1” button and allow the user to press that
                        isAddOneButtonRequiredToShow = true;
                        timer.cancel();
                    }
                }
            };

            timer.schedule(timerTask, 1000, 1000);
        }

    }

    @Override
    protected Rectangle getNumberTrayTextureRegionArea() {
        return new Rectangle(0, 100, 1510, 60);
    }

    @Override
    protected int getMaximumNumberAboveTheTray() {
        return MAXIMUM_COUNTING_NUMBER;
    }

    @Override
    protected void beforeDrawNumber(NumberScreenObject numberScreenObject) {
        if (numberScreenObject.displayText <= countingNumber) {
            numberScreenObject.setColor(ColorProperties.TEXT);
        } else {
            numberScreenObject.setColor(ColorProperties.DISABLE_TEXT);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        resetScreen();
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
        countingNumber = 0;
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (TouchUtils.isTouched(ADD_1_BUTTON_POSITION, screenX, screenY)) {
            if (countingNumber < MAXIMUM_COUNTING_NUMBER) {
                timerService.startTimer(null);
            } else {
                isAddOneButtonRequiredToShow = false;
            }
        }

    }

    @Override
    public void beforeStartTimer() {
        isShow1BlockInTheTray = false;
        countingNumber++;
    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        isShow1BlockInTheTray = true;
    }

}
