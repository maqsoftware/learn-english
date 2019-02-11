package com.maqautocognita.service;

import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class SoundService {

    private static SoundService instance = null;
    /**
     * Store the sound audio file name mapping for phonic
     */
    private Map<String, String> phonicAudioFileNameMapping;

    private Map<String, String> phonicSoundLetterMapping;

    public static SoundService getInstance() {
        if (instance == null) {
            instance = new SoundService();
        }
        return instance;
    }

    public String getWordAudioFilename(String word) {
        if (StringUtils.isNotBlank(word)) {
            return "word_" + word.replaceAll("[^A-Za-z0-9' ]", "") + ".m4a";
        }

        return null;
    }

    public String getAlphabetAudioFileName(String alphabet) {
        String audioFileName;
        if (UserPreferenceUtils.getInstance().isEnglish()) {
            audioFileName = "alphabet_";
        } else {
            audioFileName = "salphabet_";
        }
        return audioFileName + alphabet.toLowerCase() + ".m4a";
    }

    public String getAlphabetAudioFileName(char letter) {
        String audioFileName;
        if (UserPreferenceUtils.getInstance().isEnglish()) {
            audioFileName = "alphabet_";
        } else {
            audioFileName = "salphabet_";
        }
        if (Character.isUpperCase(letter)) {
            audioFileName += "big_" + letter;
        } else {
            audioFileName += "little_" + letter;
        }

        return audioFileName.toLowerCase();
    }


    public void addPhonicAudioFileName(String phonic, String audioFileName) {
        if (null == phonicAudioFileNameMapping) {
            phonicAudioFileNameMapping = new HashMap<String, String>();
        }
        if (!phonicAudioFileNameMapping.containsKey(phonic)) {
            phonicAudioFileNameMapping.put(phonic, audioFileName);
        }
    }

    public void addPhonicSoundMapping(String unitCode, String letter, String phonicSound) {
        if (null == phonicSoundLetterMapping) {
            phonicSoundLetterMapping = new HashMap<String, String>();
        }
        if (!phonicSoundLetterMapping.containsKey(getKey(unitCode, letter))) {
            phonicSoundLetterMapping.put(getKey(unitCode, letter), phonicSound);
        }
    }

    private String getKey(String unitCode, String letter) {
        return unitCode + "_" + letter;
    }

    public String getPhonicAudioFileNameByUnitCodeAndElementCode(String unitCode, String letter) {

        String phonicSound = null;
        if (null != phonicSoundLetterMapping) {
            phonicSound = phonicSoundLetterMapping.get(getKey(unitCode, letter));
        }

        return getPhonicAudioFileName(phonicSound);
    }

    public String getPhonicAudioFileName(String phonicSound) {
        if (null != phonicAudioFileNameMapping) {
            return phonicAudioFileNameMapping.get(phonicSound);
        }

        return null;
    }

    public void clearPhonicMapping() {
        if (null != phonicAudioFileNameMapping) {
            phonicAudioFileNameMapping.clear();
        }
        if (null != phonicSoundLetterMapping) {
            phonicSoundLetterMapping.clear();
        }
    }
}
