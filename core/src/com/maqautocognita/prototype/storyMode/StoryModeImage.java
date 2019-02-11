package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.DatabaseCursor;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kotarou on 31/5/16.
 */
public class StoryModeImage {
    private static final int sImageWidth = 1000;
    public String vImageType = "Object"; //Define the type of the image
    public String vObjectName; //Object name for Scene
    public String vImageName; // file name of the image
    public String vRelatedWord; // That image is relate to which word
    public StoryModeArea vImageArea;

    public String vWidthOrHeight;

    public int vWholeImageIndex = -1; // store the image part index which is for the whole image properties
    public float vRealSizeRatio; //ratio for the real size per cm

    public float vWidthHeightRatio;

    public int vSceneLocationX; //pixel without camera ratio, the location that 0,0 in the scene
    public int vSceneLocationY; //pixel without camera ratio

    public int vEdgeX = 0, vEdgeY = 0, vEdgeWidth = 0, vEdgeHeight = 0;
    public int vDispEdgeX = 0, vDispEdgeY = 0, vDispEdgeWidth = 0, vDispEdgeHeight = 0;

    public StoryModeCoordination vSourceCoordination;

    public String vSceneLocation;

    public StoryModeImagePart vImagePart[];
    public int vLevel;
    public int vLayer;
    public String vGesture;
    public String vGestureAction;

    public StoryModeImage() {

        vImageType = "Scene";
        vSourceCoordination = new StoryModeCoordination();
        vImageArea = new StoryModeArea();
        //initImage(pDatabaseImageCursor);

    }

    public StoryModeImage(StoryModeImage pObjectLocation, DatabaseCursor pDatabaseCursor, StoryModeScene pCompareScene, int pMaxSize){
        vImageType = "SceneImage";

        setLessonImage(pObjectLocation,pDatabaseCursor, pCompareScene, pMaxSize);
    }

    public StoryModeImage(StoryModeImage pObjectLocation, DatabaseCursor pDatabaseCursor, StoryModeScene pCompareScene, int pMaxSize, String pMode){
        vImageType = "SceneImage";

        if (pMode.equals("mission")) {
            setMissionImage(pObjectLocation, pDatabaseCursor, pCompareScene, pMaxSize);
        }else{
            setLessonImage(pObjectLocation,pDatabaseCursor, pCompareScene, pMaxSize);
        }
    }

    public StoryModeImage(DatabaseCursor pDatabaseImageCursor) {

        vImageType = "SceneImage";
        vSourceCoordination = new StoryModeCoordination();
        vImageArea = new StoryModeArea();
        int vTemp = initSceneImage(pDatabaseImageCursor);
        this.vLevel = pDatabaseImageCursor.getInt(7);
        this.vLayer = pDatabaseImageCursor.getInt(8);
        this.vRelatedWord = pDatabaseImageCursor.getString(9);
        this.vEdgeX =pDatabaseImageCursor.getInt(10);
        this.vEdgeY = pDatabaseImageCursor.getInt(11);
        this.vEdgeWidth = pDatabaseImageCursor.getInt(12);
        this.vEdgeHeight = pDatabaseImageCursor.getInt(13);


    }

    public StoryModeImage(DatabaseCursor pDatabaseImageCursor, DatabaseCursor pDatabaseImagePartCursor) {

        vImageType = "Object";
        vSourceCoordination = new StoryModeCoordination();
        vImageArea = new StoryModeArea();
        initImage(pDatabaseImageCursor, pDatabaseImagePartCursor);


    }

    /**
     * Check if the given compareObject is same to this object by check the {@link #vObjectName}, {@link #vSceneLocationX} ,{@link #vSceneLocationY} and  {@link #vImageName}
     *
     * @param compareObject
     * @return
     */
    @Override
    public boolean equals(Object compareObject) {
        if (null == compareObject) {
            return false;
        } else {
            StoryModeImage compareStoryModeImage = (StoryModeImage) compareObject;

            return vObjectName.equals(compareStoryModeImage.vObjectName) && vSceneLocationX == compareStoryModeImage.vSceneLocationX && vSceneLocationY == compareStoryModeImage.vSceneLocationY &&
                    (null != vImageName && vImageName.equals(compareStoryModeImage.vImageName));
        }
    }

