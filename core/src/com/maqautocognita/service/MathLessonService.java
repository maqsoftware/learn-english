package com.maqautocognita.service;

import com.maqautocognita.bo.Activity;
import com.maqautocognita.bo.MathLesson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class MathLessonService extends AbstractLessonService {

    private static MathLessonService instance = null;

    private Map<String, List<MathLesson>> unitCodeMathLessonList;

    public static MathLessonService getInstance() {
        if (instance == null) {
            instance = new MathLessonService();
        }
        return instance;
    }

    public List<MathLesson> getAllMathLesson(String unitCode) {
        if (null == unitCodeMathLessonList || !unitCodeMathLessonList.containsKey(unitCode)) {
            List<MathLesson> mathLessonList = null;
            try {
                Connection conn = SingletonConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement("select * from MathLesson " +
                        "where unit_code = ? order by lesson_code,element_sequence");
                stmt.setString(1, unitCode);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {

                    MathLesson mathLesson = new MathLesson();
                    mathLesson.setElementCode(rs.getString("element_code"));
                    mathLesson.setLessonCode(rs.getInt("lesson_code"));
                    mathLesson.setSequence(rs.getInt("element_sequence"));

                    mathLesson.setPassed(true);

                    if (null == mathLessonList) {
                        mathLessonList = new ArrayList<MathLesson>();
                        mathLesson.setSelected(true);
                    }

                    mathLessonList.add(mathLesson);

                }
                rs.close();
                stmt.close();


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (null == unitCodeMathLessonList) {
                unitCodeMathLessonList = new HashMap<String, List<MathLesson>>();
            }

            unitCodeMathLessonList.put(unitCode, mathLessonList);
        }

        return unitCodeMathLessonList.get(unitCode);
    }

    @Override
    protected String getUnitCode() {
        return null;
    }

    @Override
    protected boolean isReviewReadingActivityRequired(boolean isReviewActivity) throws SQLException {
        return false;
    }

    @Override
    protected boolean isWritingActivityRequired(boolean isReviewActivity) throws SQLException {
        return false;
    }

    @Override
    protected boolean isReviewWritingActivityRequired(boolean isReviewActivity) throws SQLException {
        return false;
    }

    @Override
    protected boolean isListenAndTypeActivityRequired(ResultSet resultSet, boolean isReviewActivity) throws SQLException {
        return false;
    }

    @Override
    public String getLetterAudioFileName(String letter) {
        return null;
    }

    @Override
    protected String getAudioFileNameKey(Activity activity) {
        return null;
    }

    @Override
    protected String getAudioFileName(String fileName, String word, String letter) {
        return null;
    }
}
