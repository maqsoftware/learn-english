package com.maqautocognita.section.Phonic;

import com.maqautocognita.adapter.IPhonicKeyboardService;
import com.maqautocognita.bo.AbstractPhonicActivity;
import com.maqautocognita.bo.WordSoundMapping;
import com.maqautocognita.bo.WordSoundMappingList;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ImageProperties;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ImageUtils;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.AbstractAutoCognitaSection;
import com.maqautocognita.section.ISectionChangeListener;
import com.maqautocognita.section.MenuSection;
import com.maqautocognita.service.MobilePhonicKeyboardService;
import com.maqautocognita.service.SoundService;
import com.maqautocognita.service.TabletPhonicKeyboardService;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 *         <p/>
 *         This abstract class is mainly used to initilize the phonic keyboard and the speech icon in the right side of the keyboard,
 *         <p/>
 *         And it will also handle the touch event on keyboard and the speech icon
 */
public abstract class AbstractPhonicKeyboardSection extends AbstractAutoCognitaSection implements ISectionChangeListener {

    public static final Color SMALL_WORD_BACKGROUND_COLOR = Color.valueOf("FFFFFFBC");
    private static final float GAP_BETWEEN_PHONIC_SYMBOL_AND_PHONIC_LETTER = 45;
    private static final float GAP_BETWEEN_PHONIC_SYMBOL_AND_KEYBOARD = 100;
    protected List<TextureScreenObject<String, ScreenObjectType>> keyBoardScreenObjectList;
    protected TextureScreenObject<String, ScreenObjectType> speechIconScreenObject;
    /**
     * Indicate the index of playing sound in the word, it will be always start from 0 , and it will increase if the user press correct sound,
     * the maximum index will be depends on the size of {@link WordSoundMappingList#getWordSoundMappingList()}, but sometimes there maybe more than 1 sound for 1 letter.
     * For example the word "box", the sound will be b-o-k-s, so the maximum index will be 3
     */
    protected int currentPlayingSoundIndex;
    protected int currentSelectedQuestionIndex;
    protected WordSoundMappingList currentPlayingWordSoundMappingList;
    protected String questionList[];
    //store the current touching sound key
    protected TextureScreenObject<String, ScreenObjectType> currentTouchingKey;
    /**
     * Store the value which is indicate the alpha of the displaying word, below value will be changes when the user answer a correct answer and going to disappear the word before appear next word
     */
    protected float displayWordAlpha = 1f;
    //store the word which is playing and located in the center of the screen and above the playing sound
    protected List<ScreenObject<Object, ScreenObjectType>> smallWordObjectList;
    protected boolean isCorrectSoundPressed;
    //store the phonic symbol which is enclosing the word
    protected List<TextureScreenObject<Object, ScreenObjectType>> phonicSymbolObjectList;
    //store the sound which  is playing and located in the center of the screen and above the phonic keyboard
    protected List<ScreenObject<String, ScreenObjectType>> soundObjectList;
    private ShapeRenderer smallWordBackground;
    private float smallWordWidth;
    private float smallWordHeight;
    private float smallWordStartXPosition;
    private String correctSound;

    private IPhonicKeyboardService phonicKeyboardService;

    public AbstractPhonicKeyboardSection(AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(abstractAutoCognitaScreen, onHelpListener);
        if (ScreenUtils.isLandscapeMode) {
            phonicKeyboardService = TabletPhonicKeyboardService.getInstance();
        } else {
            phonicKeyboardService = MobilePhonicKeyboardService.getInstance();
        }
    }

    private TextFontSizeEnum getSmallWordFontSize() {
        return TextFontSizeEnum.FONT_144;
    }

    private TextFontSizeEnum getPhonicSoundFontSize() {
        return ScreenUtils.isLandscapeMode ? TextFontSizeEnum.FONT_288 : TextFontSizeEnum.FONT_144;
    }

