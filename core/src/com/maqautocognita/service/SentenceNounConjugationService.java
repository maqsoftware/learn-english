package com.maqautocognita.service;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.bo.SentenceNounConjugation;
import com.maqautocognita.prototype.databases.DatabaseCursor;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class SentenceNounConjugationService {

    private static SentenceNounConjugationService instance = null;
    private List<SentenceNounConjugation> sentenceNounConjugationList;

    public static SentenceNounConjugationService getInstance() {
        if (instance == null) {
            instance = new SentenceNounConjugationService();
        }
        return instance;
    }

    public List<SentenceNounConjugation> getAllSentenceNounConjugation() {
        if (null == sentenceNounConjugationList) {
            DatabaseCursor databaseCursor = null;
            try {
                databaseCursor = AutoCognitaGame.storyModeDatabase.
                        rawQuery("select * from SentenceNounConjugation");
                while (databaseCursor.next()) {

                    if (null == sentenceNounConjugationList) {
                        sentenceNounConjugationList = new ArrayList<SentenceNounConjugation>();
                    }
                    SentenceNounConjugation sentenceNounConjugation = new SentenceNounConjugation();
                    sentenceNounConjugation.english = databaseCursor.getString(databaseCursor.getColumnIndex("english"));
                    sentenceNounConjugation.swahili = databaseCursor.getString(databaseCursor.getColumnIndex("swahili"));
                    sentenceNounConjugation.prefixPlural = databaseCursor.getString(databaseCursor.getColumnIndex("prefix_plural"));
                    sentenceNounConjugation.prefixSingular = databaseCursor.getString(databaseCursor.getColumnIndex("prefix_singular"));
                    sentenceNounConjugation.wordPlural = databaseCursor.getString(databaseCursor.getColumnIndex("word_plural"));
                    sentenceNounConjugation.wordSingular = databaseCursor.getString(databaseCursor.getColumnIndex("word_singular"));
                    sentenceNounConjugation.root = databaseCursor.getString(databaseCursor.getColumnIndex("root"));
                    sentenceNounConjugationList.add(sentenceNounConjugation);

                }
            } catch (Exception e) {
                Gdx.app.error(getClass().getName(), "", e);
            } finally {
                if (null != databaseCursor) {
                    databaseCursor.close();
                }
            }
        }

        return sentenceNounConjugationList;
    }
}
