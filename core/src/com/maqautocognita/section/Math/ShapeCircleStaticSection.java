package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class ShapeCircleStaticSection extends AbstractShapeStaticSection {

    public static final String IMAGE_PATH[] = new String[]{MathImagePathUtils.CIRCLE1, MathImagePathUtils.CIRCLE2, MathImagePathUtils.CIRCLE3, MathImagePathUtils.CIRCLE4,
            MathImagePathUtils.CIRCLE5, MathImagePathUtils.CIRCLE6, MathImagePathUtils.CIRCLE7, MathImagePathUtils.CIRCLE8};
    private static final Vector2 POSITIONS[] = new Vector2[]{new Vector2(181, 600), new Vector2(336, 150), new Vector2(1076, 925), new Vector2(1151, 725),
            new Vector2(681, 450), new Vector2(1026, 143), new Vector2(1351, 825), new Vector2(1589, 550)};

    public ShapeCircleStaticSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
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
