package com.maqautocognita.service;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.bo.AbstractSentence;
import com.maqautocognita.bo.Sentence;
import com.maqautocognita.bo.SentenceLesson;
import com.maqautocognita.bo.SentenceWithActivityCode;
import com.maqautocognita.bo.SwahiliSentence;
import com.maqautocognita.constant.ActivityCodeEnum;
import com.maqautocognita.prototype.databases.DatabaseCursor;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by siu-chun.chi on 5/9/2017.
 */

public abstract class AbstractSentenceLessonService {

    protected List<? extends SentenceLesson> englishLessonList;

    private List<? extends SentenceLesson> swahiliLessonList;

    public List<? extends SentenceLesson> getAllLesson() {

        if (UserPreferenceUtils.getInstance().isEnglish()) {
            if (null == englishLessonList) {
                englishLessonList = getLessonList();
            }
            return englishLessonList;
        } else {
            if (null == swahiliLessonList) {
                swahiliLessonList = getLessonList();
            }
            return swahiliLessonList;
        }
    }

    private List<SentenceLesson> getLessonList() {

        List<SentenceLesson> lessonList = null;

        DatabaseCursor databaseCursor = null;
        try {

            String tableName = UserPreferenceUtils.getInstance().isEnglish() ? "SentenceLesson" : "SwahiliSentenceLesson";

            databaseCursor = AutoCognitaGame.storyModeDatabase.
                    rawQuery("select * from " + tableName + " where unit_code = " + getUnitCode());

            SentenceLesson sentenceLesson = null;

            while (databaseCursor.next()) {
                String lessonCode = databaseCursor.getString(databaseCursor.getColumnIndex("lesson_code"));
                String unitCode = databaseCursor.getString(databaseCursor.getColumnIndex("unit_code"));
                int elementCode = databaseCursor.getInt(databaseCursor.getColumnIndex("element_code"));
                if (null == sentenceLesson || !sentenceLesson.lessonCode.equals(lessonCode)) {
                    sentenceLesson = new SentenceLesson();
                    sentenceLesson.unitCode = unitCode;
                    sentenceLesson.lessonCode = lessonCode;
                    sentenceLesson.elementSequence = elementCode;

                    if (null == lessonList) {
                        lessonList = new ArrayList<SentenceLesson>();
                        sentenceLesson.setSelected(true);
                    }

                    sentenceLesson.setPassed(true);
                    lessonList.add(sentenceLesson);
                }

                SentenceWithActivityCode sentenceWithActivityCode =
                        getSentenceWithActivityCode(databaseCursor, "activity_code_1", "audio_for_activity_1");
                if (null != sentenceWithActivityCode) {
                    sentenceLesson.addSentenceWithActivityCode(sentenceWithActivityCode);
                }

                sentenceWithActivityCode = getSentenceWithActivityCode(databaseCursor, "activity_code_2", "audio_for_activity_2");
                if (null != sentenceWithActivityCode) {
                    sentenceLesson.addSentenceWithActivityCode(sentenceWithActivityCode);
                }

            }
        } catch (Exception e) {
            Gdx.app.error(getClass().getName(), "", e);
        } finally {
            if (null != databaseCursor) {
                databaseCursor.close();
            }
        }

        return lessonList;
    }

    protected abstract String getUnitCode();

    private SentenceWithActivityCode getSentenceWithActivityCode(DatabaseCursor databaseCursor, String activityCodeColumnName,
                                                                 String activityCodeAudioColumnName) {

        String activityCodes = databaseCursor.getString(databaseCursor.getColumnIndex(activityCodeColumnName));

        if (StringUtils.isNotBlank(activityCodes)) {

            String[] activityCodeTexts = activityCodes.split(",");

            //only 1 activity will be play, so here random one of if
            String activityCode = activityCodeTexts[new Random().nextInt(activityCodeTexts.length)];

            SentenceWithActivityCode sentenceWithActivityCode = new SentenceWithActivityCode();
            sentenceWithActivityCode.activityCodeEnum = ActivityCodeEnum.getActivityCodeEnumByCode(activityCode);

            if (UserPreferenceUtils.getInstance().isSwahili()) {
                sentenceWithActivityCode.sentence = getSwahiliSentence(databaseCursor, activityCodeAudioColumnName);
            } else {
                sentenceWithActivityCode.sentence = getSentence(databaseCursor, activityCodeAudioColumnName);
            }
            return sentenceWithActivityCode;

        }
        return null;
    }

