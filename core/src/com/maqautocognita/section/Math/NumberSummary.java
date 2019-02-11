package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.scene2d.ui.TableCell;
import com.maqautocognita.scene2d.ui.TableHeader;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csc on 26/11/2016.
 */

public class NumberSummary extends AbstractTableSummary {

    private static final int CELL_WIDTH = 100;

    private static final int CELL_HEIGHT = 80;

    protected List<TableCell> tableCellList;


    public NumberSummary(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, final AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected Table createTable() {
        //there are 10 columns and with same width
        final float totalTableWidth = CELL_WIDTH * 10;
        //there are 10 rows  and with same height include header
        final float totalTableHeight = CELL_HEIGHT * 11;

        Table table = new Table();
        table.setSize(totalTableWidth, totalTableHeight);
        table.setPosition(ScreenUtils.getXPositionForCenterObject(totalTableWidth), ScreenUtils.getBottomYPositionForCenterObject(totalTableHeight) + TRASH_ICON_POSITION.y);

        //draw header
        for (int column = 0; column < 10; column++) {
            String text = null;
            int headerLineStartWidth = 0;
            if (column > 0) {
                text = "+" + column;
            } else {
                headerLineStartWidth = 50;
            }
            table.add(new TableHeader(text, CELL_WIDTH, CELL_HEIGHT, headerLineStartWidth));
        }
        table.row();

        for (int row = 0; row <= 9; row++) {
            for (int column = 0; column < 10; column++) {
                final int number = row * 10 + column;
                final TableCell numberCell = new TableCell(String.valueOf(number), CELL_WIDTH, CELL_HEIGHT);

                numberCell.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        numberCell.setHighlighted(true);
                        abstractAutoCognitaScreen.playSound("number_" + number, new AbstractSoundPlayListener() {
                            @Override
                            public void onComplete() {
                                afterNumberSoundPlayed(numberCell);
                            }

                            @Override
                            public void onStop() {
                                afterNumberSoundPlayed(numberCell);
                            }
                        });
                    }
                });
                table.add(numberCell).expand();

                if (null == tableCellList) {
                    tableCellList = new ArrayList<TableCell>();
                }
                tableCellList.add(numberCell);
            }
            table.row();
        }

        return table;
    }

    protected void afterNumberSoundPlayed(TableCell numberCell) {
        numberCell.setHighlighted(false);
    }

    @Override
    public void resetScreen() {
        super.resetScreen();
        if (null != tableCellList) {
            tableCellList.clear();
            tableCellList = null;
        }
    }

}
