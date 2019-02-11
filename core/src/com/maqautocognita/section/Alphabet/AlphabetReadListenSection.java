package com.maqautocognita.section.Alphabet;

import com.maqautocognita.Config;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.AbstractReadListenSection;
import com.maqautocognita.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class AlphabetReadListenSection extends AbstractReadListenSection {

    private float[] LETTER_SIZE;
    private float LETTER_START_Y_POSITION;

    private float LETTER_START_X_POSITION;

    private TextScreenObject upperCaseLetter;
    private TextScreenObject lowerCaseLetter;

    private List<ScreenObject> currentHighlightScreenObjectList;

    private List<ScreenObject> pictureScreenObjectList;

    public AlphabetReadListenSection(AbstractAutoCognitaScreen alphabetScreen, IOnHelpListener onHelpListener) {
        super(alphabetScreen, onHelpListener);


    }

    @Override
    protected void render() {

        if (null == LETTER_SIZE) {
            LETTER_SIZE = LetterUtils.getSizeOfWord("A", TextFontSizeEnum.FONT_288);
            LETTER_START_Y_POSITION = ScreenUtils.getScreenHeightWithoutNavigationBar() / 2 +
                    ScreenUtils.getStartYPositionForCenterObjectWithoutNavigationBar(LETTER_SIZE[1], ScreenUtils.getScreenHeightWithoutNavigationBar() / 2);
            LETTER_START_X_POSITION = ScreenUtils.getXPositionForCenterObject(
                    //here need a space for containing 3 letter, the upper letter, the space and the lower letter
                    LETTER_SIZE[0] * 3);
        }

        super.render();

    }

    @Override
    public void dispose() {
        super.dispose();
        upperCaseLetter = null;
        lowerCaseLetter = null;

        if (null != currentHighlightScreenObjectList) {
            currentHighlightScreenObjectList.clear();
            currentHighlightScreenObjectList = null;
        }
    }

    @Override
    public void reset() {
        super.reset();
        if (null != pictureScreenObjectList) {
            pictureScreenObjectList.clear();
            pictureScreenObjectList = null;
        }
    }

    protected String getImageFolderName() {
        return Config.LESSON_IMAGE_FOLDER_NAME;
    }

    @Override
    protected void addLetterPosition(List<ScreenObject<String, ScreenObjectType>> showingLetterPositionAudioList) {
        upperCaseLetter = new TextScreenObject(selectedLetter, ScreenObjectType.SOUND,
                selectedLetter.toUpperCase(), LETTER_START_X_POSITION,
                LETTER_START_Y_POSITION, TextFontSizeEnum.FONT_288);

        upperCaseLetter.audioFileName = selectedActivity.getAudioFileName();

        showingLetterPositionAudioList.add(upperCaseLetter);

        lowerCaseLetter =
                new TextScreenObject(selectedLetter, ScreenObjectType.SOUND,
                        selectedLetter.toLowerCase(), LETTER_START_X_POSITION + LETTER_SIZE[0] * 2, LETTER_START_Y_POSITION, TextFontSizeEnum.FONT_288);

        lowerCaseLetter.audioFileName = selectedActivity.getAudioFileName();

        showingLetterPositionAudioList.add(lowerCaseLetter);
    }

    @Override
    protected float getHeightForPictureSection() {
        return LETTER_START_Y_POSITION - LETTER_SIZE[1];
    }

    @Override
    protected void afterPictureInitialized(TextureScreenObject picture, int index) {
        if (null == pictureScreenObjectList) {
            pictureScreenObjectList = new ArrayList<ScreenObject>();
        }
        pictureScreenObjectList.add(picture);
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return new String[0];
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        playShortInstruction();
    }

    private void playShortInstruction() {
        abstractAutoCognitaScreen.playSound(selectedActivity.getShortInstructionAudioFilenameList(), new AbstractSoundPlayListListener() {
            @Override
            public void beforePlaySound(int index) {
                //TODO there is no way to get which sound index is required to highlight the belongs object, hardcode at this moment
                switch (index) {
                    case 3:
                        if (null != upperCaseLetter) {
                            setCurrentHighlightScreenObject(upperCaseLetter);
                        }
                        break;
                    case 5:
                        if (null != lowerCaseLetter) {
                            setCurrentHighlightScreenObject(lowerCaseLetter);
                        }
                        break;
                }

            }

            @Override
            public void onComplete() {
                clearCurrentHighlightScreenObject();
            }

            @Override
            public void onStop() {
                clearCurrentHighlightScreenObject();
            }

        });
    }

    private void setCurrentHighlightScreenObject(ScreenObject screenObject) {
        clearCurrentHighlightScreenObject();
        if (null == currentHighlightScreenObjectList) {
            currentHighlightScreenObjectList = new ArrayList<ScreenObject>();
        }
        currentHighlightScreenObjectList.add(screenObject);
        screenObject.isHighlighted = true;
    }

    private void clearCurrentHighlightScreenObject() {
        if (null != currentHighlightScreenObjectList) {
            for (ScreenObject currentHighlightScreenObject : currentHighlightScreenObjectList) {
                currentHighlightScreenObject.isHighlighted = false;
            }
            currentHighlightScreenObjectList.clear();
        }
    }

    @Override
    protected void onIntroductionAudioStopped() {
        playShortInstruction();
    }

}
