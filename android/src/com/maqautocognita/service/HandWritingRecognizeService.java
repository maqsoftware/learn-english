package com.maqautocognita.service;

import android.graphics.PointF;
import android.util.Log;

import com.maqautocognita.handWritingRecognition.LipiTKJNIInterface;
import com.maqautocognita.handWritingRecognition.LipitkResult;
import com.maqautocognita.handWritingRecognition.Stroke;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class HandWritingRecognizeService implements com.maqautocognita.adapter.IHandWritingRecognizeService {

    private final LipiTKJNIInterface lipiTKJNIInterface;
    public Stroke currentStroke;

    public HandWritingRecognizeService(LipiTKJNIInterface lipiTKJNIInterface) {
        this.lipiTKJNIInterface = lipiTKJNIInterface;
    }

    @Override
    public void addPoint(float pointX, float pointY) {

        PointF p = new PointF(pointX, pointY);

        if (null == currentStroke) {
            currentStroke = new Stroke();
        }

        currentStroke.addPoint(p);
    }

    @Override
    public void clearPoints() {
        currentStroke = null;
    }

    @Override
    public boolean isCorrect(String letterToBeCheck) {
        if (null != currentStroke) {
            if (lipiTKJNIInterface.isLibraryLoaded()) {
                LipitkResult[] results = lipiTKJNIInterface.recognize(new Stroke[]{currentStroke});
                String configFileDirectory = lipiTKJNIInterface.getLipiDirectory() + "/projects/alphanumeric/config/";
                String[] resultCharacters = new String[results.length];
                for (int i = 0; i < resultCharacters.length; i++) {
                    resultCharacters[i] = lipiTKJNIInterface.getSymbolName(results[i].Id, configFileDirectory);
                    Log.i("HandWritingRecognize", "result = " + resultCharacters[i]);
                    if (resultCharacters[i].equals(letterToBeCheck)) {
                        return true;
                    }
                }
            } else {
                //always correct if the library is not support the user mobile
                return true;
            }
        }
        return false;
    }
}
