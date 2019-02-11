package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.ScreenUtils;

/**
 * User reads given number within 10-99 and is asked to make it using blocks.
 * The given number should be shown in smaller font in the upper left side of the screen, highlighted in red.  Touching the number plays the audio of the number.  Repeat {@link #getNumberOfRoundNeedToPlay()} times.
 *
 * @author sc.chi csc19840914@gmail.com
 */

public class ReadAndMakeLargeNumberBlockSection extends AbstractLargeNumberBlockSection implements TimerService.ITimerListener {

    private final static int NUMBER_OF_ROUND_NEED_TO_PLAY = 8;
    protected final int minimumValue;
    protected int playingNumber;
    private NumberScreenObject numberScreenObject;
    private int numberOfRoundPlayed;
    private TimerService timerService;

    public ReadAndMakeLargeNumberBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int minimumValue, int maximumValue, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, maximumValue, abstractAutoCognitaScreen, onHelpListener);
        this.minimumValue = minimumValue;
        timerService = new TimerService(this);
        nextRound();
    }

    protected void nextRound() {
        playingNumber = RandomUtils.getRandomWithExclusion(minimumValue, maximumValue, playingNumber);
        if (null != numberScreenObject) {
            numberScreenObject.setDisplayText(playingNumber);
        }
    }

    @Override
    protected void render() {
        super.render();
        if (isNumberDisplayRequired()) {
            if (null == numberScreenObject) {
                numberScreenObject = new NumberScreenObject(playingNumber, ScreenUtils.getNavigationBarStartXPosition(), numberPadTextureScreenObject.yPositionInScreen + numberPadTextureScreenObject.height, TextFontSizeEnum.FONT_144, true);
            }
            batch.begin();
            ScreenObjectUtils.draw(batch, numberScreenObject);
            batch.end();
        }
    }

    /**
     * The number which is display in the upper left side
     *
     * @return
     */
    protected boolean isNumberDisplayRequired() {
        return true;
    }

    @Override
    protected boolean isMicrophoneRequired() {
        return false;
    }

    @Override
    protected void afterNumberBlockAdded() {
        super.afterNumberBlockAdded();
        doNumberBlockChanged();
    }

    @Override
    protected void afterNumberBlockRemoved() {
        super.afterNumberBlockRemoved();
        doNumberBlockChanged();
    }

    private void doNumberBlockChanged() {
        if (result == playingNumber) {
            timerService.startTimer(null, 1);
        }
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        if (numberOfRoundPlayed >= getNumberOfRoundNeedToPlay()) {
            resetScreen();
            nextRound();
            numberOfRoundPlayed = 0;
        }
    }

    protected int getNumberOfRoundNeedToPlay() {
        return NUMBER_OF_ROUND_NEED_TO_PLAY;
    }

    @Override
    public void beforeStartTimer() {

    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        if (result == playingNumber) {
            whenRoundPlayed();
        }
    }

    protected void whenRoundPlayed() {
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {

                numberOfRoundPlayed++;
                if (numberOfRoundPlayed >= getNumberOfRoundNeedToPlay()) {
                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
                } else {
                    resetScreen();
                    nextRound();
                    afterPlayCorrectlyAndNextRoundNumberIsGenerated();
                }
            }
        });
    }

    protected void afterPlayCorrectlyAndNextRoundNumberIsGenerated() {
    }
}
