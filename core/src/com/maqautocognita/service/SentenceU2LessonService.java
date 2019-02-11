package com.maqautocognita.service;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class SentenceU2LessonService extends AbstractSentenceLessonService {

    private static SentenceU2LessonService instance = null;

    private SentenceU2LessonService() {

    }

    public static SentenceU2LessonService getInstance() {
        if (instance == null) {
            instance = new SentenceU2LessonService();
        }
        return instance;
    }

    @Override
    protected String getUnitCode() {
        return "2";
    }
}
