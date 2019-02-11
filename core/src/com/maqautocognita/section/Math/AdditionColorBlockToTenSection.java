package com.maqautocognita.section.Math;

import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.DragObject;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.IconPosition;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;


/**
 * In this section, User is asked to find a number pair that adds to 10.
 * Starting with 0, user needs to drag “10” to make 10.
 * Afterward, user is given the number 1.  User needs to drag “9” to make 10.
 * This continues, one by one: 2+8+10, 3+7=10, 4+6=10…
 * Until reach 10+__ = 10, which automatically turns into 10+0=10 after two seconds, since there is no block for the number “0”.
 * After that, there is a review session where random numbers between 0 and 10 are shown, and the user is asked to find one number to be added to the given number to sum up to 10.
 * This exercise can last for 20 rounds or when the user decides to move on to the next module.
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class AdditionColorBlockToTenSection extends AbstractAdvanceMathSection {

    private static final int NUMBER_BLOCK_GAP = 2;
    private static final Rectangle NUMBER_TRAY_TEXTURE_AREA = new Rectangle(0, 0, 510, 60);
    private static final int MAXIMUM_NUMBER = 10;
    protected int startNumber;
    protected int result = 10;
    private List<DragObject<Integer>> numberBlockListInNumberTray;
    private boolean isCorrectAnswerDragged;


    public AdditionColorBlockToTenSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    public void dispose() {
        super.dispose();
        resetScreen();
    }

    private void clearNumberBlockListInNumberTray() {
        if (null != numberBlockListInNumberTray) {
            numberBlockListInNumberTray.clear();
            numberBlockListInNumberTray = null;
        }
    }

    @Override
    public void render() {

        super.render();

        batch.begin();

        //draw the formula
        StringBuilder formulaBuilder = new StringBuilder();
        formulaBuilder.append(startNumber);
        formulaBuilder.append(" + ");
        formulaBuilder.append(isCorrectAnswerDragged ? result - startNumber : "_");
        formulaBuilder.append(" = ");
        formulaBuilder.append(result);

        drawFormula(formulaBuilder.toString());


        if (null != numberBlockListInNumberTray) {
            for (DragObject blockInNumberTray : numberBlockListInNumberTray) {
                drawDragObject(blockInNumberTray);
            }
        }

        batch.end();

    }

    @Override
    protected Rectangle getNumberTrayTextureRegionArea() {
        return NUMBER_TRAY_TEXTURE_AREA;
    }

    @Override
    protected int getMaximumNumberAboveTheTray() {
        return MAXIMUM_NUMBER;
    }

    @Override
    protected void beforeDrawNumber(NumberScreenObject numberScreenObject) {

        int number = startNumber;

        if (isCorrectAnswerDragged) {
            number = result;
        }

        if (numberScreenObject.displayText <= number) {
            numberScreenObject.setColor(ColorProperties.TEXT);
        } else {
            numberScreenObject.setColor(ColorProperties.DISABLE_TEXT);
        }
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (null != draggingNumberBlock) {
            //check if the number block is drop in the number tray
            if (isTouchingNumberTray(screenX, screenY)
                    //make sure the dragging number block is the correct answer
                    && draggingNumberBlock.getId() + startNumber == result
                    ) {

                isCorrectAnswerDragged = true;

                DragObject dragObject = new DragObject();
                dragObject.setValue(draggingNumberBlock.getId());
                dragObject.setOriginalPosition(getNumberBlockIconPositionInNumberTray(draggingNumberBlock.getId() - 1));
                dragObject.setAutoCognitaTextureRegion(draggingNumberBlock.getAutoCognitaTextureRegion());

                addNumberBlock(dragObject);

                abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                    @Override
                    public void onCorrectSoundPlayed() {
                        isCorrectAnswerDragged = false;
                        clearNumberBlockListInNumberTray();

                        afterCorrectSoundPlayed();

                        //display the number block in the tray
                        addNumberBlock(startNumber);
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
            rollbackDraggingNumberBlock();

        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        isTouchingNumberBlock(screenX, screenY);
    }

    private IconPosition getNumberBlockIconPositionInNumberTray(int number) {
        return new IconPosition(numberTrayStartXPosition + getWidthOfBlockListInNumberTray() +
                NUMBER_BLOCK_GAP * ((CollectionUtils.isNotEmpty(numberBlockListInNumberTray) ? numberBlockListInNumberTray.size() : 0) + 1), 663,
                numberBlocksAutoCognitaTextureRegions[number].getRegionWidth(), numberBlocksAutoCognitaTextureRegions[number].getRegionHeight());
    }

    private void addNumberBlock(DragObject dragObject) {
        getNumberBlockListInNumberTray().add(dragObject);
    }

    protected void afterCorrectSoundPlayed() {
        startNumber++;
    }

    protected void addNumberBlock(int number) {
        DragObject dragObject = new DragObject();
        dragObject.setValue(number);
        dragObject.setOriginalPosition(getNumberBlockIconPositionInNumberTray(number - 1));
        dragObject.setAutoCognitaTextureRegion(numberBlocksAutoCognitaTextureRegions[number - 1]);
        addNumberBlock(dragObject);
    }

    private int getWidthOfBlockListInNumberTray() {
        int width = 0;
        if (null != numberBlockListInNumberTray) {
            for (DragObject numberBlockInNumberTray : numberBlockListInNumberTray) {
                width += numberBlockInNumberTray.getOriginalPosition().width;
            }
        }

        return width;
    }

    private List<DragObject<Integer>> getNumberBlockListInNumberTray() {
        if (null == numberBlockListInNumberTray) {
            numberBlockListInNumberTray = new ArrayList<DragObject<Integer>>();
        }

        return numberBlockListInNumberTray;
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return null;
    }

    @Override
    protected void resetScreen() {
        startNumber = 0;
        clearNumberBlockListInNumberTray();
    }

}