    private float getKeyboardStartYPosition() {
        return phonicKeyboardService.getKeyboardHeight() + MenuSection.MenuItemEnum.HELP.iconPosition.y + MenuSection.MenuItemEnum.HELP.iconPosition.height;
    }

    protected void initKeyboard(String[] enableKeys) {

        if (null == keyBoardScreenObjectList) {
            keyBoardScreenObjectList = new ArrayList<TextureScreenObject<String, ScreenObjectType>>();
        }

        if (null == smallWordObjectList) {
            initSmallWord();
        }

        float keyboardStartXPosition = ScreenUtils.getXPositionForCenterObject(phonicKeyboardService.getKeyboardWidth());

        float keyboardStartYPosition = getKeyboardStartYPosition();

        int theNumberOfKey = 0;

        String[] keys = phonicKeyboardService.getAllKeys();
        //init keyboard
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];

            //either show the key c or k
            if (key.equalsIgnoreCase("c") && ArrayUtils.isContainIgnoreCase(enableKeys, "k")) {
                //if k is required to show, c will be ignored
                continue;
            } else if (key.equalsIgnoreCase("k") && ArrayUtils.isContainIgnoreCase(enableKeys, "c")) {
                //if c is required to show, k will be ignored
                continue;
            } else if (key.equalsIgnoreCase("k") && ArrayUtils.isNotContainIgnoreCase(enableKeys, "k")) {
                continue;
            }


            if (StringUtils.isNotBlank(key)) {
                AutoCognitaTextureRegion keyAutoCognitaTextureRegion = phonicKeyboardService.getKeyTextureRegion(key);
                Vector2 keyPosition = phonicKeyboardService.getKeyPosition(key);

                float startXPosition = keyboardStartXPosition + ((keyPosition.x - 1) *
                        keyAutoCognitaTextureRegion.getRegionWidth());
                float startYPosition = keyboardStartYPosition - (keyPosition.y *
                        keyAutoCognitaTextureRegion.getRegionHeight());

                TextureScreenObject<String, ScreenObjectType> keyScreenObject = new TextureScreenObject<String, ScreenObjectType>(key, ScreenObjectType.KEYBOARD,
                        startXPosition,
                        startYPosition, keyAutoCognitaTextureRegion, phonicKeyboardService.getKeyHighLightedTextureRegion(key)
                );

                if (ArrayUtils.isNotContainIgnoreCase(enableKeys, key)) {
                    keyScreenObject.isDisabled = true;
                    keyScreenObject.isTouchAllow = false;
                    keyScreenObject.textureRegionInDisableState = phonicKeyboardService.getKeyDisabledTextureRegion(key);
                } else {
                    keyScreenObject.audioFileName = abstractAutoCognitaScreen.getLessonService().getLetterAudioFileName(key);
                }

                keyBoardScreenObjectList.add(keyScreenObject);

            }

