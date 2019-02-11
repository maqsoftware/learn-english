package com.maqautocognita.scene2d.actors;

import com.maqautocognita.prototype.storyMode.StoryModeImage;
import com.maqautocognita.prototype.storyMode.StoryModeScene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

import java.util.Timer;
import java.util.TimerTask;

/**
 * It is mainly used for the object which allow to drag,
 * The source {@link  ObjectActor} are required to given when initialize the class, will be copy as a new object to present the drag behaviour,
 * and the original source (the given {@link  ObjectActor}) will be invisible until the user is stop to drag the souce
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class StoryModeDropTarget extends DragAndDrop.Target {

    private final StoryModeObjectActor target;
    private final StoryModeScene scene;
    private boolean isDropped;
    private Timer timer;

    public StoryModeDropTarget(StoryModeObjectActor storyModeObjectActor, StoryModeScene scene) {
        super(storyModeObjectActor);
        this.target = storyModeObjectActor;
        this.scene = scene;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * When the source is enter to the target with 1 second, it will drop to the target automaticlly without leave the finger.
     * <p/>
     *
     * @param source
     * @param payload
     * @param x
     * @param y
     * @param pointer
     * @return
     */
    @Override
    public boolean drag(final DragAndDrop.Source source, final DragAndDrop.Payload payload, final float x, final float y, final int pointer) {

        Gdx.app.log(this.getClass().getName(), "enter container drag");

        if (!isDropped && null == timer) {
            timer = new Timer();
            //scale the image to fit to the container after 1 seconds
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    drop(source, payload, x, y, pointer);
                }
            }, 1000);
        }

        return true;
    }

    @Override
    public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
        Gdx.app.log(this.getClass().getName(), "reset container");
        if (null != timer) {
            //if the user drag the object outside the container, reset the timer,
            cancelTimer();
            //reset the original size of the source
            //((ObjectActor) payload.getDragActor()).restoreToOriginalRatio();
        }
        if (isDropped) {
            isDropped = false;
        } else {
            Gdx.app.log(this.getClass().getName(), "reset container - nothing dropped");
        }
    }

    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

        cancelTimer();

        if (!((StoryModeObjectActor) payload.getDragActor()).isDroppedToContainer) {

            ((StoryModeObjectActor) payload.getDragActor()).isDroppedToContainer = true;

            float startX = payload.getDragActor().getX();
            float startY = payload.getDragActor().getY();

//            if (ApplicationUtils.isDesktop()) {
//                startX = ScreenUtils.toScreenPosition(startX);
//                startY = ScreenUtils.toScreenPosition(startY);
//            }

            com.maqautocognita.scene2d.actors.StoryModeDragSource storyModeDragSource = (com.maqautocognita.scene2d.actors.StoryModeDragSource) source;

            StoryModeObjectActor storyModeDragSourceActor = storyModeDragSource.getSource();

            StoryModeImage storyModeSourceImage = storyModeDragSourceActor.getStoryModeImage();

            Gdx.app.log("StoryModeDropTarget before put to target", storyModeSourceImage.vSceneLocationX + "," + storyModeSourceImage.vSceneLocationY);

            storyModeSourceImage.putMeToTarget(target.getStoryModeImage(), (int) startX, (int) startY, scene);

            Gdx.app.log("StoryModeDropTarget after put to target", storyModeSourceImage.vSceneLocationX + "," + storyModeSourceImage.vSceneLocationY);

            storyModeDragSourceActor.reloadSizeAndPosition();

            storyModeDragSourceActor.setVisible(true);

            payload.getDragActor().setVisible(false);

            isDropped = true;
        }

    }

    private void cancelTimer() {
        Gdx.app.log(this.getClass().getName(), "cancel timer");
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }
}