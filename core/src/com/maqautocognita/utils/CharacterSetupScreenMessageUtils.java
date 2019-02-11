package com.maqautocognita.utils;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class CharacterSetupScreenMessageUtils {

    private static final String AUDIO_NAME_REMOVE_PATTERN = "[^\\p{L}\\p{Nd}]+";

    public static String getCharacterSexMessage(String sex) {
        if (UserPreferenceUtils.getInstance().isEnglish()) {
            return getCharacterSexEnglishMessage(sex);
        } else {
            return getSwahiliSentence(sex);
        }
    }

    private static String getCharacterSexEnglishMessage(String sex) {
        return "I am a " + sex + ".";
    }

    private static String getSwahiliSentence(String englishWord) {
        if ("boy".equalsIgnoreCase(englishWord)) {
            return "Mimi ni mvulana.";
        } else if ("girl".equalsIgnoreCase(englishWord)) {
            return "Mimi ni msichana.";
        } else if ("shirt".equalsIgnoreCase(englishWord)) {
            return "Ninaliona shati langu.";
        } else if ("T-shirt".equalsIgnoreCase(englishWord)) {
            return "Ninaliona shati langu.";
        } else if ("dress".equalsIgnoreCase(englishWord)) {
            return "Ninaiona nguo yangu.";
        } else if ("coat".equalsIgnoreCase(englishWord)) {
            return "Ninaliona koti langu.";
        } else if ("pants".equalsIgnoreCase(englishWord)) {
            return "Ninaiona suruali ndefu yangu pia.";
        } else if ("shorts".equalsIgnoreCase(englishWord)) {
            return "Ninaiona suruali fupi yangu pia.";
        } else if ("skirt".equalsIgnoreCase(englishWord)) {
            return "Ninaiona skati yangu pia.";
        } else if ("shoes".equalsIgnoreCase(englishWord)) {
            return "Ninaviona viatu vyangu pia.";
        } else if ("sandals".equalsIgnoreCase(englishWord)) {
            return "Ninaziona sandali zangu pia.";
        } else if ("socks".equalsIgnoreCase(englishWord)) {
            return "Ninaziona soksi zangu pia.";
        }

        return "";
    }

    public static String getCharacterSexAudioFileName(String sex) {
        return getAudioFileName(getCharacterSexEnglishMessage(sex));
    }

    private static String getAudioFileName(String message) {
        return message.replaceAll(AUDIO_NAME_REMOVE_PATTERN, "").toLowerCase();
    }

    public static String getSkinToneAudioFileName() {
        return getAudioFileName(getSkinToneEnglishMessage());
    }

    private static String getSkinToneEnglishMessage() {
        return "This is my skin.";
    }

    public static String getSkinToneMessage() {
        if (UserPreferenceUtils.getInstance().isEnglish()) {
            return getSkinToneEnglishMessage();
        } else {
            return "Hii ni ngozi yangu.";
        }
    }

    public static String getHairAudioFileName() {
        return getAudioFileName(getHairEnglishMessage());
    }

    private static String getHairEnglishMessage() {
        return "This is my hair.";
    }

    public static String getHairMessage() {
        if (UserPreferenceUtils.getInstance().isEnglish()) {
            return getHairEnglishMessage();
        } else {
            return "Hii ni nywele yangu.";
        }
    }

    public static String getNoseAudioFileName() {
        return getAudioFileName(getNoseEnglishMessage());
    }

    private static String getNoseEnglishMessage() {
        return "This is my nose.";
    }

    public static String getNoseMessage() {
        if (UserPreferenceUtils.getInstance().isEnglish()) {
            return getNoseEnglishMessage();
        } else {
            return "Hili ni pua langu.";
        }
    }

    public static String getMouthAudioFileName() {
        return getAudioFileName(getMouthEnglishMessage());
    }

    private static String getMouthEnglishMessage() {
        return "This is my mouth.";
    }

    public static String getMouthMessage() {
        if (UserPreferenceUtils.getInstance().isEnglish()) {
            return getMouthEnglishMessage();
        } else {
            return "Huu ni mdomo wangu.";
        }
    }

    public static String getEyeAudioFileName() {
        return getAudioFileName(getEyeEnglishMessage());
    }

    private static String getEyeEnglishMessage() {
        return "These are my eyes.";
    }

    public static String getEyeMessage() {
        if (UserPreferenceUtils.getInstance().isEnglish()) {
            return getEyeEnglishMessage();
        } else {
            return "Haya ni macho yangu.";
        }
    }

    public static String getEyeBrowsAudioFileName() {
        return getAudioFileName(getEyeBrowsEnglishMessage());
    }

    private static String getEyeBrowsEnglishMessage() {
        return "These are my eye brows.";
    }

    public static String getEyeBrowsMessage() {
        if (UserPreferenceUtils.getInstance().isEnglish()) {
            return getEyeBrowsEnglishMessage();
        } else {
            return " Hizi ni nyusi zangu.";
        }
    }

    public static String getCharacterClothesAudioFileName(String clothes) {
        return getAudioFileName(getCharacterClothesEnglishMessage(clothes));
    }

    private static String getCharacterClothesEnglishMessage(String clothes) {
        return "I can see my " + clothes + ".";
    }

    public static String getCharacterClothesMessage(String clothes) {
        if (UserPreferenceUtils.getInstance().isEnglish()) {
            return getCharacterClothesEnglishMessage(clothes);
        } else {
            return getSwahiliSentence(clothes);
        }
    }

    public static String getCharacterShortsAudioFileName(String clothes) {
        return getAudioFileName(getCharacterShortsEnglishMessage(clothes));
    }

    private static String getCharacterShortsEnglishMessage(String clothes) {
        return "I can see my " + clothes + ", too.";
    }

    public static String getCharacterShortsMessage(String clothes) {
        if (UserPreferenceUtils.getInstance().isEnglish()) {
            return getCharacterShortsEnglishMessage(clothes);
        } else {
            return getSwahiliSentence(clothes);
        }
    }

}