    private SwahiliSentence getSwahiliSentence(DatabaseCursor databaseCursor, String activityCodeAudioColumnName) {
        SwahiliSentence sentence = new SwahiliSentence();
        getSentence(sentence, databaseCursor, activityCodeAudioColumnName);
        sentence.pronoun = databaseCursor.getString(databaseCursor.getColumnIndex("pronoun"));
        sentence.nounPrefix = databaseCursor.getString(databaseCursor.getColumnIndex("noun_prefix"));
        sentence.nounRoot = databaseCursor.getString(databaseCursor.getColumnIndex("noun_root"));
        sentence.verbSubjectPrefix = databaseCursor.getString(databaseCursor.getColumnIndex("verb_subject_prefix"));
        sentence.verbTensePrefix = databaseCursor.getString(databaseCursor.getColumnIndex("verb_tense_prefix"));
        sentence.verbObjectPrefix = databaseCursor.getString(databaseCursor.getColumnIndex("verb_object_prefix"));
        sentence.verbRoot = databaseCursor.getString(databaseCursor.getColumnIndex("verb_root"));
        sentence.adjectiveRoot = databaseCursor.getString(databaseCursor.getColumnIndex("adjective_root"));
        sentence.words = databaseCursor.getString(databaseCursor.getColumnIndex("other_words"));
        sentence.verbConjugationPerson = databaseCursor.getString(databaseCursor.getColumnIndex("verb_conjugation_person"));
        sentence.verbConjugationTense = databaseCursor.getString(databaseCursor.getColumnIndex("verb_conjugation_tense"));
        return sentence;
    }

    private Sentence getSentence(DatabaseCursor databaseCursor, String activityCodeAudioColumnName) {
        Sentence sentence = new Sentence();

        getSentence(sentence, databaseCursor, activityCodeAudioColumnName);

        sentence.words = databaseCursor.getString(databaseCursor.getColumnIndex("words"));
        sentence.wordsPartOfSpeech = databaseCursor.getString(databaseCursor.getColumnIndex("words_parts_of_speech"));

        sentence.subject = databaseCursor.getString(databaseCursor.getColumnIndex("subject"));

        StringBuilder predicates = new StringBuilder();

        String action = databaseCursor.getString(databaseCursor.getColumnIndex("action"));

        if (StringUtils.isNotBlank(action)) {
            predicates.append(action.trim() + " ");
        }

        String object = databaseCursor.getString(databaseCursor.getColumnIndex("object"));
        if (StringUtils.isNotBlank(object)) {
            predicates.append(object.trim());
        }

        sentence.predicates = predicates.toString().trim();

        sentence.dependentSubject = databaseCursor.getString(databaseCursor.getColumnIndex("dependent_subject"));
        sentence.dependentAction = databaseCursor.getString(databaseCursor.getColumnIndex("dependent_action"));
        sentence.dependentObject = databaseCursor.getString(databaseCursor.getColumnIndex("dependent_object"));

        sentence.conjunction = databaseCursor.getString(databaseCursor.getColumnIndex("conjunction"));

        return sentence;
    }

    private AbstractSentence getSentence(AbstractSentence sentence, DatabaseCursor databaseCursor, String activityCodeAudioColumnName) {
        sentence.sentence = databaseCursor.getString(databaseCursor.getColumnIndex("sentence"));
        sentence.sentencePartOfSpeech = databaseCursor.getString(databaseCursor.getColumnIndex("sentence_parts_of_speech"));
        int topicColumnIndex = databaseCursor.getColumnIndex("sentence_parts_of_speech");
        if (topicColumnIndex >= 0) {
            sentence.topic = databaseCursor.getString(topicColumnIndex);
        }

        sentence.writingSentence = databaseCursor.getString(databaseCursor.getColumnIndex("writing_sentence"));
        sentence.writingPartOfSpeech = databaseCursor.getString(databaseCursor.getColumnIndex("writing_sentence_parts_of_speech"));

        sentence.audioFileName = databaseCursor.getString(databaseCursor.getColumnIndex(activityCodeAudioColumnName));

        return sentence;
    }
}
