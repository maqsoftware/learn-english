package com.maqautocognita.utils;

import com.maqautocognita.prototype.storyMode.StoryModeScene;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class StoryModeBackgroundMusicFileNameUtils {

    public static final String CHARACTER_SCREEN = "music1.wav";

    public static final String CITY_SCREEN = "music3.wav";

    public static final String HOME_SCREEN = "music1.wav";

    public static final String LIBRARY_SCREEN = "music2.wav";

    public static final String MARKET_SCREEN = "music3.wav";

    public static final String SCHOOL_SCREEN = "music2.wav";

    public static final String STORE_SCREEN = "music1.wav";

    public static final String VILLAGE_SCREEN = "music2.wav";

    public static final String CAFE_SCREEN = "music3.wav";

    public static String getMusicFileName(StoryModeScene storyModeScene) {
        if (null != storyModeScene) {
            if (StoryModeSceneNameUtils.HOME_SCREEN.equals(storyModeScene.getSceneName())) {
                return StoryModeBackgroundMusicFileNameUtils.HOME_SCREEN;
            } else if (StoryModeSceneNameUtils.CAFE_SCREEN.equals(storyModeScene.getSceneName())) {
                return StoryModeBackgroundMusicFileNameUtils.CAFE_SCREEN;
            } else if (StoryModeSceneNameUtils.CITY_SCREEN.equals(storyModeScene.getSceneName())) {
                return StoryModeBackgroundMusicFileNameUtils.CITY_SCREEN;
            } else if (StoryModeSceneNameUtils.LIBRARY_SCREEN.equals(storyModeScene.getSceneName())) {
                return StoryModeBackgroundMusicFileNameUtils.LIBRARY_SCREEN;
            } else if (StoryModeSceneNameUtils.MARKET_SCREEN.equals(storyModeScene.getSceneName())) {
                return StoryModeBackgroundMusicFileNameUtils.MARKET_SCREEN;
            } else if (StoryModeSceneNameUtils.SCHOOL_SCREEN.equals(storyModeScene.getSceneName())) {
                return StoryModeBackgroundMusicFileNameUtils.SCHOOL_SCREEN;
            } else if (StoryModeSceneNameUtils.STORE_SCREEN.equals(storyModeScene.getSceneName())) {
                return StoryModeBackgroundMusicFileNameUtils.STORE_SCREEN;
            } else if (StoryModeSceneNameUtils.VILLAGE_SCREEN.equals(storyModeScene.getSceneName())) {
                return StoryModeBackgroundMusicFileNameUtils.VILLAGE_SCREEN;
            }
        }

        return null;
    }
}
