package com.maqautocognita.section.Phonic;

import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.AbstractPhonicActivity;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.bo.WordSoundMappingList;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ImageUtils;
import com.maqautocognita.graphics.utils.SwahiliPhonicKeyboardUtils;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.screens.PhonicU4Screen;
import com.maqautocognita.section.IActivityChangeListener;
import com.maqautocognita.section.Math.AbstractMathSection;
import com.maqautocognita.service.PhonicU4LessonService;
import com.maqautocognita.service.SoundService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicSwahiliListenAndTypeFreeTypeSection extends AbstractSwahiliPhonicKeyboardSection implements IActivityChangeListener {

    private Activity selectedActivity;

    private TextureScreenObject phonicSymbolOpen;
    private TextureScreenObject phonicSymbolClose;
    private AutoCognitaTextureRegion trashAutoCognitaTextureRegion;

    public PhonicSwahiliListenAndTypeFreeTypeSection(PhonicU4Screen phonicU4Screen, IOnHelpListener onHelpListener) {
        super(phonicU4Screen, onHelpListener);
    }

    @Override
    public void setSelectedActivity(Activity selectedActivity, boolean isMasterTest) {
        this.selectedActivity = selectedActivity;
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return selectedActivity;
    }

    @Override
    public void singleTap(int screenX, int screenY) {
        super.singleTap(screenX, screenY);
        if (TouchUtils.isTouched(AbstractMathSection.TRASH_ICON_POSITION, screenX, screenY)) {
            if (CollectionUtils.isNotEmpty(soundObjectList)) {
                soundObjectList.clear();
                restorePhonicSymbolPosition();
            }
        }
    }

    private void restorePhonicSymbolPosition() {
        float startScreenXPosition = ScreenUtils.getXPositionForCenterObject(phonicSymbolOpen.width + phonicSymbolClose.width);
        phonicSymbolOpen.xPositionInScreen = startScreenXPosition;
        phonicSymbolClose.xPositionInScreen = startScreenXPosition + phonicSymbolOpen.width;
    }

    @Override
    public void dispose() {
        super.dispose();
        phonicSymbolOpen = null;
        phonicSymbolClose = null;
        trashAutoCognitaTextureRegion = null;
    }

    @Override
    protected void afterCorrectSoundIsPressed() {

    }

    @Override
    protected boolean isCorrectSoundPressed() {

        if (CollectionUtils.isNotEmpty(touchedConsonantKeyList) && null != touchedVowelKey) {
            boolean isCorrectSound = isValidSyllablePressed();
            if (isCorrectSound) {
                timerService.clearTimer();
                doWhenSoundPressed();
            }
            return isCorrectSound;
        }

        return false;
    }

    private boolean isValidSyllablePressed() {
        return PhonicU4LessonService.getInstance().isSyllable(getPressedSound());
    }

    private void doWhenSoundPressed() {
        String pressedSound = getPressedSound();
        if (StringUtils.isNotBlank(pressedSound)) {
            if (null == soundObjectList) {
                soundObjectList = new ArrayList<ScreenObject<String, ScreenObjectType>>();
            }
            float startYPosition = getSpeechIconYPosition();

            final TextScreenObject textScreenObject =
                    new TextScreenObject(pressedSound, 0, startYPosition, TextFontSizeEnum.FONT_144, true);
            if (CollectionUtils.isNotEmpty(soundObjectList)) {
                //which mean there already has a sound
                soundObjectList.add(new TextScreenObject("-", 0, startYPosition, TextFontSizeEnum.FONT_144, true));
            }
            soundObjectList.add(textScreenObject);

            float totalWidth = phonicSymbolOpen.width + phonicSymbolClose.width;

            for (ScreenObject existingSoundObject : soundObjectList) {
                totalWidth += existingSoundObject.width;
            }

            float startXPosition = ScreenUtils.getXPositionForCenterObject(totalWidth);
            phonicSymbolOpen.xPositionInScreen = startXPosition;
            startXPosition += phonicSymbolOpen.width;

            for (ScreenObject existingSoundObject : soundObjectList) {
                existingSoundObject.xPositionInScreen = startXPosition;
                startXPosition += existingSoundObject.width;
            }
            phonicSymbolClose.xPositionInScreen = startXPosition;

            textScreenObject.isHighlighted = true;

            abstractAutoCognitaScreen.playSound(SoundService.getInstance().getPhonicAudioFileName(pressedSound), new AbstractSoundPlayListener() {
                @Override
                public void onComplete() {
                    whenFinish();
                }

                private void whenFinish() {
                    clearTouchedConsonantKey();
                    clearTouchedVowelKey();
                    textScreenObject.isHighlighted = false;
                }

                @Override
                public void onStop() {
                    whenFinish();
                }


            });
        }
    }

    @Override
    protected float getSpeechIconYPosition() {
        return getStartYPositionForPhonicSymbolInPhonicKeyboardScreen() + 50;
    }

    @Override
    public void render() {
        super.render();
        initPhonicSymbolObjectList();
        boolean hasSound = CollectionUtils.isNotEmpty(soundObjectList);
        if (null != speechIconScreenObject) {
            speechIconScreenObject.isVisible = hasSound;
        }
        if (null == trashAutoCognitaTextureRegion) {
            trashAutoCognitaTextureRegion =
                    new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.ICONS), 0, 300, (int) AbstractMathSection.TRASH_ICON_POSITION.width, (int) AbstractMathSection.TRASH_ICON_POSITION.height);
        }
        if (hasSound) {
            batch.begin();
            batch.draw(trashAutoCognitaTextureRegion, AbstractMathSection.TRASH_ICON_POSITION.x, AbstractMathSection.TRASH_ICON_POSITION.y);
            batch.end();
        }
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return null;
    }

    @Override
    protected HashMap<String, WordSoundMappingList> getPhonicKeyboardWordGameMap() {
        return null;
    }

    @Override
    protected boolean isSmallWordShow() {
        return false;
    }

    @Override
    protected void doWhenCorrectSoundPress() {

    }

    @Override
    protected void afterLastCorrectKeyPressedForPlayingWord() {

    }

    @Override
    protected void onSpeechIconPressed() {
        if (CollectionUtils.isNotEmpty(soundObjectList)) {
            List<String> audioList = new ArrayList<String>();

            int totalNumberOfSound = 0;
            for (ScreenObject<String, ScreenObjectType> screenObject : soundObjectList) {
                if (screenObject instanceof TextScreenObject) {
                    if (!((TextScreenObject<String>) screenObject).displayText.equals("-")) {
                        totalNumberOfSound++;
                    }
                }
            }

            int soundIndex = 0;
            for (ScreenObject<String, ScreenObjectType> screenObject : soundObjectList) {
                if (screenObject instanceof TextScreenObject) {
                    String sound = ((TextScreenObject<String>) screenObject).displayText;
                    if (!sound.equals("-")) {

                        String soundFileName = SoundService.getInstance().getPhonicAudioFileName(sound);

                        if (totalNumberOfSound > 1) {
                            if (soundIndex == totalNumberOfSound - 2) {
                                soundFileName = soundFileName.substring(0, soundFileName.lastIndexOf("_")) + "_2";
                            } else if (soundIndex == totalNumberOfSound - 1) {
                                soundFileName = soundFileName.substring(0, soundFileName.lastIndexOf("_")) + "_3";
                            }
                        }
                        Gdx.app.log(getClass().getName(), "going to play sound = " + soundFileName);
                        audioList.add(soundFileName);
                        soundIndex++;
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(audioList)) {
                abstractAutoCognitaScreen.playSound(audioList, new AbstractSoundPlayListListener() {
                    @Override
                    public void beforePlaySound(int index) {
                        unhighlightAllSound();
                        if (CollectionUtils.isNotEmpty(soundObjectList) && soundObjectList.size() > index * 2) {
                            //because the audio is not include the hyphen object
                            soundObjectList.get(index * 2).isHighlighted = true;
                        }
                    }

                    private void unhighlightAllSound() {
                        if (CollectionUtils.isNotEmpty(soundObjectList)) {
                            for (ScreenObject sound : soundObjectList) {
                                sound.isHighlighted = false;
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        unhighlightAllSound();
                    }


                });
            }
        }
    }

    @Override
    protected AbstractPhonicActivity getAbstractPhonicModule() {
        return null;
    }

    @Override
    protected String[] getEnableKeys() {
        return SwahiliPhonicKeyboardUtils.KEYBOARDS;
    }

    @Override
    protected void initSound() {

    }

    private void initPhonicSymbolObjectList() {
        if (null == phonicSymbolObjectList) {

            phonicSymbolObjectList = new ArrayList<TextureScreenObject<Object, ScreenObjectType>>(2);

            //init the phonic symbol open icon
            phonicSymbolOpen = new TextureScreenObject<Object, ScreenObjectType>(null, ScreenObjectType.WORD, 0,
                    getStartYPositionForPhonicSymbolInPhonicKeyboardScreen(), ImageUtils.getLargePhonicSymbolOpenIcon(), null);
            //init the phonic symbol close icon
            phonicSymbolClose = new TextureScreenObject<Object, ScreenObjectType>(null, ScreenObjectType.WORD, 0,
                    getStartYPositionForPhonicSymbolInPhonicKeyboardScreen(), ImageUtils.getLargePhonicSymbolCloseIcon(), null);

            restorePhonicSymbolPosition();

            phonicSymbolObjectList.add(phonicSymbolOpen);
            phonicSymbolObjectList.add(phonicSymbolClose);
        }
    }

    @Override
    public void beforeStartTimer() {

    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        if (isValidSyllablePressed()) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    doWhenSoundPressed();
                }
            });
        } else {
            clearTouchedConsonantKey();
            clearTouchedVowelKey();
        }
    }
}