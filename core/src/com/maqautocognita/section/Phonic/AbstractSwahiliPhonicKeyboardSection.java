package com.maqautocognita.section.Phonic;

import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.SwahiliPhonicKeyboardUtils;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.PhonicLessonService;
import com.maqautocognita.service.SoundService;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractSwahiliPhonicKeyboardSection extends AbstractPhonicKeyboardSection implements TimerService.ITimerListener {

    private static final int KEYBOARD_START_X_POSITION = ScreenUtils.getNavigationBarStartXPosition();
    private static final int KEYBOARD_START_Y_POSITION = 600;
    private static final float KEYBOARD_WAIT_FOR_RESET_SECOND = 2;
    protected final TimerService timerService;
    protected List<TextureScreenObject<String, ScreenObjectType>> touchedConsonantKeyList;
    protected TextureScreenObject<String, ScreenObjectType> touchedVowelKey;
    private int vowelsKeyStartXPosition;

    public AbstractSwahiliPhonicKeyboardSection(AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(abstractAutoCognitaScreen, onHelpListener);
        vowelsKeyStartXPosition = KEYBOARD_START_X_POSITION + SwahiliPhonicKeyboardUtils.KEY_WIDTH * 11 + 100;
        timerService = new TimerService(this);
    }

    @Override
    protected void initKeyboard(String[] enableKeys) {

        if (null == smallWordObjectList) {
            initSmallWord();
        }
        //init keyboard
        for (int i = 0; i < SwahiliPhonicKeyboardUtils.KEYBOARDS.length; i++) {
            String key = SwahiliPhonicKeyboardUtils.KEYBOARDS[i];

            Vector2 position = SwahiliPhonicKeyboardUtils.getKeyPosition(key);
            float columnIndex = position.x;
            float rowIndex = position.y;

            if ("w".equalsIgnoreCase(key)) {
                rowIndex = 4;
            }

            float startYPosition = KEYBOARD_START_Y_POSITION - (rowIndex * SwahiliPhonicKeyboardUtils.KEY_HEIGHT);
            float startXPosition = KEYBOARD_START_X_POSITION + (columnIndex - 1) * SwahiliPhonicKeyboardUtils.KEY_WIDTH;

            if (SwahiliPhonicKeyboardUtils.isVowels(key)) {
                startXPosition = vowelsKeyStartXPosition;
            }

            TextureScreenObject<String, ScreenObjectType> keyScreenObject = new TextureScreenObject<String, ScreenObjectType>(key, ScreenObjectType.KEYBOARD,
                    startXPosition,
                    startYPosition, SwahiliPhonicKeyboardUtils.getKeyTextureRegion(key), SwahiliPhonicKeyboardUtils.getKeyHighLightedTextureRegion(key)
            );

            if (ArrayUtils.isNotContainIgnoreCase(enableKeys, key)) {
                keyScreenObject.isDisabled = true;
                keyScreenObject.isTouchAllow = false;
                keyScreenObject.textureRegionInDisableState = SwahiliPhonicKeyboardUtils.getKeyDisabledTextureRegion(key);
            } else {
                keyScreenObject.audioFileName = abstractAutoCognitaScreen.getLessonService().getLetterAudioFileName(key);
            }

            getKeyBoardScreenObjectList().add(keyScreenObject);
        }

    }

    private List<TextureScreenObject<String, ScreenObjectType>> getKeyBoardScreenObjectList() {
        if (null == keyBoardScreenObjectList) {
            keyBoardScreenObjectList = new ArrayList<TextureScreenObject<String, ScreenObjectType>>();
        }

        return keyBoardScreenObjectList;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (null != touchedConsonantKeyList) {
            touchedConsonantKeyList.clear();
        }
        touchedVowelKey = null;

    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        Gdx.app.log(getClass().getName(), "touchUp");
        if (isCorrectSoundPressed) {
            timerService.clearTimer();
            /// if the sound is press correctly
            doWhenCorrectSoundPress();
            //if the word is still playing, above method will be trigger when the key is finish playing
        } else {

            if (!playPressedKeySound()) {
                //reset the previous pressed key, only highlight the current pressed key
                if (CollectionUtils.isNotEmpty(touchedConsonantKeyList)) {
                    for (int i = 0; i < touchedConsonantKeyList.size() - 1; i++) {
                        touchedConsonantKeyList.get(i).isHighlighted = false;
                        touchedConsonantKeyList.remove(i);
                    }

                    playPressedKeySound();
                }

            }

            addNumberOfFails();
            timerService.startTimer(null, KEYBOARD_WAIT_FOR_RESET_SECOND);
        }

    }

    @Override
    protected void unhighlightCurrentTouchingKeyList() {

    }

    @Override
    protected boolean isBlockTouchAfterCorrectSoundPressed() {
        return false;
    }

    @Override
    protected String getTouchingKeyAudio() {
        return null;
    }

    @Override
    protected TextureScreenObject<String, ScreenObjectType> whenKeyboardIsTouched
            (TextureScreenObject<String, ScreenObjectType> touchingKey) {
        if (SwahiliPhonicKeyboardUtils.isVowels(touchingKey.id)) {
            clearTouchedVowelKey();
            touchedVowelKey = touchingKey;
        } else {
            //Do not allow user to type vowel and then consonant (i.e. non-vowel).  It must be consonant then vowel.
            if (null != touchedVowelKey) {
                clearTouchedVowelKey();
            }


            //clearTouchedConsonantKey();
            if (null == touchedConsonantKeyList) {
                touchedConsonantKeyList = new ArrayList<TextureScreenObject<String, ScreenObjectType>>();
            }

            if (touchedConsonantKeyList.contains(touchingKey)) {
                touchingKey.isHighlighted = false;
                touchedConsonantKeyList.remove(touchingKey);
                touchingKey = null;
            } else {
                touchedConsonantKeyList.add(touchingKey);
            }
        }

        return touchingKey;
    }

    @Override
    protected void addWordAudioAfterLastCorrectKeyPressedForPlayingWord(List<String> audioFileNameList) {

        String soundAudioFileName = getSoundAudioFileNameByIndex(currentPlayingSoundIndex - 1);
        audioFileNameList.add(soundAudioFileName);

        //in some case the playing word is exactly same to the syllable, and the logic will play the syllable and the word after play correctly, in order to prevent playing the syllable in twice
        if (!PhonicLessonService.getInstance().isSyllable(getCurrentPlayingWord())) {
            super.addWordAudioAfterLastCorrectKeyPressedForPlayingWord(audioFileNameList);
        }
    }

    @Override
    protected void afterCorrectSoundIsPressed() {

        if (!isAllSoundPlayedInTheWord()) {
            Gdx.app.log(getClass().getName(), "afterCorrectSoundIsPressed");
            //play the corrected sound
            abstractAutoCognitaScreen.playSound(getSoundAudioFileNameByIndex(currentPlayingSoundIndex - 1), new AbstractSoundPlayListener() {
                @Override
                public void onComplete() {
                    super.onComplete();
                    clearTouchedVowelKey();
                    clearTouchedConsonantKey();
                }
            });
        }
    }

    @Override
    protected void afterCorrectSoundPlayed() {
        super.afterCorrectSoundPlayed();
        clearTouchedConsonantKey();
        clearTouchedVowelKey();
    }

    @Override
    protected boolean isCorrectSoundPressed() {
        String sound = getCurrentPlayingSound();

        if (null == sound) {
            return false;
        } else {
            return sound.equalsIgnoreCase(getPressedSound());
        }

    }

    @Override
    protected float getStartYPositionForPhonicSymbolInPhonicKeyboardScreen() {
        return 680;
    }

    @Override
    protected float getSmallWordAbovePhonicKeyboardStartYPosition() {
        return 920;
    }

    protected void clearTouchedConsonantKey() {
        if (null != touchedConsonantKeyList) {
            //if there is a key pressed in previously
            if (CollectionUtils.isNotEmpty(touchedConsonantKeyList)) {
                for (TextureScreenObject<String, ScreenObjectType> key : touchedConsonantKeyList) {
                    key.isHighlighted = false;
                }
            }
            touchedConsonantKeyList = null;
        }
    }

    protected void clearTouchedVowelKey() {
        if (null != touchedVowelKey) {
            //if there is a key pressed in previously
            touchedVowelKey.isHighlighted = false;
            touchedVowelKey = null;
        }

    }

    private boolean playPressedKeySound() {
        String pressedKeyAudio = SoundService.getInstance().getPhonicAudioFileName(getPressedSound());
        if (StringUtils.isNotBlank(pressedKeyAudio)) {
            abstractAutoCognitaScreen.playSound(pressedKeyAudio);
            return true;
        }

        return false;
    }

    protected String getPressedSound() {
        String pressSound = "";
        if (CollectionUtils.isNotEmpty(touchedConsonantKeyList)) {
            for (TextureScreenObject<String, ScreenObjectType> key : touchedConsonantKeyList) {
                pressSound += key.id;
            }
        }
        if (null != touchedVowelKey) {

            pressSound += touchedVowelKey.id;
        }

        return pressSound;
    }

    @Override
    public void beforeStartTimer() {

    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        //When playing the sound of a syllable (whether it is correct or not),
        // keep the keys highlighted while audio is played, and then un-highlight when audio finishes playing.
        clearTouchedConsonantKey();
        clearTouchedVowelKey();
    }
}