    //set lesson object for scene
    public int setLessonImage(StoryModeImage pObjectLocation, DatabaseCursor pDatabaseCursor, StoryModeScene pCompareScene, int pMaxSize){
        Random vRan = new Random();
        StoryModeImage returnImage = new StoryModeImage();
        vImageArea = new StoryModeArea();

        vImageName = pDatabaseCursor.getString(0);
        vObjectName = pDatabaseCursor.getString(0);


        vWidthOrHeight = "h";
        vImageArea.vImageWidth = pDatabaseCursor.getInt(1);
        vImageArea.vImageHeight = pDatabaseCursor.getInt(2);

        this.vWidthHeightRatio = vImageArea.vImageWidth * 1.f / vImageArea.vImageHeight;

        vImageArea.vRealWidth = (int) (pDatabaseCursor.getInt(3) * 1.f * this.vWidthHeightRatio);
        vImageArea.vRealHeight = pDatabaseCursor.getInt(3);
        vRealSizeRatio = vImageArea.vImageHeight * 1.f / vImageArea.vRealHeight;
        vRelatedWord = pDatabaseCursor.getString(7);

        //setMyDisplaySize(this.vImageArea.vImageWidth);
        setMeToScene(pCompareScene);

        // Resize for lesson
        if (pMaxSize !=0) {
            if (vImageArea.vSceneDisplayWidth < pMaxSize || vImageArea.vSceneDisplayWidth > pMaxSize + 50) {
                float enlargeRation = pMaxSize * 1.f / vImageArea.vSceneDisplayWidth;
                if (vImageArea.vSceneDisplayHeight * enlargeRation > pMaxSize) {
                    enlargeRation = pMaxSize * 1.f / vImageArea.vSceneDisplayHeight;
                }
                if (enlargeRation == 0) {
                    enlargeRation = 1;
                }
                vImageArea.vSceneDisplayWidth = (int) (vImageArea.vSceneDisplayWidth * enlargeRation);
                vImageArea.vSceneDisplayHeight = (int) (vImageArea.vSceneDisplayHeight * enlargeRation);

            } else if (vImageArea.vSceneDisplayHeight < pMaxSize || vImageArea.vSceneDisplayHeight > pMaxSize+50) {
                float enlargeRation = pMaxSize * 1.f / vImageArea.vSceneDisplayHeight;
                if (vImageArea.vSceneDisplayWidth * enlargeRation > pMaxSize) {
                    enlargeRation = pMaxSize * 1.f / vImageArea.vSceneDisplayWidth;
                }
                if (enlargeRation == 0) {
                    enlargeRation = 1;
                }
                vImageArea.vSceneDisplayHeight = (int) (vImageArea.vSceneDisplayHeight * enlargeRation);
                vImageArea.vSceneDisplayWidth = (int) (vImageArea.vSceneDisplayWidth * enlargeRation);
            }
        }





        if (pObjectLocation.vEdgeWidth == 0 || pObjectLocation.vEdgeHeight == 0) {
            vSceneLocationX = vRan.nextInt(pObjectLocation.vImageArea.vImageWidth) + pObjectLocation.vSceneLocationX;
            vSceneLocationY = vRan.nextInt(pObjectLocation.vImageArea.vImageHeight) + pObjectLocation.vSceneLocationY;
        }else {
            vSceneLocationX = vRan.nextInt(pObjectLocation.vEdgeWidth) + pObjectLocation.vSceneLocationX + pObjectLocation.vEdgeX;
            vSceneLocationY = vRan.nextInt(pObjectLocation.vEdgeHeight) + pObjectLocation.vSceneLocationY + pObjectLocation.vEdgeY;
        }
        vSceneLocationX = checkCollision(this, pCompareScene, "");

        vLevel = 1;
        vLayer = 1;


        return 1;
    }

