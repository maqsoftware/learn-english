package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sc.chi on 31/7/16.
 */
public abstract class AbstractNumberPadSection extends AbstractMathSection {

    /**
     * The number tray start y position
     */
    protected static final float NUMBER_TRAY_START_Y_POSITION = 660;
    protected float numberTrayStartXPosition;
    private AutoCognitaTextureRegion numberTrayTextureRegion;
    /**
     * the numbers which is draw above the number tray
     */
    private List<NumberScreenObject> countingNumberScreenObjectList;
    private Rectangle numberTrayArea;

    public AbstractNumberPadSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    public void render() {

        if (isNumberTrayRequired()) {
            initNumberTray();

            if (null == numberTrayTextureRegion) {
                Rectangle area = getNumberTrayTextureRegionArea();
                numberTrayTextureRegion = new AutoCognitaTextureRegion(numberTrayTexture, (int) area.x, (int) area.y, (int) area.width, (int) area.height);

                numberTrayStartXPosition = ScreenUtils.getXPositionForCenterObject(area.width);

                numberTrayArea = new Rectangle(numberTrayStartXPosition, NUMBER_TRAY_START_Y_POSITION,
                        numberTrayTextureRegion.getRegionWidth(), numberTrayTextureRegion.getRegionHeight());
            }

            if (null == countingNumberScreenObjectList) {

                countingNumberScreenObjectList = new ArrayList<NumberScreenObject>();


                for (int i = 0; i <= getMaximumNumberAboveTheTray(); i++) {

                    countingNumberScreenObjectList.add(
                            new NumberScreenObject(i, numberTrayStartXPosition + i * 50
                                    //make the number move to left side little bit
                                    - 5,
                                    NUMBER_TRAY_START_Y_POSITION + numberTrayTextureRegion.getRegionHeight()
                                            //the space between number tray and number
                                            + 5, TextFontSizeEnum.FONT_36, true));

                }
            }

            batch.begin();

            //draw the number tray
            batch.draw(numberTrayTextureRegion, numberTrayStartXPosition, NUMBER_TRAY_START_Y_POSITION);

            for (NumberScreenObject numberScreenObject : countingNumberScreenObjectList) {
                beforeDrawNumber(numberScreenObject);
                ScreenObjectUtils.draw(batch, numberScreenObject);
            }

            batch.end();
        }

        super.render();
    }

    protected boolean isNumberTrayRequired() {
        return true;
    }

    private void initNumberTray() {
        numberTrayTexture = AssetManagerUtils.getTexture(AssetManagerUtils.NUMBER_TRAY);

        smallBlockTrayTexture = AssetManagerUtils.getTexture(AssetManagerUtils.SMALL_BLOCK);
    }

    protected abstract Rectangle getNumberTrayTextureRegionArea();

    protected abstract int getMaximumNumberAboveTheTray();

    /**
     * It will be call before draw the small number above the tray, the give numberScreenObject will be going to draw
     *
     * @param numberScreenObject
     */
    protected abstract void beforeDrawNumber(NumberScreenObject numberScreenObject);

    @Override
    public void dispose() {
        super.dispose();

        if (null != countingNumberScreenObjectList) {
            countingNumberScreenObjectList.clear();
            countingNumberScreenObjectList = null;
        }

        numberTrayTextureRegion = null;

    }

    protected boolean isTouchingNumberTray(int screenX, int screenY) {
        return TouchUtils.isTouched(numberTrayArea, screenX, screenY);
    }
}
