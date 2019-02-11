package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.DatabaseCursor;

/**
 * Created by kotarou on 9/6/16.
 */
public class StoryModeLessonGesture {

    private String vLessonCode;
    private String vElementCode;
    private String vGestures;
    private String vGestureAction;
    private String vGestureLocation;


    // create StoryModeScene
    public StoryModeLessonGesture(DatabaseCursor pDatabaseCursor) {
        /*vWord = pWord;*/
        vLessonCode = pDatabaseCursor.getString(0);
        vElementCode = pDatabaseCursor.getString(1);
        vGestures = pDatabaseCursor.getString(2);
        vGestureAction = pDatabaseCursor.getString(3);
        vGestureLocation = pDatabaseCursor.getString(4);
    }

    public String getGestures() {
        return vGestures;
    }

    public String getGestureAction() {
        return vGestureAction;
    }

    public String getGestureLocation() {return vGestureLocation;}


}
