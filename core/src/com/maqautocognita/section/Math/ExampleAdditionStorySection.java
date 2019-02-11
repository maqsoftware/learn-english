package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ExampleAdditionStorySection extends AdditionStorySection {

    private final static int NUMBER_OF_MILLISECOND_FOR_ANIMATION = 1200;
    private List<TextureScreenObject> exampleBoatList;

    public ExampleAdditionStorySection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void onHelpAudioPlay(int audioListIndex, long millisecond) {
        if (null == exampleBoatList) {
            exampleBoatList = new ArrayList<TextureScreenObject>(5);
        }
        if (millisecond <= 1000) {
            moveBoat(0);
        } else if (millisecond <= 2000) {
            moveBoat(1);
        } else if (millisecond <= 3000) {
            moveBoat(2);
        } else if (millisecond <= 4000) {
            moveBoat(3);
        } else if (millisecond <= 5000) {
            moveBoat(4);
        }

        super.onHelpAudioPlay(audioListIndex, millisecond);
    }

    @Override
    protected void onHelpAudioComplete() {
        super.onHelpAudioComplete();
        if (CollectionUtils.isNotEmpty(numberKeyBlockScreenObjectList)) {
            numberKeyBlockScreenObjectList.get(4).isHighlighted = true;
            abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                @Override
                public void onCorrectSoundPlayed() {
                    numberKeyBlockScreenObjectList.get(4).isHighlighted = false;
                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
                    abstractAutoCognitaScreen.setTouchAllow(true);
                }
            });
        }
        else{
            abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                @Override
                public void onCorrectSoundPlayed() {

                }
            });
        }

    }

    private void moveBoat(final int listIndex) {
        if (exampleBoatList.size() < listIndex + 1 && null != objectScreenObject) {
            exampleBoatList.add(duplicateAndMoveObject(backgroundScreenObject.xPositionInScreen + 100 + listIndex * objectScreenObject.width, backgroundScreenObject.yPositionInScreen + 50, NUMBER_OF_MILLISECOND_FOR_ANIMATION));
        }
    }


    @Override
    protected void render() {
        super.render();
        batch.begin();
        ScreenObjectUtils.draw(batch, exampleBoatList);
        batch.end();
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        abstractAutoCognitaScreen.setTouchAllow(false);
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
        if (null != exampleBoatList) {
            exampleBoatList.clear();
            exampleBoatList = null;
        }
    }

    @Override
    protected boolean isTrashRequired() {
        return false;
    }
}
