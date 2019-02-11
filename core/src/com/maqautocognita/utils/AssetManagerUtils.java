package com.maqautocognita.utils;

import com.maqautocognita.Config;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class AssetManagerUtils {

    public static AssetManager assetManager = new AssetManager();

    public static String GENERAL_ICONS_FOR_NINE_PATCH = Config.COMMON_IMAGE_HDPI_PATH + "General Icons.png";

    public static String NUMBER_PAD = Config.COMMON_IMAGE_HDPI_PATH + "NumberPad.png";

    public static String GENERAL_ICONS = Config.COMMON_IMAGE_HDPI_PATH + "General Icons.png";

    public static String NUMBER_PAD_GREEN = Config.COMMON_IMAGE_HDPI_PATH + "NumberPad Green.png";

    public static String PROGRESS_MAP = Config.COMMON_IMAGE_HDPI_PATH + "progress_map.png";

    public static String NUMBER_1_BLOCK = Config.COMMON_IMAGE_HDPI_PATH + "number1Block.png";
    public static String NUMBER_10_BLOCK = Config.COMMON_IMAGE_HDPI_PATH + "number10Block.png";
    public static String NUMBER_100_BLOCK = Config.COMMON_IMAGE_HDPI_PATH + "number100Block.png";

    public static String ICONS = Config.COMMON_IMAGE_HDPI_PATH + "icons.png";

    public static String GREY_48_UC_LETTER = Config.COMMON_IMAGE_HDPI_PATH + "Grey 48 UC Letter.png";
    public static String GREY_48_LC_LETTER = Config.COMMON_IMAGE_HDPI_PATH + "Grey 48 LC Letter.png";

    public static String RED_48_UC_LETTER = Config.COMMON_IMAGE_HDPI_PATH + "Red 48 UC Letter.png";
    public static String RED_48_LC_LETTER = Config.COMMON_IMAGE_HDPI_PATH + "Red 48 LC Letter.png";

    public static String MICROPHONE = Config.COMMON_IMAGE_HDPI_PATH + "Microphone Icon.png";

    public static String SMALL_MICROPHONE = Config.COMMON_IMAGE_HDPI_PATH + "Small Microphone Icon.png";

    public static String CORRECT_FRAME = Config.COMMON_IMAGE_HDPI_PATH + "Correct Answer Frame.png";

    public static String WRONG_FRAME = Config.COMMON_IMAGE_HDPI_PATH + "Wrong Answer Frame.png";

    public static String GENERAL_KEYS = Config.COMMON_IMAGE_HDPI_PATH + "General Keys.png";

    public static String RED_48_NUMBER = Config.COMMON_IMAGE_HDPI_PATH + "Red 48 Number.png";


    public static String SMALL_BLOCK = Config.COMMON_IMAGE_HDPI_PATH + "Small Block.png";

    public static String NUMBER_TRAY = Config.COMMON_IMAGE_HDPI_PATH + "Number Tray.png";

    public static String NUMBER_BLOCKS_HORIZONTAL = Config.COMMON_IMAGE_HDPI_PATH + "Number Blocks Horizontal.png";

    public static String NUMBER_BLOCKS_VERTICAL = Config.COMMON_IMAGE_HDPI_PATH + "Number Blocks Vertical.png";

    public static String COUNTING_WHITE_BLOCKS = Config.COMMON_IMAGE_HDPI_PATH + "Counting White Blocks.png";
    public static String COUNTING_WHITE_BLOCKS_HIGHLIGHTED = Config.COMMON_IMAGE_HDPI_PATH + "Counting White Blocks Highlighted.png";

    public static String STORY_MODE_ICON_IMAGE_PATH = Config.STORY_HDPI_IMAGE_FOLDER_NAME + "/icons.png";
    public static String STORY_MISSION_INVENTORY_BOX_IMAGE_PATH = Config.STORY_HDPI_IMAGE_FOLDER_NAME + "/inventory_box.png";
    /**
     * This is used for login module
     */
    public static String MENU_ICONS_FOR_MOBILE = Config.COMMON_IMAGE_HDPI_PATH + "menu_icon.png";
    //public static String MENU_ICONS_FOR_TABLET = Config.COMMON_IMAGE_XDPI_PATH + "menu_icon.png";
    public static String MENU_ICONS_FOR_TABLET = Config.COMMON_IMAGE_XDPI_PATH + "menu_icon_custom.png";

    public static String LANGUAGE_MENU_ICONS = Config.COMMON_IMAGE_HDPI_PATH + "language_menu_icon.png";

    public static String PHONICS_KEYBOARD = Config.COMMON_IMAGE_HDPI_PATH + "Phonics Keyboard.png";
    public static String PHONICS_KEYBOARD_HIGHLIGHTED = Config.COMMON_IMAGE_HDPI_PATH + "Phonics Keyboard Highlighted.png";
    public static String PHONICS_KEYBOARD_DISABLED = Config.COMMON_IMAGE_HDPI_PATH + "Phonics Keyboard Disabled.png";

    public static String MOBILE_PHONICS_KEYBOARD = Config.COMMON_IMAGE_HDPI_PATH + "mobile_phonics_keyboard.png";
    public static String MOBILE_PHONICS_KEYBOARD_HIGHLIGHTED = Config.COMMON_IMAGE_HDPI_PATH + "mobile_phonics_keyboard_highlighted.png";
    public static String MOBILE_PHONICS_KEYBOARD_DISABLED = Config.COMMON_IMAGE_HDPI_PATH + "mobile_phonics_keyboard_disabled.png";

    public static String SWAHILI_PHONICS_KEYBOARD = Config.COMMON_IMAGE_HDPI_PATH + "Swahili Phonics Keyboard.png";
    public static String SWAHILI_PHONICS_KEYBOARD_HIGHLIGHTED = Config.COMMON_IMAGE_HDPI_PATH + "Swahili Phonics Keyboard Highlighted.png";
    public static String SWAHILI_PHONICS_KEYBOARD_DISABLED = Config.COMMON_IMAGE_HDPI_PATH + "Swahili Phonics Keyboard Disabled.png";

    public static String NUMBER_KEYBOARD = Config.COMMON_IMAGE_HDPI_PATH + "Number Keyboard.png";

    public static String NUMBER_KEYBOARD_HIGHLIGHTED = Config.COMMON_IMAGE_HDPI_PATH + "Number Keyboard Highlighted.png";

    public static String PHONICS_SYMBOL = Config.COMMON_IMAGE_HDPI_PATH + "Phonics Symbol.png";

    public static String ICON_READING = Config.COMMON_IMAGE_XDPI_PATH + "reading.png";
    public static String ICON_MATH = Config.COMMON_IMAGE_XDPI_PATH + "math.png";
    public static String ICON_STORY = Config.COMMON_IMAGE_XDPI_PATH + "story.png";
    public static String ICON_LIFE_SKILL = Config.COMMON_IMAGE_XDPI_PATH + "life_skill.png";
    public static String ICON_SAY_SOMETHING = Config.COMMON_IMAGE_XDPI_PATH + "chat.png";
    public static String ICON_LOGO = Config.COMMON_IMAGE_XDPI_PATH + "logo.png";
    public static String ICON_STORY_MISSION = Config.COMMON_IMAGE_HDPI_PATH + "story_mission.png";
    public static String ICON_STORY_MODE = Config.COMMON_IMAGE_HDPI_PATH + "story_mode.png";
    public static String ICON_SUB_STORY = Config.COMMON_IMAGE_HDPI_PATH + "story_sub_icon.png";

    public static String ICON_SENTENCE_1 = Config.COMMON_IMAGE_HDPI_PATH + "sentence_1.png";
    public static String ICON_SENTENCE_2 = Config.COMMON_IMAGE_HDPI_PATH + "sentence_2.png";
    public static String ICON_SENTENCE_3 = Config.COMMON_IMAGE_HDPI_PATH + "sentence_3.png";
    public static String ICON_SENTENCE_4 = Config.COMMON_IMAGE_HDPI_PATH + "sentence_4.png";

    public static String ICON_COMPREHENSION_1 = Config.COMMON_IMAGE_HDPI_PATH + "comprehension_1.png";
    public static String ICON_COMPREHENSION_2 = Config.COMMON_IMAGE_HDPI_PATH + "comprehension_2.png";
    public static String ICON_COMPREHENSION_3 = Config.COMMON_IMAGE_HDPI_PATH + "comprehension_3.png";

    public static String ICON_STORY_1 = Config.COMMON_IMAGE_HDPI_PATH + "story_1.png";
    public static String ICON_STORY_2 = Config.COMMON_IMAGE_HDPI_PATH + "story_2.png";

    public static String ICON_PERSON = Config.COMMON_IMAGE_HDPI_PATH + "person.png";

    public static String ICON_INFO = Config.COMMON_IMAGE_HDPI_PATH + "info.png";
    public static String ICON_SNAP = Config.COMMON_IMAGE_HDPI_PATH + "snap.png";
    public static String ICON_TALK = Config.COMMON_IMAGE_HDPI_PATH + "talk.png";
    public static String ICON_SHARE = Config.COMMON_IMAGE_HDPI_PATH + "share.png";
    public static String ICON_DEMO = Config.COMMON_IMAGE_HDPI_PATH + "demo.png";
    private static List<String> loadedImagePathList;

    public static String getMenuIcon() {
        //return ScreenUtils.isTablet ? AssetManagerUtils.MENU_ICONS_FOR_TABLET : AssetManagerUtils.MENU_ICONS_FOR_MOBILE;
        return AssetManagerUtils.MENU_ICONS_FOR_TABLET;
    }

    public static void loadTexture(String image) {
        if (Gdx.files.internal(image).exists()) {
            if (!assetManager.isLoaded(image, Texture.class)) {
                TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
                param.minFilter = Texture.TextureFilter.Linear;
                param.magFilter = Texture.TextureFilter.Linear;

                // textureLoader.loadAsync(assetManager,image,);
                assetManager.load(image, Texture.class, param);
            }
        } else {
            Gdx.app.log("", image + " is missing");
        }
    }

    public static Texture getTexture(String image) {
        if (StringUtils.isNotBlank(image) && assetManager.isLoaded(image, Texture.class)) {
            return assetManager.get(image);
        }
        return null;
    }

    public static Texture getTextureWithWait(String image) {
        if (StringUtils.isNotBlank(image)) {
            if (assetManager.isLoaded(image, Texture.class)) {
                return assetManager.get(image);
            } else {
                addImage(image);
                loadTexture(image);
                blockUtilsFinishLoading();
                return getTextureWithWait(image);
            }
        }

        return null;
    }

    private static void addImage(String image) {
        if (null == loadedImagePathList) {
            loadedImagePathList = new ArrayList<String>();
        }
        if (!loadedImagePathList.contains(image)) {
            loadedImagePathList.add(image);
        }
    }

    public static void unloadAllTexture() {
        if (CollectionUtils.isNotEmpty(loadedImagePathList)) {
            for (String imagePath : loadedImagePathList) {
                unloadTexture(imagePath);
            }
            loadedImagePathList.clear();
        }
    }

    public static void unloadTexture(String image) {
        if (isImageLoaded(image)) {
            //getTexture(image).dispose();
            assetManager.unload(image);
        }
    }

    public static boolean isImageLoaded(String image) {
        return assetManager.isLoaded(image);
    }

    public static void dispose() {
        assetManager.dispose();
        assetManager = new AssetManager();
    }

    public static void clear() {
        assetManager.clear();
    }

    public static boolean isFinishLoading() {
        return assetManager.update();
    }

    public static void blockUtilsFinishLoading() {
        assetManager.finishLoading();
    }
}
