package com.maqautocognita.service;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class SentenceU4LessonService extends AbstractSentenceLessonService {

    private static SentenceU4LessonService instance = null;

    private SentenceU4LessonService() {

    }

    public static SentenceU4LessonService getInstance() {
        if (instance == null) {
            instance = new SentenceU4LessonService();
        }
        return instance;
    }

    @Override
    protected String getUnitCode() {
        return "4";
    }
}
