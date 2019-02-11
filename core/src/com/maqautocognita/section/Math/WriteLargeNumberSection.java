package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class WriteLargeNumberSection extends AbstractLargeNumberSpeakingAndWritingSection {

    private final static int NUMBER_OF_ROUND_NEED_TO_PLAY = 8;

    private int numberOfRoundPlayed;

    public WriteLargeNumberSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int minimumNumber, int maximumNumber, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, minimumNumber, maximumNumber, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        onHelp();
    }

    @Override
    public boolean isRequiredClearDrawPointsAfterTimesUp() {
        return true;
    }

    @Override
    protected boolean isMicrophoneRequired() {
        return false;
    }



    @Override
    protected void regenerateDisplayNumber() {
        hideDisplayNumber();
        super.regenerateDisplayNumber();
        mathAudioScriptWithElementCode.setHelpAudioFilenameList(mathAudioScriptWithElementCode.getNumberAudioFileList(displayNumber));
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        if (numberOfRoundPlayed >= NUMBER_OF_ROUND_NEED_TO_PLAY) {
            clearDrawPoints();
            regenerateDisplayNumber();
            numberOfRoundPlayed = 0;
        }
    }

    private void hideDisplayNumber() {
        if (null != numberHundredScreenObject) {
            numberHundredScreenObject.isVisible = false;
        }
        if (null != numberTenScreenObject) {
            numberTenScreenObject.isVisible = false;
        }
        if (null != numberDigitScreenObject) {
            numberDigitScreenObject.isVisible = false;
        }
    }

    @Override
    public void whenCorrectLetterWrite() {
        super.whenCorrectLetterWrite();
        showDisplayNumber();
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {
                //reset the formula
                clearDrawPoints();
                numberOfRoundPlayed++;
                if (numberOfRoundPlayed >= NUMBER_OF_ROUND_NEED_TO_PLAY) {
                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
                } else {
                    regenerateDisplayNumber();
                    onHelp();
                }
            }
        });
    }

    @Override
    public void whenWrongLetterWrite() {
        super.whenWrongLetterWrite();
        abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {

            }
        });
    }

    private void showDisplayNumber() {
        if (null != numberHundredScreenObject) {
            numberHundredScreenObject.isVisible = true;
        }
        if (null != numberTenScreenObject) {
            numberTenScreenObject.isVisible = true;
        }
        if (null != numberDigitScreenObject) {
            numberDigitScreenObject.isVisible = true;
        }
    }


}
