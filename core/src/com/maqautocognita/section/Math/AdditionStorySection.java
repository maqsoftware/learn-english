package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.RandomUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

/**
 * User is asked to add {@link #numberOfBoatRequiredToAddInFirstTime} boats, then {@link #numberOfBoatRequiredToAddInSecondTime} boats.  Then press the number corresponding to the total number of boats.
 * Boats must be placed below the horizon.  Repeat with random numbers that add to <=10.  Repeat {@link #MAXIMUM_NUMBER_OF_ROUND_REQUIRED_TO_PLAY} times.
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class AdditionStorySection extends AbstractAdditionSubtractionStorySection {

    private int numberOfBoatRequiredToAddInFirstTime;
    private int numberOfBoatRequiredToAddInSecondTime;

    public AdditionStorySection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected boolean isNumberKeyboardPressCorrectly(int number) {
        int totalNumberOfBoatRequiredToDrop = numberOfBoatRequiredToAddInFirstTime + numberOfBoatRequiredToAddInSecondTime;
        return CollectionUtils.isNotEmpty(dropObjectScreenObjectList) && dropObjectScreenObjectList.size() == totalNumberOfBoatRequiredToDrop &&
                number == totalNumberOfBoatRequiredToDrop;
    }

    @Override
    protected void beforePlayNextRound() {
        numberOfBoatRequiredToAddInFirstTime = 0;
        numberOfBoatRequiredToAddInSecondTime = 0;
        generateNumberOfBoatRequiredToDrop();
    }

    private void generateNumberOfBoatRequiredToDrop() {
        if (numberOfBoatRequiredToAddInFirstTime == 0) {
            numberOfBoatRequiredToAddInFirstTime = RandomUtils.getRandomWithExclusion(2, 8);
            numberOfBoatRequiredToAddInSecondTime = RandomUtils.getRandomWithExclusion(2, 10 - numberOfBoatRequiredToAddInFirstTime, numberOfBoatRequiredToAddInFirstTime);
        }
    }

    @Override
    protected void whenObjectIDroppedInTheBackground() {
        //duplicate the object
        dropObjectScreenObjectList.add(touchedDraggableScreenObject);
    }

    @Override
    protected String getScreenObjectImagePath() {
        return MathImagePathUtils.BOAT;
    }

    @Override
    protected Rectangle getAllowDropArea() {
        float dropAreaHeight = backgroundScreenObject.height / 2;
        float dropAreaWidth = backgroundScreenObject.width;
        return new Rectangle(backgroundScreenObject.xPositionInScreen, backgroundScreenObject.yPositionInScreen, dropAreaWidth, dropAreaHeight);

    }

    @Override
    protected List<String> getHelpAudioFileNameList() {
        return mathAudioScriptWithElementCode.getInstructionScriptAudioFileNameList(numberOfBoatRequiredToAddInFirstTime, numberOfBoatRequiredToAddInSecondTime);
    }
}
