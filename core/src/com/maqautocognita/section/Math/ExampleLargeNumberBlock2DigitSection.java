package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.badlogic.gdx.Gdx;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class ExampleLargeNumberBlock2DigitSection extends AbstractLargeNumberBlockSection {

    private static final int NUMBER_OF_MILLISECOND_TO_WAIT_FOR_NEXT_BLOCK = 500;

    public ExampleLargeNumberBlock2DigitSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, 99, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected boolean isMicrophoneRequired() {
        return false;
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        if (result < 10) {
            new Thread(new Runnable() {
                int numberOfAddedBlock;

                @Override
                public void run() {
                    while (numberOfAddedBlock < 10) {
                        addNumber1Block();
                        numberOfAddedBlock++;
                        try {
                            Thread.sleep(NUMBER_OF_MILLISECOND_TO_WAIT_FOR_NEXT_BLOCK);
                        } catch (InterruptedException e) {
                            Gdx.app.log(getClass().getName(), "", e);
                        }
                    }
                    if (isShowing) {
                        abstractAutoCognitaScreen.showNextSection(numberOfFails);
                    }
                }
            }).start();

        }
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();

    }
}
