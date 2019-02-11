package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class InstructionSubtractionUsingColorBlockSection extends SubtractionUsingColorBlockSection {

    private final int playingLeftValue;

    private final int playingRightValue;

    public InstructionSubtractionUsingColorBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int playingLeftValue, int playingRightValue, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);

        this.playingLeftValue = playingLeftValue;
        this.playingRightValue = playingRightValue;

    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        resetScreen();
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        onHelp();
    }

    @Override
    protected void onNoIntroductionAudioPlay() {
        super.onNoIntroductionAudioPlay();
        onHelp();
    }

    @Override
    protected List<String> getHelpAudioFileNameList() {
        if (leftValue == 0) {
            return mathAudioScriptWithElementCode.instructionScriptAudioFileNameList;
        } else {
            return mathAudioScriptWithElementCode.instructionScript2AudioFileNameList;
        }
    }

    @Override
    protected boolean isLeftValueNumberBlockAllowToDrop(int leftValue) {
        return playingLeftValue == leftValue;
    }

    @Override
    protected boolean isRightValueNumberBlockAllowToDrop(int rightValue) {
        return playingRightValue == rightValue;
    }

    @Override
    protected void whenLeftValueDroppedInTheNumberTray() {
        super.whenLeftValueDroppedInTheNumberTray();
        onHelp();

    }

    @Override
    protected void whenRightValueDroppedInTheNumberTray() {
        super.whenRightValueDroppedInTheNumberTray();
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {
                abstractAutoCognitaScreen.showNextSection(numberOfFails);
            }
        });
    }

}
