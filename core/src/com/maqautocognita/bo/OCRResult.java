package com.maqautocognita.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class OCRResult {

    public int imageWidth;

    public int imageHeight;

    private List<OCRRecognizedObject> OCRRecognizedObjectList;

    public void addRecognizedObject(OCRRecognizedObject OCRRecognizedObject) {
        if (null == OCRRecognizedObjectList) {
            OCRRecognizedObjectList = new ArrayList<OCRRecognizedObject>();
        }

        OCRRecognizedObjectList.add(OCRRecognizedObject);
    }

    public List<OCRRecognizedObject> getOCRRecognizedObjectList() {
        return OCRRecognizedObjectList;
    }

}
