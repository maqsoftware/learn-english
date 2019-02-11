package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class ShapeTriangleStaticSection extends AbstractShapeStaticSection {

    public static final String IMAGE_PATH[] = new String[]{MathImagePathUtils.TRIANGLE9, MathImagePathUtils.TRIANGLE2, MathImagePathUtils.TRIANGLE3, MathImagePathUtils.TRIANGLE4,
            MathImagePathUtils.TRIANGLE5, MathImagePathUtils.TRIANGLE6, MathImagePathUtils.TRIANGLE7, MathImagePathUtils.TRIANGLE8};
    private static final Vector2 POSITIONS[] = new Vector2[]{new Vector2(167, 526), new Vector2(396, 767), new Vector2(614, 397), new Vector2(1415, 337),
            new Vector2(1415, 614), new Vector2(1047, 676), new Vector2(1197, 106), new Vector2(218, 161)};

    public ShapeTriangleStaticSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }


    @Override
    protected String[] getImagePaths() {
        return IMAGE_PATH;
    }

    @Override
    protected Vector2[] getImagePositions() {
        return POSITIONS;
    }

}
