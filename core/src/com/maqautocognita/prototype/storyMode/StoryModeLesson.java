package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.DatabaseCursor;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by kotarou on 9/6/16.
 */
public class StoryModeLesson {

    private String vLessonCode;
    private ArrayList<StoryModeLessonElement> vStoryModeLessonElement = new ArrayList<StoryModeLessonElement>();
    private StoryModeScene vStoryModeScene;
    private StoryModeScene vAdditionalScene;
    private StoryModeScene vRemovalScene;
    private Boolean vCompleted;
    private String vLanguage;
    //private ArrayList<StoryModeUserGestureResult> vAutoGestureResult = new ArrayList<StoryModeUserGestureResult>();
    public ArrayList<String> vAutoStartScript = new ArrayList<String>();
    public ArrayList<String> vAutoStartScriptSwahili = new ArrayList<String>();
    public ArrayList<String> vAutoStartVoice = new ArrayList<String>();
    public ArrayList<String> vAutoStartPlayed = new ArrayList<String>();
    public ArrayList<String> vAutoEndScript = new ArrayList<String>();
    public ArrayList<String> vAutoEndScriptSwahili = new ArrayList<String>();
    public ArrayList<String> vAutoEndVoice = new ArrayList<String>();
    public ArrayList<String> vAutoEndPlayed = new ArrayList<String>();
    private boolean matchObject = false;


    // create StoryModeScene
    public StoryModeLesson(String pLessonCode) {
        vLessonCode = pLessonCode;
    }