    //set lesson object for scene
    public int setMissionImage(StoryModeImage pObjectLocation, DatabaseCursor pDatabaseCursor, StoryModeScene pCompareScene, int pMaxSize){
        Random vRan = new Random();
        StoryModeImage returnImage = new StoryModeImage();
        vImageArea = new StoryModeArea();

        vImageName = pDatabaseCursor.getString(0);
        vObjectName = pDatabaseCursor.getString(0);


        vWidthOrHeight = "h";
        vImageArea.vImageWidth = pDatabaseCursor.getInt(1);
        vImageArea.vImageHeight = pDatabaseCursor.getInt(2);

        this.vWidthHeightRatio = vImageArea.vImageWidth * 1.f / vImageArea.vImageHeight;

        vImageArea.vRealWidth = (int) (pDatabaseCursor.getInt(3) * 1.f * this.vWidthHeightRatio);
        vImageArea.vRealHeight = pDatabaseCursor.getInt(3);
        vRealSizeRatio = vImageArea.vImageHeight * 1.f / vImageArea.vRealHeight;
        vRelatedWord = pDatabaseCursor.getString(7);

        //setMyDisplaySize(this.vImageArea.vImageWidth);
        setMeToScene(pCompareScene);

        // Resize for lesson
        if (pMaxSize !=0) {
            if (vImageArea.vSceneDisplayWidth < pMaxSize || vImageArea.vSceneDisplayWidth > pMaxSize + 50) {
                float enlargeRation = pMaxSize * 1.f / vImageArea.vSceneDisplayWidth;
                if (vImageArea.vSceneDisplayHeight * enlargeRation > pMaxSize) {
                    enlargeRation = pMaxSize * 1.f / vImageArea.vSceneDisplayHeight;
                }
                if (enlargeRation == 0) {
                    enlargeRation = 1;
                }
                vImageArea.vSceneDisplayWidth = (int) (vImageArea.vSceneDisplayWidth * enlargeRation);
                vImageArea.vSceneDisplayHeight = (int) (vImageArea.vSceneDisplayHeight * enlargeRation);

            } else if (vImageArea.vSceneDisplayHeight < pMaxSize || vImageArea.vSceneDisplayHeight > pMaxSize+50) {
                float enlargeRation = pMaxSize * 1.f / vImageArea.vSceneDisplayHeight;
                if (vImageArea.vSceneDisplayWidth * enlargeRation > pMaxSize) {
                    enlargeRation = pMaxSize * 1.f / vImageArea.vSceneDisplayWidth;
                }
                if (enlargeRation == 0) {
                    enlargeRation = 1;
                }
                vImageArea.vSceneDisplayHeight = (int) (vImageArea.vSceneDisplayHeight * enlargeRation);
                vImageArea.vSceneDisplayWidth = (int) (vImageArea.vSceneDisplayWidth * enlargeRation);
            }
        }

        if (pObjectLocation.vEdgeWidth == 0 || pObjectLocation.vEdgeHeight == 0) {
            vSceneLocationX = vRan.nextInt(pObjectLocation.vImageArea.vImageWidth) + pObjectLocation.vSceneLocationX;
            vSceneLocationY = vRan.nextInt(pObjectLocation.vImageArea.vImageHeight) + pObjectLocation.vSceneLocationY;
        }else {
            vSceneLocationX = vRan.nextInt(pObjectLocation.vEdgeWidth) + pObjectLocation.vSceneLocationX + pObjectLocation.vEdgeX;
            vSceneLocationY = vRan.nextInt(pObjectLocation.vEdgeHeight) + pObjectLocation.vSceneLocationY + pObjectLocation.vEdgeY;
        }
        //vSceneLocationX = checkCollision(this, pCompareScene, "");

        vLevel = 1;
        vLayer = 1;

        return 1;
    }


