package com.maqautocognita.graphics.utils;

import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextScreenObject;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ScreenObjectUtils {

    public static <ID, T, S extends ScreenObject<ID, T>> void draw(Batch batch, final List<S> screenObjectList) {
        if (null != screenObjectList) {

            List<S> copyScreenObjectList = null;
            try {

                Iterator<S> screenObjects = screenObjectList.iterator();
                while (screenObjects.hasNext()) {
                    draw(batch, screenObjects.next());
                }
            } catch (ConcurrentModificationException ex) {
                Gdx.app.log(ScreenObjectUtils.class.getName(), "when drawing screenObject", ex);
            } finally {
                if (null != copyScreenObjectList) {
                    copyScreenObjectList.clear();
                }
            }


        }
    }

    public static void draw(Batch batch, ScreenObject screenObject) {
        if (null != screenObject) {
            screenObject.draw(batch);
        }
    }

    public static <ID, T, S extends ScreenObject<ID, T>> S getTouchingScreenObject(List<S> screenObjectList, float touchingX, float touchingY) {
        if (null != screenObjectList) {
            for (final S screenObject : screenObjectList) {
                S touchedObject = getTouchingScreenObject(screenObject, touchingX, touchingY);
                if (null != touchedObject) {
                    return touchedObject;
                }
            }
        }

        return null;
    }

    public static <ID, T, S extends ScreenObject<ID, T>> S getTouchingScreenObject(S screenObject, float touchingX, float touchingY) {
        if (null != screenObject && screenObject.isTouchAllow && screenObject.isVisible) {

            float yPosition = screenObject.yPositionInScreen;

            if (screenObject instanceof TextScreenObject) {
                yPosition = screenObject.yPositionInScreen - screenObject.height;
            }

            if (TouchUtils.isTouched(screenObject.xPositionInScreen, yPosition, screenObject.width, screenObject.height, touchingX, touchingY)) {
                return screenObject;
            }

        }

        return null;
    }

    public static <A extends Actor> A getTouchingActor(List<A> actorList, float touchingX, float touchingY) {
        if (CollectionUtils.isNotEmpty(actorList)) {
            for (final A actor : actorList) {
                if (null != getTouchingActor(actor, touchingX, touchingY)) {
                    return actor;
                }
            }
        }
        return null;
    }

    public static <A extends Actor> A getTouchingActor(A actor, float touchingX, float touchingY) {
        float fromBottom = actor.getParent() instanceof Table ? actor.getHeight() : 0;
        if (actor.isTouchable() && TouchUtils.isTouched(actor.getX(), actor.getY() + fromBottom, actor.getWidth(), actor.getHeight(), touchingX, touchingY)) {
            return actor;
        }
        return null;
    }

    public static <A extends Actor> A getTouchingActor(A[] actors, float touchingX, float touchingY) {
        if (ArrayUtils.isNotEmpty(actors)) {
            for (final A actor : actors) {
                if (null != getTouchingActor(actor, touchingX, touchingY)) {
                    return actor;
                }
            }
        }
        return null;
    }


    public static <ID, T, S extends ScreenObject<ID, T>> void highLightAllScreenObjectWithSameId
            (List<S> screenObjectList, ID id) {

        highLightAllScreenObject(getScreenObjectListById(screenObjectList, id));
    }

    public static <ID, T, S extends ScreenObject<ID, T>> void highLightAllScreenObject
            (List<S> screenObjectList) {

        if (CollectionUtils.isNotEmpty(screenObjectList)) {
            for (S screenObject : screenObjectList) {
                screenObject.isHighlighted = true;
            }
        }
    }

    public static <ID, T, S extends ScreenObject<ID, T>> List<S> getScreenObjectListById
            (List<S> screenObjectList, ID id) {

        List<S> screenObjectMatchList = null;

        if (CollectionUtils.isNotEmpty(screenObjectList)) {


            for (S screenObject : screenObjectList) {
                if (null != screenObject.id && screenObject.id.equals(id)) {
                    if (null == screenObjectMatchList) {
                        screenObjectMatchList = new ArrayList<S>();
                    }
                    screenObjectMatchList.add(screenObject);
                }
            }
        }

        return screenObjectMatchList;
    }

    public static <ID, T, S extends ScreenObject<ID, T>> void unhighLightAllScreenObjectWithSameId
            (List<S> screenObjectList, ID id) {

        unhighLightAllScreenObject(getScreenObjectListById(screenObjectList, id));
    }

    public static <ID, T, S extends ScreenObject<ID, T>> void unhighLightAllScreenObject
            (List<S> screenObjectList) {

        if (CollectionUtils.isNotEmpty(screenObjectList)) {
            for (S screenObject : screenObjectList) {
                screenObject.isHighlighted = false;
            }
        }
    }

    public static <ID, T, S extends ScreenObject<ID, T>> void hideAllScreenObject
            (List<S> screenObjectList) {

        if (CollectionUtils.isNotEmpty(screenObjectList)) {
            for (S screenObject : screenObjectList) {
                screenObject.isVisible = false;
            }
        }
    }
}
