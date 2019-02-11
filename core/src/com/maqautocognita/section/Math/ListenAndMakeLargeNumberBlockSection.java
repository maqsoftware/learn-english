package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

import java.util.List;

/**
 * User listens to given number within 10-99 and is asked to make it using blocks.  Repeat 8 times.
 *
 * @author sc.chi csc19840914@gmail.com
 */

public class ListenAndMakeLargeNumberBlockSection extends ReadAndMakeLargeNumberBlockSection {


    public ListenAndMakeLargeNumberBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int minimumValue, int maximumValue, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, minimumValue, maximumValue, abstractAutoCognitaScreen, onHelpListener);
    }


    @Override
    protected boolean isNumberDisplayRequired() {
        return false;
    }

    @Override
    protected void afterPlayCorrectlyAndNextRoundNumberIsGenerated() {
        onHelp();
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        onHelp();
    }

    @Override
    protected List<String> getHelpAudioFileNameList() {
        return mathAudioScriptWithElementCode.getNumberAudioFileList(playingNumber);
    }
}
