package com.maqautocognita.service;

import com.maqautocognita.bo.SentenceLesson;
import com.maqautocognita.bo.SentenceWithActivityCode;
import com.maqautocognita.constant.ActivityCodeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class SentenceU5LessonService extends AbstractSentenceLessonService {

    private static SentenceU5LessonService instance = null;

    private SentenceU5LessonService() {

    }

    public static SentenceU5LessonService getInstance() {
        if (instance == null) {
            instance = new SentenceU5LessonService();
        }
        return instance;
    }

    @Override
    public List<SentenceLesson> getAllLesson() {
        List<SentenceLesson> lessonList = new ArrayList<SentenceLesson>();

        SentenceLesson sentenceLesson = new SentenceLesson();
        sentenceLesson.unitCode = "5";
        sentenceLesson.lessonCode = "1";
        sentenceLesson.setPassed(true);
        SentenceWithActivityCode sentenceWithActivityCode = new SentenceWithActivityCode();
        sentenceWithActivityCode.activityCodeEnum = ActivityCodeEnum.POSITIVE_VERB_CONJUGATION;
        sentenceLesson.addSentenceWithActivityCode(sentenceWithActivityCode);
        sentenceLesson.setSelected(true);
        lessonList.add(sentenceLesson);

        sentenceLesson = new SentenceLesson();
        sentenceLesson.unitCode = "5";
        sentenceLesson.lessonCode = "2";
        sentenceLesson.setPassed(true);
        sentenceWithActivityCode = new SentenceWithActivityCode();
        sentenceWithActivityCode.activityCodeEnum = ActivityCodeEnum.NEGATIVE_VERB_CONJUGATION;
        sentenceLesson.addSentenceWithActivityCode(sentenceWithActivityCode);
        lessonList.add(sentenceLesson);


        sentenceLesson = new SentenceLesson();
        sentenceLesson.unitCode = "5";
        sentenceLesson.lessonCode = "3";
        sentenceLesson.setPassed(true);
        sentenceWithActivityCode = new SentenceWithActivityCode();
        sentenceWithActivityCode.activityCodeEnum = ActivityCodeEnum.NOUN_CONJUGATION;
        sentenceLesson.addSentenceWithActivityCode(sentenceWithActivityCode);
        lessonList.add(sentenceLesson);


        sentenceLesson = new SentenceLesson();
        sentenceLesson.unitCode = "5";
        sentenceLesson.lessonCode = "4";
        sentenceLesson.setPassed(true);
        sentenceWithActivityCode = new SentenceWithActivityCode();
        sentenceWithActivityCode.activityCodeEnum = ActivityCodeEnum.SENTENCE_BUILD;
        sentenceLesson.addSentenceWithActivityCode(sentenceWithActivityCode);
        lessonList.add(sentenceLesson);


        return lessonList;
    }

    @Override
    protected String getUnitCode() {
        return "5";
    }
}
