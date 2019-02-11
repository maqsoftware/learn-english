package com.maqautocognita.section.Phonic;

import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.AbstractPhonicActivity;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.bo.WordSoundMappingList;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.IActivityChangeListener;
import com.maqautocognita.utils.SectionUtils;

import java.util.HashMap;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicListenAndTypeSection extends AbstractPhonicKeyboardSection implements IActivityChangeListener {

    private Activity selectedActivity;

    /**
     * It is used to indicate if the whole word which display above the play sound is required to show,
     * it will always be true when the last correct sound of the playing word is pressed
     */
    private boolean isShowWholeWord;

    public PhonicListenAndTypeSection(AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return null;
    }

    @Override
    protected HashMap<String, WordSoundMappingList> getPhonicKeyboardWordGameMap() {
        return selectedActivity.getListenAndTypeMap();
    }

    @Override
    protected boolean isSmallWordShow() {
        return isShowWholeWord;
    }

    @Override
    protected void afterLastCorrectKeyPressedForPlayingWord() {
        isShowWholeWord = true;
        super.afterLastCorrectKeyPressedForPlayingWord();
    }


    protected void afterCorrectSoundIsPressed() {
        initSound();
    }

    @Override
    protected void afterCorrectSoundPlayed() {
        isShowWholeWord = false;
        super.afterCorrectSoundPlayed();
    }

    @Override
    protected AbstractPhonicActivity getAbstractPhonicModule() {
        return selectedActivity;
    }

    @Override
    public void reset() {
        super.reset();
        isShowWholeWord = false;
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return selectedActivity;
    }

    @Override
    public void setSelectedActivity(Activity selectedActivity, boolean isMasterTest) {
        this.selectedActivity = selectedActivity;
        initQuestionList();
        if (isShowing && SectionUtils.isListenAndType(selectedActivity.getActivityCode())) {
            //which the same screen is showing , but the showing letter is changed
            playIntroductionAudio();
        }
    }
}