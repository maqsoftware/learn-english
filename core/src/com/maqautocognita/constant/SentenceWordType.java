package com.maqautocognita.constant;

import com.maqautocognita.utils.ArrayUtils;
import com.badlogic.gdx.Gdx;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public enum SentenceWordType {

    PRONOUN("pn"), VERB("v"), NOUN("n"), CONJUNCTION("conj"),
    ARTICLE("art", "def"), PREPOSITION("prep"), ADJECTIVE("adj"), ADVERB("adv"), POSSESSIVE_PRONOUN("ppn");

    private String[] wordTypes;

    SentenceWordType(String... wordTypes) {
        this.wordTypes = wordTypes;
    }

    public static SentenceWordType getByWordType(String wordType) {
        for (SentenceWordType sentenceWordType : SentenceWordType.values()) {
            if (ArrayUtils.isContainIgnoreCase(sentenceWordType.wordTypes, wordType.trim())) {
                return sentenceWordType;
            }
        }

        Gdx.app.error(SentenceWordType.class.getName(), "cannot find the word type = " + wordType);

        return null;
    }

    public static boolean isPronoun(String wordType) {
        return ArrayUtils.isContainIgnoreCase(PRONOUN.wordTypes, wordType);
    }

    public static boolean isVerb(String wordType) {
        return ArrayUtils.isContainIgnoreCase(VERB.wordTypes, wordType);
    }

    public static boolean isNoun(String wordType) {
        return ArrayUtils.isContainIgnoreCase(NOUN.wordTypes, wordType);
    }

    public static boolean isConjunction(String wordType) {
        return ArrayUtils.isContainIgnoreCase(CONJUNCTION.wordTypes, wordType);
    }

    public static boolean isArticle(String wordType) {
        return ArrayUtils.isContainIgnoreCase(ARTICLE.wordTypes, wordType);
    }

    public static boolean isPreposition(String wordType) {
        return ArrayUtils.isContainIgnoreCase(PREPOSITION.wordTypes, wordType);
    }

    public static boolean isAdjective(String wordType) {
        return ArrayUtils.isContainIgnoreCase(ADJECTIVE.wordTypes, wordType);
    }

    public static boolean isAdverb(String wordType) {
        return ArrayUtils.isContainIgnoreCase(ADVERB.wordTypes, wordType);
    }

    public static boolean isPossessivePronoun(String wordType) {
        return ArrayUtils.isContainIgnoreCase(POSSESSIVE_PRONOUN.wordTypes, wordType);
    }

    public boolean isEquals(String wordType) {
        return ArrayUtils.isContainIgnoreCase(wordTypes, wordType);
    }
}
