package com.maqautocognita.scene2d.actors;

import com.maqautocognita.prototype.storyMode.StoryModeScene;
import com.maqautocognita.utils.ApplicationUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

/**
 * It is mainly used for the object which allow to drag,
 * The source {@link  ObjectActor} are required to given when initialize the class, will be copy as a new object to present the drag behaviour,
 * and the original source (the given {@link  ObjectActor}) will be invisible until the user is stop to drag the souce
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class StoryModeDragSource extends DragAndDrop.Source {

    private final DragAndDrop dragAndDrop;

    private final StoryModeObjectActor source;

    private final StoryModeScene scene;

    public StoryModeDragSource(StoryModeObjectActor source, DragAndDrop dragAndDrop, StoryModeScene scene) {
        super(source);
        this.source = source;
        this.dragAndDrop = dragAndDrop;
        this.scene = scene;
    }


    public StoryModeObjectActor getSource() {
        return source;
    }

    @Override
    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
        //invisible the original source
        getActor().setVisible(false);

        //clone the original source and will be visible it during dragging
        DragAndDrop.Payload payload = new DragAndDrop.Payload();
        payload.setDragActor(new StoryModeObjectActor(source.getStoryModeImage()));

        float screenPositionX = 0;
        float screenPositionY = 0;
//        if (ApplicationUtils.isDesktop()) {
//            screenPositionX = ScreenUtils.toScreenPosition(getActor().getX());
//            screenPositionY = ScreenUtils.toScreenPosition(getActor().getY());
//        }

        //set the start position for clone source
        dragAndDrop.setDragActorPosition((x < 0 ? x : -x) * ScreenUtils.getSceneRatio() - screenPositionX,
                (y < 0 ? y : -y) * ScreenUtils.getSceneRatio() + getActor().getHeight() - screenPositionY);

        return payload;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * If the source is dragged to the target , nothing will be do in this method
     * If not, the position of the original source will be same as the dragging source, and the original one will be visible
     *
     * @param event
     * @param x
     * @param y
     * @param pointer
     * @param payload
     * @param target
     */
    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {


        if (//if there is no target dropped
                null == target
                        ||
                        //or the source is not dropped to any target
                        !((StoryModeObjectActor) payload.getDragActor()).isDroppedToContainer) {

            float screenPositionX = payload.getDragActor().getX();
            float screenPositionY = payload.getDragActor().getY();
            if (ApplicationUtils.isDesktop()) {
//                    screenPositionX = ScreenUtils.toScreenPosition(screenPositionX);
//                    screenPositionY = ScreenUtils.toScreenPosition(screenPositionY);
            }
            //set the current position from the copied source to original source
            showSource(screenPositionX, screenPositionY);
            ((StoryModeObjectActor) payload.getDragActor()).isDroppedToContainer = false;
            getActor().toFront();
        }

        //make the original source visible
        getActor().setVisible(true);

    }

    private void showSource(float screenPositionX, float screenPositionY) {

        source.getStoryModeImage().setMeToScene(scene, (int) screenPositionX, (int) screenPositionY);

        source.reloadSizeAndPosition();

    }
}
