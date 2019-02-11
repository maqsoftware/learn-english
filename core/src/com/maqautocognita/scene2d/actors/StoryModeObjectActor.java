package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.prototype.storyMode.StoryModeImage;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class StoryModeObjectActor extends AbstractStoryModeObjectActor {

    private final StoryModeImage storyModeImage;
    public boolean isDroppedToContainer;

    public StoryModeObjectActor(StoryModeImage storyModeImage) {
        super(null == storyModeImage ? null : Config.STORY_HDPI_IMAGE_FOLDER_NAME + "/" + storyModeImage.vImageName + ".png");
        this.storyModeImage = storyModeImage;
        reloadSizeAndPosition();
    }

    /**
     *
     */
    public void reloadSizeAndPosition() {
        if (null != storyModeImage) {
            setOriginalXPosition(storyModeImage.vSceneLocationX);
            setOriginalYPosition(storyModeImage.vSceneLocationY);
            setSize(storyModeImage.vImageArea.vSceneDisplayWidth, storyModeImage.vImageArea.vSceneDisplayHeight);
            setPosition(storyModeImage.vSceneLocationX, storyModeImage.vSceneLocationY);
        }
    }

    public StoryModeObjectActor(StoryModeImage storyModeImage, float x, float y) {
        super(null == storyModeImage ? null : Config.STORY_HDPI_IMAGE_FOLDER_NAME + "/" + storyModeImage.vImageName + ".png");
        this.storyModeImage = storyModeImage;

        setPosition(x, y);
        setOriginalXPosition(x);
        setOriginalYPosition(y);
        setSize(storyModeImage.vImageArea.vImageWidth, storyModeImage.vImageArea.vImageHeight);
    }

    public StoryModeImage getStoryModeImage() {
        return storyModeImage;
    }



}
