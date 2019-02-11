package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.bo.MathLesson;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.section.Math.MissingNumberSection;
import com.maqautocognita.section.Math.ShapeCircleStaticSection;
import com.maqautocognita.section.Math.ShapeIntroductionSection;
import com.maqautocognita.section.Math.ShapePracticeSection;
import com.maqautocognita.section.Math.ShapeRectangleStaticSection;
import com.maqautocognita.section.Math.ShapeTriangleStaticSection;
import com.maqautocognita.section.Math.SkipCountingSection;
import com.maqautocognita.service.MathLessonService;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class MathOtherTopicScreen extends AbstractMathScreen {


    public MathOtherTopicScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);
    }

    @Override
    protected List<MathLesson> getLessonList() {

        List<MathLesson> mathLessonList = MathLessonService.getInstance().getAllMathLesson(LessonUnitCode.MATH_5.code);
        for (MathLesson mathLesson : mathLessonList) {
            IAutoCognitaSection mathSection = null;
            if ("skct2".equals(mathLesson.getElementCode())) {
                mathSection = new SkipCountingSection(new MathAudioScriptWithElementCode("skct2_hi"), 2, this, this);
            } else if ("msnm2".equals(mathLesson.getElementCode())) {
                mathSection = new MissingNumberSection(new MathAudioScriptWithElementCode("msnm2_hi"), 10, 99, 2, 5, this, this);
            } else if ("skct5".equals(mathLesson.getElementCode())) {
                mathSection = new SkipCountingSection(new MathAudioScriptWithElementCode("skct5_hi"), 5, this, this);
            } else if ("msnm5".equals(mathLesson.getElementCode())) {
                mathSection = new MissingNumberSection(new MathAudioScriptWithElementCode("msnm5_hi"), 10, 99, 5, 4, this, this);
            } else if ("skct10".equals(mathLesson.getElementCode())) {
                mathSection = new SkipCountingSection(new MathAudioScriptWithElementCode("skct10_hi"), 10, this, this);
            } else if ("msnm10".equals(mathLesson.getElementCode())) {
                mathSection = new MissingNumberSection(new MathAudioScriptWithElementCode("msnm10_hi"), 10, 99, 10, 4, this, this);
            } else if ("msnmp".equals(mathLesson.getElementCode())) {
                MissingNumberSection missingNumberSection = new MissingNumberSection(
                        new MathAudioScriptWithElementCode("msnmp_hi"), 10, 99, 10, 5, this, this);
                missingNumberSection.addDifferenceBetweenNumber(5);
                missingNumberSection.addDifferenceBetweenNumber(2);
                mathSection = missingNumberSection;
            } else if ("shin".equals(mathLesson.getElementCode())) {
                mathSection = new ShapeIntroductionSection(new MathAudioScriptWithElementCode("shin_hi"), this, this);
            } else if ("shinc".equals(mathLesson.getElementCode())) {
                mathSection = new ShapeCircleStaticSection(new MathAudioScriptWithElementCode("shinc_hi"), this, this);
            } else if ("shint".equals(mathLesson.getElementCode())) {
                mathSection = new ShapeTriangleStaticSection(new MathAudioScriptWithElementCode("shint_hi"), this, this);
            } else if ("shinr".equals(mathLesson.getElementCode())) {
                mathSection = new ShapeRectangleStaticSection(new MathAudioScriptWithElementCode("shinr_hi"), this, this);
            } else if ("shprc".equals(mathLesson.getElementCode())) {
                mathSection = new ShapePracticeSection(new MathAudioScriptWithElementCode("shprc_hi"), this, this, ShapePracticeSection.Shape.CIRCLE);
            } else if ("shprt".equals(mathLesson.getElementCode())) {
                mathSection = new ShapePracticeSection(new MathAudioScriptWithElementCode("shprt_hi"), this, this, ShapePracticeSection.Shape.TRIANGLE);
            } else if ("shprr".equals(mathLesson.getElementCode())) {
                mathSection = new ShapePracticeSection(new MathAudioScriptWithElementCode("shprr_hi"), this, this, ShapePracticeSection.Shape.RECTANGLE);
            } /*else {
                mathSection = new SkipCountingSection(new MathAudioScriptWithElementCode("skct2_hi"), 2, this, this);
            }*/
            mathLesson.setAutoCognitaSection(mathSection);
        }

        return mathLessonList;
    }

    @Override
    protected LessonUnitCode getUnitCode() {
        return LessonUnitCode.MATH_5;
    }
}
