package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class ExampleLargeNumberBlock3DigitSection extends AbstractLargeNumberBlockSection {

    /**
     * it the correct sound is played, even the user is drag the number block again in a correct position, the correct sound will not be play again
     * it will reset to false, if the screen is hiding in {@link #onHide()}
     */
    private boolean isCorrectSoundPlayed;

    public ExampleLargeNumberBlock3DigitSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, 999, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        if (result < 10) {
            addNumber10Block();
        }

    }

    @Override
    protected void onHide() {
        super.onHide();
        isCorrectSoundPlayed = false;
    }

    @Override
    protected boolean isMicrophoneRequired() {
        return false;
    }

    @Override
    protected void afterNumberBlockAdded() {
        if (result >= 100 && !isCorrectSoundPlayed) {
            isCorrectSoundPlayed = true;
            abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                @Override
                public void onCorrectSoundPlayed() {
                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
                }
            });
        }
        else{
            abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                @Override
                public void onCorrectSoundPlayed() {

                }
            });
        }
    }

}
