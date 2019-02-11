package com.maqautocognita.scene2d.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author sc.chi csc19840914@gmail.com
 *         <p>
 *         The character will be shown by follow the camera move, not only fixed in 1 position,
 *         because in the story mode, the world will be repeat when the user swipe to the end of the world
 */

public class StoryModeCharacter extends AbstractStoryModeObjectActor {

    private final BodyGroup character;

    public StoryModeCharacter(BodyGroup character) {
        super(null);
        this.character = character;
        character.setIsRequiredToCheckCameraBeforeDraw(false);
        setOriginalXPosition(character.getX());
        setSize(character.getWidth(), character.getHeight());
    }

    @Override
    protected void drawActor(Batch batch, TextureRegion textureRegion,
                             float x, float y, float width, float height, float parentAlpha) {
        character.draw(batch, parentAlpha);
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        character.setX(x);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        character.setY(y);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        character.setPosition(x, y);
    }
}