    // initialize Scene image and size
    public void initElement(DatabaseCursor pDatabaseCursorElement, ArrayList<StoryModeLessonGesture> pStoryModeGusture) {
        try {
            StoryModeLessonElement tempElement = new StoryModeLessonElement(pDatabaseCursorElement, pStoryModeGusture);
            vStoryModeLessonElement.add(tempElement);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StoryModeUserGestureResult getGestureResult(StoryModeUserGestureEnum pAction, String pWord) {

        return getGestureResult(pAction, pWord, "");
    }

    public StoryModeUserGestureResult getGestureResult(StoryModeUserGestureEnum pAction, String pWord, String pLocation) {

        StoryModeUserGestureResult storyModeUserGestureResult = new StoryModeUserGestureResult();
        StoryModeUserGestureResult returnStoryModeUserGestureResult = new StoryModeUserGestureResult();
        StoryModeUserGestureResult returnAutoGestureResult = new StoryModeUserGestureResult();
        storyModeUserGestureResult.findAction = false;
        returnStoryModeUserGestureResult.findAction = false;
        Boolean allObjectCompleted = true;
        Boolean allElementCompleted = true;
        ArrayList<String> vPicWordListAll = getActionPicWordList(true);
        ArrayList<String> vPicWordListRemain = getActionPicWordList(false);

        vPicWordListRemain = getActionPicWordList(false);


        returnAutoGestureResult.message = "";
        returnAutoGestureResult.audioFile = "";
        returnStoryModeUserGestureResult.lessonCompleted = false;

        // display start script
        /*
        if (vAutoStartPlayed.size() > 0 && pAction == StoryModeUserGestureEnum.TOUCH) {
            for (int i = 0; i < vAutoStartPlayed.size(); i++) {
                if (vAutoStartPlayed.get(i).equals("N")) {
                    returnAutoGestureResult.gesture = "A";
                    returnAutoGestureResult.nextPic = returnAutoGestureResult.audioFile; //"no-pic";
                    returnAutoGestureResult.message = vAutoStartScript.get(i);
                    returnAutoGestureResult.audioFile = vAutoStartVoice.get(i);
                    returnAutoGestureResult.findAction = true;
                    returnAutoGestureResult.lessonCompleted = false;

                    this.vCompleted = false;
                    vAutoStartPlayed.set(i, "Y");

                    if (vPicWordListRemain.size() == 0 && vAutoEndPlayed.size() == 0 && i == vAutoStartPlayed.size() - 1) {
                        returnAutoGestureResult.findAction = true;
                        returnAutoGestureResult.lessonCompleted = true;
                        this.vCompleted = true;
                    }
                    Gdx.app.log(getClass().getName(), "The object " + "getGestureResult" + " message is: " + returnAutoGestureResult.message);
                    Gdx.app.log(getClass().getName(), "The object " + "getGestureResult" + " audio file is: " + returnAutoGestureResult.audioFile);
                    return returnAutoGestureResult;
                }
            }
        }

*/
        // map the scene image name to picture word
        ArrayList<StoryModeImage> sceneImageList = vStoryModeScene.getSceneImageObject();
        for (int i = 0; i < sceneImageList.size(); i++) {
            if (pWord.equals(sceneImageList.get(i).vObjectName)) {
                if (sceneImageList.get(i).vRelatedWord != null) {
                    pWord = sceneImageList.get(i).vRelatedWord;
                }
            }
        }


        for (int i = 0; i < vStoryModeLessonElement.size(); i++) {
            storyModeUserGestureResult = vStoryModeLessonElement.get(i).getGestureResult(pAction, pWord, pLocation, vLanguage);
            if (storyModeUserGestureResult.findAction) {
                if (!pLocation.equals("")) {
                    storyModeUserGestureResult.nextPic = getRandomPicWord();
                } else {
                    for (int j = 0; j < vStoryModeLessonElement.size(); j++) {
                        if (vStoryModeLessonElement.get(j).getObjectLocation().equals(pLocation)) {
                            if (!vStoryModeLessonElement.get(j).getElementCompleted()) {
                                allObjectCompleted = false;
                            }
                        }
                    }
                    if (!allObjectCompleted) {
                        storyModeUserGestureResult.nextPic = getRandomPicWord();
                    } else {
                        storyModeUserGestureResult.nextPic = "";
                    }
                }
                i = vStoryModeLessonElement.size() - 1;
                returnStoryModeUserGestureResult = storyModeUserGestureResult;
            }



        }
        for (int i = 0; i < vStoryModeLessonElement.size(); i++) {
            Gdx.app.log(getClass().getName(), "(start)The object element code is '" + vStoryModeLessonElement.get(i).getElementCode());
            boolean tempResult = vStoryModeLessonElement.get(i).getElementCompleted();
            if (!tempResult) {
                allElementCompleted = false;
            }
            //allElementCompleted = allElementCompleted && tempResult;//&& vStoryModeLessonElement.get(i).getElementCompleted();
            Gdx.app.log(getClass().getName(), "(end)The object element code is '" + vStoryModeLessonElement.get(i).getElementCode());
        }
        if (allElementCompleted ){//&& vAutoEndPlayed.size() == 0) {
            returnStoryModeUserGestureResult.lessonCompleted = true;
            this.vCompleted = true;
            storyModeUserGestureResult.nextPic = "";
            returnAutoGestureResult.findAction = true;
            vPicWordListAll = getActionPicWordList(true);
            vPicWordListRemain = getActionPicWordList(false);
            returnStoryModeUserGestureResult.lessonCompletedPercentage = (vPicWordListAll.size() * 1.f - vPicWordListRemain.size() * 1.f) / vPicWordListAll.size() * 1.f;

            return returnStoryModeUserGestureResult;
        }


        /*
        if (!storyModeUserGestureResult.findAction) {
            if (allElementCompleted || vPicWordListRemain.size() == 0) {
                // display end script
                if (pAction == StoryModeUserGestureEnum.TOUCH) {
                    if (vAutoEndPlayed.size() > 0) {
                        for (int i = 0; i < vAutoEndPlayed.size(); i++) {
                            if (vAutoEndPlayed.get(i).equals("N")) {
                                returnStoryModeUserGestureResult.gesture = "A";
                                returnStoryModeUserGestureResult.nextPic = "no-pic";
                                returnStoryModeUserGestureResult.message = vAutoEndScript.get(i);
                                returnStoryModeUserGestureResult.audioFile = vAutoEndVoice.get(i);
                                if (i == vAutoEndPlayed.size() - 1) {
                                    returnStoryModeUserGestureResult.nextPic = "";
                                    returnStoryModeUserGestureResult.lessonCompleted = true;
                                    this.vCompleted = true;
                                } else {
                                    returnStoryModeUserGestureResult.lessonCompleted = false;
                                    this.vCompleted = false;
                                }
                                returnStoryModeUserGestureResult.findAction = true;

                                vAutoEndPlayed.set(i, "Y");
                                return returnStoryModeUserGestureResult;
                            }
                        }

                        return returnStoryModeUserGestureResult;
                    } else {

                        returnStoryModeUserGestureResult.lessonCompleted = true;
                        returnStoryModeUserGestureResult.message = " ";
                        this.vCompleted = true;
                        returnAutoGestureResult.findAction = true;
                        return returnStoryModeUserGestureResult;
                    }
                }
            }
        }*/
        returnStoryModeUserGestureResult.lessonCompleted = false;
        this.vCompleted = false;
        return returnStoryModeUserGestureResult;
    }

    public ArrayList<String> getActionPicWordList(Boolean pAll) {
        ArrayList<String> vTempList = new ArrayList<String>();
        ArrayList<String> vReturnList = new ArrayList<String>();
        for (int i = 0; i < vStoryModeLessonElement.size(); i++) {
            ArrayList<String> tempPicList = vStoryModeLessonElement.get(i).getPicWord();
            ArrayList<StoryModeUserGestureResult> tempUserGestureResult = vStoryModeLessonElement.get(i).getStoryModeUserGestureStatus();
            //ArrayList<Boolean> tempPicCompleteLsit = vStoryModeLessonElement.get(i).getPicCompleteWord();
            for (int j = 0; j < tempUserGestureResult.size(); j++) {
                if (pAll || !tempUserGestureResult.get(j).completed) {
                    //vTempList.add(tempUserGestureResult.get(j).picWord + "," + this.vStoryModeLessonElement.get(i).getPicWordGesture(tempUserGestureResult.get(j).picWord));
                    vTempList.add(tempUserGestureResult.get(j).picWord + "," + tempUserGestureResult.get(j).gesture + "," + tempUserGestureResult.get(j).gestureAction);
                    //vTempList.add(tempUserGestureResult.get(j).picWord);
                }
            }
        }
        vReturnList = new ArrayList(new HashSet(vTempList));
        return vReturnList;
    }

    public String getRandomPicWord() {
        ArrayList<String> vPicWordList = getActionPicWordList(false);
        int vListSize = vPicWordList.size();
        if (vListSize > 0) {
            Random vRan = new Random();
            return vPicWordList.get(vRan.nextInt(vListSize));
        } else {
            return "";
        }
    }

    public StoryModeScene getScene() {
        return vStoryModeScene;
    }

    public void setScene(StoryModeScene pStoryModeScene) {
        vStoryModeScene = pStoryModeScene;
    }

    public StoryModeScene getAdditionalScene() {
        return vAdditionalScene;
    }

    public void setAdditionalScene(StoryModeScene pStoryModeScene) {
        vAdditionalScene = pStoryModeScene;
    }

    public StoryModeScene getRemovalScene() {
        return vRemovalScene;
    }

    public void setRemovalScene(StoryModeScene pStoryModeScene) {
        vRemovalScene = pStoryModeScene;
    }

    public void setCompelted(Integer pCompleted) {
        if (pCompleted > 0) {
            vCompleted = true;
        } else {
            vCompleted = false;
        }
    }

    public Boolean getCompleted() {
        return this.vCompleted;
    }

    public StoryModeLessonElement getLessonElement(String pElementCode) {
        for (int i = 0; i < vStoryModeLessonElement.size(); i++) {
            if (vStoryModeLessonElement.get(i).getElementCode().equals(pElementCode)) {
                return vStoryModeLessonElement.get(i);
            }
        }
        return null;
    }

    public String getLessonLanguage() {
        return vLanguage;
    }

    public void setLessonLanguage(String pLanguage) {
        this.vLanguage = pLanguage;
    }

    public void setAutoScript(DatabaseCursor pAutoScriptCursor) {
        if (pAutoScriptCursor.getString(2).equals("start")) {
            vAutoStartScript.add(pAutoScriptCursor.getString(3));
            vAutoStartVoice.add(pAutoScriptCursor.getString(4));
            vAutoStartScriptSwahili.add(pAutoScriptCursor.getString(5));
            vAutoStartPlayed.add("N");
        } else {
            vAutoEndScript.add(pAutoScriptCursor.getString(3));
            vAutoEndVoice.add(pAutoScriptCursor.getString(4));
            vAutoEndScriptSwahili.add(pAutoScriptCursor.getString(5));
            vAutoEndPlayed.add("N");
        }
    }

}
