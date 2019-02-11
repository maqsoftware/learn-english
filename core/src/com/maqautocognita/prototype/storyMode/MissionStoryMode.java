package com.maqautocognita.prototype.storyMode;

import java.util.ArrayList;

/**
 * Created by kotarou on 9/6/16.
 */
public class MissionStoryMode {

    public String vMissionCode;
    public ArrayList<MissionTaskStoryMode> vMissionTaskList = new ArrayList<MissionTaskStoryMode>();
    public String prereq;
    public StoryModeScene vStoryModeScene; // Mission Scene
    public StoryModeScene vAdditionalSceneImage = new StoryModeScene("Additional"); // Additional object for the Scene, include the object location
    public String vSceneLocation; // where to trigger the Mission

    public String vObjectOnScreen;
    public ArrayList<String> vObjectOnScreenList = new ArrayList<String>();
    public String vObjectLocation;
    public String vEnglishText;
    public String vSwahiliText;
    public String vAudioFile;
    public String vEnglishInstruction;
    public String vSwahiliInstruction;
    public String vInstructionAudioFile;
    public int vMaxSize;
    public Boolean vEnable = false;
    public Boolean vMissionCompleted = false;

    /**
     * Mainly used to indicate if the title is already shown in the screen, as the rule, the title will only show once, if  the title shown, this flag will set to true
     */
    public boolean isTitleShown;

    public ArrayList<StoryModeLessonElement> vStoryModeLessonElement = new ArrayList<StoryModeLessonElement>();

    // create StoryModeScene
    public MissionStoryMode(String pMissionCode) {
        vMissionCode = pMissionCode;
    }


    public void setCompelted() {
        vMissionCompleted = true;
    }

    public Boolean getCompleted() {
        return this.vMissionCompleted;
    }

    public StoryModeLessonElement getLessonElement(String pElementCode) {
        for (int i = 0; i < vStoryModeLessonElement.size(); i++) {
            if (vStoryModeLessonElement.get(i).getElementCode().equals(pElementCode)) {
                return vStoryModeLessonElement.get(i);
            }
        }
        return null;
    }


}
