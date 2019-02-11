package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.DatabaseCursor;

/**
 * Created by kotarou on 31/5/16.
 */
public class StoryModeImage_bak {
    private static final int sImageWidth = 1000;
    public String vImageType = "Object";
    public String vImageName;
    public String vRelatedWord;
    public StoryModeArea vImageArea;

    public String vWidthOrHeight;

    public int vWholeImageIndex = -1; // store the image part index which is for the whole image properties
    public float vRealSizeRatio; //ratio for the real size per cm

    public float vWidthHeightRatio;

    public int vSceneLocationX; //pixel without camera ratio, the location that 0,0 in the scene
    public int vSceneLocationY; //pixel without camera ratio

    public StoryModeCoordination vSourceCoordination;

    public StoryModeImagePart vImagePart[];

    public StoryModeImage_bak() {

        vImageType = "Scene";
        vSourceCoordination = new StoryModeCoordination();
        vImageArea = new StoryModeArea();
        //initImage(pDatabaseImageCursor);

    }

    public StoryModeImage_bak(DatabaseCursor pDatabaseImageCursor, DatabaseCursor pDatabaseImagePartCursor) {

        vImageType = "Object";
        vSourceCoordination = new StoryModeCoordination();
        vImageArea = new StoryModeArea();
        initImage(pDatabaseImageCursor, pDatabaseImagePartCursor);

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

            if (vImagePart.length != 0) {
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

    public void putMeToTarget(StoryModeImage_bak pTargetImageWord, int pSceneX, int pSceneY, StoryModeScene pScene) {

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

    public void putMeToTarget(StoryModeImage_bak pTargetImageWord, int pSceneX, int pSceneY) {

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

    public void putMeIntoTarget(StoryModeImage_bak pTargetImageWord, int pSceneX, int pSceneY, StoryModeScene pScene) {
        this.vSceneLocationX = pSceneX;
        this.vSceneLocationY = pSceneY;
    }

    private int getImagePointAtTargetX(int pInt, StoryModeImage_bak pTargetImage) {
        int imageX = 0;
        imageX = pInt - pTargetImage.vSceneLocationX;
        return imageX;
    }

    private int getImagePointAtTargetY(int pInt, StoryModeImage_bak pTargetImage) {
        int imageY = 0;
        imageY = pInt - pTargetImage.vSceneLocationY;
        return imageY;
    }


    public void setMeToOriginal() {

    }

}
