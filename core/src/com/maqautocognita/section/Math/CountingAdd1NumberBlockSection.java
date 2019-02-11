package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.math.Rectangle;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CountingAdd1NumberBlockSection extends AbstractNumberPadSection implements TimerService.ITimerListener {

    private static final IconPosition ADD_1_BUTTON_POSITION = new IconPosition(1600, 820, 98, 105);
    private final boolean isRequiredToShowNumberBlockInBottom;
    private int countingNumber;
    private boolean isShow1BlockInTheTray;
    private TimerService timerService;

    public CountingAdd1NumberBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, boolean isRequiredToShowNumberBlockInBottom, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.isRequiredToShowNumberBlockInBottom = isRequiredToShowNumberBlockInBottom;
        if (isRequiredToShowNumberBlockInBottom) {
            timerService = new TimerService(this);
        }
    }

    @Override
    public void render() {
        initNumberBlocksHorizontalTexture();

        super.render();

        batch.begin();

        //draw the result number in the center of the screen
        drawNumberInScreenCenter(countingNumber);

        //draw +1 button
        batch.draw(new AutoCognitaTextureRegion(smallBlockTrayTexture, 0, 200, (int) ADD_1_BUTTON_POSITION.width, (int) ADD_1_BUTTON_POSITION.height), ADD_1_BUTTON_POSITION.x, ADD_1_BUTTON_POSITION.y);


        if (countingNumber > 0) {

            int numberOfNumberBlocksShowInBottomScreen = isShow1BlockInTheTray ? countingNumber : countingNumber - 1;

            if (ArrayUtils.isNotEmpty(numberBlocks)) {
                for (int i = 0; i < numberBlocks.length; i++) {
                    numberBlocks[i].setVisible(i < numberOfNumberBlocksShowInBottomScreen);
                }
            }

            //draw the number block in the tray
            if (isShow1BlockInTheTray) {
                batch.draw(numberBlocksAutoCognitaTextureRegions[countingNumber - 1], 710, 663);
            } else {
                //draw the one number block in the block
                for (int i = 0; i < countingNumber; i++) {
                    batch.draw(numberBlocksAutoCognitaTextureRegions[0], 710 + i * 50, 663);
                }
            }
        } else if (isRequiredToShowNumberBlockInBottom) {
            //hide all number blocks
            for (ImageActor numberOfBlock : numberBlocks) {
                numberOfBlock.setVisible(false);
            }
        }

        batch.end();
    }

    @Override
    protected Rectangle getNumberTrayTextureRegionArea() {
        return new Rectangle(0, 0, 510, 60);
    }

    @Override
    protected int getMaximumNumberAboveTheTray() {
        return 10;
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
    protected void singleTap(int screenX, int screenY) {
        if (TouchUtils.isTouched(ADD_1_BUTTON_POSITION, screenX, screenY)) {
            if (countingNumber < 10) {
                countingNumber++;
                if (10 > countingNumber) {
                    //only play the audio which is less than 10, the number 10 will be play is the method {@link #isLessonComplete}
                    playNumberAudio(countingNumber);
                }
                if (isRequiredToShowNumberBlockInBottom) {
                    timerService.startTimer(null);
                } else {
                    isLessonComplete();
                }
            }
        }

    }

    @Override
    protected boolean isNumberBlocksRequired() {
        return isRequiredToShowNumberBlockInBottom;
    }

    @Override
    protected void resetScreen() {
        countingNumber = 0;
    }

    private void isLessonComplete() {
        if (10 == countingNumber) {
            playNumberAudio(countingNumber, new AbstractSoundPlayListListener() {
                @Override
                public void onComplete() {
                    abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                        @Override
                        public void onCorrectSoundPlayed() {
                            abstractAutoCognitaScreen.showNextSection(numberOfFails);
                        }
                    });
                }
            });

        }
    }

    @Override
    public void beforeStartTimer() {
        isShow1BlockInTheTray = false;
    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        isShow1BlockInTheTray = true;
        isLessonComplete();
    }

}
