package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.scene2d.ui.TableCell;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.TimerService;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class MultiplicationSummaryWithoutAnswer extends AdditionConjugateSummary {

    private static final int NUMBER_OF_SELECTED_TIMES_FOR_NEXT_LESSON = 3;
    private int numberOfSelectedTimes;

    public MultiplicationSummaryWithoutAnswer(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected boolean isHighlightRequired() {
        return false;
    }

    @Override
    protected String getBodyTextFromRowColumn(int row, int column) {
        return column + " x " + (10 - row);
    }

    @Override
    protected void afterTableCellInitialized(final int row, final int column, final TableCell tableCell) {
        tableCell.setTextAlign(Align.center);
        tableCell.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                numberOfSelectedTimes++;
                if (numberOfSelectedTimes >= NUMBER_OF_SELECTED_TIMES_FOR_NEXT_LESSON) {
                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
                }
                tableCell.setHighlighted(true, TextFontSizeEnum.FONT_72, String.valueOf(column * (10 - row)));
                new TimerService(new TimerService.ITimerListener() {
                    @Override
                    public void beforeStartTimer() {
                    }

                    @Override
                    public void onTimerComplete(Object threadIndicator) {
                        //dehighlight the cell after 2 second
                        tableCell.setHighlighted(false);
                    }
                }).startTimer(null, 2);
            }
        });

    }

}
