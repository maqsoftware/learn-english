package com.maqautocognita.service;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class SentenceU3LessonService extends AbstractSentenceLessonService {

    private static SentenceU3LessonService instance = null;

    private SentenceU3LessonService() {

    }

    public static SentenceU3LessonService getInstance() {
        if (instance == null) {
            instance = new SentenceU3LessonService();
        }
        return instance;
    }

    @Override
    protected String getUnitCode() {
        return "3";
    }
}
