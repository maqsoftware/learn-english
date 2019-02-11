package com.maqautocognita.utils;

import com.maqautocognita.scene2d.actors.IStoryModeActor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siu-chun.chi on 7/10/2017.
 */

public class StageUtils {

    public static void dispose(Stage stage) {
        if (null != stage) {

            List<Actor> actorList = new ArrayList<Actor>(stage.getActors().size);
            for (Actor actor : stage.getActors()) {
                actorList.add(actor);
            }
            for (Actor actor : actorList) {
                if (actor instanceof IStoryModeActor) {
                    ((IStoryModeActor) actor).dispose();
                } else {
                    actor.remove();
                }
            }

            stage.dispose();

        }
    }
}