    private int checkCollision(StoryModeImage pImage, StoryModeScene pCompareScene, String pDirection){
        ArrayList<StoryModeImage> vCompareImageList = pCompareScene.getSceneImageObject();
        for (int i =0; i<vCompareImageList.size(); i++) {
            if ((vCompareImageList.get(i).vSceneLocationY < pImage.vSceneLocationY &&
                    pImage.vSceneLocationY < vCompareImageList.get(i).vSceneLocationY + vCompareImageList.get(i).vImageArea.vSceneDisplayHeight) ||
                    (vCompareImageList.get(i).vSceneLocationY < pImage.vSceneLocationY + pImage.vImageArea.vSceneDisplayHeight &&
                    pImage.vSceneLocationY + pImage.vImageArea.vSceneDisplayHeight < vCompareImageList.get(i).vSceneLocationY + vCompareImageList.get(i).vImageArea.vSceneDisplayHeight) ||
                    (pImage.vSceneLocationY <= vCompareImageList.get(i).vSceneLocationY && vCompareImageList.get(i).vSceneLocationY + vCompareImageList.get(i).vImageArea.vSceneDisplayHeight <= pImage.vSceneLocationY + pImage.vImageArea.vSceneDisplayHeight)) {

                if ((vCompareImageList.get(i).vSceneLocationX < pImage.vSceneLocationX &&
                        pImage.vSceneLocationX < vCompareImageList.get(i).vSceneLocationX + vCompareImageList.get(i).vImageArea.vSceneDisplayWidth) ||
                        (vCompareImageList.get(i).vSceneLocationX < pImage.vSceneLocationX + pImage.vImageArea.vSceneDisplayWidth &&
                                pImage.vSceneLocationX + pImage.vImageArea.vSceneDisplayWidth < vCompareImageList.get(i).vSceneLocationX + vCompareImageList.get(i).vImageArea.vSceneDisplayWidth) ||
                        (pImage.vSceneLocationX <= vCompareImageList.get(i).vSceneLocationX && vCompareImageList.get(i).vSceneLocationX + vCompareImageList.get(i).vImageArea.vSceneDisplayWidth <= pImage.vSceneLocationX + pImage.vImageArea.vSceneDisplayWidth)) {
                    if (pDirection.equals("")) {
                        if (pImage.vSceneLocationX - vCompareImageList.get(i).vSceneLocationX < vCompareImageList.get(i).vSceneLocationX + vCompareImageList.get(i).vImageArea.vSceneDisplayWidth - pImage.vSceneLocationX) {
                            pImage.vSceneLocationX = vCompareImageList.get(i).vSceneLocationX - pImage.vImageArea.vSceneDisplayWidth;
                            pDirection = "L";
                        } else {
                            pImage.vSceneLocationX = vCompareImageList.get(i).vSceneLocationX + vCompareImageList.get(i).vImageArea.vSceneDisplayWidth;
                            pDirection = "R";
                        }
                    } else if (pDirection.equals("L")) {
                        pImage.vSceneLocationX = vCompareImageList.get(i).vSceneLocationX - pImage.vImageArea.vSceneDisplayWidth;
                        pDirection = "L";
                    } else {
                        pImage.vSceneLocationX = vCompareImageList.get(i).vSceneLocationX + vCompareImageList.get(i).vImageArea.vSceneDisplayWidth;
                        pDirection = "R";
                    }
                    pImage.vSceneLocationX = checkCollision(pImage, pCompareScene, pDirection);
                }
            }
        }


        return pImage.vSceneLocationX;
    }

    public int initSceneImage(DatabaseCursor pDatabaseImageCursor) {
//"select ImageWordName, ObjectID, ObjectX, ObjectY, ObjectWodth, ObjectHeight, ObjectRealHeight, Level from SceneObject
        if (vImageType == "SceneImage") {
            int partCount = 0;
            try {

                vImageName = pDatabaseImageCursor.getString(0);
                // Scene image must using Height for real height
                vObjectName = pDatabaseImageCursor.getString(1);
                vWidthOrHeight = "h";
                vImageArea.vImageWidth = pDatabaseImageCursor.getInt(4);
                vImageArea.vImageHeight = pDatabaseImageCursor.getInt(5);

                this.vWidthHeightRatio = vImageArea.vImageWidth * 1.f / vImageArea.vImageHeight;

                vImageArea.vRealWidth = (int) (pDatabaseImageCursor.getInt(6) * 1.f * this.vWidthHeightRatio);
                vImageArea.vRealHeight = pDatabaseImageCursor.getInt(6);
                vRealSizeRatio = vImageArea.vImageHeight * 1.f / vImageArea.vRealHeight;

                setMyDisplaySize(this.vImageArea.vImageWidth);

                vSceneLocationX = pDatabaseImageCursor.getInt(2);
                vSceneLocationY = pDatabaseImageCursor.getInt(3);
                vLevel = pDatabaseImageCursor.getInt(7);
                vLayer = pDatabaseImageCursor.getInt(8);
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return -98;
            }
        } else {
            return -97; //Image only apply for SceneImage;
        }
    }

