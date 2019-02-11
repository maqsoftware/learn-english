package com.maqautocognita.section;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.SectionUtils;
import com.maqautocognita.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractReadListenSection extends AbstractDrawWordPictureSection implements IActivityChangeListener {

    protected Activity selectedActivity;
    protected String selectedLetter;

    /**
     * Store the screen object of letter, word and the picture in the list for rendering
     * And it also act as a flag to do the init again, if the list is null
     */
    protected List<ScreenObject<String, ScreenObjectType>> screenObjectList;

    public AbstractReadListenSection(AbstractAutoCognitaScreen alphabetScreen, IOnHelpListener onHelpListener) {
        super(alphabetScreen, onHelpListener);
    }

    public void setSelectedActivity(Activity selectedActivity, boolean isMasterTest) {
        if (null != selectedActivity) {
            selectedLetter = selectedActivity.getLetter();

            this.selectedActivity = selectedActivity;

            if (isShowing && SectionUtils.isReadingAndListening(selectedActivity.getActivityCode())) {
                //which the same screen is showing , but the showing letter is changed
                playIntroductionAudio();
            }
        }
    }

    @Override
    protected void afterWordCreated(List<ScreenObject<String, ScreenObjectType>> wordList) {
        for (ScreenObject<String, ScreenObjectType> screenObject : wordList) {
            if (null != screenObject.id) {
                if (screenObject.id.equalsIgnoreCase(selectedLetter)) {
                    screenObject.isHighlighted = true;
                }
            }
        }
    }

    @Override
    protected void render() {

        if (null == screenObjectList) {
            initScreenObjects();
        }


        if (null != selectedActivity) {
            render(screenObjectList);
        }

    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return selectedActivity;
    }

    @Override
    public void dispose() {
        super.dispose();
        reset();
    }

    @Override
    public void reset() {
        clearWordPictures();

        if (null != screenObjectList) {

            screenObjectList.clear();
            screenObjectList = null;
        }

    }

    private void clearWordPictures() {
        if (null != selectedActivity && ArrayUtils.isNotEmpty(selectedActivity.getPictures())) {
            //make sure the previous showing picture is released in memory
            for (String picture : selectedActivity.getPictures()) {
                AssetManagerUtils.unloadTexture(getImageFolderName() + "/" + picture);
            }
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (null != screenObjectList) {

            final ScreenObject<String, ScreenObjectType> touchingScreenObject = ScreenObjectUtils.getTouchingScreenObject(screenObjectList, screenX, screenY);

            if (null != touchingScreenObject) {

                whenScreenObjectTouched(touchingScreenObject);

                //in here, the touch behavior is only for playing the audio, so if no audio found, no highlight will be happen
                if (null != AutoCognitaGame.audioService && StringUtils.isNotBlank(touchingScreenObject.audioFileName)) {
                    whenLetterTouch(touchingScreenObject);
                }

            }

        }
    }

    protected void whenScreenObjectTouched(ScreenObject<String, ScreenObjectType> touchingScreenObject) {

    }

    protected void whenLetterTouch(ScreenObject<String, ScreenObjectType> touchingScreenObject) {
        doLetterTouch(touchingScreenObject);
    }

    protected void doLetterTouch(final ScreenObject<String, ScreenObjectType> touchingScreenObject) {


        //highlight all letter in the center of the screen, in phonic lesson, such as the phonic key "th", if the user touch the letter "h", it should also highlight the letter "t" as well
        ScreenObjectUtils.highLightAllScreenObjectWithSameId(screenObjectList, touchingScreenObject.id);

        if (ScreenObjectType.SOUND.equals(touchingScreenObject.objectType)) {
            playLetterSound(touchingScreenObject);
        } else {
            abstractAutoCognitaScreen.playSound(touchingScreenObject.audioFileName, new AbstractSoundPlayListener() {

                @Override
                public void onComplete() {
                    resumeNormal();
                }

                private void resumeNormal() {
                    ScreenObjectUtils.unhighLightAllScreenObjectWithSameId(screenObjectList, touchingScreenObject.id);
                }

                @Override
                public void onStop() {
                    resumeNormal();
                }


            });
        }
    }

    protected void playLetterSound(final ScreenObject<String, ScreenObjectType> touchingLetterObject) {
        abstractAutoCognitaScreen.playSound(touchingLetterObject.audioFileName, new AbstractSoundPlayListener() {

            @Override
            public void onComplete() {
                resumeNormal();
            }

            @Override
            public void onStop() {
                resumeNormal();
            }

            private void resumeNormal() {
                if (ScreenObjectType.SOUND.equals(touchingLetterObject.objectType)) {
                    ScreenObjectUtils.unhighLightAllScreenObjectWithSameId(screenObjectList, touchingLetterObject.id);
                }
            }
        });
    }

    private void initScreenObjects() {

        isHelpAudioPlaying = false;
        screenObjectList = new ArrayList<ScreenObject<String, ScreenObjectType>>();


        String[] picturePaths = new String[selectedActivity.getWords().length];
        for (int i = 0; i < selectedActivity.getWords().length; i++) {
            picturePaths[i] = getImageFolderName() + "/" + selectedActivity.getPictures()[i];
        }

        screenObjectList.addAll(getWordPictureScreenObjectList(picturePaths, selectedActivity.getWords(), selectedActivity.getUnitCode()));

        addLetterPosition(screenObjectList);
    }

    protected abstract String getImageFolderName();

    /**
     * add the letters which is/are required to show in the center of the screen
     *
     * @param showingLetterPositionAudioList
     */
    protected abstract void addLetterPosition(List<ScreenObject<String, ScreenObjectType>> showingLetterPositionAudioList);

}
