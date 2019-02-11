package com.maqautocognita.section.Math;


import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.scene2d.ui.TextCell;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.CollectionUtils;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

/**
 * it cycles through 1-10.  For each, start with 1 block, then user can stack the blocks up to see all the multiplication equations.
 * When it reaches ten blocks, then user can move to next number.
 * Control the time so that each new block can only appear 0.5 seconds after the previous (lower) block has appeared.
 *
 * @author sc.chi csc19840914@gmail.com
 */

public class MultiplicationFactSection extends MultiplicationMatrixSection {

    private static final float FORMULA_START_X_POSITION = NUMBER_PAD_POSITION.x + NUMBER_PAD_POSITION.width + 300;
    private int playingNumber = 1;
    private List<TextCell<String>> formulaList;

    public MultiplicationFactSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        isNumberBlockInHorizontalMode = true;
        if (playingNumber > 10) {
            playingNumber = 1;
            addNumberBlockUpInNumberPad(playingNumber);
        }
        if (CollectionUtils.isEmpty(numberBlockListInNumberPad)) {
            addNumberBlockUpInNumberPad(playingNumber);
        }
    }

    @Override
    protected boolean isTouchingNumberBlock(int screenX, int screenY) {
        //in order to make sure the user cannot drag the number block in the bottom of the screen
        return false;
    }

    @Override
    protected void removeNumberBlockInNumberPad() {
        if (CollectionUtils.isEmpty(numberBlockListInNumberPad) || numberBlockListInNumberPad.size() > 1) {
            //make sure at least have 1  number block leave in the number pad
            super.removeNumberBlockInNumberPad();
        }
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (playingNumber <= 10) {
            if (CollectionUtils.isNotEmpty(numberBlockListInNumberPad) && numberBlockListInNumberPad.size() >= 10 && numberBlockListInNumberPad.get(0).getValue() == playingNumber) {
                abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                    @Override
                    public void onCorrectSoundPlayed() {
                        playingNumber++;
                        if (playingNumber > 10) {
                            abstractAutoCognitaScreen.showNextSection(numberOfFails);
                        } else {
                            nextRound();
                        }
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
    }

    @Override
    protected boolean isSwitchModeButtonRequiredToShow() {
        return false;
    }

    @Override
    protected void drawFormula() {
        if (CollectionUtils.isNotEmpty(formulaList)) {
            String ids[] = formulaList.get(0).getId().split("_");
            int formulaPlayingNumber = Integer.valueOf(ids[0]);
            if (formulaPlayingNumber == playingNumber) {
                //which mean, the list is init before, visible the hidden block depends on the y value
                for (TextCell<String> textCell : formulaList) {
                    ids = textCell.getId().split("_");
                    int formulaYValue = Integer.valueOf(ids[1]);
                    textCell.setVisible(yValue >= formulaYValue);
                }
            } else {
                if (CollectionUtils.isNotEmpty(formulaList)) {
                    for (TextCell<String> textCell : formulaList) {
                        textCell.remove();
                    }
                    formulaList.clear();
                }
                drawVerticalFormula();
            }
        } else {
            drawVerticalFormula();
        }

    }

    @Override
    protected List<String> getIntroductionAudioFileName() {
        //only playing number 1 will play the introduction audio
        return playingNumber == 1 ? super.getIntroductionAudioFileName() : null;
    }

    private void drawVerticalFormula() {
        formulaList = new ArrayList<TextCell<String>>(10);
        for (int i = 1; i <= 10; i++) {
            int number = playingNumber;
            if (playingNumber > 10) {
                //when the correct answer is made, the playing number will increase
                number = 10;
            }

            final TextFontSizeEnum fontSize = TextFontSizeEnum.FONT_36;
            final float size[] = LetterUtils.getSizeOfWord(String.valueOf(100), fontSize);
            float maxWidth = size[0];
            float yPosition = NUMBER_PAD_POSITION.y + (i - 1) * numberBlocksAutoCognitaTextureRegions[number - 1].getRegionHeight() - 5 + size[1];
            final String id = playingNumber + "_" + i;
            final int textAlign = Align.right;

            //the left value
            float startXPosition = FORMULA_START_X_POSITION;
            formulaList.add(new TextCell<String>(id, String.valueOf(i), fontSize, maxWidth, startXPosition, yPosition, textAlign));

            startXPosition += maxWidth;
            formulaList.add(new TextCell<String>(id, "x", fontSize, maxWidth, startXPosition, yPosition, textAlign));

            startXPosition += maxWidth;
            formulaList.add(new TextCell<String>(id, String.valueOf(xValue), fontSize, maxWidth, startXPosition, yPosition, textAlign));

            startXPosition += maxWidth;
            formulaList.add(new TextCell<String>(id, "=", fontSize, maxWidth, startXPosition, yPosition, textAlign));

            startXPosition += maxWidth;
            formulaList.add(new TextCell<String>(id, String.valueOf(i * xValue), fontSize, maxWidth, startXPosition, yPosition, textAlign));

            for (TextCell textCell : formulaList) {
                textCell.setVisible(false);
                stage.addActor(textCell);
            }

        }
    }

    private void nextRound() {
        resetScreen();
        addNumberBlockUpInNumberPad(playingNumber);
    }


    @Override
    protected List<String> getHelpAudioFileNameList() {
        return getIntroductionAudioFileName();
    }


}
