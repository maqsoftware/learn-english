package com.maqautocognita.service;

import com.maqautocognita.bo.Activity;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ImageUtils;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This service is mainly used to draw the phonic sound graphic in the screen.
 * <p/>
 * The method {@link #drawPhonicSoundWithLetter(Activity, float)} is mainly used to draw phonic sound and the showing letter,
 * the phonic sound will be draw in the screen center and enclosed by the phonic open and close symbol, and the display letter will be show above the phonic sound.
 * <p/>
 * The {@link #phonicOpenSymbolScreenObject} and {@link #phonicCloseSymbolScreenObject} is public for the caller to use in anytime after init,
 * for example change the symbol texture when the phonic sound is playing
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicSoundScreenService {

    private static final float GAP_BETWEEN_PHONIC_SYMBOL_AND_PHONIC_LETTER = 50;

    private static final float GAP_BETWEEN_PHONIC_SOUND_AND_PHONIC_LETTER = 80;

    public TextureScreenObject<String, ScreenObjectType> phonicOpenSymbolScreenObject;

    public TextureScreenObject<String, ScreenObjectType> phonicCloseSymbolScreenObject;

    public List<ScreenObject<String, ScreenObjectType>> letterScreenObjectList;

    public List<ScreenObject<String, ScreenObjectType>> phonicSoundScreenObjectList;


    public PhonicSoundScreenService() {

    }

    public List<ScreenObject<String, ScreenObjectType>> drawPhonicSoundWithLetter(Activity selectedActivity, float startYPosition) {

        clearScreenObject();

        List<ScreenObject<String, ScreenObjectType>> showingLetterPositionAudioList = new ArrayList<ScreenObject<String, ScreenObjectType>>();

        float[] phonicSoundPosition = addPhonicSound(showingLetterPositionAudioList, selectedActivity, startYPosition);

        String selectedLetter = selectedActivity.getLetter();

        addLetter(showingLetterPositionAudioList, selectedActivity.getAudioFileName(), selectedLetter, phonicSoundPosition[0],
                //the phonic sound start y position + the height of the sound
                phonicSoundPosition[1] + GAP_BETWEEN_PHONIC_SOUND_AND_PHONIC_LETTER,
                phonicSoundPosition[2]);

        return showingLetterPositionAudioList;

    }

    private void clearScreenObject() {
        phonicOpenSymbolScreenObject = null;
        phonicCloseSymbolScreenObject = null;
        letterScreenObjectList = null;
        phonicSoundScreenObjectList = null;
    }

    private float[] addPhonicSound(List<ScreenObject<String, ScreenObjectType>> showingLetterPositionAudioList, Activity selectedActivity, float startYPosition) {

        String phonicSound = selectedActivity.getPhonic();

        final float letterTotalWidth = LetterUtils.getTotalWidthOfWord(phonicSound, TextFontSizeEnum.FONT_288);

        final float totalWidth = letterTotalWidth + ImageUtils.LARGE_PHONIC_SYMBOL_OPEN.width + ImageUtils.LARGE_PHONIC_SYMBOL_CLOSE.width +
                GAP_BETWEEN_PHONIC_SYMBOL_AND_PHONIC_LETTER * 2;

        final float startXPosition = ScreenUtils.getXPositionForCenterObject(totalWidth);

        phonicOpenSymbolScreenObject = new TextureScreenObject<String, ScreenObjectType>(null, ScreenObjectType.HINTS, startXPosition,
                startYPosition, ImageUtils.getLargePhonicSymbolOpenIcon(), null);
        //add phonic symbol open icon
        showingLetterPositionAudioList.add(phonicOpenSymbolScreenObject);


        if ("_th".equals(phonicSound)) {
            phonicSoundScreenObjectList = LetterUtils.getTHSound(ScreenObjectType.SOUND, startXPosition + ImageUtils.LARGE_PHONIC_SYMBOL_OPEN.width,
                    startYPosition,
                    TextFontSizeEnum.FONT_288, letterTotalWidth + GAP_BETWEEN_PHONIC_SYMBOL_AND_PHONIC_LETTER * 2);

            for (ScreenObject<String, ScreenObjectType> screenObject : phonicSoundScreenObjectList) {
                screenObject.audioFileName = selectedActivity.getAudioFileName();
                screenObject.yPositionInScreen = startYPosition + screenObject.height + (Math.abs(phonicOpenSymbolScreenObject.height - screenObject.height)) / 2;

            }
        } else {

            ScreenObject phonicSoundScreenObject = new TextScreenObject(phonicSound, ScreenObjectType.SOUND, phonicSound,
                    startXPosition + ImageUtils.LARGE_PHONIC_SYMBOL_OPEN.width,
                    startYPosition, TextFontSizeEnum.FONT_288, letterTotalWidth + GAP_BETWEEN_PHONIC_SYMBOL_AND_PHONIC_LETTER * 2);

            phonicSoundScreenObject.yPositionInScreen =
                    //in order to make the symbol to the center of the sound
                    startYPosition + phonicSoundScreenObject.height + (Math.abs(phonicOpenSymbolScreenObject.height - phonicSoundScreenObject.height)) / 2;


            if (null == phonicSoundScreenObjectList) {
                phonicSoundScreenObjectList = new ArrayList<ScreenObject<String, ScreenObjectType>>();
            }

            phonicSoundScreenObjectList.add(phonicSoundScreenObject);


            phonicSoundScreenObject.audioFileName = selectedActivity.getAudioFileName();
        }

        float phonicCloseSymbolStartXPosition = startXPosition + ImageUtils.LARGE_PHONIC_SYMBOL_OPEN.width + letterTotalWidth + GAP_BETWEEN_PHONIC_SYMBOL_AND_PHONIC_LETTER * 2;

        phonicCloseSymbolScreenObject =
                new TextureScreenObject<String, ScreenObjectType>(null, ScreenObjectType.HINTS, phonicCloseSymbolStartXPosition, startYPosition, ImageUtils.getLargePhonicSymbolCloseIcon(), null);
        //add phonic symbol close icon
        showingLetterPositionAudioList.add(phonicCloseSymbolScreenObject);

        showingLetterPositionAudioList.addAll(phonicSoundScreenObjectList);

        return new float[]{startXPosition, phonicSoundScreenObjectList.get(0).yPositionInScreen, totalWidth};
    }

    private void addLetter(List<ScreenObject<String, ScreenObjectType>> screenObjectList, String audioFileName, String letter, float startXPosition, float startYPosition, float withinArea) {
        float letterTotalWidth = LetterUtils.getTotalWidthOfWord(letter, TextFontSizeEnum.FONT_144);

        float letterStartXPosition = startXPosition + (withinArea - letterTotalWidth) / 2;

        if ("_th".equals(letter)) {
            letterScreenObjectList =
                    LetterUtils.getTHSound(ScreenObjectType.LETTER, letterStartXPosition, startYPosition,
                            TextFontSizeEnum.FONT_144, letterTotalWidth);

            for (ScreenObject textScreenObject : letterScreenObjectList) {
                textScreenObject.audioFileName = audioFileName;
            }

        } else {
            ScreenObject letterScreenObject = LetterUtils.getTextScreenObjectWithYPositionNotIncludeHeight(null, ScreenObjectType.LETTER,
                    letterStartXPosition,
                    startYPosition, letter,
                    TextFontSizeEnum.FONT_144);

            if (null == letterScreenObjectList) {
                letterScreenObjectList = new ArrayList<ScreenObject<String, ScreenObjectType>>();
            }

            letterScreenObject.audioFileName = audioFileName;

            letterScreenObjectList.add(letterScreenObject);
        }

        screenObjectList.addAll(letterScreenObjectList);
    }

}
