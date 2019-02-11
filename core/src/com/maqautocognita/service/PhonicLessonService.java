package com.maqautocognita.service;

import com.maqautocognita.bo.Activity;
import com.maqautocognita.bo.Question;
import com.maqautocognita.bo.WordSoundMapping;
import com.maqautocognita.bo.WordSoundMappingList;
import com.maqautocognita.constant.LessonUnitCode;
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
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicLessonService extends AbstractLessonService {

    private static PhonicLessonService instance = null;

    private List<String> swahiliSyllableList;

    public static PhonicLessonService getInstance() {
        if (instance == null) {
            instance = new PhonicLessonService();
        }
        return instance;
    }

    @Override
    protected void afterInitAllLesson() {
        super.afterInitAllLesson();
        if (UserPreferenceUtils.getInstance().isSwahili()) {
            try {
                Connection conn = SingletonConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement("select sound,audio_file_name from PhonicSoundAudioFileMapping where language = ?");
                stmt.setString(1, UserPreferenceUtils.getInstance().getLanguage());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    SoundService.getInstance().addPhonicAudioFileName(rs.getString("sound"), rs.getString("audio_file_name"));
                }
                rs.close();
                stmt.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected String getUnitCode() {
        return LessonUnitCode.PHONIC_UNIT_1.code;
    }

    @Override
    public String getLetterAudioFileName(String letter) {

        String audioFileName = SoundService.getInstance().getPhonicAudioFileName(letter);

        if (null == audioFileName) {

            try {
                Connection conn = SingletonConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement("select audio_file_name from PhonicSoundAudioFileMapping where sound = ? and language = ?");
                stmt.setString(1, letter);
                stmt.setString(2, UserPreferenceUtils.getInstance().getLanguage());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    audioFileName = StringUtils.trim(rs.getString("audio_file_name"));
                    SoundService.getInstance().addPhonicAudioFileName(letter, audioFileName);

                }
                rs.close();
                stmt.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return audioFileName;
    }

    @Override
    protected String getAudioFileNameKey(Activity activity) {
        return activity.getPhonic();
    }

    @Override
    protected void setAdditionalInformationForActivity(Activity activity, ResultSet resultSet) throws SQLException {

        activity.setEnableKeys(StringUtils.removeAllSpace(resultSet.getString("enable_phonics_keys")));

        if (SectionUtils.isGrouping(activity.getActivityCode())) {
            Question question = new Question();
            question.setQuestions(StringUtils.removeAllSpace(resultSet.getString("grouping_words")));
            question.setAnswers(StringUtils.removeAllSpace(resultSet.getString("grouping_answers")));
            activity.setQuestion(question);
        } else if (SectionUtils.isWordBlend(activity.getActivityCode())) {
            activity.setWordBlendMap(getWordBlendMap(resultSet));
        } else if (SectionUtils.isListenAndType(activity.getActivityCode())) {
            activity.setListenAndTypeMap(getListenAndTypeMap(resultSet));
        } else if (SectionUtils.isFillInTheBlanks(activity.getActivityCode())) {

            Question question = new Question();
            question.setQuestions(StringUtils.removeAllSpace(resultSet.getString("fill_in_the_blanks_words")));
            question.setAnswers(StringUtils.removeAllSpace(resultSet.getString("fill_in_the_blanks_answers")));
            question.setChoices(StringUtils.removeAllSpace(resultSet.getString("fill_in_the_blanks_choices")));
            activity.setQuestion(question);
        }
    }

    @Override
    protected String getAudioFileName(String fileName, String word, String letter) {
        if ("word_***".equals(fileName)) {
            fileName = SoundService.getInstance().getWordAudioFilename(word);
        } else if ("phonic sound_***".equals(fileName) || "syllable_***".equals(fileName)) {
            if (StringUtils.isNotBlank(letter)) {
                fileName = SoundService.getInstance().getPhonicAudioFileName(letter);
            }
        } else if ("alphabet letter_***".equals(fileName)) {
            if (StringUtils.isNotBlank(letter)) {
                fileName = SoundService.getInstance().getAlphabetAudioFileName(letter);
            }
        }

        return fileName;
    }

    private HashMap<String, WordSoundMappingList> getListenAndTypeMap(ResultSet resultSet) throws SQLException {

        HashMap<String, WordSoundMappingList> wordSoundMap = new LinkedHashMap<String, WordSoundMappingList>();

        getWordSoundList(resultSet, wordSoundMap, "listen_and_type_begin_sound_words", null, true);
        getWordSoundList(resultSet, wordSoundMap, "listen_and_type_long_instruction_words", "listen_and_type_long_instruction", false);
        getWordSoundList(resultSet, wordSoundMap, "listen_and_type_short_instruction_words", null, false);
        getWordSoundList(resultSet, wordSoundMap, "listen_and_type_no_instruction_words", null, false);

        return wordSoundMap;
    }

    private HashMap<String, WordSoundMappingList> getWordBlendMap(ResultSet resultSet) throws SQLException {

        HashMap<String, WordSoundMappingList> wordSoundMap = new LinkedHashMap<String, WordSoundMappingList>();

        getWordSoundList(resultSet, wordSoundMap, "word_blend_long_instruction_words", "word_blend_long_instruction", false);
        getWordSoundList(resultSet, wordSoundMap, "word_blend_short_instruction_words", null, false);
        getWordSoundList(resultSet, wordSoundMap, "word_blend_no_instruction_words", null, false);

        return wordSoundMap;
    }

    private void getWordSoundList(ResultSet resultSet, HashMap<String, WordSoundMappingList> wordSoundMap, String wordBlendWordsColumnName,
                                  String wordBlendInstructionName, boolean isPlayFirstSoundOnly) throws SQLException {
        String wordBlendsInDatabase = StringUtils.removeAllSpace(resultSet.getString(wordBlendWordsColumnName));

        if (StringUtils.isNotBlank(wordBlendsInDatabase)) {
            String[] wordBlends = wordBlendsInDatabase.split(",");
            for (String word : wordBlends) {

                String instructionName = null;
                if (StringUtils.isNotBlank(wordBlendInstructionName)) {
                    instructionName = resultSet.getString(wordBlendInstructionName);
                }

                List<WordSoundMapping> wordSoundList = getWordSoundList(word, instructionName);
                if (CollectionUtils.isNotEmpty(wordSoundList)) {

                    WordSoundMappingList wordSoundMappingList = new WordSoundMappingList();
                    wordSoundMappingList.setPlayFirstSoundOnly(isPlayFirstSoundOnly);
                    wordSoundMappingList.setWordSoundMappingList(wordSoundList);

                    wordSoundMap.put(word, wordSoundMappingList);
                }
            }
        }
    }

    private List<WordSoundMapping> getWordSoundList(String word, String instructionName) throws SQLException {
        List<WordSoundMapping> wordSoundList = null;
        Connection conn = SingletonConnectionPool.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from words where word = ?");
        stmt.setString(1, word);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {

            if (null == wordSoundList) {
                wordSoundList = new ArrayList<WordSoundMapping>();
            }

            getWordSound(rs, instructionName, 1, word, wordSoundList);
            getWordSound(rs, instructionName, 2, word, wordSoundList);
            getWordSound(rs, instructionName, 3, word, wordSoundList);
            getWordSound(rs, instructionName, 4, word, wordSoundList);

        }
        rs.close();
        stmt.close();

        return wordSoundList;

    }

    private void getWordSound(ResultSet resultSet, String voScriptName, int soundSequence, String word,
                              List<WordSoundMapping> wordSoundMappingList) throws SQLException {

        String name = null;

        if (1 == soundSequence) {
            name = "first";
        } else if (2 == soundSequence) {
            name = "second";
        } else if (3 == soundSequence) {
            name = "third";
        } else if (4 == soundSequence) {
            name = "fourth";
        }

        String sound = resultSet.getString(name + "_sound");
        if (StringUtils.isNotBlank(sound)) {
            WordSoundMapping wordSoundMapping = new WordSoundMapping();
            if (StringUtils.isNotBlank(voScriptName)) {
                wordSoundMapping.setAudioFileName(getVoScriptAudioFileNameList(voScriptName + soundSequence, word, null));
            }
            wordSoundMapping.setSound(sound);
            wordSoundMapping.setStartIndex(resultSet.getInt(name + "_sound_position"));
            wordSoundMapping.setLetterLength(resultSet.getInt(name + "_sound_length"));
            wordSoundMappingList.add(wordSoundMapping);
        }

    }

    /**
     * This is mainly used to init the letter sound mapping for phonic module, for example the letter "qu" showing on the screen, but the phonic sound is "kw"
     */
    public void initPhonicSoundMapping() {
        Connection conn = null;
        try {
            conn = SingletonConnectionPool.getInstance().getConnection();

            PreparedStatement stmt = conn.prepareStatement("select unit_code,element_code,phonic from LessonAndReview where element_code is not null and element_code<>'' and phonic is not null and phonic<>'' and language = ? ");
            stmt.setString(1, UserPreferenceUtils.getInstance().getLanguage());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SoundService.getInstance().addPhonicSoundMapping(rs.getString("unit_code"), rs.getString("element_code"), rs.getString("phonic"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public boolean isSyllable(String syllable) {
        if (null == swahiliSyllableList) {
            swahiliSyllableList = new ArrayList<String>();
            try {
                Connection conn = SingletonConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement("select syllable from SwahiliSyllable");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    swahiliSyllableList.add(rs.getString("syllable"));
                }
                rs.close();
                stmt.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return swahiliSyllableList.contains(syllable);
    }


}