package com.maqautocognita.bo;

import java.util.HashMap;

/**
 * @author sc.chi csc19840914@gmail.com
 *         <p/>
 *         This is used to store the phonic module information which is word blend or listen & type
 */
public abstract class AbstractPhonicActivity extends AbstractAudioFile {

    private HashMap<String, WordSoundMappingList> listenAndTypeMap;

    private HashMap<String, WordSoundMappingList> wordBlendMap;

    /**
     * Store the which phonic key(s) are able to touch in the keyboard
     * <p/>
     * If there is more than 1 key, the pattern will be using comma to separate the keys, for example "a,e,i,o"
     */
    private String enableKeys;

    private Question question;

    public HashMap<String, WordSoundMappingList> getListenAndTypeMap() {
        return listenAndTypeMap;
    }

    public void setListenAndTypeMap(HashMap<String, WordSoundMappingList> listenAndTypeMap) {
        this.listenAndTypeMap = listenAndTypeMap;
    }

    public HashMap<String, WordSoundMappingList> getWordBlendMap() {
        return wordBlendMap;
    }

    public void setWordBlendMap(HashMap<String, WordSoundMappingList> wordBlendMap) {
        this.wordBlendMap = wordBlendMap;
    }


    public String getEnableKeys() {
        return enableKeys;
    }

    public void setEnableKeys(String enableKeys) {
        this.enableKeys = enableKeys;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
