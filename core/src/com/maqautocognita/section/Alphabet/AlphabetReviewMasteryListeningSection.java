package com.maqautocognita.section.Alphabet;

import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ImageUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.screens.AbstractLetterScreen;
import com.maqautocognita.screens.AlphabetScreen;
import com.maqautocognita.section.MenuSection;
import com.maqautocognita.service.SoundService;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ReviewUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class AlphabetReviewMasteryListeningSection extends AlphabetReviewMasteryReadingSection {

    private Character currentPlayingLetters[];
    /**
     * It will store those letter which already answered by user
     */
    private List<Character> answeredCharacterList;
    private Character currentPlayingLetter;

    private TextureScreenObject<Character, ScreenObjectType> touchingKeyboardObject;

    public AlphabetReviewMasteryListeningSection(AbstractLetterScreen abstractLetterScreen) {
        super(abstractLetterScreen);
    }

    @Override
    public void setSelectedActivity(Activity review, boolean isMasterTest) {

        Character[] letters = isMasterTest ? ReviewUtils.getAllLetterForAlphabetMasteryTest() : ReviewUtils.getAllLettersInReview(review);

        if (ArrayUtils.isNotEmpty(letters)) {
            selectedReview = review;

            this.isMasterTest = isMasterTest;

            allLetters = new Character[letters.length * 2];

            int letterIndex = 0;

            int index = 0;
            while (letterIndex < letters.length) {
                allLetters[index] = Character.toLowerCase(letters[letterIndex]);
                index++;
                allLetters[index] = Character.toUpperCase(letters[letterIndex]);
                index++;
                letterIndex++;
            }

            Collections.shuffle(Arrays.asList(allLetters));

            setPlayLetters();
        }
    }

    @Override
    protected void setPlayLetters() {

        int numberOfShowingLetter;

        if (allLetters.length == 12) {
            //reach the max
            numberOfShowingLetter = 12;
        } else {
            numberOfShowingLetter = Math.min(isMasterTest ? 10 : 12, allLetters.length);
        }

        currentPlayingLetters = new Character[numberOfShowingLetter];

        for (int i = 0; i < numberOfShowingLetter; i++) {
            currentPlayingLetters[i] = allLetters[i];
        }

        answeredCharacterList = new ArrayList<Character>(currentPlayingLetters.length);

        getNextLetter();
    }

    @Override
    protected void initScreenObjectList() {

        if (null == keyboardTexture) {
            keyboardTexture = AssetManagerUtils.getTexture(AssetManagerUtils.GENERAL_KEYS);
        }

        int numberOfShowingLetter = currentPlayingLetters.length;

        screenObjectList = new ArrayList<TextureScreenObject<Character, ScreenObjectType>>(numberOfShowingLetter + 1);

        screenObjectList.add(
                new TextureScreenObject<Character, ScreenObjectType>(null,
                        ScreenObjectType.SPEECH_ICON,
                        ScreenUtils.getScreenWidth() - MenuSection.MenuItemEnum.HELP.iconPosition.x - ImageUtils.SPEECH_ICON_POSITION.width,
                        MenuSection.MenuItemEnum.HELP.iconPosition.y,
                        ImageUtils.getSpeechIcon(), null));

        initKeyboardObjects(currentPlayingLetters);
//
//        for (int i = 0; i < numberOfShowingLetter; i++) {
//            int startXPosition = 330 + (i >= numberOfShowingLetter / 2 ? i - numberOfShowingLetter / 2 : i) * (numberOfShowingLetter / 2 < 6 ? 300 : 240);
//
//            char letter = currentPlayingLetters[i];
//
//            boolean isLowerCase = Character.isLowerCase(letter);
//
//            int startYPosition = i < numberOfShowingLetter / 2 ? START_Y_POSITION_UPPER_CASE : START_Y_POSITION_LOWER_CASE;
//
//            //init letter
//            screenObjectList.add(
//                    new TextureScreenObject<Character, ScreenObjectType>(letter,
//                            isLowerCase ? ScreenObjectType.LOWERCASE : ScreenObjectType.UPPERCASE,
//                            startXPosition,
//                            startYPosition,
//                            getLetterTextureRegion(letter, false),
//                            getLetterTextureRegion(letter, true)));
//        }
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return selectedReview;
    }

    @Override
    public void reset() {
        super.reset();
        if (null != answeredCharacterList) {
            answeredCharacterList.clear();
            answeredCharacterList = null;
        }

        currentPlayingLetter = null;
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);

        if (null != touchingKeyboardObject) {

            touchingKeyboardObject.isHighlighted = false;

            if (null != touchingKeyboardObject.id) {
                if (touchingKeyboardObject.id.equals(currentPlayingLetter)) {
                    //which mean selected correct sound
                    removeCurrentPlayingLetter();
                    answeredCharacterList.add(currentPlayingLetter);

                    final ScreenObject<Character, ScreenObjectType> touchingKeyboardObjectTemp = touchingKeyboardObject;
                    final Character pressedCharacter = touchingKeyboardObjectTemp.id;

                    abstractAutoCognitaScreen.playCorrectSound(new AlphabetScreen.ICorrectSoundListener() {
                        @Override
                        public void onCorrectSoundPlayed() {

                            for (int i = 0; i < screenObjectList.size(); i++) {

                                ScreenObject<Character, ScreenObjectType> screenObject = screenObjectList.get(i);

                                if (null != screenObject.id && screenObject.id.equals(pressedCharacter)) {
                                    screenObjectList.remove(screenObject);
                                    break;
                                }
                            }

                            screenObjectList.remove(touchingKeyboardObjectTemp);
                            if (allLetters.length == 0) {
                                abstractAutoCognitaScreen.showNextSection(numberOfFails);
                            } else if (answeredCharacterList.size() == currentPlayingLetters.length) {
                                reset();
                                setPlayLetters();
                                playLetterAudio();
                            } else {
                                finishCurrentLetterAndPlayNext();
                            }
                        }
                    });
                } else {
                    addNumberOfFails();
                    //if wrong key is pressed, play the key audio
                    abstractAutoCognitaScreen.playSound(getLetterAudioFileName(touchingKeyboardObject.id));
                    abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                        @Override
                        public void onCorrectSoundPlayed() {

                        }
                    });
                }

                touchingKeyboardObject = null;
            }
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        touchingKeyboardObject = ScreenObjectUtils.getTouchingScreenObject(screenObjectList, screenX, screenY);

        if (null != touchingKeyboardObject) {


            if (ScreenObjectType.SPEECH_ICON.equals(touchingKeyboardObject.objectType)) {
                playLetterAudio();
            } else {
                //only keyboard can be highlighted
                touchingKeyboardObject.isHighlighted = true;
            }
        }
    }

    private void playLetterAudio() {
        if (null != currentPlayingLetter) {
            abstractAutoCognitaScreen.playSound(getCurrentPlayingAudioFileName());
        }
    }

    private String getCurrentPlayingAudioFileName() {
        return getLetterAudioFileName(currentPlayingLetter);
    }

    private String getLetterAudioFileName(Character letter) {
        return SoundService.getInstance().getAlphabetAudioFileName(letter);
    }

    private void getNextLetter() {
        if (allLetters.length > 0) {
            //get the current play letters
            List<Character> remainLetterList = new ArrayList<Character>(Arrays.asList(currentPlayingLetters));
            //remove all answered letter
            remainLetterList.removeAll(answeredCharacterList);
            //random the remain letter list which mean still not answer yet
            Collections.shuffle(remainLetterList);
            //get the fist one in the random list for next playing letter
            currentPlayingLetter = remainLetterList.get(0);

            Gdx.app.log("Review Listening", "play letter = " + currentPlayingLetter);
        }
    }

    private void finishCurrentLetterAndPlayNext() {
        isHelpAudioPlaying = false;
        currentPlayingLetter = null;
        getNextLetter();
        playLetterAudio();
    }

    /**
     * In this module, introduction audio file name will be store in the {@link AbstractAudioFile#shortInstructionAudioFilenameList}.
     * <p/>
     * And there is a audio named in the pattern "alphabet_***", which mean it is a dynamic audio file,
     * will be depends current playing letter, for example now is playing the big letter A, and the file name will be replaced by the audio big letter A.
     *
     * @return
     */
    @Override
    protected List<String> getIntroductionAudioFileName() {
        if (null != getAudioFile()) {
            List<String> shortInstructionAudioNameList = getAudioFile().getShortInstructionAudioFilenameList();

            if (CollectionUtils.isNotEmpty(shortInstructionAudioNameList)) {

                List<String> newAudioFileNameList = new ArrayList<String>(shortInstructionAudioNameList.size());
                for (String audioFileName : shortInstructionAudioNameList) {

                    if ("alphabet_***".equals(audioFileName)) {
                        audioFileName = getCurrentPlayingAudioFileName();
                    }

                    newAudioFileNameList.add(audioFileName);
                }

                return newAudioFileNameList;
            }
        }

        return null;
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        playLetterAudio();
    }

    @Override
    protected void onNoIntroductionAudioPlay() {
        playLetterAudio();
    }

    protected void onHelpAudioComplete() {
        playLetterAudio();
    }

    private AutoCognitaTextureRegion getLetterTextureRegion(char letter, boolean isHighlightState) {
        return getLetterTextureRegion(letter, isHighlightState, Character.isLowerCase(letter));
    }

    private void removeCurrentPlayingLetter() {
        List<Character> list = new ArrayList<Character>(Arrays.asList(allLetters));
        list.remove(currentPlayingLetter);
        allLetters = list.toArray(new Character[]{});
    }
}
