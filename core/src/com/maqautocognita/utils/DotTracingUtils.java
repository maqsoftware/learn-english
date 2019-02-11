package com.maqautocognita.utils;

import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class DotTracingUtils {

    private static final float SECOND_BETWEEN_EACH_DOT_TRACING = 0.3f;
    private static final float SECOND_FOR_DOT_DISAPPEAR = 4f;
    public static float startTracingDotTime = 0;
    public static int tracingDotIndex = 0;
    private static AutoCognitaTextureRegion redDot;
    private static List<Float> dotShowStartTimeList;

    /**
     * Draw a red dot according to the points in letter in sequence, it will update the tracingDotIndex by increase 1 in every 0.5 second,
     * if the tracingDotIndex is larger than the size of the letter points,which mean complete the dot drawing for the given letter,
     * then true will be return and the {@link #tracingDotIndex} and {@link #startTracingDotTime} will be restore to 0
     *
     * @param batch
     * @param letterIconPositionList
     * @param dotSize
     * @return true if the letter is complete tracing
     */
    public static boolean drawDotTracingLetter(Batch batch, List<IconPosition> letterIconPositionList, int dotSize) {

        if (null == redDot) {
            redDot = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.GENERAL_ICONS), 952, 111, 30, 30);
        }

        if (null == dotShowStartTimeList) {
            dotShowStartTimeList = new ArrayList<Float>();
        }

        float time = Gdx.graphics.getDeltaTime();
        startTracingDotTime += time;

        for (int i = 0; i < dotShowStartTimeList.size(); i++) {
            if (null != dotShowStartTimeList.get(i)) {
                dotShowStartTimeList.set(i, dotShowStartTimeList.get(i) + time);
            }
        }

        if (startTracingDotTime >= SECOND_BETWEEN_EACH_DOT_TRACING) {
            tracingDotIndex++;
            startTracingDotTime = 0;
        }


        if (null != letterIconPositionList) {
            for (int i = 0; i < letterIconPositionList.size() && i < tracingDotIndex; i++) {
                float alpha;
                if (dotShowStartTimeList.size() <= i) {
                    dotShowStartTimeList.add(0f);
                    alpha = 1f;
                } else if (dotShowStartTimeList.get(i) > SECOND_FOR_DOT_DISAPPEAR) {
                    alpha = 0;
                } else {
                    alpha = (SECOND_FOR_DOT_DISAPPEAR - dotShowStartTimeList.get(i)) / SECOND_FOR_DOT_DISAPPEAR;
                }
                batch.setColor(1.0f, 1.0f, 1.0f, alpha);
                drawDot(batch, redDot, letterIconPositionList.get(i), dotSize);
                batch.setColor(1.0f, 1.0f, 1.0f, 1f);
            }

            if (tracingDotIndex > letterIconPositionList.size()) {
                restart();
                return true;
            }
        }


        return false;
    }

    public static void drawDot(Batch batch, AutoCognitaTextureRegion dotAutoCognitaTextureRegion, IconPosition point, int dotSize) {

        batch.draw(dotAutoCognitaTextureRegion, point.x, point.y, dotSize, dotSize);
    }

    public static void restart() {
        tracingDotIndex = 0;
        startTracingDotTime = 0;
        dotShowStartTimeList = null;
        redDot = null;
    }
}
