package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.DatabaseCursor;

/**
 * Created by kotarou on 9/6/16.
 */
public class MissionTaskStoryMode {

    public String vMissionCode;
    public String vTaskCode;
    public String vClickObject;
    public String vDragObject;
    public String vDropToObject;
    public String vSpeaker;
    public String vEnglishText;
    public String vSwahiliText;
    public String vAudioFile;
    public String vEnglishInstruction;
    public String vSwahiliInstruction;
    public String vInstructionAudioFile;
    public String vInventoryAdd;
    public String vInventoryDrop;
    public int vMaxSize;

    public MissionTaskStoryMode(DatabaseCursor pDatabaseCursor) {
        vMissionCode = pDatabaseCursor.getString(0);
        vTaskCode = pDatabaseCursor.getString(1);
        vClickObject = pDatabaseCursor.getString(5);
        vDragObject = pDatabaseCursor.getString(6);
        vDropToObject = pDatabaseCursor.getString(7);
        vSpeaker = pDatabaseCursor.getString(10);
        vEnglishText = pDatabaseCursor.getString(11);
        vSwahiliText = pDatabaseCursor.getString(12);
        vAudioFile = pDatabaseCursor.getString(13);
        vEnglishInstruction = pDatabaseCursor.getString(14);
        vSwahiliInstruction = pDatabaseCursor.getString(15);
        vInstructionAudioFile = pDatabaseCursor.getString(16);
        vInventoryAdd = pDatabaseCursor.getString(17);
        vInventoryDrop = pDatabaseCursor.getString(18);
        vMaxSize = pDatabaseCursor.getInt(19);
    }

    @Override
    public String toString() {
        return "MissionTaskStoryMode{" +
                "vMissionCode='" + vMissionCode + '\'' +
                ", vTaskCode='" + vTaskCode + '\'' +
                ", vClickObject='" + vClickObject + '\'' +
                ", vDragObject='" + vDragObject + '\'' +
                ", vDropToObject='" + vDropToObject + '\'' +
                ", vSpeaker='" + vSpeaker + '\'' +
                ", vEnglishText='" + vEnglishText + '\'' +
                ", vSwahiliText='" + vSwahiliText + '\'' +
                ", vAudioFile='" + vAudioFile + '\'' +
                ", vEnglishInstruction='" + vEnglishInstruction + '\'' +
                ", vSwahiliInstruction='" + vSwahiliInstruction + '\'' +
                ", vInstructionAudioFile='" + vInstructionAudioFile + '\'' +
                ", vInventoryAdd='" + vInventoryAdd + '\'' +
                ", vInventoryDrop='" + vInventoryDrop + '\'' +
                ", vMaxSize=" + vMaxSize +
                '}';
    }
}
