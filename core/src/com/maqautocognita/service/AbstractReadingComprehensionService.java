package com.maqautocognita.service;

import com.maqautocognita.bo.ReadingComprehension;
import com.maqautocognita.bo.ReadingComprehensionLesson;
import com.maqautocognita.bo.SentenceWithActivityCode;
import com.maqautocognita.constant.ActivityCodeEnum;
import com.badlogic.gdx.Gdx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by siu-chun.chi on 5/9/2017.
 */

public abstract class AbstractReadingComprehensionService extends AbstractSentenceLessonService {

    private List<ReadingComprehensionLesson> lessonList;


    public List<ReadingComprehensionLesson> getAllLesson() {

        if (null == lessonList) {

            ResultSet rs = null;
            PreparedStatement stmt = null;
            try {

                Connection conn = SingletonConnectionPool.getInstance().getConnection();
                stmt = conn.prepareStatement("select * from ReadingComprehension where unit_Code = ? order by lesson_Code,element_sequence");

                stmt.setString(1, getUnitCode());

                rs = stmt.executeQuery();

                ReadingComprehensionLesson readingComprehensionLesson = null;

                while (rs.next()) {

                    String lessonCode = rs.getString("lesson_code");
                    String unitCode = rs.getString("unit_code");
                    int elementSequence = rs.getInt("element_sequence");
                    if (null == readingComprehensionLesson || !readingComprehensionLesson.lessonCode.equals(lessonCode)) {
                        readingComprehensionLesson = new ReadingComprehensionLesson();
                        readingComprehensionLesson.unitCode = unitCode;
                        readingComprehensionLesson.lessonCode = lessonCode;
                        readingComprehensionLesson.elementSequence = elementSequence;


                        if (null == lessonList) {
                            lessonList = new ArrayList<ReadingComprehensionLesson>();
                            readingComprehensionLesson.setSelected(true);
                        }

                        readingComprehensionLesson.setPassed(true);
                        lessonList.add(readingComprehensionLesson);
                    }

                    SentenceWithActivityCode sentenceWithActivityCode = new SentenceWithActivityCode();
                    sentenceWithActivityCode.activityCodeEnum = ActivityCodeEnum.getActivityCodeEnumByCode(rs.getString("activity_code"));
                    ReadingComprehension readingComprehension = new ReadingComprehension();
                    readingComprehension.readingPassage = rs.getString("reading_passage");
                    readingComprehension.question = rs.getString("question");
                    readingComprehension.beforeAnswer = rs.getString("before_answer");
                    readingComprehension.sentence = rs.getString("answer");
                    readingComprehension.afterAnswer = rs.getString("after_answer");
                    readingComprehension.answerWords = rs.getString("answer_words");

                    readingComprehension.sentencePartOfSpeech = rs.getString("answer_parts_of_speech");
                    readingComprehension.words = rs.getString("words");
                    readingComprehension.wordsPartOfSpeech = rs.getString("words_parts_of_speech");
                    readingComprehension.audioFileName = rs.getString("audio");

                    sentenceWithActivityCode.sentence = readingComprehension;

                    if (null != sentenceWithActivityCode) {
                        readingComprehensionLesson.addSentenceWithActivityCode(sentenceWithActivityCode);
                    }

                }
            } catch (Exception e) {
                Gdx.app.error(getClass().getName(), "", e);
            } finally {
                if (null != stmt) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (null != rs) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        return lessonList;
    }

}
