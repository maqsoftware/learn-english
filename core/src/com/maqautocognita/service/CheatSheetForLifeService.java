package com.maqautocognita.service;

import com.maqautocognita.bo.CheatSheetBox;
import com.maqautocognita.bo.CheatSheetLesson;
import com.maqautocognita.bo.storyMode.CheatSheetImage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public final class CheatSheetForLifeService {

    private static CheatSheetForLifeService instance = null;

    private List<CheatSheetLesson> cheatSheetLessonList;

    public static CheatSheetForLifeService getInstance() {
        if (instance == null) {
            instance = new CheatSheetForLifeService();
        }
        return instance;
    }

    public List<CheatSheetLesson> getAllCheatSheet() {
        if (null == cheatSheetLessonList) {
            try {

                Connection conn = SingletonConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement("select * from CheatSheetBox order by id");

                ResultSet rs = stmt.executeQuery();
                CheatSheetLesson cheatSheetLesson = null;
                CheatSheetImage cheatSheetImage = null;

                int previousCheatSheetId = -1;
                String previousImageName = null;

                while (rs.next()) {
                    int cheatSheetId = (int) rs.getDouble("id");
                    if (previousCheatSheetId != cheatSheetId) {
                        cheatSheetLesson = new CheatSheetLesson();
                        cheatSheetLesson.id = cheatSheetId;
                        if (null == cheatSheetLessonList) {
                            cheatSheetLessonList = new ArrayList<CheatSheetLesson>();
                        }
                        cheatSheetLessonList.add(cheatSheetLesson);
                        previousCheatSheetId = cheatSheetId;
                    }

                    String imageName = rs.getString("image_name");

                    if (!imageName.equals(previousImageName)) {
                        cheatSheetImage = new CheatSheetImage();
                        cheatSheetImage.imageName = imageName;
                        cheatSheetLesson.addCheatSheetImage(cheatSheetImage);
                        previousImageName = imageName;
                    }

                    CheatSheetBox cheatSheetBox = new CheatSheetBox();
                    cheatSheetBox.xPositionFromLeft = rs.getInt("x_position_from_left");
                    cheatSheetBox.yPositionFromTop = rs.getInt("y_position_from_top");
                    cheatSheetBox.width = rs.getInt("width");
                    cheatSheetBox.height = rs.getInt("height");
                    cheatSheetBox.text = rs.getString("text");
                    cheatSheetBox.spanishTranslation = rs.getString("spanish_translation");
                    cheatSheetBox.voiceFilename = rs.getString("voice_filename");
                    cheatSheetImage.addCheatSheetBox(cheatSheetBox);
                    cheatSheetImage.lesson = cheatSheetLesson;
                }
                rs.close();
                stmt.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return cheatSheetLessonList;
    }
}