    public int initImage(DatabaseCursor pDatabaseImageCursor) {

        if (vImageType == "Scene") {
            int partCount = 0;
            try {

                vImageName = pDatabaseImageCursor.getString(0);
                vRelatedWord = pDatabaseImageCursor.getString(1);
                vWidthOrHeight = pDatabaseImageCursor.getString(4);
                vImageArea.vImageWidth = pDatabaseImageCursor.getInt(2);
                vImageArea.vImageHeight = pDatabaseImageCursor.getInt(3);

                this.vWidthHeightRatio = vImageArea.vImageWidth * 1.f / vImageArea.vImageHeight;
                if (vWidthOrHeight.equals("w")) {

                    vImageArea.vRealWidth = pDatabaseImageCursor.getInt(5);
                    vImageArea.vRealHeight = (int) (pDatabaseImageCursor.getInt(5) * 1.f / this.vWidthHeightRatio);
                    vRealSizeRatio = vImageArea.vImageWidth * 1.f / vImageArea.vRealWidth;

                } else {
                    vImageArea.vRealWidth = (int) (pDatabaseImageCursor.getInt(5) * 1.f * this.vWidthHeightRatio);
                    vImageArea.vRealHeight = pDatabaseImageCursor.getInt(5);
                    vRealSizeRatio = vImageArea.vImageHeight * 1.f / vImageArea.vRealHeight;
                }

                setMyDisplaySize(this.vImageArea.vImageWidth);

                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return -98;
            }
        } else {
            return -97; //Image only apply for Scene;
        }
    }


    public int initImage(DatabaseCursor pDatabaseImageCursor, DatabaseCursor pDatabaseImagePartCursor) {

        int partCount = 0;
        try {

            vImageName = pDatabaseImageCursor.getString(0);
            vRelatedWord = pDatabaseImageCursor.getString(1);
            vWidthOrHeight = pDatabaseImageCursor.getString(4);
            vImageArea.vImageWidth = pDatabaseImageCursor.getInt(2);
            vImageArea.vImageHeight = pDatabaseImageCursor.getInt(3);

            this.vWidthHeightRatio = vImageArea.vImageWidth * 1.f / vImageArea.vImageHeight;
            if (vWidthOrHeight.equals("w")) {

                vImageArea.vRealWidth = pDatabaseImageCursor.getInt(5);
                vImageArea.vRealHeight = (int) (pDatabaseImageCursor.getInt(5) * 1.f / this.vWidthHeightRatio);
                vRealSizeRatio = vImageArea.vImageWidth * 1.f / vImageArea.vRealWidth;

            } else {
                vImageArea.vRealWidth = (int) (pDatabaseImageCursor.getInt(5) * 1.f * this.vWidthHeightRatio);
                vImageArea.vRealHeight = pDatabaseImageCursor.getInt(5);
                vRealSizeRatio = vImageArea.vImageHeight * 1.f / vImageArea.vRealHeight;
            }

            // set image Part
            vImagePart = new StoryModeImagePart[pDatabaseImagePartCursor.getCount()];
            partCount = 0;
            if (pDatabaseImagePartCursor.moveToFirst()) {
                do {
                    vImagePart[partCount] = new StoryModeImagePart();
                    vImagePart[partCount].setImagePart(pDatabaseImagePartCursor);
                    if (vImagePart[partCount].vPart.equals("whole")) {
                        this.vWholeImageIndex = partCount;
                    }

                    partCount = partCount + 1;
                } while (pDatabaseImagePartCursor.next());
            }
            setMyDisplaySize(this.vImageArea.vImageWidth);

            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -98;
        }
    }

