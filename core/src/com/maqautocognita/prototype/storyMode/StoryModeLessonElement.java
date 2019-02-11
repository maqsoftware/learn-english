package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.DatabaseCursor;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kotarou on 9/6/16.
 */
public class StoryModeLessonElement {

    private String vLessonCode;
    private String vElementCode;
    //private StoryModeScene vScene;
    private String vSceneLocation;
    private String vZoom;
    private String vSentenceStem;
    private ArrayList<String> vNonPicWord;
    private ArrayList<String> vPicWord = new ArrayList<String>();
    private ArrayList<Boolean> vPicWordCompleted = new ArrayList<Boolean>();
    private String vObjectLocation;
    private String vLessonDescription;
    private ArrayList<StoryModeLessonGesture> vStoryModeLessonGesture = new ArrayList<StoryModeLessonGesture>();
    private ArrayList<StoryModeUserGestureResult> vStoryModeUserGestureStatus = new ArrayList<StoryModeUserGestureResult>();
    private ArrayList<String> vAudioFile = new ArrayList<String>();
    private ArrayList<String> vPicWordSwahiliSentence = new ArrayList<String>();
    private int vMaxSize;


    // create StoryModeScene
    public StoryModeLessonElement(DatabaseCursor pDatabaseCursor, ArrayList<StoryModeLessonGesture> pStoryModeGesture) {
        String tNonPicWord;
        String tPicWord;
        ArrayList<String> tPicWordArray;
        vLessonCode = pDatabaseCursor.getString(0);
        vElementCode = pDatabaseCursor.getString(1);
        //vScene = pScene;
        vSceneLocation = pDatabaseCursor.getString(3);
        vZoom = pDatabaseCursor.getString(4);
        vSentenceStem = pDatabaseCursor.getString(5).replace(". ",".\n").replace("! ","!\n").replace("? ","? \n");

        tNonPicWord = pDatabaseCursor.getString(6);
        vNonPicWord = new ArrayList<String>(Arrays.asList(tNonPicWord.split(",")));
        //tPicWord = pDatabaseCursor.getString(7);
        StoryModeUserGestureResult tmpStoryModeUserGestureResult;


        vObjectLocation = pDatabaseCursor.getString(8);
        vLessonDescription = pDatabaseCursor.getString(9);
        this.vStoryModeLessonGesture = pStoryModeGesture;
        tPicWordArray = new ArrayList<String>(Arrays.asList(pDatabaseCursor.getString(7).split(",")));
        vAudioFile = new ArrayList<String>((Arrays.asList(pDatabaseCursor.getString(10).split(","))));
        vPicWordSwahiliSentence = new ArrayList<String>((Arrays.asList(pDatabaseCursor.getString(11).split("#"))));
        vMaxSize = pDatabaseCursor.getInt(12);

        //vPicWord = new ArrayList<String>(Arrays.asList(tPicWord.split(",")));
        for (int i = 0; i < tPicWordArray.size(); i++) {
            vPicWord.add(tPicWordArray.get(i));
            for (int j = 0; j < vStoryModeLessonGesture.size(); j++) {
                tmpStoryModeUserGestureResult = new StoryModeUserGestureResult();
                tmpStoryModeUserGestureResult.picWord = tPicWordArray.get(i).trim();
                tmpStoryModeUserGestureResult.gesture = vStoryModeLessonGesture.get(j).getGestures();
                tmpStoryModeUserGestureResult.gestureAction = vStoryModeLessonGesture.get(j).getGestureAction();
                tmpStoryModeUserGestureResult.expectedAction = "";
                tmpStoryModeUserGestureResult.message = "";
                tmpStoryModeUserGestureResult.findAction = false;
                tmpStoryModeUserGestureResult.completed = false;
                tmpStoryModeUserGestureResult.gestureLocation = vStoryModeLessonGesture.get(j).getGestureLocation();
                tmpStoryModeUserGestureResult.audioFile = "";
                vStoryModeUserGestureStatus.add(tmpStoryModeUserGestureResult);
            }
            vPicWordCompleted.add(false);
        }
    }

    public ArrayList<StoryModeLessonGesture> getGesturesAction() {
        return vStoryModeLessonGesture;
    }


