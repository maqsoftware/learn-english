package com.maqautocognita.section.Math;


import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.AnimateTextureScreenObject;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.maqautocognita.service.TimerService;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class ExampleMultiplicationStorySection extends MultiplicationStorySection implements TimerService.ITimerListener {

    private static final int[] NUMBER_OF_GROUP_PAIR = new int[]{3};
    private static final int[] NUMBER_OF_OBJECT_ALLOW_IN_GROUP_PAIR = new int[]{2};
    private TimerService timerService;
    private int playingIndex;

    public ExampleMultiplicationStorySection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        timerService = new TimerService(this);
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        playingIndex = 0;
        abstractAutoCognitaScreen.setTouchAllow(false);
    }

    @Override
    protected void beforePlayHelpAudio(int index) {
        super.beforePlayHelpAudio(index);
        addLion();
        timerService.startTimer(null, 0.5f);
    }

    private void addLion() {
        AnimateTextureScreenObject.IAnimationListener animationListener = null;
        if (playingIndex == getTotalNumberOfObjectRequiredToAdd() - 1) {
            animationListener = new AnimateTextureScreenObject.IAnimationListener() {
                @Override
                public void onComplete() {
                    abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.getInstruction2ScriptAudioFileNameList(), new AbstractSoundPlayListListener() {
                        @Override
                        public void onComplete() {
                            abstractAutoCognitaScreen.setTouchAllow(true);
                            abstractAutoCognitaScreen.showNextSection(numberOfFails);
                        }
                    });
                }
            };
        }
        addObject(animationListener);
        playingIndex++;
    }

    @Override
    protected String getScreenObjectImagePath() {
        return MathImagePathUtils.LION;
    }

    @Override
    protected int[] getNumberOfGroupPair() {
        return NUMBER_OF_GROUP_PAIR;
    }

    @Override
    protected int[] getNumberOfObjectAllowInGroupPair() {
        return NUMBER_OF_OBJECT_ALLOW_IN_GROUP_PAIR;
    }

    @Override
    public void beforeStartTimer() {

    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        addLion();
        if (playingIndex < getTotalNumberOfObjectRequiredToAdd()) {
            timerService.startTimer(null, 0.5f);
        }

    }
}
