package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * User starts with {@link #numberOfObjectDroppedAtBeginning} zebras then take away {@link #numberOfObjectRequiredToDrag} zebras
 * Then press the number corresponding to the remaining number of zebras  Zebras must be placed below the horizon.  Repeat with random numbers where the beginning number is <=10 and the number to be subtracted is >=1 and <=starting number.  Repeat 5 times.
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class SubtractionStorySection extends AbstractAdditionSubtractionStorySection {

    private int numberOfObjectDroppedAtBeginning;
    private int numberOfObjectRequiredToDrag;

    public SubtractionStorySection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected List<String> getHelpAudioFileNameList() {
        return mathAudioScriptWithElementCode.getInstructionScriptAudioFileNameList(numberOfObjectDroppedAtBeginning, numberOfObjectRequiredToDrag);
    }

    @Override
    protected float getStartXPositionOfNumberKeyboard(float totalWidthOfNumberKeyboard) {
        return ScreenUtils.getXPositionForCenterObject(totalWidthOfNumberKeyboard);
    }

    @Override
    protected float getStartYPositionOfNumberKeyboard() {
        return BOTTOM_RECTANGLE_START_Y_POSITION + PADDING_TOP_BOTTOM_IN_RECTANGLE;
    }

    @Override
    protected boolean isNumberKeyboardPressCorrectly(int number) {
        int totalNumberOfZebraRequiredToDrop = numberOfObjectDroppedAtBeginning - numberOfObjectRequiredToDrag;
        return CollectionUtils.isNotEmpty(dropObjectScreenObjectList) && dropObjectScreenObjectList.size() == totalNumberOfZebraRequiredToDrop &&
                number == totalNumberOfZebraRequiredToDrop;
    }

    @Override
    protected void beforePlayNextRound() {
        numberOfObjectRequiredToDrag = 0;
        numberOfObjectDroppedAtBeginning = 0;
        if (numberOfObjectDroppedAtBeginning == 0) {
            numberOfObjectDroppedAtBeginning = RandomUtils.getRandomWithExclusion(3, 9);
            numberOfObjectRequiredToDrag = RandomUtils.getRandomWithExclusion(2, numberOfObjectDroppedAtBeginning - 1, numberOfObjectDroppedAtBeginning);
        }

        if (null != dropObjectScreenObjectList) {
            dropObjectScreenObjectList.clear();
            dropObjectScreenObjectList = null;
        }

        initDropObjectAtBeginning(numberOfObjectDroppedAtBeginning);

    }

    @Override
    protected boolean isAllowDropToOutside() {
        int totalNumberOfZebraRequiredToDrop = numberOfObjectDroppedAtBeginning - numberOfObjectRequiredToDrag;
        return dropObjectScreenObjectList.size() > totalNumberOfZebraRequiredToDrop;
    }

    @Override
    protected boolean isObjectRequiredToDrag() {
        return false;
    }

    @Override
    protected String getScreenObjectImagePath() {
        return MathImagePathUtils.ZEBRA;
    }

    @Override
    protected String getBackgroundScreenObjectImagePath() {
        return MathImagePathUtils.COUNTING_BACKGROUND_IMAGE_PATH;
    }

    @Override
    protected Rectangle getAllowDropArea() {
        float dropAreaHeight = backgroundScreenObject.height / 2;
        float dropAreaWidth = backgroundScreenObject.width;
        return new Rectangle(backgroundScreenObject.xPositionInScreen, backgroundScreenObject.yPositionInScreen, dropAreaWidth, dropAreaHeight);

    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
    }

    protected void initDropObjectAtBeginning(int numberOfObjectDropped) {
        dropObjectScreenObjectList = new ArrayList<TextureScreenObject>(numberOfObjectDropped);

        Texture objectTexture = AssetManagerUtils.getTexture(getScreenObjectImagePath());

        for (Vector2 position : RandomUtils.getRandomPositions(numberOfObjectDropped, getAllowDropArea(), new Vector2(objectTexture.getWidth(), objectTexture.getHeight()))) {
            dropObjectScreenObjectList.add(new TextureScreenObject(null, objectTexture,
                    position.x, position.y));
        }
    }

    @Override
    protected boolean isTrashRequired() {
        return false;
    }
}
