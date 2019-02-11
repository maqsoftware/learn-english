package com.maqautocognita.adapter;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface IAnalyticSpotService {

    void updateScore(int ProgressCompleted, String UnitCode, String LessonCode, String ElementSequence, String ElementCode, String ProgressType, String language);
    void updateMathScore(int ProgressCompleted, String ElementCode, String language);

}
