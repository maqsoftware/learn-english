package com.maqautocognita.adapter;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public interface IDeviceService {

    void setScreenOrientationToLandscape();

    void setScreenOrientationToPortrait();

    /**
     * @return the current user location , index[0] = Latitude,index [1] = Longitude
     */
    double[] getUserCurrentLocation();

    boolean isStoryModeEnable();

    String getVersionName();

    boolean isSpanishLocale();
}
