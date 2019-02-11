package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.Database;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kotarou on 3/4/2017.
 */
public class MissionListStoryMode {
    private ArrayList<MissionStoryMode> vMission = new ArrayList<MissionStoryMode>();
    private ArrayList<String> vMissionCode = new ArrayList<String>();
    private ArrayList<String> vMissionEnable = new ArrayList<String>();
    private MissionStoryModeLogic vMissionLogic;
    private ArrayList<String> vMissionPocket = new ArrayList<String>();
    private ArrayList<String> vAddItem = new ArrayList<String>();
    private ArrayList<String> vDropItem = new ArrayList<String>();

    public MissionListStoryMode(Database database){
        vMissionLogic = new MissionStoryModeLogic(database);
        vMissionCode = vMissionLogic.getMissionCodeList();
        vMissionPocket = vMissionLogic.getMissionPocket();

        for (int i = 0; i<vMissionCode.size() ; i++){
            vMission.add(vMissionLogic.getMission(vMissionCode.get(i)));
        }

        checkEnable();
    }

    private void checkEnable(){
        vMissionEnable.clear();


        for (int i = 0; i< vMission.size();i++){
            Boolean tEnable = true;
            ArrayList<String> checklist = new ArrayList(Arrays.asList(vMission.get(i).prereq.split(",")));
            for (int j= 0; j < vMission.size();j ++){
                for (int k = 0; k<checklist.size();k++){
                    if (!vMission.get(j).vMissionCompleted && vMission.get(j).vMissionCode.equals(checklist.get(k).trim())){
                            tEnable = false;
                    }
                }
            }
            if (vMission.get(i).vMissionCompleted){
                tEnable = false;
            }
            vMission.get(i).vEnable = tEnable;
            if (tEnable){
                vMissionEnable.add(vMission.get(i).vMissionCode);
            }
        }
    }

    public void restartGame() {
        vMissionLogic.restartGame();
    }

    public void setMissionCompleted(String pMissionCode){
        for(int i=0;i<vMission.size();i++) {
            if (vMission.get(i).vMissionCode.equals(pMissionCode)) {
                Boolean vUpdateStatus = vMissionLogic.updateMissionCompleted(pMissionCode);
                if (vUpdateStatus) {
                    vMissionLogic.updateMissionPocket(vMissionPocket);
                    vMission.get(i).setCompelted();
                }
            }
        }
        checkEnable();
    }

    public void addItemToPocketList(String pImage){
        vMissionPocket.add(pImage);
        vAddItem.add(pImage);
    }
    public ArrayList<String> getPocketList(){
        return vMissionPocket;
    }
    public void removeItemFromPocketList(String pImage){
        ArrayList<String> finalMissionPocket=vMissionPocket;
        for (int i = 0; i<vMissionPocket.size(); i++) {
            if (vMissionPocket.get(i).equals(pImage)) {
                vMissionPocket.remove(i);
                vDropItem.add(pImage);
            }
        }
    }

    public ArrayList<MissionStoryMode> getEnabledMission(){
        ArrayList<MissionStoryMode> retMission = new ArrayList<MissionStoryMode>();

        for(int i=0;i<vMission.size();i++){
            if (vMission.get(i).vEnable){
                retMission.add(vMission.get(i));
            }
        }
        return retMission;
    }

    public ArrayList<MissionStoryMode> getAllMission(){
        return vMission;
    }

    public MissionStoryMode getMission(String pMissionCode){
        for (int i = 0; i< vMission.size(); i++){
            if (vMission.get(i).vMissionCode.equals(pMissionCode)){
                return vMission.get(i);
            }
        }
        return new MissionStoryMode("-1");
    }

    public ArrayList<String> getEnabledMissionCode(){
        return vMissionEnable;
    }

    public ArrayList<String> getAllMissionCode(){
        return vMissionCode;
    }

    public StoryModeScenePath getScenePath(String pFrom, String pTo){
        return vMissionLogic.getScenePath(pFrom, pTo);
    }
}
