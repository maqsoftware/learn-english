package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.bo.MathLesson;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.section.Math.IMathSection;
import com.maqautocognita.utils.AssetManagerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class MathScreen extends AbstractMathScreen {

    private MathCountingScreen mathCountingScreen;
    private MathAdditionSubtractionScreen mathAdditionSubtractionScreen;
    private MathLargeNumberScreen mathLargeNumberScreen;
    private MathMultiplicationScreen mathMultiplicationScreen;
    private MathOtherTopicScreen mathOtherTopicScreen;

    private List<IMathSection> mathSectionList;
    private List<MathLesson> lessonList;

    public MathScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener, AssetManagerUtils.GENERAL_ICONS_FOR_NINE_PATCH, AssetManagerUtils.NUMBER_TRAY, AssetManagerUtils.SMALL_BLOCK,
                AssetManagerUtils.NUMBER_BLOCKS_HORIZONTAL,
                AssetManagerUtils.GREY_48_UC_LETTER, AssetManagerUtils.GREY_48_LC_LETTER, AssetManagerUtils.RED_48_UC_LETTER, AssetManagerUtils.RED_48_LC_LETTER,
                AssetManagerUtils.RED_48_NUMBER,
                AssetManagerUtils.CORRECT_FRAME,
                AssetManagerUtils.WRONG_FRAME);
    }

    @Override
    protected List<MathLesson> getLessonList() {
        if (null == lessonList) {
            if (null == mathCountingScreen) {
                mathCountingScreen = new MathCountingScreen(game, menuScreenListener);
            }
            if (null == mathAdditionSubtractionScreen) {
                mathAdditionSubtractionScreen = new MathAdditionSubtractionScreen(game, menuScreenListener);
            }
            if (null == mathLargeNumberScreen) {
                mathLargeNumberScreen = new MathLargeNumberScreen(game, menuScreenListener);
            }
            if (null == mathMultiplicationScreen) {
                mathMultiplicationScreen = new MathMultiplicationScreen(game, menuScreenListener);
            }
            if (null == mathOtherTopicScreen) {
                mathOtherTopicScreen = new MathOtherTopicScreen(game, menuScreenListener);
            }

            lessonList = new ArrayList<MathLesson>();

            lessonList.addAll(mathCountingScreen.getLessonList());
            lessonList.addAll(mathAdditionSubtractionScreen.getLessonList());
            lessonList.addAll(mathLargeNumberScreen.getLessonList());
            lessonList.addAll(mathMultiplicationScreen.getLessonList());
            lessonList.addAll(mathOtherTopicScreen.getLessonList());

            for (int i = 0; i < lessonList.size(); i++) {
                lessonList.get(i).getAutoCognitaSection().setAutoCognitaScreen(this, this);
                lessonList.get(i).setSelected(i == 0);
                lessonList.get(i).setSequence(i);
            }
        }

        return lessonList;
    }

    @Override
    protected LessonUnitCode getUnitCode() {
        return null;
    }
}
