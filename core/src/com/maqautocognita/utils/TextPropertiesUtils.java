package com.maqautocognita.utils;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class TextPropertiesUtils {

    public static String getMathAddition() {
        return isEnglish() ? (ScreenUtils.isLandscapeMode ? English.MATH_ADDITION : MOBILE_ENGLISH.MATH_ADDITION) : Swahili.MATH_ADDITION;
    }

    private static boolean isEnglish() {
        return UserPreferenceUtils.getInstance().isEnglish();
    }

    public static String getMathCount() {
        return isEnglish() ? English.MATH_COUNT : Swahili.MATH_COUNT;
    }

    public static String getMathPlaceValue() {
        return isEnglish() ? (ScreenUtils.isLandscapeMode ? English.MATH_LARGE_NUMBER : MOBILE_ENGLISH.MATH_LARGE_NUMBER) : Swahili.MATH_LARGE_NUMBER;
    }

    public static String getMathMultiplication() {
        return isEnglish() ? English.MATH_MULTIPLICATION : Swahili.MATH_MULTIPLICATION;
    }

    public static String getMathOtherTopics() {
        return isEnglish() ? (ScreenUtils.isLandscapeMode ? English.MATH_OTHER_TOPICS : MOBILE_ENGLISH.MATH_OTHER_TOPICS) : Swahili.MATH_OTHER_TOPICS;
    }

    public static String getAlphabet() {
        return isEnglish() ? English.ALPHABET : Swahili.ALPHABET;
    }


    public static String getSentence1() {
        return isEnglish() ? English.SENTENCE_1 : Swahili.SENTENCE_1;
    }

    public static String getSentence2() {
        return isEnglish() ? English.SENTENCE_2 : Swahili.SENTENCE_2;
    }

    public static String getSentence3() {
        return isEnglish() ? English.SENTENCE_3 : Swahili.SENTENCE_3;
    }

    public static String getSentence4() {
        return isEnglish() ? English.SENTENCE_4 : Swahili.SENTENCE_4;
    }

    public static String getSentence5() {
        return isEnglish() ? English.SENTENCE_5 : Swahili.SENTENCE_5;
    }

    public static String getPhonics1() {
        return isEnglish() ? (ScreenUtils.isLandscapeMode ? English.PHONICS1 : MOBILE_ENGLISH.PHONICS1) : Swahili.PHONICS1;
    }

    public static String getPhonics2() {
        return isEnglish() ? (ScreenUtils.isLandscapeMode ? English.PHONICS2 : MOBILE_ENGLISH.PHONICS2) : Swahili.PHONICS2;
    }

    public static String getPhonics3() {
        return isEnglish() ? (ScreenUtils.isLandscapeMode ? English.PHONICS3 : MOBILE_ENGLISH.PHONICS3) : Swahili.PHONICS3;
    }

    public static String getPhonics4() {
        return isEnglish() ? (ScreenUtils.isLandscapeMode ? English.PHONICS4 : MOBILE_ENGLISH.PHONICS4) : Swahili.PHONICS4;
    }

    public static String getLibrary() {
        return isEnglish() ? English.LIBRARY : Swahili.LIBRARY;
    }

    public static String getSchool() {
        return isEnglish() ? English.SCHOOL : Swahili.SCHOOL;
    }

    public static String getHome() {
        return isEnglish() ? English.HOME : Swahili.HOME;
    }

    public static String getVillage() {
        return isEnglish() ? English.VILLAGE : Swahili.VILLAGE;
    }

    public static String getMarket() {
        return isEnglish() ? English.MARKET : Swahili.MARKET;
    }

    public static String getCafe() {
        return isEnglish() ? English.CAFE : Swahili.CAFE;
    }

    public static String getStore() {
        return isEnglish() ? English.STORE : Swahili.STORE;
    }

    public static String getCity() {
        return isEnglish() ? English.CITY : Swahili.CITY;
    }

    public static String getTitleReading() {
        return isEnglish() ? English.TITLE_READING : Swahili.TITLE_READING;
    }

    public static String getTitleMath() {
        return isEnglish() ? English.TITLE_MATH : Swahili.TITLE_MATH;
    }

    public static String getTitleAlphabet() {
        return isEnglish() ? English.TITLE_ALPHABET : Swahili.TITLE_ALPHABET;
    }

    public static String getTitleBasicPhonics() {
        return isEnglish() ? English.TITLE_BASIC_PHONICS : Swahili.TITLE_BASIC_PHONICS;
    }

    public static String getTitleDigraph() {
        return isEnglish() ? English.TITLE_DIGRAPH : Swahili.TITLE_DIGRAPH;
    }

    public static String getTitleLongVowel() {
        return isEnglish() ? English.TITLE_LONG_VOWEL : Swahili.TITLE_LONG_VOWEL;
    }

    public static String getTitleActivities() {
        return isEnglish() ? English.TITLE_ACTIVITIES : Swahili.TITLE_ACTIVITIES;
    }

    public static String getTitleReadAndListen() {
        return isEnglish() ? English.TITLE_READ_AND_LISTEN : Swahili.TITLE_READ_AND_LISTEN;
    }

    public static String getTitleSpeak() {
        return isEnglish() ? English.TITLE_SPEAK : Swahili.TITLE_SPEAK;
    }

    public static String getTitleWordBlend() {
        return isEnglish() ? English.TITLE_WORD_BLEND : Swahili.TITLE_WORD_BLEND;
    }

    public static String getTitleListenType() {
        return isEnglish() ? English.TITLE_LISTEN_TYPE : Swahili.TITLE_LISTEN_TYPE;
    }

    public static String getTitleGroup() {
        return isEnglish() ? English.TITLE_GROUP : Swahili.TITLE_GROUP;
    }

    public static String getTitleFillInBlanks() {
        return isEnglish() ? English.TITLE_FILL_IN_BLANKS : Swahili.TITLE_FILL_IN_BLANKS;
    }

    public static String getTitleMatch() {
        return isEnglish() ? English.TITLE_MATCH : Swahili.TITLE_MATCH;
    }

    public static String getTitleListenWrite() {
        return isEnglish() ? English.TITLE_LISTEN_WRITE : Swahili.TITLE_LISTEN_WRITE;
    }

    public static String getTitleTopic() {
        return isEnglish() ? English.TITLE_TOPIC : Swahili.TITLE_TOPIC;
    }

    public static String getCircle() {
        return isEnglish() ? English.CIRCLE : Swahili.CIRCLE;
    }

    public static String getTriangle() {
        return isEnglish() ? English.TRIANGLE : Swahili.TRIANGLE;
    }

    public static String getRectangle() {
        return isEnglish() ? English.RECTANGLE : Swahili.RECTANGLE;
    }

    public static String getTitleCount() {
        return isEnglish() ? English.TITLE_COUNT : Swahili.TITLE_COUNT;
    }

    public static String getTitleMultiply() {
        return isEnglish() ? English.TITLE_MULTIPLY : Swahili.TITLE_MULTIPLY;
    }

    public static String getTitlePlaceValue() {
        return isEnglish() ? English.TITLE_PLACE_VALUE : Swahili.TITLE_PLACE_VALUE;
    }

    public static String getTitleNumberPattern() {
        return isEnglish() ? English.TITLE_NUMBER_PATTERN : Swahili.TITLE_NUMBER_PATTERN;
    }

    public static String getTitleShape() {
        return isEnglish() ? English.TITLE_SHAPE : Swahili.TITLE_SHAPE;
    }

    public static String getTitleCompare() {
        return isEnglish() ? English.TITLE_COMPARE : Swahili.TITLE_COMPARE;
    }

    public static String getTitleAdd() {
        return isEnglish() ? English.TITLE_ADD : Swahili.TITLE_ADD;
    }

    public static String getTitleSubtract() {
        return isEnglish() ? English.TITLE_SUBTRACT : Swahili.TITLE_SUBTRACT;
    }

    public static String getTitleClick() {
        return isEnglish() ? English.TITLE_CLICK : Swahili.TITLE_CLICK;
    }

    public static String getTitleType() {
        return isEnglish() ? English.TITLE_TYPE : Swahili.TITLE_TYPE;
    }

    public static String getTitleWrite() {
        return isEnglish() ? English.TITLE_WRITE : Swahili.TITLE_WRITE;
    }

    public static String getTitleMakePicture() {
        return isEnglish() ? English.TITLE_MAKE_PICTURE : Swahili.TITLE_MAKE_PICTURE;
    }

    public static String getTitleColorBlocks() {
        return isEnglish() ? English.TITLE_COLOR_BLOCKS : Swahili.TITLE_COLOR_BLOCKS;
    }

    public static String getTitleLongForm() {
        return isEnglish() ? English.TITLE_LONG_FORM : Swahili.TITLE_LONG_FORM;
    }

    public static String getStoryMode() {
        return isEnglish() ? English.STORY : Swahili.STORY;
    }

    public static String getStoryMission() {
        return isEnglish() ? English.MISSION : Swahili.MISSION;
    }

    private final class English {
        static final String MATH_ADDITION = "add / subtract";
        static final String MATH_COUNT = "counting";
        static final String MATH_LARGE_NUMBER = "place value";
        static final String MATH_MULTIPLICATION = "multiply";
        static final String MATH_OTHER_TOPICS = "other math";
        static final String ALPHABET = "alphabet";
        static final String PHONICS1 = "phonics 1";
        static final String PHONICS2 = "phonics 2";
        static final String PHONICS3 = "phonics 3";
        static final String PHONICS4 = "phonics 4";
        static final String LIBRARY = "library";
        static final String SCHOOL = "school";
        static final String HOME = "home";
        static final String VILLAGE = "village";
        static final String MARKET = "market";
        static final String CAFE = "cafe";
        static final String STORE = "store";
        static final String CITY = "city/country";


        static final String SENTENCE_1 = "word /\n sentence 1";
        static final String SENTENCE_2 = "word /\n sentence 2";
        static final String SENTENCE_3 = "word /\n sentence 3";
        static final String SENTENCE_4 = "word /\n sentence 4";
        static final String SENTENCE_5 = "word /\n sentence 5";

        static final String STORY = "vocabulary";
        static final String MISSION = "comprehension";

        static final String TITLE_READING = "READING";
        static final String TITLE_TOPIC = "TOPICS";
        static final String TITLE_ALPHABET = "Alphabet";
        static final String TITLE_BASIC_PHONICS = "Basic Phonics";
        static final String TITLE_DIGRAPH = "Digraph";
        static final String TITLE_LONG_VOWEL = "Long Vowel";
        static final String TITLE_ACTIVITIES = "ACTIVITIES";
        static final String TITLE_READ_AND_LISTEN = "Read/Listen";
        static final String TITLE_WORD_BLEND = "Word Blend";
        static final String TITLE_LISTEN_TYPE = "Listen Type";
        static final String TITLE_GROUP = "Group";
        static final String TITLE_FILL_IN_BLANKS = "Fill in Blanks";
        static final String TITLE_LISTEN_WRITE = "Listen Write";
        static final String CIRCLE = "Circle";
        static final String TRIANGLE = "Triangle";
        static final String RECTANGLE = "Rectangle";

        static final String STORY_MODE = "my story";

        static final String TITLE_MATH = "Math";
        static final String TITLE_COUNT = "Count";
        static final String TITLE_COMPARE = "Compare";
        static final String TITLE_ADD = "Add";
        static final String TITLE_SUBTRACT = "Subtract";

        static final String TITLE_MULTIPLY = "Multiply";
        static final String TITLE_PLACE_VALUE = "Place Value";
        static final String TITLE_NUMBER_PATTERN = "Number Pattern";
        static final String TITLE_SHAPE = "Shape";

        static final String TITLE_CLICK = "Click";
        static final String TITLE_TYPE = "Type";
        static final String TITLE_WRITE = "Write";
        static final String TITLE_SPEAK = "Speak";
        static final String TITLE_MAKE_PICTURE = "Make Picture";
        static final String TITLE_MATCH = "Match";
        static final String TITLE_COLOR_BLOCKS = "Color Blocks";
        static final String TITLE_LONG_FORM = "Long Form";
    }

    private final class MOBILE_ENGLISH {
        static final String PHONICS1 = "phonics 1";
        static final String PHONICS2 = "phonics 2";
        static final String PHONICS3 = "phonics 3";
        static final String PHONICS4 = "phonics 4";
        static final String MATH_OTHER_TOPICS = "other topics";
        static final String MATH_ADDITION = "add subtract";
        static final String MATH_LARGE_NUMBER = "large numbers";
    }

    private final class Swahili {
        static final String MATH_ADDITION = "kujumlisha na kutoa";
        static final String MATH_COUNT = "hesabu";
        static final String MATH_LARGE_NUMBER = "idadi kubwa";
        static final String MATH_MULTIPLICATION = "kuzidisha";
        static final String MATH_OTHER_TOPICS = "mada nyingine";
        static final String ALPHABET = "alfabeti";
        static final String PHONICS1 = "fonetiki 1";
        static final String PHONICS2 = "fonetiki 2";
        static final String PHONICS3 = "fonetiki 3";
        static final String PHONICS4 = "fonetiki 4";

        static final String SENTENCE_1 = "maneno na hukumu 1";
        static final String SENTENCE_2 = "maneno na hukumu 2";
        static final String SENTENCE_3 = "maneno na hukumu 3";
        static final String SENTENCE_4 = "maneno na hukumu 4";
        static final String SENTENCE_5 = "maneno na hukumu 5";


        static final String LIBRARY = "maktaba";
        static final String SCHOOL = "shule";
        static final String HOME = "nyumbani";
        static final String VILLAGE = "kijiji";
        static final String MARKET = "soko";
        static final String CAFE = "mkahawa";
        static final String STORE = "duka";
        static final String CITY = "mji/nchi";

        static final String STORY = "msamiati";
        static final String MISSION = "ufahamu";

        static final String TITLE_READING = "KUSOMA";
        static final String TITLE_TOPIC = "MADA";
        static final String TITLE_ALPHABET = "Alfabeti";
        static final String TITLE_BASIC_PHONICS = "Foniki 1";
        static final String TITLE_DIGRAPH = "Foniki 2";
        static final String TITLE_LONG_VOWEL = "Foniki 3";
        static final String TITLE_ACTIVITIES = "SHUGHULI";
        static final String TITLE_READ_AND_LISTEN = "Soma/Sikiliza";
        static final String TITLE_WORD_BLEND = "Mchanganyiko";
        static final String TITLE_LISTEN_TYPE = "Sikiliza PigaChapa";
        static final String TITLE_GROUP = "Kikundi";
        static final String TITLE_FILL_IN_BLANKS = "Jaza Mapengo";
        static final String TITLE_LISTEN_WRITE = "Sikiliza Andika";

        static final String CIRCLE = "Mduara";
        static final String TRIANGLE = "Pembe tatu";
        static final String RECTANGLE = "Mstatili";

        static final String TITLE_MATH = "HISABATI";
        static final String TITLE_COUNT = "Hesabu";
        static final String TITLE_COMPARE = "Linganisha";
        static final String TITLE_ADD = "Jumlisha";
        static final String TITLE_SUBTRACT = "Toa";
        static final String TITLE_MULTIPLY = "Zidisha";
        static final String TITLE_PLACE_VALUE = "ldadi Kubwa";
        static final String TITLE_NUMBER_PATTERN = "Mfumo wa Nambari";
        static final String TITLE_SHAPE = "Umbo";
        static final String TITLE_CLICK = "Bonyeza";
        static final String TITLE_TYPE = "Piga Chapa";
        static final String TITLE_WRITE = "Andika";
        static final String TITLE_SPEAK = "Ongea";
        static final String TITLE_MAKE_PICTURE = "Tengenza Picha";
        static final String TITLE_MATCH = "Unganisha";
        static final String TITLE_COLOR_BLOCKS = "Bloku za Rangi";
        static final String TITLE_LONG_FORM = "Njia Ndefu";
    }


}
