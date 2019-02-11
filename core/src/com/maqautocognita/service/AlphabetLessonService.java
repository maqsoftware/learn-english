package com.maqautocognita.service;

import com.maqautocognita.bo.Activity;
import com.maqautocognita.utils.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class AlphabetLessonService extends AbstractLessonService {

    private static AlphabetLessonService instance = null;

    public static AlphabetLessonService getInstance() {
        if (instance == null) {
            instance = new AlphabetLessonService();
        }
        return instance;
    }

    @Override
    protected String getUnitCode() {
        return "A";
    }

    @Override
    protected boolean isReviewReadingActivityRequired(boolean isReviewActivity) throws SQLException {
        return isReviewActivity;
    }

    @Override
    protected boolean isWritingActivityRequired(boolean isReviewActivity) throws SQLException {
        return !isReviewActivity;
    }

    @Override
    protected boolean isReviewWritingActivityRequired(boolean isReviewActivity) throws SQLException {
        return isReviewActivity;
    }

    @Override
    protected boolean isListenAndTypeActivityRequired(ResultSet resultSet, boolean isReviewActivity) throws SQLException {
        return isReviewActivity;
    }

    @Override
    public String getLetterAudioFileName(String letter) {
        return SoundService.getInstance().getAlphabetAudioFileName(letter);
    }

    @Override
    protected String getAudioFileNameKey(Activity activity) {
        return activity.getLetter();
    }

    @Override
    protected String getAudioFileName(String fileName, String word, String letter) {
        if ("alphabet_***".equals(fileName)) {
            if (StringUtils.isNotBlank(letter)) {
                fileName = SoundService.getInstance().getAlphabetAudioFileName(letter);
            }
        }

        return fileName;
    }
}
