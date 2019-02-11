package com.maqautocognita.bo;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public abstract class AbstractSentence {

    public String topic;

    //the entire sentence
    public String sentence;

    //the entire sentence
    public String writingSentence;

    //part of speech for each word in the correct sequence as it appears in the sentence.
    // This is used for two purposes: 1) show which color block to use with each word, and 2) show which color underline to show
    public String sentencePartOfSpeech;

    //part of speech for each word in the correct sequence as it appears in the writingSentence.
    public String writingPartOfSpeech;

    //all the words used in that sentence in the correct order
    public String words;


    //part of speech for each word in the correct sequence as it appears in the words.
    public String wordsPartOfSpeech;

    //the filename of the audio which is always play the instruction
    public String audioFileName;

}
