package com.maqautocognita.bo;

import com.maqautocognita.constant.ActivityCodeEnum;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class Activity extends AbstractPhonicActivity {

    private int id;

    private String unitCode;

    private String lessonCode;

    /**
     * store the letter which the lesson is going to teach, it will show in the read and listening section and speaking section
     * For example if it is going to teach the letter "A" in alphabet module, it will store "A"
     * For example if it is going to teach the phonic key "ch" in phonic module, it will store "ch", and this letter is
     */
    private String letter;

    /**
     * It is mainly used for the phonic module, to indicate which phonic sound is using for the letter.
     * For example the letter "a_e", the phonic will be _a
     */
    private String phonic;

    private String[] pictures;

    /**
     * Store those word which are used to teach in the lesson, the word is always related to the {@link #letter}
     */
    private String[] words;

    /**
     * Store the audio file name which is used to play the {@link #letter}
     */
    private String audioFileName;

    private boolean selected;

    private boolean passed;

    /**
     * This is mainly store the lesson activityCode which the lesson is belongs to , the activityCode is should be already specify in the {@link ActivityCodeEnum}
     */
    private ActivityCodeEnum activityCode;
    private LessonWithReview parent;

    private int sequence;

    private String instructorPlayback;

    private int audioDuration;

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getLessonCode() {
        return lessonCode;
    }

    public void setLessonCode(String lessonCode) {
        this.lessonCode = lessonCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    public String getAudioFileName() {
        String fileName = audioFileName;
        if ((fileName == null) || (fileName == ""))
            fileName = instructorPlayback;
        return fileName;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ActivityCodeEnum getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(ActivityCodeEnum activityCode) {
        this.activityCode = activityCode;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public LessonWithReview getParent() {
        return parent;
    }

    public void setParent(LessonWithReview parent) {
        this.parent = parent;
    }

    public String getPhonic() {
        return phonic;
    }

    public void setPhonic(String phonic) {
        this.phonic = phonic;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(int audioDuration) {
        this.audioDuration = audioDuration;
    }

    public void setInstructorPlayback(String instructorPlayback) {
        this.instructorPlayback = instructorPlayback;
    }
}
