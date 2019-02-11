package com.maqautocognita.service;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.prototype.databases.DatabaseCursor;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class SentenceVerbConjugationService {

    private static SentenceVerbConjugationService instance = null;
    private List<String> verbList;

    public static SentenceVerbConjugationService getInstance() {
        if (instance == null) {
            instance = new SentenceVerbConjugationService();
        }
        return instance;
    }

    public List<String> getAllVerb() {
        if (null == verbList) {
            DatabaseCursor databaseCursor = null;
            try {
                databaseCursor = AutoCognitaGame.storyModeDatabase.
                        rawQuery("select verb from SentenceVerbConjugation");
                while (databaseCursor.next()) {

                    if (null == verbList) {
                        verbList = new ArrayList<String>();
                    }
                    verbList.add(databaseCursor.getString(0));

                }
            } catch (Exception e) {
                Gdx.app.error(getClass().getName(), "", e);
            } finally {
                if (null != databaseCursor) {
                    databaseCursor.close();
                }
            }
        }

        return verbList;
    }
}
