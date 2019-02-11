package com.maqautocognita.service;

import com.maqautocognita.AbstractLearningGame;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.bo.LessonWithReview;
import com.maqautocognita.bo.ProgressMapElementResult;
import com.maqautocognita.bo.ProgressMapSkillResult;
import com.maqautocognita.bo.ProgressMapTopicResult;
import com.maqautocognita.constant.ActivityCodeEnum;
import com.maqautocognita.constant.Language;
import com.maqautocognita.constant.LessonProgressType;
import com.maqautocognita.screens.HomeMenuMapScreen;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.SectionUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;

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
public abstract class AbstractLessonService {

    private List<LessonWithReview> lessonList;

    private List<String> alphabetList;

    private Map<String, String> swahiliEnglishWordMap;


    private ActivityCodeEnum[] DEFAULT_ACTIVITY_SEQUENCE =
            new ActivityCodeEnum[]{ActivityCodeEnum.READING_AND_LISTENING,
                    ActivityCodeEnum.READING,
                    ActivityCodeEnum.SPEAKING,
                    ActivityCodeEnum.WRITING,
                    ActivityCodeEnum.GROUPING, ActivityCodeEnum.FILL_IN_THE_BLANKS, ActivityCodeEnum.WORD_BLEND, ActivityCodeEnum.LISTEN_AND_TYPE};

    private void getSwahiliMapping() {
        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select * from SwahiliEnglishMapping");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (null == swahiliEnglishWordMap) {
                    swahiliEnglishWordMap = new HashMap<String, String>();
                }

