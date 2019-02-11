package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.Database;
import com.maqautocognita.prototype.databases.DatabaseCursor;
import com.maqautocognita.prototype.databases.SQLiteGdxException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kotarou on 30/5/16.
 */
public class MissionStoryModeLogic {
    Database vDatabase;

    public MissionStoryModeLogic(Database database) {
        vDatabase = database;
    }


    public ArrayList<String> getMissionCodeList() {
        ArrayList<String> vRetList = new ArrayList<String>();
        DatabaseCursor vDatabaseMissionList = null;
        try {
            vDatabaseMissionList = vDatabase.rawQuery("select MissionCode from MissionScriptMaster where TaskCode = '00' order by MissionCode");
            if (vDatabaseMissionList.moveToFirst()) {
                do {
                    vRetList.add(vDatabaseMissionList.getString(0));
                } while (vDatabaseMissionList.next());
            }
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseMissionList) {
                vDatabaseMissionList.close();
            }
        }

        return vRetList;
    }


    public MissionStoryMode getMission(String pMissionCode) {
        MissionStoryMode vMissionStoryMode = new MissionStoryMode(pMissionCode);

        DatabaseCursor vDatabaseMissionList = null;
        try {
            vDatabaseMissionList = vDatabase.rawQuery("select MissionCode, TaskCode, Prereq, SceneName, SceneLocation, ClickObject, DragObject, DragTo, ObjectOnScene, ObjectPlacement, Speaker, EnglishText, SwahiliText, TextAudioFile, EnglishInstruction, SwahiliInstruction, InstructionAudioFile, InventoryAdd, InventoryDrop, MaxSize, MissionCompleted from MissionScriptMaster where MissionCode = '" + pMissionCode + "' order by MissionCode");
            if (vDatabaseMissionList.moveToFirst()) {
                do {
                    if (vDatabaseMissionList.getString(1).equals("00")) {
                        vMissionStoryMode.prereq = vDatabaseMissionList.getString(2);
                        vMissionStoryMode.vStoryModeScene = getStoryModeScene(vDatabaseMissionList.getString(3), 1f);
//                       vMissionStoryMode.vAdditionalSceneImage.setSceneDisplayRatio(vMissionStoryMode.vStoryModeScene.getSceneDisplayRatio());
//                       vMissionStoryMode.vAdditionalSceneImage.setToSceneRatio(vMissionStoryMode.vStoryModeScene.getToSceneRatio());
                        vMissionStoryMode.vSceneLocation = vDatabaseMissionList.getString(4);
                        vMissionStoryMode.vObjectOnScreen = vDatabaseMissionList.getString(8);
                        vMissionStoryMode.vObjectOnScreenList = new ArrayList(Arrays.asList(vMissionStoryMode.vObjectOnScreen.split(",")));
                        for (int i = 0; i < vMissionStoryMode.vObjectOnScreenList.size(); i++) {
                            vMissionStoryMode.vObjectOnScreenList.set(i, vMissionStoryMode.vObjectOnScreenList.get(i).trim());
                        }
                        vMissionStoryMode.vObjectLocation = vDatabaseMissionList.getString(9);
                        vMissionStoryMode.vEnglishText = vDatabaseMissionList.getString(11);
                        vMissionStoryMode.vSwahiliText = vDatabaseMissionList.getString(12);
                        vMissionStoryMode.vAudioFile = vDatabaseMissionList.getString(13);
                        vMissionStoryMode.vEnglishInstruction = vDatabaseMissionList.getString(14);
                        vMissionStoryMode.vSwahiliInstruction = vDatabaseMissionList.getString(15);
                        vMissionStoryMode.vInstructionAudioFile = vDatabaseMissionList.getString(16);
                        vMissionStoryMode.vMaxSize = vDatabaseMissionList.getInt(19);
                        if (vDatabaseMissionList.getString(20).equals("completed")) {
                            vMissionStoryMode.vMissionCompleted = true;
                        } else {
                            vMissionStoryMode.vMissionCompleted = false;
                        }
                        vMissionStoryMode.vAdditionalSceneImage = setAdditionalObject(vMissionStoryMode.vObjectOnScreen, vMissionStoryMode.vObjectLocation, vMissionStoryMode.vStoryModeScene, vMissionStoryMode.vSceneLocation, vMissionStoryMode.vMaxSize);

                    } else {

                        vMissionStoryMode.vMissionTaskList.add(new MissionTaskStoryMode(vDatabaseMissionList));

                    }
                } while (vDatabaseMissionList.next());
            }

        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseMissionList) {
                vDatabaseMissionList.close();
            }
        }
        return vMissionStoryMode;
    }

    //=====================
    // get Scene and all scene related information and image
    public StoryModeScene getStoryModeScene(String pSceneName, float pRatio) {

        StoryModeScene vStoryModeScene = new StoryModeScene(pSceneName);

        DatabaseCursor vDatabaseImageCursor = null;

        int vCount = 0;

        // get scene image
        try {

            // get data from database
            vDatabaseImageCursor = vDatabase.rawQuery("select SceneName, ImageName, ImageWidth, ImageHeight, RealWidth from Scene" + " where SceneName = '" + pSceneName + "'");

            // loop for all record for ImageWord
            if (vDatabaseImageCursor.moveToFirst()) {
                vStoryModeScene.initScene(vDatabaseImageCursor, pRatio);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseImageCursor) {
                vDatabaseImageCursor.close();
            }
        }
        // get all location
        try {
            vDatabaseImageCursor = vDatabase.rawQuery("select SceneName, LocationName, CenterX, StartX, EndX from SceneLocation" + " where SceneName = '" + pSceneName + "'");
            // loop for all record for ImageWord
            if (vDatabaseImageCursor.moveToFirst()) {
                vStoryModeScene.initSceneLocation(vDatabaseImageCursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseImageCursor) {
                vDatabaseImageCursor.close();
            }
        }

        // get all SceneImageObject
        try {
            vDatabaseImageCursor = vDatabase.rawQuery("select distinct sm.ImageName, sm.ObjectName, sm.ObjectX, sm.ObjectY, sm.ObjectWidth, sm.ObjectHeight, sm.ObjectRealHeight, sm.Level, sm.Layer, RelatedWord, slio.ContainerX, slio.ContainerY, slio.ContainerWidth, slio.ContainerHeight, smel.EdgeX, smel.EdgeY, smel.EdgeWidth, smel.EdgeHeight from SceneMaking sm left join SceneLessonImageObject slio on sm.ImageName = slio.ImageName left join SceneMakingExtraImg smel on sm.ImageName = smel.WordImageName" + " where sm.SceneName = '" + pSceneName + "' order by Level desc");
            // loop for all record for ImageWord
            if (vDatabaseImageCursor.moveToFirst()) {
                vStoryModeScene.initSceneObject(vDatabaseImageCursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseImageCursor) {
                vDatabaseImageCursor.close();
            }
        }

        // get all SceneAction
        try {
            vDatabaseImageCursor = vDatabase.rawQuery("select SceneName, ObjectName, Gestures, GesturesAction from SceneAction" + " where SceneName = '" + pSceneName + "'");
            // loop for all record for Action
            if (vDatabaseImageCursor.moveToFirst()) {
                vStoryModeScene.initSceneAction(vDatabaseImageCursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseImageCursor) {
                vDatabaseImageCursor.close();
            }
        }

        // get Scene Conjunction Location
        try {
            vDatabaseImageCursor = vDatabase.rawQuery("select ObjectImageName, ToScene, ToSceneObject from SceneConjunction " + " where SceneName = '" + pSceneName + "'");
            // loop for all record for Action
            if (vDatabaseImageCursor.moveToFirst()) {
                vStoryModeScene.initSceneConjunction(vDatabaseImageCursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseImageCursor) {
                vDatabaseImageCursor.close();
            }
        }

        return vStoryModeScene;
    }

    private StoryModeScene setAdditionalObject(String pObjectOnScreen, String pObjectLocation, StoryModeScene pStoryModeScene, String pSceneLocation, int pMaxSize) {
        // if cannot found image from current scene, assume put at floor, left center right, or whole floor
        StoryModeScene vAdditionalSceneImage = new StoryModeScene("Additional");
        vAdditionalSceneImage.setSceneDisplayRatio(pStoryModeScene.getSceneDisplayRatio());
        vAdditionalSceneImage.setToSceneRatio(pStoryModeScene.getToSceneRatio());

        if (!pObjectOnScreen.equals("")) {
            ArrayList<String> vObjectList = new ArrayList(Arrays.asList(pObjectOnScreen.split(",")));
            for (int i = 0; i < vObjectList.size(); i++) {
                StoryModeImage tStoryModeImageLocation = pStoryModeScene.getSceneImageObject(pObjectLocation);

                int vSceneWidth = 1600; // 1920

                if (tStoryModeImageLocation.vObjectName.equals("")) {
                    if (pObjectLocation.equals("left")) {
                        int tSceneCenterLocation = pStoryModeScene.getSceneCenterLocation(pSceneLocation);
                        tStoryModeImageLocation.vObjectName = pObjectLocation;
                        tStoryModeImageLocation.vImageArea.vImageWidth = vSceneWidth / 3;
                        tStoryModeImageLocation.vImageArea.vImageHeight = 150;
                        tStoryModeImageLocation.vSceneLocationX = tSceneCenterLocation - vSceneWidth / 2;
                        tStoryModeImageLocation.vSceneLocationY = 0;
                    } else if (pObjectLocation.equals("center")) {
                        int tSceneCenterLocation = pStoryModeScene.getSceneCenterLocation(pSceneLocation);
                        tStoryModeImageLocation.vObjectName = pObjectLocation;
                        tStoryModeImageLocation.vImageArea.vImageWidth = vSceneWidth / 3;
                        tStoryModeImageLocation.vImageArea.vImageHeight = 150;
                        tStoryModeImageLocation.vSceneLocationX = tSceneCenterLocation - vSceneWidth / 2 + tStoryModeImageLocation.vImageArea.vImageWidth;
                        tStoryModeImageLocation.vSceneLocationY = 0;
                    } else if (pObjectLocation.equals("centertop")) {
                        int tSceneCenterLocation = pStoryModeScene.getSceneCenterLocation(pSceneLocation);
                        tStoryModeImageLocation.vObjectName = pObjectLocation;
                        tStoryModeImageLocation.vImageArea.vImageWidth = vSceneWidth / 3;
                        tStoryModeImageLocation.vImageArea.vImageHeight = 200;
                        tStoryModeImageLocation.vSceneLocationX = tSceneCenterLocation - vSceneWidth / 2 + tStoryModeImageLocation.vImageArea.vImageWidth;
                        tStoryModeImageLocation.vSceneLocationY = 800;
                    } else if (pObjectLocation.equals("centercenter")) {
                        int tSceneCenterLocation = pStoryModeScene.getSceneCenterLocation(pSceneLocation);
                        tStoryModeImageLocation.vObjectName = pObjectLocation;
                        tStoryModeImageLocation.vImageArea.vImageWidth = vSceneWidth / 3;
                        tStoryModeImageLocation.vImageArea.vImageHeight = 200;
                        tStoryModeImageLocation.vSceneLocationX = tSceneCenterLocation - vSceneWidth / 2 + tStoryModeImageLocation.vImageArea.vImageWidth;
                        tStoryModeImageLocation.vSceneLocationY = 600;
                    } else if (pObjectLocation.equals("right")) {
                        int tSceneCenterLocation = pStoryModeScene.getSceneCenterLocation(pSceneLocation);
                        tStoryModeImageLocation.vObjectName = pObjectLocation;
                        tStoryModeImageLocation.vImageArea.vImageWidth = vSceneWidth / 3;
                        tStoryModeImageLocation.vImageArea.vImageHeight = 150;
                        tStoryModeImageLocation.vSceneLocationX = (tSceneCenterLocation - vSceneWidth / 2) + tStoryModeImageLocation.vImageArea.vImageWidth * 2;
                        tStoryModeImageLocation.vSceneLocationY = 0;
                    } else if (pObjectLocation.equals("floor")) {
                        int tSceneCenterLocation = pStoryModeScene.getSceneCenterLocation(pSceneLocation);
                        tStoryModeImageLocation.vObjectName = pObjectLocation;
                        tStoryModeImageLocation.vImageArea.vImageWidth = vSceneWidth;
                        tStoryModeImageLocation.vImageArea.vImageHeight = 150;
                        tStoryModeImageLocation.vSceneLocationX = tSceneCenterLocation - vSceneWidth / 2;
                        tStoryModeImageLocation.vSceneLocationY = 0;
                    } else {
                        int tSceneCenterLocation = pStoryModeScene.getSceneCenterLocation(pSceneLocation);
                        tStoryModeImageLocation.vObjectName = pObjectLocation;
                        tStoryModeImageLocation.vImageArea.vImageWidth = vSceneWidth;
                        tStoryModeImageLocation.vImageArea.vImageHeight = 150;
                        tStoryModeImageLocation.vSceneLocationX = tSceneCenterLocation;// - 1920 / 2;
                        tStoryModeImageLocation.vSceneLocationY = 0;
                    }
                } else {
                    if (tStoryModeImageLocation.vEdgeWidth < 0) {
                        tStoryModeImageLocation.vEdgeWidth = 0;
                    }
                    if (tStoryModeImageLocation.vEdgeHeight < 0) {
                        tStoryModeImageLocation.vEdgeHeight = 0;
                    }
                }

                DatabaseCursor vDatabaseCursorImageObject = null;
                try {
                    vDatabaseCursorImageObject = vDatabase.rawQuery("select WordImageName, ImageWidth, ImageHeight, RealSizeHeight, 0, 0, '', Word from SceneMakingExtraImg" + " where Word = '" + vObjectList.get(i).trim() + "'");
                    if (vDatabaseCursorImageObject.moveToFirst()) {
                        StoryModeImage tStoryModeImage = new StoryModeImage(tStoryModeImageLocation, vDatabaseCursorImageObject, vAdditionalSceneImage, pMaxSize, "mission");

                        vAdditionalSceneImage.putImageObjectToMe(tStoryModeImage);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != vDatabaseCursorImageObject) {
                        vDatabaseCursorImageObject.close();
                    }
                }
            }
        }
        return vAdditionalSceneImage;
    }

    public Boolean updateMissionCompleted(String pMission) {
        try {
            // update data from database
            String[] fieldname = {"MissionCompleted"};
            String[] updatevalue = {"completed"};
            String[] arg = {pMission};
            vDatabase.updateTable("MissionScriptMaster", fieldname, updatevalue, "MissionCode = ?", arg);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Boolean updateMissionPocket(ArrayList<String> pPocketList) {
        try {
            // reset all to no
            String[] fieldname = {"Active"};
            String[] updatevalue = {"no"};
            String[] arg = {""};
            vDatabase.updateTable("MissionPocket", fieldname, updatevalue, "imageName <> ?", arg);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            for (int i = 0; i < pPocketList.size(); i++) {
                // update data from database
                String[] fieldname1 = {"Active"};
                String[] updatevalue1 = {"yes"};
                String[] arg = {pPocketList.get(i)};
                vDatabase.updateTable("MissionPocket", fieldname1, updatevalue1, "imageName = ?", arg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public ArrayList<String> getMissionPocket() {
        ArrayList<String> vMissionPocket = new ArrayList<String>();
        DatabaseCursor vDatabaseCursorPocket = null;
        try {
            vDatabaseCursorPocket = vDatabase.rawQuery("select ImageName from MissionPocket where Active = 'yes'");
            if (vDatabaseCursorPocket.moveToFirst()) {
                do {
                    vMissionPocket.add(vDatabaseCursorPocket.getString(0));
                } while (vDatabaseCursorPocket.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursorPocket) {
                vDatabaseCursorPocket.close();
            }
        }
        return vMissionPocket;
    }

    public StoryModeScenePath getScenePath(String pFrom, String pTo) {
        StoryModeScenePath retScenePath = new StoryModeScenePath();
        DatabaseCursor vDatabaseCursorPocket = null;
        try {
            vDatabaseCursorPocket = vDatabase.rawQuery("select FromScene, ToScene, Step1, Step2, Step3 from SceneTransition where FromScene = '" + pFrom + "' and ToScene ='" + pTo + "'");
            if (vDatabaseCursorPocket.moveToFirst()) {
                do {
                    retScenePath.vFrom = vDatabaseCursorPocket.getString(0);
                    retScenePath.vTo = vDatabaseCursorPocket.getString(1);
                    retScenePath.vStep1 = vDatabaseCursorPocket.getString(2);
                    retScenePath.vStep2 = vDatabaseCursorPocket.getString(3);
                    retScenePath.vStep3 = vDatabaseCursorPocket.getString(4);

                } while (vDatabaseCursorPocket.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursorPocket) {
                vDatabaseCursorPocket.close();
            }
        }
        return retScenePath;
    }

    public ArrayList<StoryModeImage> getSceneImage(StoryModeScene pScene) {
        ArrayList vArrayList = new ArrayList<StoryModeImage>();

        StoryModeImage vStoryModeImage = new StoryModeImage();

        DatabaseCursor vDatabaseImageCursor = null;
        DatabaseCursor vDatabaseSceneCursor = null;

        int vCount = 0;

        try {
            vDatabaseSceneCursor = vDatabase.rawQuery("select ImageWordName, ObjectX, ObjectY from SceneObject" + " where SceneName = '" + pScene.getSceneName() + "'");

            if (vDatabaseSceneCursor.moveToFirst()) {
                do {
                    // get all image that related to pWord
                    try {

                        // get data from database
                        vDatabaseImageCursor = vDatabase.rawQuery("select WordImagename, RelatedWord, ImageWidth, ImageHeight, WidthOrHeight, RealSize from ImageWord" + " where WordImagename = '" + vDatabaseSceneCursor.getString(0) + "'");
                        if (vDatabaseImageCursor.moveToFirst()) {
                            vStoryModeImage = new StoryModeImage();
                            vStoryModeImage.initImage(vDatabaseImageCursor);
                            vStoryModeImage.setMeToScene(pScene, (int) (vDatabaseSceneCursor.getInt(1) * pScene.getSceneDisplayRatio()), (int) (vDatabaseSceneCursor.getInt(2) * pScene.getSceneDisplayRatio()));
                            vArrayList.add(vStoryModeImage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (null != vDatabaseImageCursor) {
                            vDatabaseImageCursor.close();
                        }
                    }

                } while (vDatabaseSceneCursor.next());

            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (null != vDatabaseSceneCursor) {
                vDatabaseSceneCursor.close();
            }
        }
        return vArrayList;
    }

    //get Word image information and part information
    public StoryModeWord getStoryModeImageWord(String pWord) {

        StoryModeWord vStoryModeWord = new StoryModeWord(pWord);
        DatabaseCursor vDatabaseImageCursor;
        DatabaseCursor vDatabaseImagePartCursor = null;

        int vCount = 0;

        // get all image that related to pWord
        try {

            // get data from database
            vDatabaseImageCursor = vDatabase.rawQuery("select WordImagename, RelatedWord, ImageWidth, ImageHeight, WidthOrHeight, RealSize from SceneImageWord" + " where RelatedWord = '" + pWord + "'");
            vStoryModeWord.setWordImageSize(vDatabaseImageCursor.getCount());

            // loop for all record for ImageWord
            if (vDatabaseImageCursor.moveToFirst()) {
                do {

                    // get image part data
                    vDatabaseImagePartCursor = vDatabase.rawQuery("select WordImagename, Part, Action, ActionTarget, ActionSource, TargetX, TargetY, TargetXWidth, TargetYHeight, TargetOrientation, SourceX, SourceY, SourceXWidth, SourceYHeight, SourceOrientation, TouchAction from ImagePart where WordImageName = '" + vDatabaseImageCursor.getString(0) + "'");
                    vStoryModeWord.setWordImage(vCount, vDatabaseImageCursor, vDatabaseImagePartCursor);

                    vCount = vCount + 1;

                } while (vDatabaseImageCursor.next());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseImagePartCursor) {
                vDatabaseImagePartCursor.close();
            }
        }
        return vStoryModeWord;
    }


    public void saveLessonComplete(String pLesson, String pLanguage) {
        try {

            saveCurrentLesson(pLesson, pLanguage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCurrentLesson(String pLesson, String pLanguage) {
        try {

            // update data from database
            //vDatabase.execSQL("update LessonCompleted set CurrentLesson = "+ pLesson + " where language = '" + pLanguage + "'");
            String[] fieldname = {"CurrentLesson"};
            String[] updatevalue = {pLesson};
            String[] arg = {pLanguage};
            vDatabase.updateTable("LessonCompleted", fieldname, updatevalue, "language = ?", arg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLastCompletedLesson() {
        String retLesson = "1";
        DatabaseCursor vDatabaseCursorElement = null;

        try {


            // get data from database
            vDatabaseCursorElement = vDatabase.rawQuery("select min(LessonCode) from LessonCompleted" + " where LessonCompleted = 0");

            if (vDatabaseCursorElement.moveToFirst()) {
                retLesson = vDatabaseCursorElement.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursorElement) {
                vDatabaseCursorElement.close();
            }
        }
        return Integer.parseInt(retLesson);
    }

    public int getCurrentLesson(String pLanguage) {
        String retLesson = "1";
        DatabaseCursor vDatabaseCursorElement = null;
        try {
            // get data from database
            vDatabaseCursorElement = vDatabase.rawQuery("select min(CurrentLesson) from LessonCompleted" + " where language = '" + pLanguage + "'");

            if (vDatabaseCursorElement.moveToFirst()) {
                retLesson = vDatabaseCursorElement.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursorElement) {
                vDatabaseCursorElement.close();
            }
        }
        return Integer.parseInt(retLesson);
    }

    public void restartGame() {
        try {
            vDatabase.updateTable("MissionScriptMaster", new String[]{"MissionCompleted"}, new String[]{""}, null,
                    null);
            vDatabase.updateTable("MissionPocket", new String[]{"Active"}, new String[]{"no"}, null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