    private void setMyDisplaySize(int pWidth/* point */) {

        try {
            float displayResizeRatio = (float) pWidth / this.vImageArea.vImageWidth;
            this.vImageArea.vSceneDisplayWidth = (int) (this.vImageArea.vImageWidth * displayResizeRatio);
            this.vImageArea.vSceneDisplayHeight = (int) (this.vImageArea.vImageHeight * displayResizeRatio);

            this.vDispEdgeX = (int) (this.vEdgeX * displayResizeRatio);
            this.vDispEdgeY = (int) (this.vEdgeY * displayResizeRatio);
            this.vDispEdgeWidth = (int) (this.vEdgeWidth * displayResizeRatio);
            this.vDispEdgeHeight = (int) (this.vEdgeHeight * displayResizeRatio);

            if (null != vImagePart && vImagePart.length != 0) {
                if (vWholeImageIndex >= 0) {
                    vSourceCoordination.vSceneConnectorX = (int) (this.vImagePart[vWholeImageIndex].vSourceCoordination.vX * displayResizeRatio);
                    vSourceCoordination.vSceneConnectorY = (int) (this.vImagePart[vWholeImageIndex].vSourceCoordination.vY * displayResizeRatio);
                    vSourceCoordination.vSceneConnectorDX = (int) (this.vImagePart[vWholeImageIndex].vSourceCoordination.vDX * displayResizeRatio);
                    vSourceCoordination.vSceneConnectorDY = (int) (this.vImagePart[vWholeImageIndex].vSourceCoordination.vDY * displayResizeRatio);
                }
                for (int i = 0; i < this.vImagePart.length; i++) {
                    this.vImagePart[i].resizePart(displayResizeRatio);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setMeToScene(StoryModeScene pScene, int pSceneX, int pSceneY) {
        //vMyScene = pScene;
        vSceneLocationX = pSceneX; // (int) (pSceneX / pScene.getCameraRatio());
        vSceneLocationY = pSceneY; // (int) (pSceneY / pScene.getCameraRatio());
        setMyDisplaySize((int) (vImageArea.vRealWidth * pScene.getToSceneRatio() * pScene.getSceneDisplayRatio()));
    }

    public void setMeToScene(StoryModeScene pScene){
        setMyDisplaySize((int) (vImageArea.vRealWidth * pScene.getToSceneRatio() * pScene.getSceneDisplayRatio()));
    }


    public void putMeToTarget(StoryModeImage pTargetImageWord, int pSceneX, int pSceneY, StoryModeScene pScene) {

        setMyDisplaySize((int) (vImageArea.vRealWidth * pScene.getToSceneRatio() * pScene.getSceneDisplayRatio()));

        int vTargetPart = -1;

        int vImageToTargetX = getImagePointAtTargetX(pSceneX, pTargetImageWord);
        int vImageToTargetY = getImagePointAtTargetY(pSceneY, pTargetImageWord);
        /// loop for all part in the target
        for (int partCount = 0; partCount < pTargetImageWord.vImagePart.length; partCount++) {
            if (pTargetImageWord.vImagePart[partCount].collisionWithMe(vImageToTargetX, vImageToTargetY)) {
                vTargetPart = partCount;
            }
        }

        // check if match the connection (Orientation)
        if (vTargetPart >= 0) {
            // handle point target
            if (this.vImagePart[vWholeImageIndex].vSourceOrientation.equals("point")) {
                this.vSceneLocationX = pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vX + pTargetImageWord.vSceneLocationX - this.vSourceCoordination.vSceneConnectorX;
                this.vSceneLocationY = pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vY + pTargetImageWord.vSceneLocationY - this.vSourceCoordination.vSceneConnectorY;
            } else if (this.vImagePart[vWholeImageIndex].vSourceOrientation.equals("bottom")) {
                this.vSceneLocationX = pTargetImageWord.vSceneLocationX + pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vSceneConnectorX - this.vSourceCoordination.vSceneConnectorX;
                this.vSceneLocationY = pTargetImageWord.vSceneLocationY + pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vSceneConnectorY + pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vSceneConnectorDY - this.vSourceCoordination.vSceneConnectorDY;
            } else if (this.vImagePart[vWholeImageIndex].vSourceOrientation.equals("top")) {
                this.vSceneLocationX = pTargetImageWord.vSceneLocationX + pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vSceneConnectorX - this.vSourceCoordination.vSceneConnectorX;
                this.vSceneLocationY = pTargetImageWord.vSceneLocationY + pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vSceneConnectorY - this.vSourceCoordination.vSceneConnectorDY;
            }
        } else {

            this.vSceneLocationX = pSceneX;
            this.vSceneLocationY = pSceneY;
        }

    }

    public void putMeToTarget(StoryModeImage pTargetImageWord, int pSceneX, int pSceneY) {

        setMyDisplaySize((int) (vImageArea.vRealWidth * pTargetImageWord.vRealSizeRatio));

        int vTargetPart = -1;

        int vImageToTargetX = getImagePointAtTargetX(pSceneX, pTargetImageWord);
        int vImageToTargetY = getImagePointAtTargetY(pSceneY, pTargetImageWord);
        /// loop for all part in the target
        for (int partCount = 0; partCount < pTargetImageWord.vImagePart.length; partCount++) {
            if (pTargetImageWord.vImagePart[partCount].collisionWithMe(vImageToTargetX, vImageToTargetY)) {
                vTargetPart = partCount;
            }
        }

        // check if match the connection (Orientation)
        if (vTargetPart >= 0) {
            // handle point target
            if (this.vImagePart[vWholeImageIndex].vSourceOrientation.equals("point")) {
                this.vSceneLocationX = pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vX + pTargetImageWord.vSceneLocationX - this.vSourceCoordination.vSceneConnectorX;
                this.vSceneLocationY = pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vY + pTargetImageWord.vSceneLocationY - this.vSourceCoordination.vSceneConnectorY;
            } else if (this.vImagePart[vWholeImageIndex].vSourceOrientation.equals("bottom")) {
                this.vSceneLocationX = pTargetImageWord.vSceneLocationX + pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vSceneConnectorX - this.vSourceCoordination.vSceneConnectorX;
                this.vSceneLocationY = pTargetImageWord.vSceneLocationY + pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vSceneConnectorY + pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vSceneConnectorDY - this.vSourceCoordination.vSceneConnectorDY;
            } else if (this.vImagePart[vWholeImageIndex].vSourceOrientation.equals("top")) {
                this.vSceneLocationX = pTargetImageWord.vSceneLocationX + pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vSceneConnectorX - this.vSourceCoordination.vSceneConnectorX;
                this.vSceneLocationY = pTargetImageWord.vSceneLocationY + pTargetImageWord.vImagePart[vTargetPart].vTargetCoordination.vSceneConnectorY - this.vSourceCoordination.vSceneConnectorDY;
            }
        } else {

            this.vSceneLocationX = pSceneX;
            this.vSceneLocationY = pSceneY;
        }

    }

    public void putMeIntoTarget(StoryModeImage pTargetImageWord, int pSceneX, int pSceneY, StoryModeScene pScene) {
        this.vSceneLocationX = pSceneX;
        this.vSceneLocationY = pSceneY;
    }

    private int getImagePointAtTargetX(int pInt, StoryModeImage pTargetImage) {
        int imageX = 0;
        imageX = pInt - pTargetImage.vSceneLocationX;
        return imageX;
    }

    private int getImagePointAtTargetY(int pInt, StoryModeImage pTargetImage) {
        int imageY = 0;
        imageY = pInt - pTargetImage.vSceneLocationY;
        return imageY;
    }


    public void setMeToOriginal() {

    }

}
