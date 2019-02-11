package com.maqautocognita.adapter;


import com.maqautocognita.scene2d.actions.IAdvanceActionListener;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface IDeviceCameraService {

    void stopPreview();

    void takePicture(float heightRatioFromPictureTop, String savePictureFullPath, final IAdvanceActionListener<Boolean> actionListener, int compressQuality);

    void takePictureFromTablet(final float widthRatio, final String savePictureFullPath,
                               final IAdvanceActionListener<Boolean> actionListener, final int compressQuality);

    // Asynchronous interface - need when called from a non platform thread (GDX OpenGl thread)
    void startPreviewAsync();

    boolean isReady();
}
