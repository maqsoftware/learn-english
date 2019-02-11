package com.maqautocognita.bo;

import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.CollectionUtils;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class WordSoundMappingList {

    private List<WordSoundMapping> wordSoundMappingList;

    private boolean playFirstSoundOnly;

    /**
     * store the total number of sound of the word, it is always equals to the size of {@link #wordSoundMappingList},
     * but in some case such as box, there are 4 sounds b-o-k-s, and the last 2 sound will be store in {@link WordSoundMapping} to represent the letter "x"
     */
    private int totalNumberOfSound;

    /**
     * Store all sounds required for the word in sequence.
     * For example the word box, it will be store the b,o,k,s
     */
    private String[] sounds;

    public List<WordSoundMapping> getWordSoundMappingList() {
        return wordSoundMappingList;
    }

    public void setWordSoundMappingList(List<WordSoundMapping> wordSoundMappingList) {
        this.wordSoundMappingList = wordSoundMappingList;
    }

    public boolean isPlayFirstSoundOnly() {
        return playFirstSoundOnly;
    }

    public void setPlayFirstSoundOnly(boolean playFirstSoundOnly) {
        this.playFirstSoundOnly = playFirstSoundOnly;
    }

    public String[] getSounds() {
        if (ArrayUtils.isEmpty(sounds) && CollectionUtils.isNotEmpty(wordSoundMappingList)) {
            sounds = new String[getTotalNumberOfSound()];
            int i = 0;
            for (WordSoundMapping wordSoundMapping : wordSoundMappingList) {
                for (String sound : wordSoundMapping.getSound().split(",")) {
                    sounds[i] = sound;
                    i++;
                }
            }
        }

        return sounds;
    }

    public int getTotalNumberOfSound() {
        if (0 == totalNumberOfSound && CollectionUtils.isNotEmpty(wordSoundMappingList)) {
            for (WordSoundMapping wordSoundMapping : wordSoundMappingList) {
                totalNumberOfSound += wordSoundMapping.getSound().split(",").length;
            }
        }

        return totalNumberOfSound;
    }

    /**
     * Get the {@link WordSoundMapping} which is belongs to the given soundIndex
     * @param soundIndex
     * @return {@link WordSoundMapping}
     */
    public WordSoundMapping getWordSoundMappingBySoundIndex(int soundIndex) {
        int i = 0;
        for (WordSoundMapping wordSoundMapping : wordSoundMappingList) {
            int numberOfSound = wordSoundMapping.getSound().split(",").length;
            if (soundIndex < i + numberOfSound) {
                return wordSoundMapping;
            }
            i += numberOfSound;
        }
        return null;
    }
}
