package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.scene2d.ui.TableCell;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class AdditionConjugateSummary extends AbstractTableSummary implements TimerService.ITimerListener {

    private static final int CELL_WIDTH = 180;

    private static final int CELL_HEIGHT = 80;

    private Map<Integer, List<TableCell>> columnTableCellListMap;

    private TimerService timerService;

    private int highlightingColumnNumber = 1;

    public AdditionConjugateSummary(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        highlightingColumnNumber = getStartNumber();
    }

    protected int getStartNumber() {
        return 1;
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        if (null != columnTableCellListMap && highlightingColumnNumber > columnTableCellListMap.size()) {
            //restart to start number
            highlightingColumnNumber = getStartNumber();
        }
        if (isHighlightRequired()) {
            if (null == timerService) {
                timerService = new TimerService(this);
            }
            timerService.startTimer(null, 1);
        }
    }

    protected boolean isHighlightRequired() {
        return true;
    }

    @Override
    protected Table createTable() {
        //there are 10 columns and with same width
        final float totalTableWidth = getCellWidth() * (getEndNumber() - getStartNumber() + 1);
        //there are 10 rows  and with same height include header
        final float totalTableHeight = CELL_HEIGHT * (getNumberOfRow()
                //include header
                + 1);

        Table table = new Table();
        //table.setDebug(true);
        table.setSize(totalTableWidth, totalTableHeight);
        table.setPosition(ScreenUtils.getXPositionForCenterObject(totalTableWidth),
                ScreenUtils.getBottomYPositionForCenterObject(totalTableHeight) + TRASH_ICON_POSITION.y);

        //draw header
        for (int column = getStartNumber(); column <= getEndNumber(); column++) {
            TableCell tableCell = new TableCell(String.valueOf(column),
                    getCellWidth(), CELL_HEIGHT, TextFontSizeEnum.FONT_72, Align.center);
            addValueIntoMap(column, tableCell);
            table.add(tableCell);
        }
        table.row();

        for (int row = 0; row < getNumberOfRow(); row++) {
            for (int column = getStartNumber(); column <= getEndNumber(); column++) {
                TableCell tableCell = new TableCell(getBodyTextFromRowColumn(row, column), getCellWidth(), CELL_HEIGHT, Align.center);
                afterTableCellInitialized(row, column, tableCell);
                addValueIntoMap(column, tableCell);
                table.add(tableCell).expand();
            }
            table.row();
        }

        return table;
    }

    protected int getCellWidth() {
        return CELL_WIDTH;
    }

    protected int getEndNumber() {
        return 10;
    }

    protected int getNumberOfRow() {
        return 10;
    }

    private void addValueIntoMap(int columnNumber, TableCell value) {
        if (null == columnTableCellListMap) {
            columnTableCellListMap = new HashMap<Integer, List<TableCell>>();
        }

        if (!columnTableCellListMap.containsKey(columnNumber)) {
            columnTableCellListMap.put(columnNumber, new ArrayList<TableCell>());
        }

        columnTableCellListMap.get(columnNumber).add(value);
    }

    /**
     * @param row    is start from zero
     * @param column is start from the {@link #getStartNumber()}}
     * @return
     */
    protected String getBodyTextFromRowColumn(int row, int column) {
        String text = null;
        if (column > row) {
            text = row + "+" + (column - row) + "=" + column;
        }

        return text;
    }

    protected void afterTableCellInitialized(int row, int column, TableCell tableCell) {

    }

    @Override
    public void beforeStartTimer() {
        if (null != columnTableCellListMap) {
            for (TableCell tableCell : columnTableCellListMap.get(highlightingColumnNumber)) {
                tableCell.setHighlighted(true);
            }
        }
    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        if (null != columnTableCellListMap) {
            for (TableCell tableCell : columnTableCellListMap.get(highlightingColumnNumber)) {
                tableCell.setHighlighted(false);
            }
            highlightingColumnNumber++;
            if (isShowing) {
                if (highlightingColumnNumber > getEndNumber()) {
                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
                } else {
                    timerService.startTimer(null, 1);
                }
            }
        }
    }
}
