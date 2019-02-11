package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.scene2d.ui.TableCell;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.CollectionUtils;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class SkipCountingSection extends NumberSummary implements TimerService.ITimerListener {
    private final int multiples;

    private int startNumber;

    private TimerService timerService;

    public SkipCountingSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int multiples, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.multiples = multiples;
        timerService = new TimerService(this);
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        startNumber = multiples;
        timerService.startTimer(null, 1.5f);
    }

    @Override
    protected void afterNumberSoundPlayed(TableCell numberCell) {
        if (startNumber == Integer.valueOf(numberCell.getText())) {
            numberCell.setHighlighted(true);
            //if 6 numbers  are highlighted ,allow jump to next lesson
            if (startNumber >= multiples * 8) {
                abstractAutoCognitaScreen.showNextSection(numberOfFails);
            }
            startNumber += multiples;
        } else {
            super.afterNumberSoundPlayed(numberCell);
        }
    }

    @Override
    public void beforeStartTimer() {

    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        highlightNumber();
        startNumber += multiples;
        //only 3 time are required to auto highlight
        if (startNumber <= multiples * 5) {
            timerService.startTimer(null, 1.5f);
        }
    }

    private void highlightNumber() {
        if (CollectionUtils.isNotEmpty(tableCellList)) {
            for (TableCell tableCell : tableCellList) {
                if (startNumber == Integer.valueOf(tableCell.getText())) {
                    tableCell.setHighlighted(true);
                    break;
                }
            }
        }
    }
}
