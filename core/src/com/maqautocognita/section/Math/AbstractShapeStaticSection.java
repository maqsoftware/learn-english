package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public abstract class AbstractShapeStaticSection extends AbstractMathSection {

    private List<ImageActor> shapeImageList;


    public AbstractShapeStaticSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        initShape();
    }

    private void initShape() {
        shapeImageList = new ArrayList<ImageActor>();
        for (String imagePath : getImagePaths()) {
            shapeImageList.add(new ImageActor(imagePath));
        }

        for (int i = 0; i < shapeImageList.size(); i++) {
            shapeImageList.get(i).setPosition(getImagePositions()[i].x, getImagePositions()[i].y);
            stage.addActor(shapeImageList.get(i));
        }
    }

    protected abstract String[] getImagePaths();

    protected abstract Vector2[] getImagePositions();

    @Override
    protected boolean isTrashRequired() {
        return false;
    }

    @Override
    protected boolean isNumberBlocksRequired() {
        return false;
    }
}
