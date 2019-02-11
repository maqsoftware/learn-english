package com.maqautocognita.section.Phonic;

import com.maqautocognita.Config;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.utils.ImageUtils;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.AbstractReadListenSection;
import com.maqautocognita.service.PhonicSoundScreenService;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicReadListenSection extends AbstractReadListenSection {

    private PhonicSoundScreenService phonicSoundScreenService;

    private List<ScreenObject<String, ScreenObjectType>> currentHighlightScreenObjectList;

    public PhonicReadListenSection(AbstractAutoCognitaScreen alphabetScreen, IOnHelpListener onHelpListener) {
        super(alphabetScreen, onHelpListener);
    }


    @Override
    protected String[] getAllRequiredTextureName() {
        return null;
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        playShortInstruction();
    }

    @Override
    protected void onIntroductionAudioStopped() {
        playShortInstruction();
    }

    private void playShortInstruction() {
        abstractAutoCognitaScreen.playSound(selectedActivity.getShortInstructionAudioFilenameList(), new AbstractSoundPlayListListener() {
            @Override
            public void beforePlaySound(int index) {
                //TODO there is no way to get which sound index is required to highlight the belongs object, hardcode at this moment
                switch (index) {
                    case 1:
                        setCurrentHighlightScreenObject(phonicSoundScreenService.letterScreenObjectList);
                        break;
                    case 3:
                        setCurrentHighlightScreenObject(phonicSoundScreenService.phonicSoundScreenObjectList);
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

    private void setCurrentHighlightScreenObject(List<ScreenObject<String, ScreenObjectType>> highlightScreenObjectList) {
        if (CollectionUtils.isNotEmpty(highlightScreenObjectList)) {
            clearCurrentHighlightScreenObject();
            currentHighlightScreenObjectList = highlightScreenObjectList;

            for (ScreenObject screenObject : highlightScreenObjectList) {
                screenObject.isHighlighted = true;
            }
        }

    }

    private void clearCurrentHighlightScreenObject() {
        if (null != currentHighlightScreenObjectList) {
            for (ScreenObject currentHighlightScreenObject : currentHighlightScreenObjectList) {
                currentHighlightScreenObject.isHighlighted = false;
            }
        }
    }

    @Override
    protected void whenScreenObjectTouched(ScreenObject<String, ScreenObjectType> touchingScreenObject) {
        if (ScreenObjectType.HINTS.equals(touchingScreenObject.objectType)) {
            doLetterTouch(phonicSoundScreenService.phonicSoundScreenObjectList.get(0));
        }
    }

    @Override
    protected void whenLetterTouch(ScreenObject<String, ScreenObjectType> touchingScreenObject) {
        if (ScreenObjectType.LETTER.equals(touchingScreenObject.objectType)) {
            //In general, for all Phonics lessons, we do not need to highlight the letter when pressed.
            // Only highlight the phonics sound (i.e. the one with the ((( ))) around it, not the smaller font letter above it).
            // This is because we are sounding out the phonics sound and not the letter itself.
            doLetterTouch(phonicSoundScreenService.phonicSoundScreenObjectList.get(0));
        } else {
            super.whenLetterTouch(touchingScreenObject);
        }
    }

    @Override
    protected void playLetterSound(final ScreenObject<String, ScreenObjectType> touchingLetterObject) {
        abstractAutoCognitaScreen.playSound(touchingLetterObject.audioFileName, new AbstractSoundPlayListener() {

            @Override
            public void onPlay(int audioListIndex, long millisecond) {
                if (isShowing && isPhonicSymbolInit()) {
                    if (millisecond < 300) {
                        phonicSoundScreenService.phonicOpenSymbolScreenObject.textureRegion = ImageUtils.getLargePhonicSymbolOpenIconWave75Percent();
                        phonicSoundScreenService.phonicCloseSymbolScreenObject.textureRegion = ImageUtils.getLargePhonicSymbolCloseIconWave75Percent();
                    } else if (millisecond < 600) {
                        phonicSoundScreenService.phonicOpenSymbolScreenObject.textureRegion = ImageUtils.getLargePhonicSymbolOpenIconWave50Percent();
                        phonicSoundScreenService.phonicCloseSymbolScreenObject.textureRegion = ImageUtils.getLargePhonicSymbolCloseIconWave50Percent();
                    } else {
                        phonicSoundScreenService.phonicOpenSymbolScreenObject.textureRegion = ImageUtils.getLargePhonicSymbolOpenIconWave25Percent();
                        phonicSoundScreenService.phonicCloseSymbolScreenObject.textureRegion = ImageUtils.getLargePhonicSymbolCloseIconWave25Percent();
                    }
                }
            }

            @Override
            public void onComplete() {
                resumeNormal();
            }

            @Override
            public void onStop() {
                resumeNormal();
            }

            private void resumeNormal() {
                if (isShowing && isPhonicSymbolInit()) {
                    //make sure the phonic symbol is rollback to the original image
                    phonicSoundScreenService.phonicOpenSymbolScreenObject.textureRegion = ImageUtils.getLargePhonicSymbolOpenIcon();
                    phonicSoundScreenService.phonicCloseSymbolScreenObject.textureRegion = ImageUtils.getLargePhonicSymbolCloseIcon();

                    if (ScreenObjectType.SOUND.equals(touchingLetterObject.objectType)) {
                        List<ScreenObject<String, ScreenObjectType>> screenObjectList = ScreenObjectUtils.getScreenObjectListById(PhonicReadListenSection.this.screenObjectList, touchingLetterObject.id);
                        if (CollectionUtils.isNotEmpty(screenObjectList)) {
                            for (ScreenObject screenObject : screenObjectList) {
                                screenObject.isHighlighted = false;
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected String getImageFolderName() {
        return Config.LESSON_IMAGE_FOLDER_NAME;
    }

    @Override
    protected void addLetterPosition(List<ScreenObject<String, ScreenObjectType>> showingLetterPositionAudioList) {

        if (null == phonicSoundScreenService) {
            phonicSoundScreenService = new PhonicSoundScreenService();
        }
        showingLetterPositionAudioList.addAll(phonicSoundScreenService.drawPhonicSoundWithLetter(selectedActivity,
                ScreenUtils.isLandscapeMode ?
                        ScreenUtils.getScreenHeightWithoutNavigationBar() / 2 + ScreenUtils.getNavigationBarHeight() -
                                LetterUtils.getMaximumHeight(TextFontSizeEnum.FONT_144) :
                        ScreenUtils.getScreenHeightWithoutNavigationBar() / 2 + LetterUtils.getSizeOfWord(selectedActivity.getLetter(), TextFontSizeEnum.FONT_144)[1]))
        ;
    }

    private boolean isPhonicSymbolInit() {
        return isShowing && null != phonicSoundScreenService && null != phonicSoundScreenService.phonicOpenSymbolScreenObject && null != phonicSoundScreenService.phonicCloseSymbolScreenObject;
    }

    @Override
    protected float getHeightForPictureSection() {
        return ScreenUtils.getScreenHeightWithoutNavigationBar() / 2 + ScreenUtils.getNavigationBarHeight();
    }
}
