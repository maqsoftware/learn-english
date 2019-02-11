package com.maqautocognita.service;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class SentenceU1LessonService extends AbstractSentenceLessonService {

    private static SentenceU1LessonService instance = null;

    private SentenceU1LessonService() {

    }

    public static SentenceU1LessonService getInstance() {
        if (instance == null) {
            instance = new SentenceU1LessonService();
        }
        return instance;
    }

    @Override
    protected String getUnitCode() {
        return "1";
    }
}
