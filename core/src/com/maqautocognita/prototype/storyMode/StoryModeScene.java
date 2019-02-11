package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.DatabaseCursor;

import java.util.ArrayList;

/**
 * Created by kotarou on 9/6/16.
 */
public class StoryModeScene {

    private String vSceneName;
    private String vImageName;
    private int vSceneID;
    private StoryModeArea vSceneArea;
    //private ArrayList<Integer> vSceneCenterLocation = new ArrayList<Integer>();
    //private ArrayList<String> vSceneLocationName = new ArrayList<String>();
    private ArrayList<StoryModeSceneLocation> vSceneLocation = new ArrayList<StoryModeSceneLocation>();

    private ArrayList<StoryModeImage> vSceneImageObject = new ArrayList<StoryModeImage>();
    private float vToSceneRatio = 1; //
    private float vCameraRatio =1;
    private float vSceneDisplayRatio;
    private ArrayList<String> vSceneWordObject = new ArrayList<String>();
    private ArrayList<String> vSceneGestures = new ArrayList<String>();
    private ArrayList<String> vSceneGesturesAction = new ArrayList<String>();

    private ArrayList<StoryModeSceneConjunction> vSceneConjunctionObject = new ArrayList<StoryModeSceneConjunction>();


    // create StoryModeScene
    public StoryModeScene(String pSceneName)
    {
        /*vWord = pWord;*/
        vImageName = pSceneName;
        vSceneName = pSceneName;
        vSceneArea = new StoryModeArea();
    }

    // initialize Scene image and size
    public void initScene(DatabaseCursor pDatabaseCursor, float pRatio){
        vSceneDisplayRatio = pRatio;
        this.vSceneName = pDatabaseCursor.getString(0);
        this.vImageName = pDatabaseCursor.getString(1);
        this.vSceneArea.vImageWidth = pDatabaseCursor.getInt(2);
        this.vSceneArea.vImageHeight = pDatabaseCursor.getInt(3);
        this.vSceneArea.vRealWidth = pDatabaseCursor.getInt(4);
        this.vToSceneRatio = this.vSceneArea.vImageWidth * 1.f / this.vSceneArea.vRealWidth;
        this.vSceneArea.vRealHeight = (int) (this.vSceneArea.vImageHeight / this.vToSceneRatio);
        setSceneDisplaySize();
    }

    private void setSceneDisplaySize() {
        this.vSceneArea.vSceneDisplayWidth = (int) (vSceneArea.vImageWidth * vSceneDisplayRatio);
        this.vSceneArea.vSceneDisplayHeight = (int) (vSceneArea.vImageHeight * vSceneDisplayRatio);
    }

