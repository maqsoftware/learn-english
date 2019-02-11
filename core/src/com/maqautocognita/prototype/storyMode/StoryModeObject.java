package com.maqautocognita.prototype.storyMode;

import java.util.ArrayList;

/**
 * Created by kotarou on 15/6/16.
 */
public class StoryModeObject {
    StoryModeImage vBaseImage;
    private ArrayList<StoryModeImage> vImageList = new ArrayList<StoryModeImage>();
    private ArrayList<StoryModeObject> vObjectList = new ArrayList<StoryModeObject>();
    private int vParentLocationX;
    private int vParentLocationY;

    public StoryModeObject(StoryModeImage pBaseImage) {

        //vNumberOfImage = 1;
        //vImageList = new StoryModeImage[vNumberOfImage];
        vBaseImage = pBaseImage;
        vParentLocationX = 0;
        vParentLocationY = 0;
        //vImageList[vNumberOfImage - 1] = baseImage;
        //vNumberOfObject = 0;

    }

    public void addImage(StoryModeImage pImage, int pScreenX, int pScreenY) {
        //vNumberOfImage ++ ;
        //ArrayList<StoryModeImage> vImageList = new ArrayList<StoryModeImage>(Arrays.asList(vImageList));
        //  pImage.putMeIntoTarget(vBaseImage,pX,pY);
        pImage.putMeToTarget(vBaseImage, pScreenX - vParentLocationX, pScreenY - vParentLocationY);
        vImageList.add(pImage);


        //vImageList = new StoryModeImage[vNumberOfImage];
        //vImageList[vNumberOfImage - 1] = pImage;
    }

    public void changeImage(StoryModeImage pImage){

    }

    public void putObjectToMe(StoryModeObject pObject) {
        vObjectList.add(pObject);
        //vObjectList =
    }

    public ArrayList<StoryModeImage> getAllImage() {
        ArrayList vArrayList = new ArrayList<StoryModeImage>();


        vArrayList.add(vBaseImage);
        for (int i = 0; i < vImageList.size(); i++) {
            vArrayList.add(vImageList.get(i));
        }

        for (int j = 0; j < vObjectList.size(); j++) {
            for (int k = 0; k < vObjectList.get(j).getAllImage().size(); k++) {
                vArrayList.add(vObjectList.get(j).getAllImage().get(k));
            }

        }

        return vArrayList;
    }


    public void setMeToScene(StoryModeScene pScene, int pSceneX, int pSceneY) {
        //vMyScene = pScene;
        int vDLocationX = vParentLocationX - pSceneX;
        int vDLocationY = vParentLocationY - pSceneY;
        vParentLocationX = pSceneX; // (int) (pSceneX / pScene.getCameraRatio());
        vParentLocationY = pSceneY; // (int) (pSceneY / pScene.getCameraRatio());
        vBaseImage.setMeToScene(pScene, pSceneX, pSceneY);
        for (int i = 0; i < vImageList.size(); i++) {
            //vImageList.get(i).move(vDLocationX, vDLocationY);
        }
    }
}