            theNumberOfKey++;
        }

    }

    private void initSpeechIcon() {
        if (null == speechIconScreenObject) {

            speechIconScreenObject = new TextureScreenObject<String, ScreenObjectType>(null, ScreenObjectType.SPEECH_ICON,
                    getSpeechIconXPosition(), getSpeechIconYPosition(), ImageUtils.getSpeechIcon(),
                    ImageUtils.getSpeechIconInHighlightState());
            //add speech icon
            keyBoardScreenObjectList.add(speechIconScreenObject);
        }
    }

    private float getSpeechIconXPosition() {

        if (ScreenUtils.isLandscapeMode) {
            return ImageProperties.SPEECH_ICON_SCREEN_POSITION.x;
        }

        return ScreenUtils.getScreenWidth() - MenuSection.MenuItemEnum.HELP.iconPosition.x - ImageUtils.SPEECH_ICON_POSITION.width;
    }

    protected float getSpeechIconYPosition() {
        if (ScreenUtils.isLandscapeMode) {
            return ImageProperties.SPEECH_ICON_SCREEN_POSITION.y;
        }

        return MenuSection.MenuItemEnum.HELP.iconPosition.y;
    }

    protected void afterSmallWordInitialized() {

    }

    @Override
    public void render() {
        if (null == keyBoardScreenObjectList) {
            initKeyboard(getEnableKeys());
        }

        initSpeechIcon();

        if (null == soundObjectList) {
            getCurrentPlayingWordSound();
            initSound();
        }

        batch.begin();
        if (isSmallWordShow()) {
            if (null == smallWordBackground) {
                smallWordBackground = new ShapeRenderer();
                smallWordBackground.setProjectionMatrix(batch.getProjectionMatrix());
                smallWordBackground.setColor(SMALL_WORD_BACKGROUND_COLOR);
            }

            batch.end();
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            smallWordBackground.begin(ShapeRenderer.ShapeType.Filled);
            smallWordBackground.rect(smallWordStartXPosition, getSmallWordAbovePhonicKeyboardStartYPosition(), smallWordWidth, smallWordHeight);
            smallWordBackground.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();

            ScreenObjectUtils.draw(batch, smallWordObjectList);
        }
        ScreenObjectUtils.draw(batch, keyBoardScreenObjectList);

        batch.setColor(1.0f, 1.0f, 1.0f, displayWordAlpha);
        ScreenObjectUtils.draw(batch, soundObjectList);
        ScreenObjectUtils.draw(batch, phonicSymbolObjectList);
        batch.setColor(1.0f, 1.0f, 1.0f, 1f);

        batch.end();
    }

    @Override
    protected void onHide() {
        super.onHide();
        if (null != smallWordBackground) {
            smallWordBackground.dispose();
        }
        smallWordBackground = null;
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return new String[]{AssetManagerUtils.PHONICS_KEYBOARD, AssetManagerUtils.PHONICS_KEYBOARD_HIGHLIGHTED, AssetManagerUtils.PHONICS_KEYBOARD_DISABLED};
    }

    @Override
    protected List<String> getIntroductionAudioFileName() {
        if (null != getAudioFile()) {
            if (null != getAudioFile().getIntroductionAudioFilenameList()) {
                return getAudioFile().getIntroductionAudioFilenameList();
            }
            return getAudioFile().getShortInstructionAudioFilenameList();
        }
        return null;
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        playCurrentPlayingSoundAudio(true);
    }

    @Override
    protected void onIntroductionAudioStopped() {
        playCurrentPlayingSoundAudio(true);
    }

    @Override
    protected void onNoIntroductionAudioPlay() {
        playCurrentPlayingSoundAudio(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        clearScreenObjects();
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        unhighlightCurrentTouchingKeyList();

        if (isCorrectSoundPressed) {
            /// if the sound is press correctly
            //doWhenCorrectSoundPress();
            //if the word is still playing, above method will be trigger when the key is finish playing
        } else {
            addNumberOfFails();
        }

    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        final TextureScreenObject<String, ScreenObjectType> touchingKeyboardObject = ScreenObjectUtils.getTouchingScreenObject(keyBoardScreenObjectList, screenX, screenY);

        if (null != touchingKeyboardObject) {

            if (ScreenObjectType.KEYBOARD.equals(touchingKeyboardObject.objectType)) {

                currentTouchingKey = touchingKeyboardObject;

                currentTouchingKey.isHighlighted = true;

                currentTouchingKey = whenKeyboardIsTouched(touchingKeyboardObject);

                if (null != currentTouchingKey) {
                    if (isCorrectSoundPressed()) {
                        isCorrectSoundPressed = true;
                        currentPlayingSoundIndex++;
                        //show the correct sound word
                        initSound();
                        //to make sure the check correct sound behavior must be after the phonic key sound played
                        //isPhonicKeySoundPlaying = true;
                        correctSound = touchingKeyboardObject.id;
                        if (isBlockTouchAfterCorrectSoundPressed()) {
                            abstractAutoCognitaScreen.setTouchAllow(false);
                        }
                    } else {
                        isCorrectSoundPressed = false;
                    }

                    String currentKeyAudioFileName = getTouchingKeyAudio();

                    if (StringUtils.isNotBlank(currentKeyAudioFileName)) {
                        Gdx.app.log(getClass().getName(), "touch down play sound");
                        //play phonic key audio
                        abstractAutoCognitaScreen.playSound(currentKeyAudioFileName, new AbstractSoundPlayListener() {

                            @Override
                            public void onComplete() {
                                onFinish();
                            }

                            private void onFinish() {
                                doWhenKeyPressed(touchingKeyboardObject);
                            }

                            @Override
                            public void onStop() {
                                onFinish();
                            }


                        });
                    }
                }


            } else if (ScreenObjectType.SPEECH_ICON.equals(touchingKeyboardObject.objectType)) {
                onSpeechIconPressed();
            }

        }
    }

    protected void unhighlightCurrentTouchingKeyList() {
        if (null != currentTouchingKey) {
            currentTouchingKey.isHighlighted = false;
        }
    }

    private void clearScreenObjects() {
        if (null != keyBoardScreenObjectList) {
            keyBoardScreenObjectList.clear();
            keyBoardScreenObjectList = null;
        }

        if (null != phonicSymbolObjectList) {
            phonicSymbolObjectList.clear();
            phonicSymbolObjectList = null;
        }
        if (null != soundObjectList) {
            soundObjectList.clear();
            soundObjectList = null;
        }
        if (null != smallWordObjectList) {
            smallWordObjectList.clear();
            smallWordObjectList = null;
        }

        speechIconScreenObject = null;
    }

    /**
     * It is used to play the current play sound audio, the audio is mainly used to help the user for playing the current sound
     */
    protected void playCurrentPlayingSoundAudio(boolean isPlayWordSoundRequired) {
        List<String> audioFileNameList = getCurrentPlayingSoundAudioFileName();

        if (CollectionUtils.isEmpty(audioFileNameList) && isPlayWordSoundRequired) {
            //play the current playing word audio
            abstractAutoCognitaScreen.playSound(SoundService.getInstance().getWordAudioFilename(getCurrentPlayingWord()));
        }

        if (CollectionUtils.isNotEmpty(audioFileNameList)) {
            abstractAutoCognitaScreen.playSound(audioFileNameList);
        }

        abstractAutoCognitaScreen.setTouchAllow(true);
    }

    private List<String> getCurrentPlayingSoundAudioFileName() {
        WordSoundMapping wordSoundMapping = getCurrentPlayingWordSound();

        if (null != wordSoundMapping) {
            return wordSoundMapping.getAudioFileName();
        }

        return null;
    }

    protected String getCurrentPlayingWord() {
        if (ArrayUtils.isEmpty(questionList)) {
            return null;
        }
        return questionList[currentSelectedQuestionIndex];
    }

    protected WordSoundMapping getCurrentPlayingWordSound() {

        currentPlayingWordSoundMappingList = getWordSoundMapping(getCurrentPlayingWord());

        if (null == currentPlayingWordSoundMappingList) {
            return null;
        }

        if (currentPlayingSoundIndex >= currentPlayingWordSoundMappingList.getTotalNumberOfSound()) {
            return null;
        }

        return currentPlayingWordSoundMappingList.getWordSoundMappingBySoundIndex(currentPlayingSoundIndex);
    }

    protected WordSoundMappingList getWordSoundMapping(String word) {
        if (null != getPhonicKeyboardWordGameMap() && StringUtils.isNotBlank(word)) {
            return getPhonicKeyboardWordGameMap().get(word);
        }
        return null;
    }

    protected abstract HashMap<String, WordSoundMappingList> getPhonicKeyboardWordGameMap();

    protected boolean isBlockTouchAfterCorrectSoundPressed() {
        return true;
    }

    private void doWhenKeyPressed(TextureScreenObject<String, ScreenObjectType> pressedKey) {
        if (pressedKey.id.equals(correctSound) && isCorrectSoundPressed) {
            doWhenCorrectSoundPress();
            correctSound = null;
        }
    }

    protected String getTouchingKeyAudio() {
        return currentTouchingKey.audioFileName;
    }

    protected TextureScreenObject<String, ScreenObjectType> whenKeyboardIsTouched(TextureScreenObject<String, ScreenObjectType> touchingKey) {
        //make sure there is no highlighted key before set another key as current touching key
        unhighlightCurrentTouchingKeyList();

        touchingKey.isHighlighted = true;

        return touchingKey;
    }

    protected boolean isSmallWordShow() {
        return true;
    }

    public boolean isAllSoundPlayedInTheWord() {
        return currentPlayingSoundIndex >= getNumberOfSoundRequiredToPlayInCurrentWord();
    }

    protected void doWhenCorrectSoundPress() {

        afterCorrectSoundIsPressed();

        if (isAllSoundPlayedInTheWord()) {
            afterLastCorrectKeyPressedForPlayingWord();
        } else {
            //highlight the next sound in the word
            doHighlightNextSound();
            playCurrentPlayingSoundAudio(false);
        }

        unhighlightCurrentTouchingKeyList();
        currentTouchingKey = null;

        isCorrectSoundPressed = false;

    }

    protected String getSoundAudioFileNameByIndex(int soundIndex) {
        return SoundService.getInstance().getPhonicAudioFileName(
                //after the correct sound is pressed , the variable currentPlayingSoundIndex will be add 1, so here minus 1
                getSoundByIndex(soundIndex));
    }

    private String getSoundByIndex(int soundIndex) {
        if (null != currentPlayingWordSoundMappingList && soundIndex < currentPlayingWordSoundMappingList.getSounds().length) {
            return currentPlayingWordSoundMappingList.getSounds()[soundIndex];
        }

        return null;
    }

    /**
     * It will be call when the user press the last correct key for the playing word
     * <p/>
     * play the word sound, then correct sound after all sound play correctly
     */
    protected void afterLastCorrectKeyPressedForPlayingWord() {

        //prevent the user to touch other key before finish the playing the audio of  sound, word and the correct sound
        abstractAutoCognitaScreen.setTouchAllow(false);

        List<String> audioFileNameList = new ArrayList<String>();

        addWordAudioAfterLastCorrectKeyPressedForPlayingWord(audioFileNameList);

        abstractAutoCognitaScreen.playSound(audioFileNameList, new AbstractSoundPlayListListener() {

            @Override
            public void onComplete() {
                onFinish();
            }

            @Override
            public void onStop() {
                onFinish();
            }

            @Override
            public void onAudioFileMissing() {
                onFinish();
            }

            private void onFinish() {
                //separate the correct sound file , not include in the audioFileNameList, because want to present the correct sound behaviour such as show the green border
                abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                    @Override
                    public void onCorrectSoundPlayed() {
                        if (isAllQuestionAreAnswered()) {
                            afterLastQuestionAnswered();
                        } else {
                            afterCorrectSoundPlayed();
                            currentPlayingSoundIndex = 0;

                            afterQuestionAnsweredCorrect();
                            getCurrentPlayingWordSound();
                            //init next word in the screen
                            initNextWordSound();
                            initSmallWord();
                            afterInitNextWord();
                        }
                        abstractAutoCognitaScreen.setTouchAllow(true);
                    }
                });
            }

        });
    }

    protected void addWordAudioAfterLastCorrectKeyPressedForPlayingWord(List<String> audioFileNameList) {
        String wordAudioFileName = SoundService.getInstance().getWordAudioFilename(getCurrentPlayingWord());
        audioFileNameList.add(wordAudioFileName);
    }

    /**
     * This method will be call when the correct sound is pressed, this will be call before the correct action is play, such as display the green border
     */
    protected void afterCorrectSoundIsPressed() {

    }

    /**
     * This method will be called after the word in the new round is initiliaed {@link #initNextWordSound()}, normally it will play the current sound instruction or the word sound
     */
    protected void afterInitNextWord() {
        playCurrentPlayingSoundAudio(true);
    }

    private void afterQuestionAnsweredCorrect() {
        //jump to next question
        currentSelectedQuestionIndex++;
    }

    protected void onSpeechIconPressed() {
        playCurrentPlayingSoundAudio(true);
    }

    protected boolean isAllQuestionAreAnswered() {
        return currentSelectedQuestionIndex + 1 >= getNumberOfQuestion();
    }

    private void afterLastQuestionAnswered() {
        abstractAutoCognitaScreen.showNextSection(numberOfFails);
    }

    protected void initNextWordSound() {
        initSound();
    }

    /**
     * It will be call after the correct sound is played
     */
    protected void afterCorrectSoundPlayed() {

    }

    protected void doHighlightNextSound() {
        initSound();
    }

    private int getNumberOfQuestion() {
        return questionList.length;
    }

    private int getNumberOfSoundRequiredToPlayInCurrentWord() {
        if (null != currentPlayingWordSoundMappingList) {
            return currentPlayingWordSoundMappingList.isPlayFirstSoundOnly() ? 1 : currentPlayingWordSoundMappingList.getTotalNumberOfSound();
        }
        return 0;
    }

    protected String getCurrentPlayingSound() {
        return getSoundByIndex(currentPlayingSoundIndex);
    }

    protected boolean isCorrectSoundPressed() {
        String sound = getCurrentPlayingSound();

        if (null == sound) {
            return false;
        } else {
            if (isCurrentTouchingKeyContainSound(sound)) {
                return true;
            }

            return false;

        }
    }

    private boolean isCurrentTouchingKeyContainSound(String sound) {
        if (null != currentTouchingKey) {
            return currentTouchingKey.id.equals(sound);
        }

        return false;
    }

    protected abstract AbstractPhonicActivity getAbstractPhonicModule();

    protected String[] getEnableKeys() {
        if (null != getAbstractPhonicModule()) {
            String enableKeys = getAbstractPhonicModule().getEnableKeys();
            if (StringUtils.isNotBlank(enableKeys)) {
                return enableKeys.split(",");
            }
        }

        return null;
    }

    @Override
    public void reset() {
        clearScreenObjects();
        currentPlayingSoundIndex = 0;
        currentSelectedQuestionIndex = 0;
        currentPlayingWordSoundMappingList = null;
        questionList = null;

    }

    protected void initQuestionList() {
        if (null == questionList && null != getPhonicKeyboardWordGameMap()) {
            questionList = new String[getPhonicKeyboardWordGameMap().size()];
            getPhonicKeyboardWordGameMap().keySet().toArray(questionList);
        }
    }

    protected final void initSmallWord() {

        if (null == smallWordObjectList) {
            smallWordObjectList = new ArrayList<ScreenObject<Object, ScreenObjectType>>();
        } else {
            smallWordObjectList.clear();
        }

        if (ArrayUtils.isNotEmpty(questionList) && currentSelectedQuestionIndex < questionList.length) {
            String selectQuestionWord = getCurrentPlayingWord();

            currentPlayingWordSoundMappingList = getWordSoundMapping(selectQuestionWord);

            smallWordWidth = LetterUtils.getTotalWidthOfWord(selectQuestionWord, getSmallWordFontSize());

            smallWordStartXPosition = ScreenUtils.getXPositionForCenterObject(smallWordWidth);
            float startXPositionForSmallWord = smallWordStartXPosition;

            for (char character : selectQuestionWord.toCharArray()) {
                TextScreenObject characterScreenObject = LetterUtils.getTextScreenObjectWithYPositionNotIncludeHeight(null, ScreenObjectType.WORD,
                        startXPositionForSmallWord,
                        getSmallWordAbovePhonicKeyboardStartYPosition(),
                        String.valueOf(character), getSmallWordFontSize());
                startXPositionForSmallWord += characterScreenObject.width;

                smallWordHeight = Math.max(smallWordHeight, LetterUtils.getHeightOfWord(String.valueOf(character), getSmallWordFontSize()));

                smallWordObjectList.add(characterScreenObject);
            }
            //check the current play sound
            afterSmallWordInitialized();
        }
    }

    protected float getStartYPositionForPhonicSymbolInPhonicKeyboardScreen() {
        return getKeyboardStartYPosition() + GAP_BETWEEN_PHONIC_SYMBOL_AND_KEYBOARD;
    }

    protected float getSmallWordAbovePhonicKeyboardStartYPosition() {
        return getStartYPositionForPhonicSymbolInPhonicKeyboardScreen() +
                Math.max(ImageUtils.LARGE_PHONIC_SYMBOL_OPEN.height,
                        LetterUtils.getMaximumHeight(getPhonicSoundFontSize())) + 50;
    }

    protected void initSound() {
        if (null != currentPlayingWordSoundMappingList) {

            List<WordSoundMapping> wordSoundMappingList = currentPlayingWordSoundMappingList.getWordSoundMappingList();

            if (CollectionUtils.isNotEmpty(wordSoundMappingList)) {

                if (CollectionUtils.isNotEmpty(soundObjectList)) {
                    //make sure the previous display sound is clear
                    soundObjectList.clear();
                }

                List<String> displaySoundList = new ArrayList<String>();

                int soundIndex = 0;
                int letterIndex = 0;
                for (WordSoundMapping wordSoundMapping : currentPlayingWordSoundMappingList.getWordSoundMappingList()) {
                    if (currentPlayingSoundIndex > soundIndex ||
                            //if the word is only play the first sound
                            (letterIndex > 0 && currentPlayingWordSoundMappingList.isPlayFirstSoundOnly())) {

                        //which mean it is already played
                        for (int i = 0; i < wordSoundMapping.getSound().split(",").length; i++) {
                            //there may be has multiple sound for 1 letter
                            if (currentPlayingSoundIndex > soundIndex + i ||
                                    //if the word is playing for first sound only, beside the first sound, other sound should be displayed
                                    currentPlayingWordSoundMappingList.isPlayFirstSoundOnly()) {
                                //which mean the sound is already played
                                displaySoundList.add(replaceSpecialSound(wordSoundMapping.getSound().split(",")[i]));

                            } else {
                                //which mean the sound in the letter is not played
                                displaySoundList.add(LetterUtils.UNDERSCORE);
                            }
                        }

                        if (letterIndex < currentPlayingWordSoundMappingList.getWordSoundMappingList().size() - 1) {
                            //if this is not the last letter
                            displaySoundList.add("-");
                        }

                    } else {
                        //which mean it is not played
                        displaySoundList.add(LetterUtils.UNDERSCORE);

                        if (0 == letterIndex && currentPlayingWordSoundMappingList.isPlayFirstSoundOnly()) {
                            displaySoundList.add("-");
                        }
                    }

                    soundIndex += wordSoundMapping.getNumberOfSound();
                    letterIndex++;
                }

                float totalWidthOfSound = 0;

                for (String displaySound : displaySoundList) {
                    totalWidthOfSound += LetterUtils.getTotalWidthOfWord(displaySound, getPhonicSoundFontSize());
                }

                //the start x position of the phonic letter which located above the keyboard
                float startXPosition = ScreenUtils.getXPositionForCenterObject(totalWidthOfSound +
                        ImageUtils.LARGE_PHONIC_SYMBOL_OPEN.width * 2
                        + GAP_BETWEEN_PHONIC_SYMBOL_AND_PHONIC_LETTER * 2);


                phonicSymbolObjectList = new ArrayList<TextureScreenObject<Object, ScreenObjectType>>(2);

                TextureScreenObject phonicSymbolOpen = new TextureScreenObject<Object, ScreenObjectType>(null, ScreenObjectType.WORD, startXPosition,
                        getStartYPositionForPhonicSymbolInPhonicKeyboardScreen(), ImageUtils.getLargePhonicSymbolOpenIcon(), null);
                //init the phonic symbol open icon
                phonicSymbolObjectList.add(phonicSymbolOpen);


                startXPosition += ImageUtils.LARGE_PHONIC_SYMBOL_OPEN.width + GAP_BETWEEN_PHONIC_SYMBOL_AND_PHONIC_LETTER;


                boolean isSoundHighlighted = false;
                for (String displaySound : displaySoundList) {

                    List<ScreenObject<String, ScreenObjectType>> screenObjectList = null;

                    if ("_th".equals(displaySound)) {
                        screenObjectList = LetterUtils.getTHSound(
                                ScreenObjectType.WORD, startXPosition, getStartYPositionForPhonicSymbolInPhonicKeyboardScreen(), getPhonicSoundFontSize(), 0);
                        startXPosition += screenObjectList.get(0).width;
                    } else {
                        screenObjectList = LetterUtils.getTextScreenObjectListSeparatelyWithYPositionNotIncludeHeight(displaySound, ScreenObjectType.WORD,
                                startXPosition,
                                getStartYPositionForPhonicSymbolInPhonicKeyboardScreen(),
                                displaySound,
                                getPhonicSoundFontSize());

                        for (ScreenObject screenObject : screenObjectList) {
                            startXPosition += screenObject.width;
                        }
                    }

                    for (ScreenObject screenObject : screenObjectList) {
                        //recaculate the y position of the phonic sounds,  make sure it is center in within the symbol
                        screenObject.yPositionInScreen =
                                getStartYPositionForPhonicSymbolInPhonicKeyboardScreen() + screenObject.height +
                                        (Math.abs(phonicSymbolOpen.height - screenObject.height)) / 2;
                    }


                    if (null == soundObjectList) {
                        soundObjectList = new ArrayList<ScreenObject<String, ScreenObjectType>>();
                    }
                    soundObjectList.addAll(screenObjectList);

                    if (LetterUtils.UNDERSCORE.equals(displaySound)) {
                        ScreenObject underscoreScreenObject = screenObjectList.get(0);
                        if (isSoundHighlighted) {
                            underscoreScreenObject.isDisabled = true;
                        } else {
                            underscoreScreenObject.isHighlighted = true;
                            //to indicate that the underscore in the bebind is mean not playing yet, so no need highlight
                            isSoundHighlighted = true;
                        }
                    }
                }

                startXPosition += GAP_BETWEEN_PHONIC_SYMBOL_AND_PHONIC_LETTER;
                //init the phonic symbol close icon
                phonicSymbolObjectList.add(new TextureScreenObject<Object, ScreenObjectType>(null, ScreenObjectType.WORD, startXPosition,
                        getStartYPositionForPhonicSymbolInPhonicKeyboardScreen(), ImageUtils.getLargePhonicSymbolCloseIcon(), null));
            }

        }
    }

    private String replaceSpecialSound(String sound) {
        if ("_a".equals(sound)) {
            return "ā";
        } else if ("_e".equals(sound)) {
            return "ē";
        } else if ("_i".equals(sound)) {
            return "ī";
        } else if ("_o".equals(sound)) {
            return "ō";
        } else if ("_oo".equals(sound)) {
            return "ōō";
        } else if ("_u".equals(sound)) {
            return "ū";
        }

        return sound;
    }
}