    public StoryModeUserGestureResult getGestureResult(StoryModeUserGestureEnum pAction, String pWord, String pLocation) {
        return getGestureResult(pAction, pWord, "", "english");
    }

    public StoryModeUserGestureResult getGestureResult(StoryModeUserGestureEnum pAction, String pWord, String pLocation, String pLanguage) {

        StoryModeUserGestureResult vReturnResult = new StoryModeUserGestureResult();
        vReturnResult.findAction = false;
        // return result according to Action and Word
        // result include action: sentence / Zoom to object
        //
        //int vElementWordIndex = isElementWord(pWord);
        //vReturnResult.findAction = false;
        if (pAction.toString().equals(StoryModeUserGestureEnum.ZOOM_IN)){
            pAction = StoryModeUserGestureEnum.DOUBLE_CLICK;
        }

        for (int i = 0; i < vStoryModeUserGestureStatus.size(); i++) {
            if (pAction.toString().equals(vStoryModeUserGestureStatus.get(i).gesture) && pWord.equals(vStoryModeUserGestureStatus.get(i).picWord.trim())) {
                if(!pLocation.equals(vStoryModeUserGestureStatus.get(i).gestureLocation)) { // if the location is not same need zoom in
                    vReturnResult = vStoryModeUserGestureStatus.get(i);
                    vReturnResult.expectedAction = StoryModeUserGestureEnum.ZOOM_IN.toString();
                    vReturnResult.message = "";
                    vReturnResult.gestureLocation = vStoryModeUserGestureStatus.get(i).gestureLocation.toString();
                    vReturnResult.findAction = true;
                    vReturnResult.completed = vReturnResult.completed || false;
                    vStoryModeUserGestureStatus.set(i, vReturnResult);
                    return vStoryModeUserGestureStatus.get(i);
                }else if (pAction == StoryModeUserGestureEnum.TOUCH) {
                    vReturnResult = vStoryModeUserGestureStatus.get(i);
                    vReturnResult.expectedAction = vStoryModeUserGestureStatus.get(i).gestureAction;
                    vReturnResult.message = getSentence(pWord, pLanguage);
                    vReturnResult.audioFile = getAudioFile(pWord);
                    vReturnResult.findAction = true;
                    vReturnResult.completed = true;

                    vStoryModeUserGestureStatus.set(i, vReturnResult);
                    Gdx.app.log(getClass().getName(), "The Element '" + vStoryModeUserGestureStatus.get(i). picWord +"' Gesture Result '" + vStoryModeUserGestureStatus.get(i).completed);
                    return vStoryModeUserGestureStatus.get(i);

                    //vPicWordCompleted.set(vElementWordIndex,true);
                } else if (pAction == StoryModeUserGestureEnum.DRAG) {
                    vReturnResult = vStoryModeUserGestureStatus.get(i);
                    vReturnResult.expectedAction = StoryModeUserGestureEnum.DROP.toString();
                    vReturnResult.message = vStoryModeUserGestureStatus.get(i).gestureAction;
                    vReturnResult.findAction = true;
                    vReturnResult.completed = true;
                    vStoryModeUserGestureStatus.set(i, vReturnResult);
                    return vStoryModeUserGestureStatus.get(i);
                } else if (pAction == StoryModeUserGestureEnum.DROP) {
                    vReturnResult = vStoryModeUserGestureStatus.get(i);
                    vReturnResult.expectedAction = vStoryModeUserGestureStatus.get(i).gestureAction;
                    vReturnResult.message = getSentence(pWord,pLanguage);
                    vReturnResult.audioFile = getAudioFile(pWord);
                    vReturnResult.findAction = true;
                    vReturnResult.completed = true;
                    vStoryModeUserGestureStatus.set(i, vReturnResult);
                    return vStoryModeUserGestureStatus.get(i);
                } else if (pAction == StoryModeUserGestureEnum.DOUBLE_CLICK) {
                    vReturnResult = vStoryModeUserGestureStatus.get(i);
                    vReturnResult.expectedAction = vStoryModeUserGestureStatus.get(i).gestureAction;
                    vReturnResult.message = "";
                    vReturnResult.findAction = true;
                    vReturnResult.completed = true;
                    vStoryModeUserGestureStatus.set(i, vReturnResult);
                    return vStoryModeUserGestureStatus.get(i);
                } else if (pAction == StoryModeUserGestureEnum.ZOOM_IN) {
                    vReturnResult = vStoryModeUserGestureStatus.get(i);
                    vReturnResult.expectedAction = vStoryModeUserGestureStatus.get(i).gestureAction;
                    vReturnResult.message = "";
                    vReturnResult.findAction = true;
                    vReturnResult.completed = true;
                    vStoryModeUserGestureStatus.set(i, vReturnResult);
                    return vStoryModeUserGestureStatus.get(i);
                } else if (pAction == StoryModeUserGestureEnum.ROTATE) {

                }
            }
        }


        return vReturnResult;
    }

