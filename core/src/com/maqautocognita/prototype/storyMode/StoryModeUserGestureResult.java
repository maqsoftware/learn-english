package com.maqautocognita.prototype.storyMode;

/**
 * It is used to store the expected result that the {@link StoryModeUserGestureEnum} should be dp
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class StoryModeUserGestureResult {


    public String picWord = "";
    public String gesture ="";
    public String gestureAction ="";
    /**
     * the action expected to be do, such as "sentence", "play sound", "zoom"
     */
    public String expectedAction = "";


    /**
     * it is used to store the result belongs to the {@link #expectedAction}
     * <p/>
     * For example, if the {@link #expectedAction} is "sentence", it will be store the sentence required to show on the screen
     * <p/>
     * For example, if the {@link #expectedAction} is "zoom", it will be null, as the zoom gesture must be zoom to their own
     * <p/>
     * For example, if the {@link #expectedAction} is "drop", it will be return the word which will be the expected destination such as "bed"
     * <p/>
     * For example, if the {@link #expectedAction} is "play sound", it will be return the word which will be the list of audio file names separated by comma
     */
    public String message = "";

    public Boolean findAction = false; // return false if cannot find the word
    public Boolean completed = false;
    public String nextPic = ""; // next picture for shaking, if blank means no next picture
    /*
    nextPic form word,action,location
    e.g.
    shoe,TOUCH,sentence
    cat,DRAG,bed
     */
    public Boolean lessonCompleted = false; //
    public float lessonCompletedPercentage  = 0.0f;
    public String gestureLocation = ""; // location that the picture word located, if gesture action is not within the location, zoom will return on result message
    public String audioFile = ""; // audio file name
    public String autoPlayAfterComplete = "";
}
