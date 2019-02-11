package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.DatabaseCursor;

/**
 * Created by kotarou on 6/6/16.
 */
public class StoryModeImagePart {

    public String vImageName;
    public String vPart;
    public String vAction;
    public String vActionTarget;
    public String vActionSource;

    public String vTargetOrientation;
    public String vSourceOrientation;

    public String vTouchAction;

    public StoryModeCoordination vTargetCoordination;
    public StoryModeCoordination vSourceCoordination;

    public int setImagePart(DatabaseCursor pDatabaseCursor){

        vTargetCoordination = new StoryModeCoordination();
        vSourceCoordination = new StoryModeCoordination();
        try {
            vImageName = pDatabaseCursor.getString(0);
            vPart = pDatabaseCursor.getString(1);
            vAction =  pDatabaseCursor.getString(2);
            vActionTarget = pDatabaseCursor.getString(3);
            vActionSource = pDatabaseCursor.getString(4);
            vTargetCoordination.vX = pDatabaseCursor.getInt(5);
            vTargetCoordination.vY = pDatabaseCursor.getInt(6);
            vTargetCoordination.vDX = pDatabaseCursor.getInt(7);
            vTargetCoordination.vDY = pDatabaseCursor.getInt(8);
            vTargetOrientation = pDatabaseCursor.getString(9);
            vSourceCoordination.vX = pDatabaseCursor.getInt(10);
            vSourceCoordination.vY = pDatabaseCursor.getInt(11);
            vSourceCoordination.vDX = pDatabaseCursor.getInt(12);
            vSourceCoordination.vDY = pDatabaseCursor.getInt(13);
            vSourceOrientation = pDatabaseCursor.getString(14);
            vTouchAction = pDatabaseCursor.getString(15);

            resizePart(1);

            return 1;
        }
        catch  (Exception e) {
            e.printStackTrace();
            return -98;
        }
    }

    public void resizePart(float pRatio){
        vTargetCoordination.vSceneConnectorX = (int) (vTargetCoordination.vX * pRatio);
        vTargetCoordination.vSceneConnectorY = (int) (vTargetCoordination.vY * pRatio);
        vTargetCoordination.vSceneConnectorDX = (int) (vTargetCoordination.vDX * pRatio);
        vTargetCoordination.vSceneConnectorDY = (int) (vTargetCoordination.vDY * pRatio);
        vSourceCoordination.vSceneConnectorX = (int) (vSourceCoordination.vX * pRatio);
        vSourceCoordination.vSceneConnectorY = (int) (vSourceCoordination.vY * pRatio);
        vSourceCoordination.vSceneConnectorDX = (int) (vSourceCoordination.vDX * pRatio);
        vSourceCoordination.vSceneConnectorDY = (int) (vSourceCoordination.vDY * pRatio);
    }

    public boolean collisionWithMe(int pCheckX, int pCheckY){ //, int pSensitivePoint){
        if (this.vTargetCoordination.vSceneConnectorX  < pCheckX &&
                pCheckX  < this.vTargetCoordination.vSceneConnectorX + this.vTargetCoordination.vSceneConnectorDX &&
                this.vTargetCoordination.vSceneConnectorY < pCheckY &&
                pCheckY < this.vTargetCoordination.vSceneConnectorY + this.vTargetCoordination.vSceneConnectorDY) {

            return true;
        } else {
            return false;
        }
    }
}
