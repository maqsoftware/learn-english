package com.maqautocognita.bo;

import com.maqautocognita.utils.StringUtils;

import java.util.List;

/**
 * This is mainly used to store the round for the game which is required the phonic keyboards, such as the game Word blend and Listen & Type
 *
 * @author csc
 */
public class WordSoundMapping {

    /**
     * Store the sound for the word, there may has more than 1 phonic key for 1 sound, and they will be stored by a comma
     */
    private String sound;

    /**
     * The letter which in the below index will be highlighted, it is used for ask the user to key the phonic key (sound) which is match to the highlighted letter
     */
    private int startIndex;

    /**
     * Number of letter will be highlighted start from the {@link #startIndex}
     */
    private int letterLength;


    private List<String> audioFileName;

    public List<String> getAudioFileName() {
        return audioFileName;
    }

    public void setAudioFileName(List<String> audioFileName) {
        this.audioFileName = audioFileName;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getLetterLength() {
        return letterLength;
    }

    public void setLetterLength(int letterLength) {
        this.letterLength = letterLength;
    }


    public int getNumberOfSound() {
        if (StringUtils.isNotBlank(sound)) {
            return sound.split(",").length;
        }

        return 0;
    }

}
