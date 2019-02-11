package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.DatabaseCursor;

/**
 * Created by kotarou on 10/6/16.
 */
public class StoryModeSceneLocation {
    public String vSceneName;
    public String vLocationName;
    public int vCenterX;
    public int vStartX;
    public int vEndX;

    public StoryModeSceneLocation(DatabaseCursor pLocationCursor){
        vSceneName = pLocationCursor.getString(0);
        vLocationName = pLocationCursor.getString(1);
        vCenterX = pLocationCursor.getInt(2);
        vStartX = pLocationCursor.getInt(3);
        vEndX = pLocationCursor.getInt(4);
    }

    public String getSceneName(){
        return vSceneName;
    }

    public String getLocationName(){
        return vLocationName;
    }

    public int getCenterX(){
        return vCenterX;
    }

    public int getStartX(){
        return vStartX;
    }

    public int getEndX(){
        return vEndX;
    }
}