                swahiliEnglishWordMap.put(rs.getString("swahili"), rs.getString("english"));
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initAllLesson() {
        try {

            if (null == lessonList) {


                SoundService.getInstance().clearPhonicMapping();

                getSwahiliMapping();

                Connection conn = SingletonConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement("select * from LessonAndReview " +
                        "where unit_code = ? and lesson_code like ? and language = ? order by lesson_code,element_sequence");
                stmt.setString(1, getUnitCode());
                stmt.setString(2, "L%");
                stmt.setString(3, UserPreferenceUtils.getInstance().getLanguage());

                ResultSet rs = stmt.executeQuery();
                boolean isLessonSelected = false;
                String selectedLessonCode = null;
                LessonWithReview lessonWithReview = null;
                int lessonSequence = 0;
                while (rs.next()) {

                    String lessonCode = rs.getString("lesson_code");
                    if (!lessonCode.equals(selectedLessonCode)) {
                        lessonSequence++;
                        //which mean join to next lesson
                        selectedLessonCode = lessonCode;
                        lessonWithReview = new LessonWithReview();

                        lessonWithReview.setLetterList(getDistinctLetterListByLessonCode(lessonCode));

                        if (null == alphabetList) {
                            alphabetList = new ArrayList<String>();
                        }

                        alphabetList.addAll(lessonWithReview.getLetterList());

                        lessonWithReview.setSequence(lessonSequence);

                        addReview(lessonWithReview, lessonSequence);

                        if (null == lessonList) {
                            lessonList = new ArrayList<LessonWithReview>();
                        }
                        lessonList.add(lessonWithReview);
                    }

                    addActivities(lessonWithReview, rs, false);

                }
                rs.close();
                stmt.close();

                if (!isLessonSelected && CollectionUtils.isNotEmpty(lessonList)) {
                    //if no lessonWithReview selected, which mean the user is just start the game
                    lessonList.get(0).setSelected(true);
                }

                afterInitAllLesson();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void afterInitAllLesson() {

    }

    private List<String> getDistinctLetterListByLessonCode(String lessonCode) {
        List<String> letterList = null;

        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select distinct element_code as element from LessonAndReview  " +
                    "where unit_code = ? and lesson_code = ? and language = ? order by element_sequence");
            stmt.setString(1, getUnitCode());
            stmt.setString(2, lessonCode);
            stmt.setString(3, UserPreferenceUtils.getInstance().getLanguage());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                if (null == letterList) {
                    letterList = new ArrayList<String>();
                }

                letterList.add(rs.getString("element"));

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return letterList;
    }

    private void addReview(LessonWithReview lessonWithReview, int lessonSequence) {

        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select * from LessonAndReview " +
                    "where unit_code = ? and lesson_code = ? and language = ? order by lesson_code,element_sequence");
            stmt.setString(1, getUnitCode());
            stmt.setString(2, "R" + lessonSequence);
            stmt.setString(3, UserPreferenceUtils.getInstance().getLanguage());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                addActivities(lessonWithReview, rs, true);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public LessonWithReview getMasteryTestList() {
        LessonWithReview masterTest = null;

        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select * from LessonAndReview " +
                    "where unit_code = ? and lesson_code like ? and language = ? order by lesson_code,element_sequence");
            stmt.setString(1, getUnitCode());
            stmt.setString(2, "M%");
            stmt.setString(3, UserPreferenceUtils.getInstance().getLanguage());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (null == masterTest) {
                    masterTest = new LessonWithReview();
                }
                addActivities(masterTest, rs, true);


            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (null != masterTest) {
            masterTest.setMasterTestList(masterTest.getReviewList());
            masterTest.setReviewList(null);
            for (Activity activity : masterTest.getMasterTestList()) {
                activity.setUnitCode(getUnitCode());
            }
        }

        return masterTest;
    }

    protected abstract String getUnitCode();

    private void addActivities(LessonWithReview lessonWithReview, ResultSet rs, boolean isReviewActivity) throws SQLException {


        String activitySequence = StringUtils.removeAllSpace(rs.getString("activity_sequence"));

        ActivityCodeEnum[] activityCodeSequence = null;

        if (StringUtils.isNotBlank(activitySequence)) {
            String activitySequences[] = activitySequence.split(",");
            activityCodeSequence = new ActivityCodeEnum[activitySequences.length];
            for (int i = 0; i < activitySequences.length; i++) {
                activityCodeSequence[i] = getActivityCodeEnumByCode(activitySequences[i]);
            }
        }

        if (null == activityCodeSequence) {
            activityCodeSequence = DEFAULT_ACTIVITY_SEQUENCE;
        }

        for (ActivityCodeEnum activityCodeEnum : activityCodeSequence) {
            switch (activityCodeEnum) {
                case READING_AND_LISTENING:
                    if (isReadAndListenActivityRequired(rs)) {
                        addActivity(lessonWithReview, rs, ActivityCodeEnum.READING_AND_LISTENING, "read_and_listen_introduction", "read_and_listen_short_instruction", "read_and_listen_help", isReviewActivity);
                    }
                    break;

                case SPEAKING:
                    if (//if this is the lesson module
                            !isReviewActivity ||
                                    //or the speaking word is not blank in the field
                                    isSpeakWordActivityRequired(rs)) {
                        addActivity(lessonWithReview, rs, ActivityCodeEnum.SPEAKING, "speak_introduction", null, "speak_help", isReviewActivity);
                    }

                    break;
                case READING:
                    if (isReviewReadingActivityRequired(isReviewActivity)) {
                        addActivity(lessonWithReview, rs, ActivityCodeEnum.READING, "matching_instruction", null, "matching_help", isReviewActivity);
                    }
                    break;
                case WRITING:
                    if (isWritingActivityRequired(isReviewActivity)) {
                        addActivity(lessonWithReview, rs, ActivityCodeEnum.WRITING, "write_introduction", "write_short_instruction", "write_help", isReviewActivity);
                    } else if (isReviewWritingActivityRequired(isReviewActivity)) {
                        addActivity(lessonWithReview, rs, ActivityCodeEnum.WRITING, "listen_and_write_instruction", null, "listen_and_write_help", isReviewActivity);
                    }
                    break;
                case GROUPING:
                    if (isGroupingActivityRequired(rs)) {
                        addActivity(lessonWithReview, rs, ActivityCodeEnum.GROUPING, null, "grouping_short_instruction", "grouping_help", isReviewActivity);
                    }
                    break;
                case FILL_IN_THE_BLANKS:
                    if (isFillInTheBlanksActivityRequired(rs)) {
                        addActivity(lessonWithReview, rs, ActivityCodeEnum.FILL_IN_THE_BLANKS, null, "fill_in_the_blank_short_instruction", "fill_in_the_blank_help", isReviewActivity);
                    }
                    break;
                case WORD_BLEND:
                    if (isWordBlendActivityRequired(rs)) {
                        addActivity(lessonWithReview, rs, ActivityCodeEnum.WORD_BLEND, "word_blend_introduction", "word_blend_short_instruction", "word_blend_help", isReviewActivity);
                    }

                    break;
                case LISTEN_AND_TYPE:
                    if (isListenAndTypeActivityRequired(rs, isReviewActivity)) {
                        addActivity(lessonWithReview, rs, ActivityCodeEnum.LISTEN_AND_TYPE, "listen_and_type_introduction", "listen_and_type_short_instruction", "listen_and_type_help", isReviewActivity);
                    }
                    break;
            }
        }
    }

    private ActivityCodeEnum getActivityCodeEnumByCode(String code) {

        if ("wb".equalsIgnoreCase(code)) {
            return ActivityCodeEnum.WORD_BLEND;
        } else if ("sp".equalsIgnoreCase(code)) {
            return ActivityCodeEnum.SPEAKING;
        } else if ("lt".equalsIgnoreCase(code) || "ALT".equalsIgnoreCase(code)) {
            return ActivityCodeEnum.LISTEN_AND_TYPE;
        } else if ("ma".equalsIgnoreCase(code) || "AMA".equalsIgnoreCase(code)) {
            return ActivityCodeEnum.READING;
        } else if ("lw".equalsIgnoreCase(code) || "ALW".equalsIgnoreCase(code)) {
            return ActivityCodeEnum.WRITING;
        } else if ("gr".equalsIgnoreCase(code)) {
            return ActivityCodeEnum.GROUPING;
        } else if ("fb".equalsIgnoreCase(code)) {
            return ActivityCodeEnum.FILL_IN_THE_BLANKS;
        } else if ("rl".equalsIgnoreCase(code)) {
            return ActivityCodeEnum.READING_AND_LISTENING;
        }

        return null;
    }

    private boolean isReadAndListenActivityRequired(ResultSet resultSet) throws SQLException {
        return StringUtils.isNotBlank(resultSet.getString("read_and_listen_words"));
    }

    private void addActivity(LessonWithReview lessonWithReview, ResultSet resultSet, ActivityCodeEnum activityCode, String introductionColumnName, String shortInstructionColumnName,
                             String helpColumnName, boolean isReviewActivity) throws SQLException {
        Activity activity = getActivity(resultSet, activityCode, introductionColumnName, shortInstructionColumnName, helpColumnName);

        if (null != activity) {
            if (isReviewActivity) {
                activity.setParent(lessonWithReview);
                lessonWithReview.addReviewActivity(activity);
            } else {
                lessonWithReview.addActivity(activity);
            }
            lessonWithReview.setPassed(true);
        }
    }

    private boolean isSpeakWordActivityRequired(ResultSet resultSet) throws SQLException {
        return StringUtils.isNotBlank(resultSet.getString("speak_words"));
    }

    protected boolean isReviewReadingActivityRequired(boolean isReviewActivity) throws SQLException {
        return false;
    }

    protected boolean isWritingActivityRequired(boolean isReviewActivity) throws SQLException {
        return false;
    }

    protected boolean isReviewWritingActivityRequired(boolean isReviewActivity) throws SQLException {
        return false;
    }

    private boolean isGroupingActivityRequired(ResultSet resultSet) throws SQLException {
        return StringUtils.isNotBlank(resultSet.getString("grouping_words"));
    }

    private boolean isFillInTheBlanksActivityRequired(ResultSet resultSet) throws SQLException {
        return StringUtils.isNotBlank(resultSet.getString("fill_in_the_blanks_words"));
    }

    private boolean isWordBlendActivityRequired(ResultSet resultSet) throws SQLException {
        return StringUtils.isNotBlank(resultSet.getString("word_blend_long_instruction_words")) || StringUtils.isNotBlank(resultSet.getString("word_blend_short_instruction_words")) || StringUtils.isNotBlank(resultSet.getString("word_blend_no_instruction_words"));
    }

    protected boolean isListenAndTypeActivityRequired(ResultSet resultSet, boolean isReviewActivity) throws SQLException {
        return StringUtils.isNotBlank(resultSet.getString("listen_and_type_begin_sound_words")) || StringUtils.isNotBlank(resultSet.getString("listen_and_type_long_instruction_words")) ||
                StringUtils.isNotBlank(resultSet.getString("listen_and_type_short_instruction_words")) || StringUtils.isNotBlank(resultSet.getString("listen_and_type_no_instruction_words"));
    }

    private Activity getActivity(ResultSet resultSet, ActivityCodeEnum activityCode,
                                 String introductionColumnName, String shortInstructionColumnName, String helpColumnName) throws SQLException {

        Activity activity = new Activity();
        activity.setLetter(resultSet.getString("element_code"));
        activity.setPhonic(resultSet.getString("phonic"));
        activity.setUnitCode(resultSet.getString("unit_code"));
        activity.setLessonCode(resultSet.getString("lesson_code"));
        activity.setSequence(resultSet.getInt("element_sequence"));
        activity.setAudioDuration(resultSet.getInt("audio_duration"));
        activity.setInstructorPlayback(resultSet.getString("instructor_playback"));
        activity.setActivityCode(activityCode);

        setWords(resultSet, activity);

        //set the audio file name which used to play the letter sound
        activity.setAudioFileName(StringUtils.trim(getLetterAudioFileName(getAudioFileNameKey(activity))));

        if (StringUtils.isNotBlank(introductionColumnName)) {

            activity.setIntroductionAudioFilenameList(getVoScriptAudioFileNameList(StringUtils.trim(resultSet.getString(introductionColumnName)), null, activity.getLetter()));
        }
        if (StringUtils.isNotBlank(shortInstructionColumnName)) {
            activity.setShortInstructionAudioFilenameList(getVoScriptAudioFileNameList(StringUtils.trim(resultSet.getString(shortInstructionColumnName)), null, activity.getLetter()));
        }
        if (StringUtils.isNotBlank(helpColumnName)) {
            activity.setHelpAudioFilenameList(getVoScriptAudioFileNameList(StringUtils.trim(resultSet.getString(helpColumnName)), null, activity.getLetter()));
        }

        setAdditionalInformationForActivity(activity, resultSet);


        activity.setPassed(true);

        return activity;
    }

    private void setWords(ResultSet resultSet, Activity activity) throws SQLException {
        if (SectionUtils.isReadingAndListening(activity.getActivityCode())) {

            setWords(resultSet, activity, "read_and_listen_words");

        } else if (SectionUtils.isSpeaking(activity.getActivityCode())) {
            setWords(resultSet, activity, "speak_words");
        }
    }

    public abstract String getLetterAudioFileName(String letter);

    protected abstract String getAudioFileNameKey(Activity activity);

    protected List<String> getVoScriptAudioFileNameList(String voScriptName, String word, String letter) {


        List<String> audioFileNameList = null;

        if (StringUtils.isNotBlank(voScriptName)) {
            try {
                Connection conn = SingletonConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement("select * from vo_script where vo_script_name = ? and language = ?");
                stmt.setString(1, voScriptName);
                stmt.setString(2, UserPreferenceUtils.getInstance().isEnglish() ? Language.ENGLISH : Language.SWAHILI);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    audioFileNameList = getAudioFileNameList(
                            getAudioFileName(rs, "vo_filename1", word, letter),
                            getAudioFileName(rs, "vo_filename2", word, letter),
                            getAudioFileName(rs, "vo_filename3", word, letter),
                            getAudioFileName(rs, "vo_filename4", word, letter),
                            getAudioFileName(rs, "vo_filename5", word, letter),
                            getAudioFileName(rs, "vo_filename6", word, letter));
                }
                rs.close();
                stmt.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return audioFileNameList;
    }

    protected void setAdditionalInformationForActivity(Activity activity, ResultSet resultSet) throws SQLException {

    }

    private void setWords(ResultSet resultSet, Activity activity, String wordsColumnName) throws SQLException {
        String wordsInDatabase = StringUtils.removeAllSpace(resultSet.getString(wordsColumnName));

        if (StringUtils.isNotBlank(wordsInDatabase)) {
            String[] words = StringUtils.removeAllSpace(wordsInDatabase).split(",");

            String[] pictures = new String[words.length];
            for (int i = 0; i < words.length; i++) {
                pictures[i] = getEnglishWordBySwahili(words[i]).toLowerCase() + ".png";
            }

            activity.setPictures(pictures);
            activity.setWords(words);
        }

    }

    private List<String> getAudioFileNameList(String... audioFileNames) {
        List<String> audioFileNameList = null;
        if (null != audioFileNames) {
            for (String audioFileName : audioFileNames) {
                if (StringUtils.isNotBlank(audioFileName)) {
                    if (null == audioFileNameList) {
                        audioFileNameList = new ArrayList<String>();
                    }
                    audioFileNameList.add(audioFileName);
                }
            }
        }

        return audioFileNameList;
    }

    protected String getAudioFileName(ResultSet resultSet, String columnName, String word, String letter) throws SQLException {
        String fileName = StringUtils.trim(resultSet.getString(columnName));
        if (StringUtils.isNotBlank(fileName)) {

            fileName = getAudioFileName(fileName, word, letter);
        }

        return fileName;
    }

    public String getEnglishWordBySwahili(String word) {
        if (null != swahiliEnglishWordMap) {
            String english = swahiliEnglishWordMap.get(word);
            if (StringUtils.isNotBlank(english)) {
                word = english;
            }
        }

        return word;
    }

    protected abstract String getAudioFileName(String fileName, String word, String letter);

    public List<LessonWithReview> getLessonList() {
        initAllLesson();
        return lessonList;
    }

    public List<String> getAlphabetList() {
        return alphabetList;
    }

    public boolean updateMathLessonProgress(int errorCount, String elementCode) {
        PreparedStatement preparedStatement = null;
        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            preparedStatement = conn.prepareStatement("update LessonProgress " +
                    "set progress_type_completed = ?  where language=? and element_code=?");
            preparedStatement.setString(1, String.valueOf(3 - Math.min(3, errorCount)));
            preparedStatement.setString(2, UserPreferenceUtils.getInstance().getLanguage());
            preparedStatement.setString(3, elementCode);
            int numberOFRecordUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();
            AbstractLearningGame.analyticSpotService.updateMathScore(3 - Math.min(3, errorCount), elementCode, UserPreferenceUtils.getInstance().getLanguage());
            return numberOFRecordUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public boolean updateLessonProgress(int pErrorCount, String pUnitCode, String pLessonCode, String pElementSequence,
                                        String pElementCode, String pProgressType, String language) {
        float average_count = 0.f;
        try {

            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            //PreparedStatement stmt = conn.prepareStatement("select * from LessonAndReview " +
            //        "where unit_code = ? and lesson_code like ? and language = ? order by lesson_code,element_sequence");


            PreparedStatement stmt4 = conn.prepareStatement("update LessonProgress set last_run = '' " +
                    "where language=?");

            stmt4.setString(1, language);

            stmt4.executeUpdate();
            stmt4.close();

            PreparedStatement stmt = conn.prepareStatement("update LessonProgress set progress_type_completed = ? , last_run = 'yes' " +
                    "where unit_code = ? and lesson_code = ? and element_sequence = ? and element_code = ? and progress_type = ? and language=?");

            if (pErrorCount > 3) {
                pErrorCount = 3;
            }
            stmt.setString(1, String.valueOf(3 - pErrorCount));
            stmt.setString(2, pUnitCode);
            stmt.setString(3, pLessonCode);
            stmt.setString(4, pElementSequence);
            stmt.setString(5, pElementCode);
            stmt.setString(6, pProgressType);
            stmt.setString(7, language);
            //stmt.setString(2, UserPreferenceUtils.getInstance().isEnglish() ? Language.ENGLISH : Language.SWAHILI);

            stmt.executeUpdate();
            stmt.close();

            PreparedStatement stmt3 = conn.prepareStatement("select avg(progress_type_completed) average_count from LessonProgress " +
                    "where unit_code = ? and lesson_code = ? and element_sequence = ? and element_code = ? and progress_type not in ('AL', 'P1', 'P2', 'P3') and language = ? ");

            stmt3.setString(1, pUnitCode);
            stmt3.setString(2, pLessonCode);
            stmt3.setString(3, pElementSequence);
            stmt3.setString(4, pElementCode);
            stmt3.setString(5, language);

            AbstractLearningGame.analyticSpotService.updateScore(3 - pErrorCount, pUnitCode, pLessonCode, pElementSequence, pElementCode, pProgressType, language);

            ResultSet rs = stmt3.executeQuery();
            while (rs.next()) {
                average_count = rs.getFloat("average_count");
            }
            rs.close();
            stmt3.close();


            PreparedStatement stmt2 = conn.prepareStatement("update LessonProgress set progress_type_completed = ? " +
                    "where unit_code = ? and lesson_code = ? and element_sequence = ? and element_code = ? and " +
                    "progress_type in ('AL', 'P1', 'P2', 'P3') and language = ? ");

            stmt2.setInt(1, (int) Math.ceil(average_count));
            stmt2.setString(2, pUnitCode);
            stmt2.setString(3, pLessonCode);
            stmt2.setString(4, pElementSequence);
            stmt2.setString(5, pElementCode);
            stmt2.setString(6, language);
            stmt2.executeUpdate();
            stmt2.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public List<ProgressMapTopicResult> getMathProgressTopic() {

        return getProgressTopic(getProgressTypeText(LessonProgressType.COUNT, LessonProgressType.COMPARE, LessonProgressType.ADD,
                LessonProgressType.SUBTRACT, LessonProgressType.MULTIPLY, LessonProgressType.PLACE_VALUE, LessonProgressType.NUMBER_PATTERN, LessonProgressType.SHAPE));
    }

    private ArrayList<ProgressMapTopicResult> getProgressTopic(String progressTypes) {
        ArrayList<ProgressMapTopicResult> retProgressMap = new ArrayList<ProgressMapTopicResult>();

        String currType = "";

        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select lesson_code || element_code element_code, progress_type, progress_type_completed from LessonProgress " +
                    "where progress_type in (" + progressTypes + ") and language = ? order by progress_type, sequence");

            stmt.setString(1, UserPreferenceUtils.getInstance().getLanguage());

            ResultSet rs = stmt.executeQuery();
            int typeCount = 0;
            while (rs.next()) {
                if (currType != rs.getString("progress_type")) {
                    currType = rs.getString("progress_type");
                    typeCount = 0;
                } else {
                    typeCount++;
                }
                ProgressMapTopicResult currProgressMap = new ProgressMapTopicResult();
                currProgressMap.progressType = rs.getString("progress_type");
                currProgressMap.elementCode = rs.getString("element_code");
                currProgressMap.completeCount = rs.getInt("progress_type_completed");
                currProgressMap.sequenceNumber = typeCount;
                retProgressMap.add(currProgressMap);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return retProgressMap;
    }

    private String getProgressTypeText(LessonProgressType... lessonProgressTypes) {
        StringBuilder progressTypeText = new StringBuilder();
        for (LessonProgressType lessonProgressType : lessonProgressTypes) {
            progressTypeText.append("'" + lessonProgressType.type + "',");
        }

        progressTypeText.deleteCharAt(progressTypeText.length() - 1);
        return progressTypeText.toString();
    }

    public List<ProgressMapSkillResult> getMathProgressSkill() {
        return getProgressSkill(getProgressTypeText(LessonProgressType.CLICK, LessonProgressType.MATH_TYPE, LessonProgressType.WRITE, LessonProgressType.SPEAK, LessonProgressType.MAKE_PICTURE,
                LessonProgressType.MATCH, LessonProgressType.COLOR_BLOCKS, LessonProgressType.LONG_FORM));
    }

    private ArrayList<ProgressMapSkillResult> getProgressSkill(String progressTypes) {
        ArrayList<ProgressMapSkillResult> retProgressMap = new ArrayList<ProgressMapSkillResult>();

        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select progress_type, sum(progress_type_weight) fullMark , " +
                    "sum(CASE WHEN progress_type_completed > 0 THEN progress_type_weight ELSE 0 END) currentMark from LessonProgress " +
                    "where progress_type in (" + progressTypes + ") and language = ? group by progress_type");

            stmt.setString(1, UserPreferenceUtils.getInstance().getLanguage());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProgressMapSkillResult currProgressMap = new ProgressMapSkillResult();
                currProgressMap.progressType = rs.getString("progress_type");
                currProgressMap.fullMark = rs.getFloat("fullMark");
                currProgressMap.currentMark = rs.getFloat("currentMark");
                retProgressMap.add(currProgressMap);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return retProgressMap;
    }

    public List<ProgressMapTopicResult> getReadingProgressTopic() {
        return getProgressTopic("'AL', 'P1', 'P2', 'P3'");
    }

    public List<ProgressMapSkillResult> getReadingProgressSkill() {
        return getProgressSkill("'RL', 'SP', 'WB', 'LT', 'GR', 'FB', 'MA', 'LW'");
    }

    public float getAlphabetPhonicProgress() {
        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select sum(CASE WHEN progress_type_completed > 0 THEN 1 ELSE 0 END) * 1.0 /  count(*) * " + (1 - HomeMenuMapScreen.DEFAULT_UNIT_ALPHA) +
                    " from LessonProgress where unit_code in ('A','U1','U2','U3','U4') and language = ?");
            stmt.setString(1, UserPreferenceUtils.getInstance().getLanguage());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return rs.getFloat(1);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 1;
    }

    public ArrayList<String> getNextUnitLesson() {
        return getNextUnitLesson("");
    }

    public ArrayList<String> getNextUnitLesson(String pUnitCode) {
        ArrayList<String> returnList = new ArrayList<String>();
        String vUnitCode = pUnitCode;
        if (vUnitCode.equals("")) {
            try {
                Connection conn = SingletonConnectionPool.getInstance().getConnection();
                PreparedStatement stmt_last = conn.prepareStatement("select min(unit_code) from LessonProgress where last_run = 'yes' ");

                ResultSet rs_last = stmt_last.executeQuery();

                if (rs_last.next()) {
                    vUnitCode = rs_last.getString(1);
                } else {
                    vUnitCode = "A";
                }
            } catch (SQLException e) {
                vUnitCode = "A";
                //throw new RuntimeException(e);
            }
        }


        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select sequence,unit_code, lesson_code, element_sequence, element_code, sum(progress_type_completed) from LessonProgress " +
                    "group by sequence, unit_code, lesson_code, element_sequence, element_code " +
                    "having sum(progress_type_completed) = 0 and unit_code =? and language = ? " +
                    "order by sequence ");

            stmt.setString(1, vUnitCode);
            stmt.setString(2, UserPreferenceUtils.getInstance().getLanguage());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                returnList.add(String.valueOf(rs.getInt(1)));
                returnList.add(rs.getString(2)); //Unit Code
                returnList.add(rs.getString(3)); //Lesson Code
                returnList.add(rs.getString(4)); //Element Sequence
                returnList.add(rs.getString(5)); //Element Code
                return returnList;
                //return rs.getFloat(0);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return returnList;
        //return 1 - HomeMenuMapScreen.DEFAULT_UNIT_ALPHA;
    }


    public List<ProgressMapElementResult> getProgressMapStatusForPopup(String unitCode) {

        List<ProgressMapElementResult> progressMapElementResultList = null;

        try {
            Connection conn = SingletonConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select lt.unit_code,lp.lesson_code,topic,element_code, sum(progress_type_weight) fullMark , " +
                    "sum(CASE WHEN progress_type_completed > 0 THEN progress_type_weight ELSE 0 END) currentMark from LessonProgress lp " +
                    "inner join lessontopic lt on (lt.unit_code = lp.unit_code and lt.lesson_code = lp.lesson_code and lt.element_sequence = lp.element_sequence) " +
                    "where lt.unit_code = ? and language = ? and element_code<>'' " +
                    "group by topic order by sequence");

            stmt.setString(1, unitCode);
            stmt.setString(2, UserPreferenceUtils.getInstance().getLanguage());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                ProgressMapElementResult progressMapElementResult = new ProgressMapElementResult();
                progressMapElementResult.topic = rs.getString("topic");
                progressMapElementResult.elementCode = rs.getString("element_code");
                progressMapElementResult.currentMark = rs.getDouble("currentMark");
                progressMapElementResult.fullMark = rs.getDouble("fullMark");
                progressMapElementResult.unitCode = rs.getString("unit_code");
                progressMapElementResult.lessonCode = rs.getString("lesson_code");

                if (null == progressMapElementResultList) {
                    progressMapElementResultList = new ArrayList<ProgressMapElementResult>();
                }

                progressMapElementResultList.add(progressMapElementResult);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return progressMapElementResultList;
    }
}
