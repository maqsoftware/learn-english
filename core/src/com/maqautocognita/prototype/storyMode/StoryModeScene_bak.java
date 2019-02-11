package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.DatabaseCursor;

/**
 * Created by kotarou on 9/6/16.
 */
public class StoryModeScene_bak {

    private String vSceneName;
    private String vImageName;
    private StoryModeArea vSceneArea;
    private float vToSceneRatio = 1; //
    private float vCameraRatio = 1;
    private float vSceneDisplayRatio;


    public StoryModeScene_bak(String pSceneName) {
        /*vWord = pWord;*/
        vImageName = pSceneName;
        vSceneArea = new StoryModeArea();
    }

    public void initScene(DatabaseCursor pDatabaseCursor, float pRatio) {
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


    public String getSceneName() {
        return vSceneName;
    }

    public float getToSceneRatio() {
        return vToSceneRatio;
    }

    public float getCameraRatio() {
        return vCameraRatio;
    }

    public void setCameraRatio(float pCameraRatio) {
        vCameraRatio = pCameraRatio;
    }

    public float getSceneDisplayRatio() {
        return vSceneDisplayRatio;
    }

    public void setSceneDisplayRatio(float pRatio) {
        vSceneDisplayRatio = pRatio;
        setSceneDisplaySize();
    }

    private void setSceneDisplaySize() {
        this.vSceneArea.vSceneDisplayWidth = (int) (vSceneArea.vImageWidth * vSceneDisplayRatio);
        this.vSceneArea.vSceneDisplayHeight = (int) (vSceneArea.vImageHeight * vSceneDisplayRatio);
    }

}
