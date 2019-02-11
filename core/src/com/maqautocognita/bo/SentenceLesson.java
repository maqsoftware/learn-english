package com.maqautocognita.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * store the lesson information for Sentence module
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class SentenceLesson<T extends AbstractSentence> extends Lesson {

    public String unitCode;

    public String lessonCode;

    public int elementSequence;

    public List<SentenceWithActivityCode<T>> sentenceWithActivityCodeList;

    public void addSentenceWithActivityCode(SentenceWithActivityCode<T> sentenceWithActivityCode) {
        if (null == sentenceWithActivityCodeList) {
            sentenceWithActivityCodeList = new ArrayList<SentenceWithActivityCode<T>>();
        }
        sentenceWithActivityCodeList.add(sentenceWithActivityCode);
    }

}
