package com.maqautocognita;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class Config {

    //the expected virtual size of the screen
    public static int TABLET_SCREEN_WIDTH = 1920;
    public static int TABLET_SCREEN_HEIGHT = 1200;

    public static int MOBILE_SCREEN_WIDTH = 1080;
    public static int MOBILE_SCREEN_HEIGHT = 1794;

    public static float DEFAULT_IMAGE_SIZE_TIMES = 1f;

    /**
     * Store the start x position of the center area which is contains the main content on the screen
     */
    public static int SCREEN_CENTER_START_X_POSITION = 200;

    /**
     * The start y position for the sound which displayed in center screen for the read and listen module and single sound speaking module
     */
    public static int SPEAKING_SOUND_START_Y_POSITION = 570;

    /**
     * Store the width of the center area
     */
    public static int SCREEN_CENTER_WIDTH = TABLET_SCREEN_WIDTH - SCREEN_CENTER_START_X_POSITION * 2;

    public static String IMAGE_FOLDER_NAME = "images/";
    
    /**
     * the folder name in the assets folder in android side which used to store all image which will show in the lesson section
     */
    public static String LESSON_IMAGE_FOLDER_NAME = IMAGE_FOLDER_NAME + "lesson";

    /**
     * the folder name in the assets folder in android side which used to store allimage which will show in the story mode
     */
    public static String STORY_HDPI_IMAGE_FOLDER_NAME = IMAGE_FOLDER_NAME + "story/hdpi";

    public static String STORY_MDPI_IMAGE_FOLDER_NAME = IMAGE_FOLDER_NAME + "story/mdpi";

    public static String HAIR_STYLE_IMAGE_FOLDER_NAME = IMAGE_FOLDER_NAME + "story/hairstyle";

    public static String CLOTHES_IMAGE_FOLDER_NAME = IMAGE_FOLDER_NAME + "story/clothing";

    public static String COMMON_IMAGE_XDPI_PATH = IMAGE_FOLDER_NAME + "common/xdpi/";

    public static String COMMON_IMAGE_HDPI_PATH = IMAGE_FOLDER_NAME + "common/hdpi/";

    /**
     * the folder name in the assets folder in android side which used to store the story mode image for math
     */
    public static String MATH_STORY_IMAGE_FOLDER_NAME = IMAGE_FOLDER_NAME + "math";

    public static String CHEAT_SHEET_IMAGE_PATH = IMAGE_FOLDER_NAME + "cheat_sheet/";

    public static String SENTENCE_IMAGE_PATH = IMAGE_FOLDER_NAME + "sentence/";

    public static String CONVERSATION_IMAGE_PATH = IMAGE_FOLDER_NAME + "conversation/";

}
