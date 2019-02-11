package com.maqautocognita.prototype.sentence;

import com.maqautocognita.prototype.databases.Database;
import com.maqautocognita.prototype.databases.DatabaseCursor;

import java.util.ArrayList;

/**
 * Created by kotarou on 30/5/16.
 */
public class SentenceLogic {
    Database vDatabase;

    public SentenceLogic(Database database) {
        vDatabase = database;
    }

    public ArrayList<SentenceAdjective> getSentenceAdjective(){
        DatabaseCursor vDatabaseCursor = null;
        SentenceAdjective vSentenceAdjective;
        ArrayList<SentenceAdjective> retSentenceAdjective = new ArrayList<SentenceAdjective>();

        try {
            vDatabaseCursor = vDatabase.rawQuery("select Stem, Type from SentenceAdjective");

            if (vDatabaseCursor.moveToFirst()) {
                do {
                    // get all data
                    vSentenceAdjective = new SentenceAdjective();
                    vSentenceAdjective.vStem = vDatabaseCursor.getString(0);
                    vSentenceAdjective.vType = vDatabaseCursor.getString(1);
                    retSentenceAdjective.add(vSentenceAdjective);
                } while (vDatabaseCursor.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
        }
        return retSentenceAdjective;
    }

    public ArrayList<SentenceAdverb> getSentenceAdverb(){
        DatabaseCursor vDatabaseCursor = null;
        SentenceAdverb vSentenceAdverb;
        ArrayList<SentenceAdverb> retSentenceAdverb = new ArrayList<SentenceAdverb>();

        try {
            vDatabaseCursor = vDatabase.rawQuery("select Stem from SentenceAdverb");

            if (vDatabaseCursor.moveToFirst()) {
                do {
                    // get all data
                    vSentenceAdverb = new SentenceAdverb();
                    vSentenceAdverb.vStem = vDatabaseCursor.getString(0);
                    retSentenceAdverb.add(vSentenceAdverb);
                } while (vDatabaseCursor.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
        }
        return retSentenceAdverb;
    }

    public ArrayList<SentenceArticle> getSentenceArticle(){
        DatabaseCursor vDatabaseCursor = null;
        SentenceArticle vSentenceArticle = new SentenceArticle();
        ArrayList<SentenceArticle> retSentenceArticle = new ArrayList<SentenceArticle>();

        try {
            vDatabaseCursor = vDatabase.rawQuery("select Stem, Type, Countable, Vowel from SentenceArticle");

            if (vDatabaseCursor.moveToFirst()) {
                do {
                    // get all data
                    vSentenceArticle = new SentenceArticle();
                    vSentenceArticle.vStem = vDatabaseCursor.getString(0);
                    vSentenceArticle.vType = vDatabaseCursor.getString(1);
                    vSentenceArticle.vCountable = vDatabaseCursor.getString(2);
                    vSentenceArticle.vVowel = vDatabaseCursor.getString(3);
                    retSentenceArticle.add(vSentenceArticle);
                } while (vDatabaseCursor.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
        }
        return retSentenceArticle;
    }

    public ArrayList<SentenceClauseStructure> getSentenceClauseStructure(){
        DatabaseCursor vDatabaseCursor = null;
        SentenceClauseStructure vSentenceClauseStructure = new SentenceClauseStructure();
        ArrayList<SentenceClauseStructure> retSentenceClauseStructure = new ArrayList<SentenceClauseStructure>();

        try {
            vDatabaseCursor = vDatabase.rawQuery("select State, PoSPattern, ClauseType, " +
                    "addbefore, addafter1, addafter2, delete1, delete2, change1, change2, change3 from SentenceClauseStructure");

            if (vDatabaseCursor.moveToFirst()) {
                do {
                    // get all data
                    vSentenceClauseStructure = new SentenceClauseStructure();
                    vSentenceClauseStructure.vState = vDatabaseCursor.getString(0);
                    vSentenceClauseStructure.setvPoSPattern(vDatabaseCursor.getString(1));
                    vSentenceClauseStructure.vClauseType = vDatabaseCursor.getString(2);
                    vSentenceClauseStructure.setvAddBefore(vDatabaseCursor.getString(3));
                    vSentenceClauseStructure.setvAddAfter1(vDatabaseCursor.getString(4));
                    vSentenceClauseStructure.setvAddAfter2(vDatabaseCursor.getString(5));
                    vSentenceClauseStructure.setvDelete1(vDatabaseCursor.getString(6));
                    vSentenceClauseStructure.setvDelete2(vDatabaseCursor.getString(7));
                    vSentenceClauseStructure.setvChange1(vDatabaseCursor.getString(8));
                    vSentenceClauseStructure.setvChange2(vDatabaseCursor.getString(9));
                    vSentenceClauseStructure.setvChange3(vDatabaseCursor.getString(10));
                    retSentenceClauseStructure.add(vSentenceClauseStructure);
                } while (vDatabaseCursor.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
        }
        return retSentenceClauseStructure;
    }

    public ArrayList<SentenceConjunction> getSentenceConjunction(){
        DatabaseCursor vDatabaseCursor = null;
        SentenceConjunction vSentenceConjunction = new SentenceConjunction();
        ArrayList<SentenceConjunction> retSentenceConjunction = new ArrayList<SentenceConjunction>();

        try {
            vDatabaseCursor = vDatabase.rawQuery("select Stem from SentenceAdverb");

            if (vDatabaseCursor.moveToFirst()) {
                do {
                    // get all data
                    vSentenceConjunction = new SentenceConjunction();
                    vSentenceConjunction.vStem = vDatabaseCursor.getString(0);
                    retSentenceConjunction.add(vSentenceConjunction);
                } while (vDatabaseCursor.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
        }
        return retSentenceConjunction;
    }

    public ArrayList<SentenceNoun> getSentenceNoun(){
        DatabaseCursor vDatabaseCursor = null;
        SentenceNoun vSentenceNoun = new SentenceNoun();
        ArrayList<SentenceNoun> retSentenceNoun = new ArrayList<SentenceNoun>();

        try {
            vDatabaseCursor = vDatabase.rawQuery("select Stem, Type, Noun3S, Noun3P, Pronoun3S, Pronoun3P, Countable, Vowel from SentenceNoun");

            if (vDatabaseCursor.moveToFirst()) {
                do {
                    // get all data
                    vSentenceNoun = new SentenceNoun();
                    vSentenceNoun.vStem = vDatabaseCursor.getString(0);
                    vSentenceNoun.vType = vDatabaseCursor.getString(1);
                    vSentenceNoun.vNoun3S = vDatabaseCursor.getString(2);
                    vSentenceNoun.vNoun3P = vDatabaseCursor.getString(3);
                    vSentenceNoun.vPronoun3S = vDatabaseCursor.getString(4);
                    vSentenceNoun.vPronoun3P = vDatabaseCursor.getString(5);
                    vSentenceNoun.vCountable = vDatabaseCursor.getString(6);
                    vSentenceNoun.vVowel = vDatabaseCursor.getString(7);
                    retSentenceNoun.add(vSentenceNoun);
                } while (vDatabaseCursor.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
        }
        return retSentenceNoun;
    }

    public ArrayList<SentencePreposition> getSentencePreposition(){
        DatabaseCursor vDatabaseCursor = null;
        SentencePreposition vSentencePreposition = new SentencePreposition();
        ArrayList<SentencePreposition> retSentencePreposition = new ArrayList<SentencePreposition>();

        try {
            vDatabaseCursor = vDatabase.rawQuery("select Stem from SentencePreposition");

            if (vDatabaseCursor.moveToFirst()) {
                do {
                    // get all data
                    vSentencePreposition = new SentencePreposition();
                    vSentencePreposition.vStem = vDatabaseCursor.getString(0);
                    retSentencePreposition.add(vSentencePreposition);
                } while (vDatabaseCursor.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
        }
        return retSentencePreposition;
    }

    public ArrayList<SentencePronoun> getSentencePronoun(){
        DatabaseCursor vDatabaseCursor = null;
        SentencePronoun vSentencePronoun = new SentencePronoun();
        ArrayList<SentencePronoun> retSentencePronoun = new ArrayList<SentencePronoun>();

        try {
            vDatabaseCursor = vDatabase.rawQuery("select Stem, Person, SubjectPN, ObjectPN, SubjectPPN, Reflexive from SentencePronoun where Enable = 'Y'");

            if (vDatabaseCursor.moveToFirst()) {
                do {
                    // get all data
                    vSentencePronoun = new SentencePronoun();
                    vSentencePronoun.vStem = vDatabaseCursor.getString(0);
                    vSentencePronoun.vPerson = vDatabaseCursor.getString(1);
                    vSentencePronoun.vSubjectPN = vDatabaseCursor.getString(2);
                    vSentencePronoun.vObjectPN = vDatabaseCursor.getString(3);
                    vSentencePronoun.vSubjectPPN = vDatabaseCursor.getString(4);
                    vSentencePronoun.vReflexive = vDatabaseCursor.getString(5);
                    retSentencePronoun.add(vSentencePronoun);
                } while (vDatabaseCursor.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
        }
        return retSentencePronoun;
    }

    public ArrayList<SentenceStructure> getSentenceStructure(){
        DatabaseCursor vDatabaseCursor = null;
        SentenceStructure vSentenceStructure = new SentenceStructure();
        ArrayList<SentenceStructure> retSentenceStructure = new ArrayList<SentenceStructure>();

        try {
            vDatabaseCursor = vDatabase.rawQuery("select State, ClausePattern, Punc, " +
                    "addafter1, addafter2, delete1, delete2, delete3 from SentenceStructure");

            if (vDatabaseCursor.moveToFirst()) {
                do {
                    // get all data
                    vSentenceStructure = new SentenceStructure();
                    vSentenceStructure.vState = vDatabaseCursor.getString(0);
                    vSentenceStructure.setvClausePattern(vDatabaseCursor.getString(1));
                    vSentenceStructure.vPunc = vDatabaseCursor.getString(2);
                    vSentenceStructure.setvAddAfter1(vDatabaseCursor.getString(3));
                    vSentenceStructure.setvAddAfter1(vDatabaseCursor.getString(4));
                    vSentenceStructure.setvDelete1(vDatabaseCursor.getString(5));
                    vSentenceStructure.setvDelete2(vDatabaseCursor.getString(6));
                    vSentenceStructure.setvDelete3(vDatabaseCursor.getString(7));
                    retSentenceStructure.add(vSentenceStructure);
                } while (vDatabaseCursor.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
        }
        return retSentenceStructure;
    }


    public ArrayList<SentenceVerb> getSentenceVerb(){
        DatabaseCursor vDatabaseCursor = null;
        SentenceVerb vSentenceVerb = new SentenceVerb();
        ArrayList<SentenceVerb> retSentenceVerb = new ArrayList<SentenceVerb>();

        try {
            vDatabaseCursor = vDatabase.rawQuery("select Stem, Tense, Person, Result, PrepositionRequired, VerbRequired, AdjectivePossible, ToVerbPossible, PrepositionChoices from SentenceVerb");

            if (vDatabaseCursor.moveToFirst()) {
                do {
                    // get all data
                    vSentenceVerb = new SentenceVerb();
                    vSentenceVerb.vStem = vDatabaseCursor.getString(0);
                    vSentenceVerb.vTenseText = vDatabaseCursor.getString(1);
                    if (vDatabaseCursor.getString(1).equals("Past")) {
                        vSentenceVerb.vTense = SentenceTenseEnum.PAST;
                    }else if(vDatabaseCursor.getString(1).equals("Present")){
                        vSentenceVerb.vTense = SentenceTenseEnum.PRESENT;
                    }else if(vDatabaseCursor.getString(1).equals("Future")){
                        vSentenceVerb.vTense = SentenceTenseEnum.FUTURE;
                    }else if(vDatabaseCursor.getString(1).equals("Past Continuous")){
                        vSentenceVerb.vTense = SentenceTenseEnum.PASTCONTINUOUS;
                    }else if(vDatabaseCursor.getString(1).equals("Present Continuous")){
                        vSentenceVerb.vTense = SentenceTenseEnum.PRESENTCONTINUOUS;
                    }else if(vDatabaseCursor.getString(1).equals("Future Continuous")){
                        vSentenceVerb.vTense = SentenceTenseEnum.FUTURECONTINUOUS;
                    }
                    vSentenceVerb.vPerson = vDatabaseCursor.getString(2);
                    vSentenceVerb.vResult = vDatabaseCursor.getString(3);
                    vSentenceVerb.vPrepositionRequired = vDatabaseCursor.getString(4);
                    vSentenceVerb.vVerbRequired = vDatabaseCursor.getString(5);
                    vSentenceVerb.vAdjectivePossible = vDatabaseCursor.getString(6);
                    vSentenceVerb.vToVerbPossible = vDatabaseCursor.getString(7);
                    vSentenceVerb.vPrepositionChoices = vDatabaseCursor.getString(8);
                    retSentenceVerb.add(vSentenceVerb);
                } while (vDatabaseCursor.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
            if (null != vDatabaseCursor) {
                vDatabaseCursor.close();
            }
        }
        return retSentenceVerb;
    }
}
