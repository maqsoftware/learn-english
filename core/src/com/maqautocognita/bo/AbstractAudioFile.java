package com.maqautocognita.bo;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 *         <p/>
 *         Mainly used to store the introduction audio file name and help audio file name for the lesson , reviews or mastery test
 */
public abstract class AbstractAudioFile {

    private List<String> introductionAudioFilenameList;
    private List<String> shortInstructionAudioFilenameList;
    private List<String> helpAudioFilenameList;

    public List<String> getIntroductionAudioFilenameList() {
        return introductionAudioFilenameList;
    }

    public void setIntroductionAudioFilenameList(List<String> introductionAudioFilenameList) {
        this.introductionAudioFilenameList = introductionAudioFilenameList;
    }

    public List<String> getShortInstructionAudioFilenameList() {
        return shortInstructionAudioFilenameList;
    }

    public void setShortInstructionAudioFilenameList(List<String> shortInstructionAudioFilenameList) {
        this.shortInstructionAudioFilenameList = shortInstructionAudioFilenameList;
    }

    public List<String> getHelpAudioFilenameList() {
        return helpAudioFilenameList;
    }

    public void setHelpAudioFilenameList(List<String> helpAudioFilenameList) {
        this.helpAudioFilenameList = helpAudioFilenameList;
    }

}