    public String getElementCode() {
        return vElementCode;
    }

    // if the word is listed in the lesson element,
    // form the sentence for the word that pass into the method
    public String getSentence(String pWordObject, String pLanguage) {

        for (int i = 0; i < vPicWord.size(); i++) {
            if (vPicWord.get(i) != null) {
                if (pWordObject.equals(vPicWord.get(i).toString().trim())) {
                    if (pLanguage.equals("swahili")){
                        return vPicWordSwahiliSentence.get(i);
                    }else {
                        return vSentenceStem.replace("<object>", pWordObject);
                    }
                }
            }
        }
        return "";
    }

    // if the word is listed in the lesson element,
    // form the sentence for the word that pass into the method
    public String getAudioFile(String pWordObject) {
        for (int i = 0; i < vPicWord.size(); i++) {
            if (vPicWord.get(i) != null) {
                if (pWordObject.equals(vPicWord.get(i).toString().trim())) {
                    return vAudioFile.get(i);
                }
            }
        }
        return "";
    }

    // check word that pass in, is that an word listed in the lesson element
    public int isElementWord(String pWordObject) {
        for (int i = 0; i < vPicWord.size(); i++) {
            if (vPicWord.get(i) != null)
                if (pWordObject.equals(vPicWord.get(i).toString().trim())) {
                    return i;
                }
        }
        return -1;
    }

    public String zoomInObject() {
        return vZoom;
    }

    /*public StoryModeScene getScene(){
        return vScene;
    }
*/
    public String getSceneLocation() {
        return vSceneLocation;
    }

    public ArrayList<String> getPicWord() {
        return vPicWord;
    }

/*    public String getPicWordGesture(String pPicWord){
        for (int i = 0; i < vPicWord.size(); i++) {
            if (vPicWord.get(i) != null) {
                if (pPicWord.equals(vPicWord.get(i).toString().trim())) {
                    return this.vStoryModeLessonGesture.get(i).getGestures().toString() +","+this.vStoryModeLessonGesture.get(i).getGestureAction();
                }
            }
        }
        return "";
    }
*/
    public ArrayList<String> getNonPicWord() {
        return vNonPicWord;
    }

    public ArrayList<Boolean> getPicCompleteWord() {
        return vPicWordCompleted;
    }

    public ArrayList<StoryModeUserGestureResult> getStoryModeUserGestureStatus() {
        return vStoryModeUserGestureStatus;
    }

    public String getObjectLocation(){
        return vObjectLocation;
    }

    public Boolean getElementCompleted(){
        Boolean vAllCompelted = true;
        /*for (int i = 0 ; i < vPicWordCompleted.size() ; i++){
            if (!vPicWordCompleted.get(i)){
                vAllCompelted = false;
            }
        }*/
        Gdx.app.log(getClass().getName(), "The loop for  '" + getElementCode() + "' is start");

        for (int i = 0; i <vStoryModeUserGestureStatus.size() ; i++){
            Gdx.app.log(getClass().getName(), "The object '" + vStoryModeUserGestureStatus.get(i).picWord + "' is " + vStoryModeUserGestureStatus.get(i).completed.toString());
            if(!vStoryModeUserGestureStatus.get(i).completed){
                vAllCompelted = false;
            }
        }
        Gdx.app.log(getClass().getName(), "The loop for  '" + getElementCode() + "' is end");
        return vAllCompelted;
    }
 /*   public void setCameraRatio(float pRatio){
        vScene.setCameraRatio(pRatio);
    }
*/
    public int getMaxSize(){
        return vMaxSize;
    }
}
