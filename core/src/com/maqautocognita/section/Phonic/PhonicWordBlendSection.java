package com.maqautocognita.section.Phonic;

import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.AbstractPhonicActivity;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.bo.WordSoundMapping;
import com.maqautocognita.bo.WordSoundMappingList;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.IActivityChangeListener;
import com.maqautocognita.utils.SectionUtils;

import java.util.HashMap;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicWordBlendSection extends AbstractPhonicKeyboardSection implements IActivityChangeListener {

    private Activity selectedActivity;

    public PhonicWordBlendSection(AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return selectedActivity;
    }

    @Override
    public void setSelectedActivity(Activity selectedActivity, boolean isMasterTest) {
        this.selectedActivity = selectedActivity;

        initQuestionList();

        if (isShowing && SectionUtils.isWordBlend(selectedActivity.getActivityCode())) {
            //which the same screen is showing , but the content of review is changed
            playIntroductionAudio();
        }
    }

    @Override
    protected void afterSmallWordInitialized() {
        super.afterSmallWordInitialized();
        highlightCurrentPlayingSound();
    }

    /**
     * Highlight the playing sound in {@link #smallWordObjectList}
     */
    private void highlightCurrentPlayingSound() {


        WordSoundMapping wordSoundMapping = getCurrentPlayingWordSound();
        //get the start index for highlighting the letter in the word
        int startIndex = wordSoundMapping.getStartIndex();
        int numberOfLetter = wordSoundMapping.getLetterLength();


        for (int i = 0; i < smallWordObjectList.size(); i++) {
            ScreenObject<Object, ScreenObjectType> wordScreenObject = smallWordObjectList.get(i);
            wordScreenObject.isHighlighted = i >= startIndex && i < startIndex + numberOfLetter;
        }


    }

    @Override
    public void render() {

        super.render();

        batch.begin();

        batch.setColor(1.0f, 1.0f, 1.0f, displayWordAlpha);

        batch.setColor(1.0f, 1.0f, 1.0f, 1f);

        batch.end();

    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return new String[0];
    }

    @Override
    protected HashMap<String, WordSoundMappingList> getPhonicKeyboardWordGameMap() {
        return selectedActivity.getWordBlendMap();
    }

    @Override
    protected void doHighlightNextSound() {
        highlightCurrentPlayingSound();
        super.doHighlightNextSound();
    }

    @Override
    protected AbstractPhonicActivity getAbstractPhonicModule() {
        return selectedActivity;
    }

}