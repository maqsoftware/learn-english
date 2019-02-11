package com.maqautocognita.service;

import com.maqautocognita.utils.IconPosition;

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
public class LetterPointsSequenceService {

    private static LetterPointsSequenceService instance = null;

    private int[] minMaxYPositionOfLowerCaseA;

    private Map<String, List<IconPosition>> letterPointSequencePositionMap;

    public static LetterPointsSequenceService getInstance() {
        if (instance == null) {
            instance = new LetterPointsSequenceService();
        }
        return instance;
    }

    public int[] getMaxAndMinYPosition(String letter) {
        if (null == minMaxYPositionOfLowerCaseA) {
            minMaxYPositionOfLowerCaseA = new int[4];
        } else {
            return minMaxYPositionOfLowerCaseA;
        }
        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select  max(letterY),min(letterY),max(letterX),min(letterX) from LetterPointSeq where letter = ?");
            stmt.setString(1, letter);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                minMaxYPositionOfLowerCaseA[0] = rs.getInt("min(letterY)");
                minMaxYPositionOfLowerCaseA[1] = rs.getInt("max(letterY)");
                minMaxYPositionOfLowerCaseA[2] = rs.getInt("min(letterX)");
                minMaxYPositionOfLowerCaseA[3] = rs.getInt("max(letterY)");
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return minMaxYPositionOfLowerCaseA;
    }

    public List<IconPosition> getDrawLetterPoints(String letter, float startX, float startY, float minXPositionOfLetter, float minYPositionOfLetter, float ratio, int dotSize) {

        List<IconPosition> letterPositionList = null;

        for (int i = 0; i < letter.length(); i++) {

            char character = letter.charAt(i);
            List<IconPosition> characterPositionList = getLetterPointsByLetter(String.valueOf(character));

            if (null != characterPositionList) {

                float minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;

                for (IconPosition point : characterPositionList) {

                    float xPosition = startX + (point.x - minXPositionOfLetter) * ratio;

                    if (maxX < xPosition) {
                        maxX = xPosition;
                    }

                    if (minX > xPosition) {
                        minX = xPosition;
                    }

                    if (null == letterPositionList) {
                        letterPositionList = new ArrayList<IconPosition>();
                    }
                    letterPositionList.add(new IconPosition(xPosition,
                            (startY - (point.y * ratio - minYPositionOfLetter) - dotSize),
                            dotSize, dotSize));

                }

                if (i + 1 < letter.length()) {
                    startX += getWidthOfTheLetter(letter.charAt(i + 1), ratio);
                }
            }
        }

        return letterPositionList;
    }

    private List<IconPosition> getLetterPointsByLetter(String letter) {

        if (null != letterPointSequencePositionMap && null != letterPointSequencePositionMap.get(letter)) {
            return letterPointSequencePositionMap.get(letter);
        }

        List<IconPosition> iconPositionList = null;
        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select letterX, letterY from LetterPointSeq where letter = ? order by letterSeq");
            stmt.setString(1, letter);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (null == iconPositionList) {
                    iconPositionList = new ArrayList<IconPosition>();
                }
                iconPositionList.add(new IconPosition(rs.getInt("letterX"), rs.getInt("letterY"), 0, 0));

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (null == letterPointSequencePositionMap) {
            letterPointSequencePositionMap = new HashMap<String, List<IconPosition>>();
        }
        letterPointSequencePositionMap.put(letter, iconPositionList);


        return iconPositionList;
    }

    public int getWidthOfTheLetter(char letter, float ratio) {
        int width = 0;

        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select  max(letterX) - min(letterX) from LetterPointSeq where letter = ?");
            stmt.setString(1, String.valueOf(letter));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                width = (int) (rs.getInt(1) * ratio);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return width;
    }


}
