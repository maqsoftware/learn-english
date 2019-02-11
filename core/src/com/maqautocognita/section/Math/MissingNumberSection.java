package com.maqautocognita.section.Math;

import com.maqautocognita.Config;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.scene2d.ui.TableBorderCell;
import com.maqautocognita.scene2d.ui.TableNumberWritingCell;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.HandWritingRecognizeScreenService;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class MissingNumberSection extends AbstractTableSummary implements HandWritingRecognizeScreenService.IHandWritingRecognizeListener {

    private static final int CELL_HEIGHT = 200;
    private static final int NUMBER_OF_ROW = 4;
    private final int numberOfColumn;
    private final HandWritingRecognizeScreenService handWritingRecognizeScreenService;
    private final int minimumValue;
    private final int maximumValue;
    private List<Integer> differenceBetweenNumberList;
    private List<TableNumberWritingCell> tableNumberWritingCellList;
    private TableNumberWritingCell drawingTableNumberWritingCell;

    private Map<Integer, List<Integer>> rowIndexColumnValueMap;
    private Map<Integer, List<Integer>> differentBetweenNumberGeneratedNumberMap;
    private List<Integer> columnIndexRequiredToWriteInEachRow;
    private ShapeRenderer shapeRenderer;

    public MissingNumberSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int minimumValue, int maximumValue, int differenceBetweenNumber, int numberOfColumn, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.handWritingRecognizeScreenService = new HandWritingRecognizeScreenService(this);
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        differenceBetweenNumberList = new ArrayList<Integer>();
        differenceBetweenNumberList.add(differenceBetweenNumber);
        this.numberOfColumn = numberOfColumn;
    }

    public void addDifferenceBetweenNumber(int differenceBetweenNumber) {
        differenceBetweenNumberList.add(differenceBetweenNumber);
    }

    @Override
    protected void onShowAgain() {
        nextRound();
        super.onShowAgain();
    }

    private void nextRound() {
        if (null == columnIndexRequiredToWriteInEachRow) {
            columnIndexRequiredToWriteInEachRow = new ArrayList<Integer>();
        } else {
            columnIndexRequiredToWriteInEachRow.clear();
        }
        if (null == rowIndexColumnValueMap) {
            rowIndexColumnValueMap = new HashMap<Integer, List<Integer>>();
        } else {
            rowIndexColumnValueMap.clear();
        }
        if (null == differentBetweenNumberGeneratedNumberMap) {
            differentBetweenNumberGeneratedNumberMap = new HashMap<Integer, List<Integer>>();
        } else {
            differentBetweenNumberGeneratedNumberMap.clear();
        }
        Collections.shuffle(differenceBetweenNumberList);
        for (int i = 0; i < NUMBER_OF_ROW; i++) {
            generateNumber(i);
            //generate the number which required to hidden and give user to write
            //generate the hide number of each row
            int index = RandomUtils.getRandomWithExclusion(1, numberOfColumn - 1);
            columnIndexRequiredToWriteInEachRow.add(index);
        }
    }

    private void generateNumber(int rowIndex) {

        int differenceBetweenNumber = differenceBetweenNumberList.size() > rowIndex ? differenceBetweenNumberList.get(rowIndex) : differenceBetweenNumberList.get(0);

        List<Integer> excludeNumberList = differentBetweenNumberGeneratedNumberMap.get(differenceBetweenNumber);
        if (null == excludeNumberList) {
            excludeNumberList = new ArrayList<Integer>();
        }

        List<Integer> rowNumberList = new ArrayList<Integer>();
        int startNumber = RandomUtils.getRandomWithExclusion(minimumValue / differenceBetweenNumber,
                (int) Math.round((double) maximumValue / differenceBetweenNumber)
                        //this will generate the start number for the row, if there is maximum number is generated, such as 99, then this class will accumulate to 100,101,102.....,so here need to minus the number of column
                        - numberOfColumn, ArrayUtils.toArray(excludeNumberList));
        Gdx.app.log(getClass().getName(), "generated random number = " + startNumber);

        for (int i = 0; i < numberOfColumn; i++) {
            rowNumberList.add((startNumber + i) * differenceBetweenNumber);
            if (i == 0) {
                //only exclude the first number, prevent the first number duplicate
                excludeNumberList.add(startNumber + i);
            }
        }

        differentBetweenNumberGeneratedNumberMap.put(differenceBetweenNumber, excludeNumberList);

        rowIndexColumnValueMap.put(rowIndex, rowNumberList);

    }

    @Override
    protected Table createTable() {

        final float totalTableWidth = Config.TABLET_SCREEN_WIDTH;
        final float totalTableHeight = CELL_HEIGHT * NUMBER_OF_ROW;

        Table table = new Table();
        table.setSize(totalTableWidth, totalTableHeight);
        table.setPosition(ScreenUtils.getXPositionForCenterObject(totalTableWidth),
                ScreenUtils.getBottomYPositionForCenterObject(totalTableHeight));

        for (int row = 0; row < NUMBER_OF_ROW; row++) {
            for (int column = 0; column < numberOfColumn; column++) {
                int number = rowIndexColumnValueMap.get(row).get(column);
                TableBorderCell cell;
                if (column == columnIndexRequiredToWriteInEachRow.get(row)) {
                    cell = new TableNumberWritingCell(String.valueOf(number), Config.TABLET_SCREEN_WIDTH / numberOfColumn, CELL_HEIGHT);
                    if (null == tableNumberWritingCellList) {
                        tableNumberWritingCellList = new ArrayList<TableNumberWritingCell>();
                    }
                    tableNumberWritingCellList.add((TableNumberWritingCell) cell);
                } else {
                    cell = new TableBorderCell(String.valueOf(number), Config.TABLET_SCREEN_WIDTH / numberOfColumn, CELL_HEIGHT);
                }
                table.add(cell).expand();
            }
            table.row();
        }

        return table;
    }

    @Override
    protected void render() {
        super.render();
        handWritingRecognizeScreenService.drawLine();

        if (CollectionUtils.isNotEmpty(tableNumberWritingCellList)) {
            if (null == shapeRenderer) {
                shapeRenderer = new ShapeRenderer();
            }
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(ColorProperties.DISABLE_TEXT);
            for (TableNumberWritingCell numberWritingCell : tableNumberWritingCellList) {
                int lengthOfAnswer = String.valueOf(numberWritingCell.getText()).length();
                if (lengthOfAnswer > 1) {
                    float startXPosition = numberWritingCell.getX();
                    for (int i = 1; i < lengthOfAnswer; i++) {
                        startXPosition += +numberWritingCell.getWidth() / lengthOfAnswer;
                        //draw the line to indicate the digit write in the cell area
                        shapeRenderer.rect(startXPosition,
                                numberWritingCell.getY() + numberWritingCell.getHeight(), 2, numberWritingCell.getHeight());
                    }
                }
            }
            shapeRenderer.end();
        }

    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        handWritingRecognizeScreenService.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
    }

    @Override
    public void resetScreen() {
        if (null != tableNumberWritingCellList) {
            tableNumberWritingCellList.clear();
            tableNumberWritingCellList = null;
        }
        handWritingRecognizeScreenService.clearDrawPoints();
        handWritingRecognizeScreenService.clearCorrectDrawPoints();
        super.resetScreen();

    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (null != drawingTableNumberWritingCell && drawingTableNumberWritingCell.isAnswerWrote()) {
            handWritingRecognizeScreenService.touchUp();
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        handWritingRecognizeScreenService.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
    }

    @Override
    public boolean isDrawAllow(int screenX, int screenY) {
        return null != getTouchingNumberWritingScreenObject(screenX, screenY);
    }

    private TableNumberWritingCell getTouchingNumberWritingScreenObject(int screenX, int screenY) {
        return ScreenObjectUtils.getTouchingActor(tableNumberWritingCellList, screenX, screenY);
    }

    @Override
    public boolean isSaveCorrectDrawPointsRequired() {
        return true;
    }


    @Override
    public void whenCorrectLetterWrite() {
        tableNumberWritingCellList.remove(drawingTableNumberWritingCell);
        drawingTableNumberWritingCell = null;
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {
                if (CollectionUtils.isEmpty(tableNumberWritingCellList)) {
                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
                }
            }
        });
    }

    @Override
    public void whenWrongLetterWrite() {
        tableNumberWritingCellList.remove(drawingTableNumberWritingCell);
        drawingTableNumberWritingCell = null;
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {

            }
        });
    }

    @Override
    public void whenLetterWriteFails() {
        if (drawingTableNumberWritingCell.isAnswerWrote()) {
            if (null != drawingTableNumberWritingCell) {
                drawingTableNumberWritingCell.clearDrawPoints();
            }
            drawingTableNumberWritingCell = null;
        }
    }

    @Override
    public boolean isWriteCorrect() {
        if (null != drawingTableNumberWritingCell) {
            return drawingTableNumberWritingCell.isWriteCorrect();
        }
        return false;
    }

    @Override
    public void afterDrawPointAdded(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (null == drawingTableNumberWritingCell) {
            drawingTableNumberWritingCell = getTouchingNumberWritingScreenObject(screenX, screenY);
        }
        if (null != drawingTableNumberWritingCell) {
            drawingTableNumberWritingCell.addDrawPoint(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        }

    }

    @Override
    public boolean isRequiredClearDrawPointsAfterTimesUp() {
        return true;
    }

}
