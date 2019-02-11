package com.maqautocognita.service;

import com.maqautocognita.constant.Language;
import com.maqautocognita.constant.LessonUnitCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicU4LessonService extends PhonicLessonService {

    private static PhonicU4LessonService instance = null;

    private List<String> swahiliPhonicKeyList;

    public static PhonicU4LessonService getInstance() {
        if (instance == null) {
            instance = new PhonicU4LessonService();
        }
        return instance;
    }

    @Override
    protected String getUnitCode() {
        return LessonUnitCode.PHONIC_UNIT_4.code;
    }

    public List<String> getSwahiliPhonicKeyList() {
        if (null == swahiliPhonicKeyList) {
            try {
                swahiliPhonicKeyList = new ArrayList<String>();
                Connection conn = SingletonConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement("select first_sound,second_sound,third_sound,fourth_sound from words where language = ?");
                stmt.setString(1, Language.SWAHILI);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {

                    addSound(rs, "first_sound");
                    addSound(rs, "second_sound");
                    addSound(rs, "third_sound");
                    addSound(rs, "fourth_sound");

                }
                rs.close();
                stmt.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return swahiliPhonicKeyList;

    }

    private void addSound(ResultSet rs, String columnName) throws SQLException {
        String sound = rs.getString(columnName);
        if (!swahiliPhonicKeyList.contains(sound)) {
            swahiliPhonicKeyList.add(sound);
        }
    }


}
