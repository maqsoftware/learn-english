package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.adapter.IAnalyticSpotService;
import com.maqautocognita.prototype.databases.Database;
import com.maqautocognita.prototype.databases.DatabaseCursor;
import com.maqautocognita.prototype.databases.SQLiteGdxException;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kotarou on 30/5/16.
 */
public class StoryModeLogic {
    Database vDatabase;
    IAnalyticSpotService analyticSpotService;

    public StoryModeLogic(Database database, IAnalyticSpotService analyticSpotService) {
        vDatabase = database;
        this.analyticSpotService = analyticSpotService;
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
                    }

                } while (vDatabaseSceneCursor.next());

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseImageCursor) {
                vDatabaseImageCursor.close();
            }
            if (null != vDatabaseSceneCursor) {
                vDatabaseSceneCursor.close();
            }
        }
        return vArrayList;
    }

    //get Word image information and part information
    public StoryModeWord getStoryModeImageWord(String pWord) {

        StoryModeWord vStoryModeWord = new StoryModeWord(pWord);
        DatabaseCursor vDatabaseImageCursor = null;
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
            if (null == vDatabaseImageCursor) {
                vDatabaseImageCursor.close();
            }
            if (null == vDatabaseImagePartCursor) {
                vDatabaseImagePartCursor.close();
            }
        }
        return vStoryModeWord;
    }

    public StoryModeLesson getStoryModeLesson(String pLessonCode) {
        return getStoryModeLesson(pLessonCode, "english");
    }

    /*public StoryModeCoordination subjectOnObject(String pTargetWordImage, String pSourceWordImage, int pObjectSize){
        StoryModeCoordination vSMSub = new StoryModeCoordination(pTargetWordImage, pObjectWordImage);
        StoryModeCoordination vSMObj = new StoryModeCoordination(pSourceWordImage, pObjectWordImage);
        return vSMSub;
    }*/

    // get Scene and all scene related information and image
    public StoryModeLesson getStoryModeLesson(String pLessonCode, String pLanguage) {

        StoryModeLesson vStoryModeLesson = new StoryModeLesson(pLessonCode);

        StoryModeScene vStoryModeScene = new StoryModeScene("blank");
        StoryModeScene vAdditionalScene = new StoryModeScene("Additional");
        StoryModeScene vRemovalScene = new StoryModeScene("Removal");
        StoryModeImage vRemovalImage = new StoryModeImage();
        ArrayList<StoryModeLessonGesture> vStoryModeGesture;
        DatabaseCursor vDatabaseCursorElement = null;
        DatabaseCursor vDatabaseCursorGesture = null;
        DatabaseCursor vDatabaseCursorLessonImageObject = null;
        DatabaseCursor vDatabaseCursorAuto = null;
        ArrayList<String> tPicWordArray;
        vStoryModeLesson.setLessonLanguage(pLanguage);

        int vCount = 0;

        try {
            // get data from database
            vDatabaseCursorElement = vDatabase.rawQuery("select LessonCompleted from LessonCompleted" + " where LessonCode = '" + pLessonCode + "'");

            // loop for all record for ImageWord
            if (vDatabaseCursorElement.moveToFirst()) {
                vStoryModeLesson.setCompelted(vDatabaseCursorElement.getInt(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get scene image
        try {


            // get data from database
            vDatabaseCursorElement = vDatabase.rawQuery("select LessonCode, ElementCode, SceneName, SceneLocation, Zoom, SentenceStem, NonPicWord, PicWord, ObjectLocation, LessonDescription, AudioFile, Swahili, MaxSize from LessonScriptMaster" + " where LessonCode = '" + pLessonCode + "' order by LessonCode, ElementCode");

            // loop for all record for ImageWord
            if (vDatabaseCursorElement.moveToFirst()) {
                if (vStoryModeScene.getSceneName().equals("blank")) {
                    vStoryModeScene = getStoryModeScene(vDatabaseCursorElement.getString(2), 1f);
                    vAdditionalScene.setSceneDisplayRatio(vStoryModeScene.getSceneDisplayRatio());
                    vAdditionalScene.setToSceneRatio(vStoryModeScene.getToSceneRatio());
                }

                try {
                    vDatabaseCursorAuto = vDatabase.rawQuery(" select lg.LessonCode, lg.ElementCode, lg.GesturesAction, lm.SentenceStem, lm.AudioFile, lm.Swahili from LessonScriptGestures lg, LessonScriptMaster lm " +
                            "where lg.lessonCode = lm.LessonCode" + " and lg.ElementCode = lm.ElementCode and Gestures = 'A' " +
                            " and Gesturesaction in ('start', 'end') and lm.LessonCode = '" + pLessonCode + "' order by lm.ElementCode");

                    if (vDatabaseCursorAuto.moveToFirst()) {

                        try {
                            do {
                                vStoryModeLesson.setAutoScript(vDatabaseCursorAuto);

                            } while (vDatabaseCursorAuto.next());
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    do {
                        String tPicWord = vDatabaseCursorElement.getString(7);
                        tPicWordArray = new ArrayList<String>(Arrays.asList(tPicWord.split(",")));
                        String tSceneLocation = vDatabaseCursorElement.getString(3);
                        String tObjectLocation = vDatabaseCursorElement.getString(8);
                        int tMaxSize = vDatabaseCursorElement.getInt(12);
                        //String Test = "select LessonCode, ElementCode, Gestures, GesturesAction from LessonScriptGestures" + " where LessonCode = '" + pLessonCode +"' and ElementCode = '" + vDatabaseCursorElement.getString(1) +"'";
                        vDatabaseCursorGesture = vDatabase.rawQuery("select LessonCode, ElementCode, Gestures, GesturesAction, GesturesLocation from LessonScriptGestures" + " where LessonCode = '" + pLessonCode + "' and ElementCode = '" + vDatabaseCursorElement.getString(1) + "' and Gestures <> 'A' order by LessonCode, ElementCode");
                        if (vDatabaseCursorGesture.moveToFirst()) {
                            vStoryModeGesture = new ArrayList<StoryModeLessonGesture>();
                            try {
                                do {

                                    vStoryModeGesture.add(new StoryModeLessonGesture(vDatabaseCursorGesture));

                                } while (vDatabaseCursorGesture.next());
                                vStoryModeLesson.initElement(vDatabaseCursorElement, vStoryModeGesture);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        // get picture word from Image Object and put it into the Scene
                        if (!tObjectLocation.equals("default")) {
                            // get group from Scene by image name

                            String[] tempLocation = tObjectLocation.split(",");
                            if (tempLocation.length > 0) {
                                tObjectLocation = tempLocation[0];
                            }

                            StoryModeImage tStoryModeImageLocation = vStoryModeScene.getSceneImageObject(tObjectLocation);

                            if (tStoryModeImageLocation.vObjectName.equals("")) {
                                tStoryModeImageLocation = vAdditionalScene.getSceneImageObject(tObjectLocation);
                            }
                            // if cannot found image from current scene, assume put at floor, left center right, or whole floor
                            if (tStoryModeImageLocation.vObjectName.equals("")) {
                                if (tObjectLocation.equals("left")) {
                                    int tSceneCenterLocation = vStoryModeScene.getSceneCenterLocation(tSceneLocation);
                                    tStoryModeImageLocation.vObjectName = tObjectLocation;
                                    tStoryModeImageLocation.vImageArea.vImageWidth = 1920 / 3;
                                    tStoryModeImageLocation.vImageArea.vImageHeight = 150;
                                    tStoryModeImageLocation.vSceneLocationX = tSceneCenterLocation - 1920 / 2;
                                    tStoryModeImageLocation.vSceneLocationY = 0;
                                } else if (tObjectLocation.equals("center")) {
                                    int tSceneCenterLocation = vStoryModeScene.getSceneCenterLocation(tSceneLocation);
                                    tStoryModeImageLocation.vObjectName = tObjectLocation;
                                    tStoryModeImageLocation.vImageArea.vImageWidth = 1920 / 3;
                                    tStoryModeImageLocation.vImageArea.vImageHeight = 150;
                                    tStoryModeImageLocation.vSceneLocationX = tSceneCenterLocation - 1920 / 2 + tStoryModeImageLocation.vImageArea.vImageWidth;
                                    tStoryModeImageLocation.vSceneLocationY = 0;
                                } else if (tObjectLocation.equals("centertop")) {
                                    int tSceneCenterLocation = vStoryModeScene.getSceneCenterLocation(tSceneLocation);
                                    tStoryModeImageLocation.vObjectName = tObjectLocation;
                                    tStoryModeImageLocation.vImageArea.vImageWidth = 1920 / 3;
                                    tStoryModeImageLocation.vImageArea.vImageHeight = 200;
                                    tStoryModeImageLocation.vSceneLocationX = tSceneCenterLocation - 1920 / 2 + tStoryModeImageLocation.vImageArea.vImageWidth;
                                    tStoryModeImageLocation.vSceneLocationY = 800;
                                } else if (tObjectLocation.equals("centercenter")) {
                                    int tSceneCenterLocation = vStoryModeScene.getSceneCenterLocation(tSceneLocation);
                                    tStoryModeImageLocation.vObjectName = tObjectLocation;
                                    tStoryModeImageLocation.vImageArea.vImageWidth = 1920 / 3;
                                    tStoryModeImageLocation.vImageArea.vImageHeight = 200;
                                    tStoryModeImageLocation.vSceneLocationX = tSceneCenterLocation - 1920 / 2 + tStoryModeImageLocation.vImageArea.vImageWidth;
                                    tStoryModeImageLocation.vSceneLocationY = 600;
                                } else if (tObjectLocation.equals("right")) {
                                    int tSceneCenterLocation = vStoryModeScene.getSceneCenterLocation(tSceneLocation);
                                    tStoryModeImageLocation.vObjectName = tObjectLocation;
                                    tStoryModeImageLocation.vImageArea.vImageWidth = 1920 / 3;
                                    tStoryModeImageLocation.vImageArea.vImageHeight = 150;
                                    tStoryModeImageLocation.vSceneLocationX = (tSceneCenterLocation - 1920 / 2) + tStoryModeImageLocation.vImageArea.vImageWidth * 2;
                                    tStoryModeImageLocation.vSceneLocationY = 0;
                                } else if (tObjectLocation.equals("floor")) {
                                    int tSceneCenterLocation = vStoryModeScene.getSceneCenterLocation(tSceneLocation);
                                    tStoryModeImageLocation.vObjectName = tObjectLocation;
                                    tStoryModeImageLocation.vImageArea.vImageWidth = 1920;
                                    tStoryModeImageLocation.vImageArea.vImageHeight = 150;
                                    tStoryModeImageLocation.vSceneLocationX = tSceneCenterLocation - 1920 / 2;
                                    tStoryModeImageLocation.vSceneLocationY = 0;
                                    //vAdditionalScene.putImageObjectToMe(tStoryModeImageLocation);
                                } else {
                                    int tSceneCenterLocation = vStoryModeScene.getSceneCenterLocation(tSceneLocation);
                                    tStoryModeImageLocation.vObjectName = tObjectLocation;
                                    tStoryModeImageLocation.vImageArea.vImageWidth = 1920;
                                    tStoryModeImageLocation.vImageArea.vImageHeight = 150;
                                    tStoryModeImageLocation.vSceneLocationX = tSceneCenterLocation - 1920 / 2;
                                    tStoryModeImageLocation.vSceneLocationY = 0;
                                    //vAdditionalScene.putImageObjectToMe(tStoryModeImageLocation);
                                }
                            }

                            for (int j = 0; j < tPicWordArray.size(); j++) {
                                vDatabaseCursorLessonImageObject = vDatabase.rawQuery("select WordImageName, ImageWidth, ImageHeight, RealSizeHeight, -1, -1, '', Word from SceneMakingExtraImg" + " where Word = '" + tPicWordArray.get(j).toString().trim() + "'");
                                if (vDatabaseCursorLessonImageObject.moveToFirst()) {
                                    StoryModeImage tStoryModeImage = new StoryModeImage(tStoryModeImageLocation, vDatabaseCursorLessonImageObject, vAdditionalScene, tMaxSize);

                                    // right or left of an object
                                    if (tempLocation.length > 1) {
                                        if (tempLocation[1].trim().equals("left")) {
                                            tStoryModeImage.vSceneLocationX = tStoryModeImageLocation.vSceneLocationX - tStoryModeImage.vImageArea.vSceneDisplayWidth;
                                            tStoryModeImage.vSceneLocationY = tStoryModeImageLocation.vSceneLocationY;
                                        } else if (tempLocation[1].trim().equals("right")) {
                                            tStoryModeImage.vSceneLocationX = tStoryModeImageLocation.vSceneLocationX + tStoryModeImageLocation.vImageArea.vSceneDisplayWidth;
                                            tStoryModeImage.vSceneLocationY = tStoryModeImageLocation.vSceneLocationY;
                                        } else {
                                            tStoryModeImage.vSceneLocationX = tStoryModeImageLocation.vSceneLocationX + tStoryModeImageLocation.vImageArea.vSceneDisplayWidth / 2;
                                            tStoryModeImage.vSceneLocationY = tStoryModeImageLocation.vSceneLocationY + tStoryModeImageLocation.vImageArea.vSceneDisplayHeight / 2;
                                        }

                                    }
                                    vAdditionalScene.putImageObjectToMe(tStoryModeImage);
                                    vRemovalImage = vStoryModeScene.getSceneImageObject(tStoryModeImage.vObjectName);
                                    if (!vRemovalImage.vObjectName.equals("")) {
                                        vRemovalScene.putImageObjectToMe(vRemovalImage);
                                    }
                                }
                            }
                        }


                    } while (vDatabaseCursorElement.next());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != vDatabaseCursorElement) {
                vDatabaseCursorElement.close();
            }
            if (null != vDatabaseCursorGesture) {
                vDatabaseCursorGesture.close();
            }
            if (null != vDatabaseCursorAuto) {
                vDatabaseCursorAuto.close();
            }
            if (null != vDatabaseCursorLessonImageObject) {
                vDatabaseCursorLessonImageObject.close();
            }
        }

        vStoryModeLesson.setScene(vStoryModeScene);
        vStoryModeLesson.setAdditionalScene(vAdditionalScene);
        vStoryModeLesson.setRemovalScene(vRemovalScene);
        return vStoryModeLesson;
    }

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

    public void saveLessonComplete(String pLesson, String pLanguage) {
        try {

            // update data from database
            //vDatabase.execSQL("update LessonCompleted set LessonCompleted = LessonCompleted + 1 where LessonCode = '" + pLesson + "' and language = '" + pLanguage + "'");

            // update data from database
            //vDatabase.execSQL("update LessonCompleted set CurrentLesson = "+ pLesson + " where language = '" + pLanguage + "'");
            /*String [] fieldname = {"LessonCompleted"};
            String [] updatevalue = {"1"};
            String [] arg = {pLesson, pLanguage};
            vDatabase.updateTable("LessonCompleted",fieldname,updatevalue,"LessonCode = ? and language = ?", arg);
            */

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

    public StoryModeImage getStoryModeImage(String objectName) {
        StoryModeImage storyModeImage = null;

        DatabaseCursor databaseCursor =
                null;
        try {
            databaseCursor = vDatabase.rawQuery("select ImageWidth,ImageHeight,WordImageName from SceneMakingExtraImg" +
                    " where word = '" + objectName + "'");
            if (databaseCursor.moveToFirst()) {
                storyModeImage = new StoryModeImage();
                StoryModeArea storyModeArea = new StoryModeArea();
                storyModeArea.vImageWidth = databaseCursor.getInt(0);
                storyModeArea.vImageHeight = databaseCursor.getInt(1);
                storyModeImage.vImageArea = storyModeArea;
                storyModeImage.vImageName = databaseCursor.getString(2);
                storyModeImage.vObjectName = objectName;
            }
        } catch (SQLiteGdxException e) {
            Gdx.app.error(getClass().getName(), "", e);
        } finally {
            if (null == databaseCursor) {
                databaseCursor.close();
            }
        }

        return storyModeImage;
    }

}
