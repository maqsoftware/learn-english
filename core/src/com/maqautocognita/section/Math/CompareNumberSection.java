package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.scene2d.ui.TableBorderCell;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class CompareNumberSection extends AbstractTableSummary {

    private static final int CELL_WIDTH = 400;
    private static final int CELL_HEIGHT = 200;
    private static final int NUMBER_OF_COLUMN = 2;
    private static final int NUMBER_OF_ROW = 4;
    private static final int SPACE_BETWEEN_ROW = 20;
    private final int minimumValue;
    private final int maximumValue;
    private int playingRowIndex;

    private List<TableBorderCell> cellList;

    public CompareNumberSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int minimumValue, int maximumValue, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        playingRowIndex = 0;
    }

    @Override
    protected Table createTable() {
        List<Integer> generatedNumberList = new ArrayList<Integer>();

        final float totalTableWidth = CELL_WIDTH * NUMBER_OF_COLUMN;
        final float totalTableHeight = CELL_HEIGHT * NUMBER_OF_ROW + SPACE_BETWEEN_ROW * (NUMBER_OF_ROW - 1);

        Table table = new Table();
        table.setSize(totalTableWidth, totalTableHeight);
        table.setPosition(ScreenUtils.getXPositionForCenterObject(totalTableWidth),
                ScreenUtils.getBottomYPositionForCenterObject(totalTableHeight));

        for (int row = 0; row < NUMBER_OF_ROW; row++) {
            final List<Integer> rowNumber = new ArrayList<Integer>(NUMBER_OF_COLUMN);
            for (int column = 0; column < NUMBER_OF_COLUMN; column++) {
                int number = RandomUtils.getRandomWithExclusion(minimumValue, maximumValue, ArrayUtils.toArray(generatedNumberList));
                generatedNumberList.add(number);
                rowNumber.add(number);
                final TableBorderCell cell = new TableBorderCell(String.valueOf(number), CELL_WIDTH, CELL_HEIGHT);
                if (0 == row) {
                    //for the first time, it will highlight the first row to indicate the user to play the first row
                    cell.setHighlighted(true);
                }
                cell.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        if (playingRowIndex == cell.getRowIndex()) {
                            //make sure the app is playing/focus on this row
                            //get the maximum number in the list of the row
                            int maximumNumber = 0;
                            for (Integer number : rowNumber) {
                                maximumNumber = Math.max(maximumNumber, number);
                            }
                            if (Integer.valueOf(cell.getText()) == maximumNumber) {
                                unHighlightRowByIndex(playingRowIndex);
                                playingRowIndex++;
                                highlightRowByIndex(playingRowIndex);
                                cell.setTextColor(ColorProperties.BORDER);
                                abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                                    @Override
                                    public void onCorrectSoundPlayed() {
                                        if (playingRowIndex >= NUMBER_OF_ROW) {
                                            abstractAutoCognitaScreen.showNextSection(numberOfFails);
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
                });
                table.add(cell).expand();
                cell.setRowIndex(row);

                if (null == cellList) {
                    cellList = new ArrayList<TableBorderCell>();
                }
                cellList.add(cell);
            }
            table.row();
        }

        return table;
    }

    private void unHighlightRowByIndex(int rowIndex) {
        setHighlightRowStatusByIndex(rowIndex, false);
    }

    private void highlightRowByIndex(int rowIndex) {
        setHighlightRowStatusByIndex(rowIndex, true);
    }

    private void setHighlightRowStatusByIndex(int rowIndex, boolean isHighlightRequired) {
        if (CollectionUtils.isNotEmpty(cellList)) {
            for (TableBorderCell tableBorderCell : cellList) {
                if (tableBorderCell.getRowIndex() == rowIndex) {
                    tableBorderCell.setHighlighted(isHighlightRequired);
                }
            }
        }
    }


}
