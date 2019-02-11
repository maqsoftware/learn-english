package com.maqautocognita.adapter;


import com.maqautocognita.bo.OCRResult;
import com.maqautocognita.scene2d.actions.IAdvanceActionListener;

import java.util.ArrayList;

/**
 * Created by kotarou on 7/2/2017.
 */
public interface IOCR {
    public ArrayList<int[]> getOCRRect(String pImage);

    void processImage(String imageFilePath, IAdvanceActionListener<OCRResult> actionListener);

    public ArrayList<String> processImage(String pImageFilePath, int left, int top, int width, int height);
}
