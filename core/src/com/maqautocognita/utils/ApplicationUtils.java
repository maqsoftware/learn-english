package com.maqautocognita.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ApplicationUtils {

    public static boolean isDesktop() {
        return Application.ApplicationType.Desktop.equals(Gdx.app.getType());
    }

    public static boolean isImageRequired(String activityUnitCode) {
        return !(UserPreferenceUtils.getInstance().isSwahili() && "u3".equalsIgnoreCase(activityUnitCode));
    }
}
