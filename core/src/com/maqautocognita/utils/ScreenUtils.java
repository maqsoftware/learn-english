package com.maqautocognita.utils;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.Config;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ScreenUtils {

    public static Rectangle viewport;
    public static boolean isLandscapeMode;
    public static boolean isTablet;
    public static float heightRatio;
    public static float widthRatio;
    public static int miniSDKVersion;


    public static void setLandscapeMode() {
        isLandscapeMode = true;
        if (null != AutoCognitaGame.deviceService) {
            AutoCognitaGame.deviceService.setScreenOrientationToLandscape();
        }
    }

    public static void setPortraitMode() {
        isLandscapeMode = false;
        if (null != AutoCognitaGame.deviceService) {
            AutoCognitaGame.deviceService.setScreenOrientationToPortrait();
        }
    }

    public static void setViewport(Rectangle viewport1) {

        viewport = viewport1;

        heightRatio = (float) getPreferredScreenHeight() / viewport.height;

        widthRatio = (float) getPreferredScreenWidth() / viewport.width;
    }

    private static int getPreferredScreenHeight() {
        return isLandscapeMode ? Config.TABLET_SCREEN_HEIGHT : Config.MOBILE_SCREEN_HEIGHT;
    }

    private static int getPreferredScreenWidth() {
        return isLandscapeMode ? Config.TABLET_SCREEN_WIDTH : Config.MOBILE_SCREEN_WIDTH;
    }

    public static float getExactYPositionOnScreen(float systemDetectYPosition) {
        return getScreenHeight() - systemDetectYPosition * heightRatio + viewport.y * heightRatio;
    }

    public static int getScreenHeight() {
        return getPreferredScreenHeight();
    }

    public static float getExactYPositionOnScreenStartFromBottom(float systemDetectYPosition) {
        return systemDetectYPosition * heightRatio + viewport.y * heightRatio;
    }

    public static float toViewPosition(float systemDetectXPosition) {
        return systemDetectXPosition * widthRatio - viewport.x * widthRatio;
    }

    public static float toScreenPosition(float value) {
        return value / widthRatio + viewport.x / widthRatio;
    }

    public static float getSceneRatio() {
        return Math.min(0.5f, (float) Gdx.graphics.getHeight() / getScreenHeight());
    }

    public static float getXPositionForCenterObject(float objectWidth) {
        return getXPositionForCenterObject(objectWidth, getScreenWidth());
    }

    public static float getXPositionForCenterObject(float objectWidth, float targetWidth) {
        return (targetWidth - objectWidth) / 2;
    }


    public static int getScreenWidth() {
        return getPreferredScreenWidth();
    }

    public static boolean isSmallResolution() {
        return Gdx.graphics.getWidth() < 1000;
    }

    public static float getStartYPositionForCenterObject(float objectHeight) {
        return getBottomYPositionForCenterObject(objectHeight, getScreenHeight()) + objectHeight;
    }

    public static float getBottomYPositionForCenterObject(float objectHeight, float targetAreaHeight) {
        return (targetAreaHeight - objectHeight) / 2;
    }

    public static float getStartYPositionForCenterObject(float objectHeight, float targetHeight) {
        return getBottomYPositionForCenterObject(objectHeight, targetHeight) + objectHeight;
    }

    public static float getStartYPositionForCenterObjectWithoutNavigationBar(float objectHeight) {
        return getBottomYPositionForCenterObject(objectHeight, getScreenHeightWithoutNavigationBar()) + objectHeight;
    }

    public static int getScreenHeightWithoutNavigationBar() {
        return ScreenUtils.getScreenHeight() - getNavigationBarHeight();
    }

    public static int getNavigationBarHeight() {
        return ScreenUtils.getScreenHeight() - getNavigationBarStartYPosition();
    }

    public static int getNavigationBarStartYPosition() {
        return ScreenUtils.getScreenHeight() - 150;
    }

    public static float getStartYPositionForCenterObjectWithoutNavigationBar(float objectHeight, float targetHeight) {
        return getBottomYPositionForCenterObject(objectHeight, targetHeight) + objectHeight;
    }

    /**
     * Get the y position for the object which is required to render in the vertical center of the screen.
     * The return y position is expected that the object will be render from bottom to top
     *
     * @param objectHeight
     * @return
     */
    public static float getBottomYPositionForCenterObject(float objectHeight) {
        return (getScreenHeight() - objectHeight) / 2;

    }

    public static int getNavigationBarWidth() {
        return getNavigationRightArrowStartXPosition() - getNavigationBarStartXPosition();
    }

    public static int getNavigationRightArrowStartXPosition() {
        return ScreenUtils.getScreenWidth() - 100;
    }

    public static int getNavigationBarStartXPosition() {
        return ScreenUtils.isLandscapeMode ? Config.SCREEN_CENTER_START_X_POSITION : 130;
    }


}
