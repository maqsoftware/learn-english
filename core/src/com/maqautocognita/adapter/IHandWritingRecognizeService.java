package com.maqautocognita.adapter;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface IHandWritingRecognizeService {

    void addPoint(float pointX, float pointY);

    void clearPoints();

    boolean isCorrect(String letterToBeCheck);

}
