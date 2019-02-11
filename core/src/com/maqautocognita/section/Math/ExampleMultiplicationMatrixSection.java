package com.maqautocognita.section.Math;


import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.TimerService;

import java.util.List;

/**
 * automatically shows a series of screen. 1x3=3; change to white blocks; change back; 2x3=6;
 * change to white blocks and back; 3x3=9;
 * change to white blocks and back; 4x3=12;
 * change to white blocks and back; 5x3=15; change to white blocks and back.
 *
 * @author sc.chi csc19840914@gmail.com
 */

public class ExampleMultiplicationMatrixSection extends MultiplicationMatrixSection implements TimerService.ITimerListener {

    private static final int PLAY_NUMBER = 3;

    private static final int NUMBER_OF_ROW_TO_PLAY = 5;

    private int numberOfPlayedRound;

    private TimerService timerService;

    public ExampleMultiplicationMatrixSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        timerService = new TimerService(this);
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        abstractAutoCognitaScreen.setTouchAllow(false);
        if (numberOfPlayedRound >= NUMBER_OF_ROW_TO_PLAY) {
            numberOfPlayedRound = 0;
        }
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        doExample();
    }

    @Override
    protected List<String> getHelpAudioFileNameList() {
        return mathAudioScriptWithElementCode.getInstructionScriptAudioFileNameList(yValue, xValue, xValue * yValue);
    }

    @Override
    protected void onHelpAudioComplete() {
        super.onHelpAudioComplete();
        doExample();
    }

    private void doExample() {
        if (numberOfPlayedRound > 0) {
            isCountingView = true;
            timerService.startTimer(null, 1);
        } else {
            startExample();
        }
    }

    private void startExample() {
        isCountingView = false;
        if (numberOfPlayedRound < NUMBER_OF_ROW_TO_PLAY) {
            addNumberBlockUpInNumberPad(PLAY_NUMBER);
            onHelp();
            numberOfPlayedRound++;
        } else {
            abstractAutoCognitaScreen.showNextSection(numberOfFails);
            abstractAutoCognitaScreen.setTouchAllow(true);
        }
    }

    @Override
    public void beforeStartTimer() {

    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        startExample();
    }
}
