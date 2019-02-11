package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class ShapeRectangleStaticSection extends AbstractShapeStaticSection {

    public static final String IMAGE_PATH[] = new String[]{MathImagePathUtils.RECTANGLE1, MathImagePathUtils.RECTANGLE2, MathImagePathUtils.RECTANGLE3, MathImagePathUtils.RECTANGLE4,
            MathImagePathUtils.RECTANGLE5, MathImagePathUtils.RECTANGLE6, MathImagePathUtils.RECTANGLE7, MathImagePathUtils.RECTANGLE8};
    private static final Vector2 POSITIONS[] = new Vector2[]{new Vector2(248, 130),
            new Vector2(1033, 687), new Vector2(1627, 594), new Vector2(309, 569),
            new Vector2(803, 862), new Vector2(114, 769), new Vector2(883, 269), new Vector2(1402, 132)};

    public ShapeRectangleStaticSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
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
