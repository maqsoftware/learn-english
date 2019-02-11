package com.maqautocognita.utils;

import com.maqautocognita.constant.SentenceWordType;
import com.badlogic.gdx.Gdx;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class SentenceWordTypeUtils {

    public static String getImageNameByWordType(String wordType) {

        String imageName = null;

        if (StringUtils.isNotBlank(wordType)) {
            wordType = wordType.trim();
            if (SentenceWordType.isPronoun(wordType)) {
                imageName = getPronounImageName();
            } else if (SentenceWordType.isVerb(wordType)) {
                imageName = getVerbImageName();
            } else if (SentenceWordType.isNoun(wordType)) {
                imageName = getNounImageName();
            } else if (SentenceWordType.isConjunction(wordType)) {
                imageName = getConjunctionImageName();
            } else if (SentenceWordType.isArticle(wordType)) {
                imageName = getArticleImageName();
            } else if (SentenceWordType.isPreposition(wordType)) {
                imageName = getPrepositionImageName();
            } else if (SentenceWordType.isAdjective(wordType)) {
                imageName = getAdjectiveImageName();
            } else if (SentenceWordType.isAdverb(wordType)) {
                imageName = getAdverbImageName();
            } else if (SentenceWordType.isPossessivePronoun(wordType)) {
                imageName = getPossessivePronounImageName();
            }

            if (StringUtils.isBlank(imageName)) {
                Gdx.app.error(SentenceWordTypeUtils.class.getName(), "missing image file for the wordType = " + wordType);
            }
        }

        return imageName;
    }

    private static String getPronounImageName() {
        return "pronoun";
    }

    private static String getVerbImageName() {
        return "verb";
    }

    private static String getNounImageName() {
        return "noun";
    }

    private static String getConjunctionImageName() {
        return "conjunction";
    }

    private static String getArticleImageName() {
        return "article";
    }

    private static String getPrepositionImageName() {
        return "preposition";
    }

    private static String getAdjectiveImageName() {
        return "adjective";
    }

    private static String getAdverbImageName() {
        return "adverb";
    }

    private static String getPossessivePronounImageName() {
        return "pronoun";
    }

    public static String getImageNameByWordType(SentenceWordType wordType) {

        if (null != wordType) {
            switch (wordType) {
                case PRONOUN:
                    return getPronounImageName();
                case VERB:
                    return getVerbImageName();
                case NOUN:
                    return getNounImageName();
                case CONJUNCTION:
                    return getConjunctionImageName();
                case ARTICLE:
                    return getArticleImageName();
                case PREPOSITION:
                    return getPrepositionImageName();
                case ADJECTIVE:
                    return getAdjectiveImageName();
                case ADVERB:
                    return getAdverbImageName();
                case POSSESSIVE_PRONOUN:
                    return getPossessivePronounImageName();
            }

        }

        Gdx.app.error(SentenceWordType.class.getName(), "cannot find the word type image = " + wordType);

        return null;
    }
}
