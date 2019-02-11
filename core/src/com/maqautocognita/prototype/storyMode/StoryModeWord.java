package com.maqautocognita.prototype.storyMode;

import com.maqautocognita.prototype.databases.DatabaseCursor;

/**
 * Created by kotarou on 31/5/16.
 */
public class StoryModeWord {

    public String vWord;
    public StoryModeImage[] vImageWord;

    public StoryModeWord(String pWord){
        vWord = pWord;
    }

    public void setWordImageSize(int pSize){
        vImageWord = new StoryModeImage[pSize];
    }

    public void setWordImage(int pIndex, DatabaseCursor vDatabaseImageCursor, DatabaseCursor vDatabaseImagePartCursor) {
        vImageWord[pIndex] = new StoryModeImage(vDatabaseImageCursor, vDatabaseImagePartCursor);
    }

    public StoryModeImage getImageWord(int pImageIndex){
        return vImageWord[pImageIndex];
    }


}