    // initialize Scene location information
    public void initSceneLocation(DatabaseCursor pDatabaseCursor) {

        try {
            do {
                vSceneLocation.add(new StoryModeSceneLocation(pDatabaseCursor));
                //this.vSceneLocationName.add(pDatabaseCursor.getString(0));
                //this.vSceneCenterLocation.add(pDatabaseCursor.getInt(1));
            } while (pDatabaseCursor.next());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // get all image and image information for the scene and store in StoryModeImage Array
    public void initSceneObject(DatabaseCursor pDatabaseCursor) {

        StoryModeImage tempStoryModeImage;
        this.vSceneImageObject = new ArrayList<StoryModeImage>();
        try {
            do {
                tempStoryModeImage = new StoryModeImage(pDatabaseCursor);
                tempStoryModeImage.vSceneLocation = this.getSceneLocationName(tempStoryModeImage.vSceneLocationX);
                this.vSceneImageObject.add(tempStoryModeImage);
            } while (pDatabaseCursor.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSceneLocationName(int pX) {
        for (int i = 0; i < vSceneLocation.size(); i++) {
            if (vSceneLocation.get(i).getStartX() <= pX && pX <= vSceneLocation.get(i).getEndX()) {
                return vSceneLocation.get(i).getLocationName();
            }
        }
        return "";
    }

    public void initSceneAction(DatabaseCursor pDatabaseCursor){

        try {
            do {
                /*this.vSceneWordObject.add(pDatabaseCursor.getString(1));
                this.vSceneGestures.add(pDatabaseCursor.getString(2));
                this.vSceneGesturesAction.add(pDatabaseCursor.getString(3));*/
                for (int i = 0; i < vSceneImageObject.size(); i++){
                    if (vSceneImageObject.get(i).vObjectName.equals( pDatabaseCursor.getString(1))){
                        StoryModeImage tempObject  = vSceneImageObject.get(i);
                        tempObject.vGesture = pDatabaseCursor.getString((2));
                        tempObject.vGestureAction = pDatabaseCursor.getString(3);
                        vSceneImageObject.set(i,tempObject);
                    }
                }
            } while (pDatabaseCursor.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initSceneConjunction(DatabaseCursor pDatabaseCursor){
        try {
            do {
                StoryModeSceneConjunction vSCO = new StoryModeSceneConjunction();
                vSCO.vObject = pDatabaseCursor.getString(0);
                vSCO.vToScene = pDatabaseCursor.getString(1);
                vSCO.vToSceneObject = pDatabaseCursor.getString(2);
                vSceneConjunctionObject.add(vSCO);
            } while (pDatabaseCursor.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSceneGesturesAction(String pWordObject){
        for (int i = 0; i < vSceneWordObject.size();i++){
            if (vSceneWordObject.get(i).equals(pWordObject)){
                return vSceneGesturesAction.get(i);
            }
        }
        return "";
    }

    public String getSceneName() {
        return vSceneName;
    }

    public float getToSceneRatio(){
        return vToSceneRatio;
    }

    public void setToSceneRatio(float pToSceneRatio){
        vToSceneRatio = pToSceneRatio;
    }

    public float getCameraRatio (){
        return vCameraRatio;
    }

    public void setCameraRatio(float pCameraRatio) {
        vCameraRatio = pCameraRatio;
    }

    public float getSceneDisplayRatio(){
        return vSceneDisplayRatio;
    }

    public void setSceneDisplayRatio(float pRatio) {
        vSceneDisplayRatio = pRatio;
        setSceneDisplaySize();
    }

    public ArrayList<StoryModeSceneLocation> getvSceneLocationName() {
        return vSceneLocation;
    }

    public ArrayList<StoryModeImage> getSceneImageObject() {
        return vSceneImageObject;
    }

    public StoryModeImage getSceneImageObject(String pObjectName){
        StoryModeImage tReturnImage = new StoryModeImage();
        tReturnImage.vObjectName = "";

        for (int i = 0 ; i<vSceneImageObject.size();i++){
            if (vSceneImageObject.get(i).vObjectName.equals(pObjectName)){
                tReturnImage = vSceneImageObject.get(i);
                return tReturnImage;
            }
        }

        return tReturnImage;
    }

    public void putImageObjectToMe(StoryModeImage pStoryModeImage){
        vSceneImageObject.add(pStoryModeImage);
    }


    public int getSceneCenterLocation(String pLocationName){
        for (int i =0; i<vSceneLocation.size(); i++) {
            if (pLocationName.equals(vSceneLocation.get(i).getLocationName())) {

                return vSceneLocation.get(i).getCenterX();
            }
        }
        return 0;
    }

    public int getSceneStartXLocation(String pLocationName) {
        for (int i = 0; i < vSceneLocation.size(); i++) {
            if (pLocationName.equals(vSceneLocation.get(i).getLocationName())) {
                return vSceneLocation.get(i).getStartX();
            }
        }
        return 0;
    }
/*
    public void setSceneCenterLocation(ArrayList<Integer> vSceneCenterLocation) {
        this.vSceneCenterLocation = vSceneCenterLocation;
    }
*/
    public String getImageName() {
        return vImageName;
    }

    public StoryModeArea getvSceneArea() {
        return vSceneArea;
    }

    public StoryModeSceneConjunction checkSceneConjunction(String pObjectName){
        StoryModeSceneConjunction retConjunction = new StoryModeSceneConjunction();
        for (int i =0;i<vSceneConjunctionObject.size();i++ ){
            if(vSceneConjunctionObject.get(i).vObject.equals(pObjectName)){
                retConjunction = vSceneConjunctionObject.get(i);
            }
        }
        return retConjunction;
    }
}
