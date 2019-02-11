package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.bo.MathLesson;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.section.Math.ExampleMultiplicationMatrixSection;
import com.maqautocognita.section.Math.ExampleMultiplicationStorySection;
import com.maqautocognita.section.Math.MultiplicationFactSection;
import com.maqautocognita.section.Math.MultiplicationMatrixReview;
import com.maqautocognita.section.Math.MultiplicationMatrixSection;
import com.maqautocognita.section.Math.MultiplicationReview;
import com.maqautocognita.section.Math.MultiplicationStorySection;
import com.maqautocognita.section.Math.MultiplicationSummary;
import com.maqautocognita.section.Math.MultiplicationSummaryWithoutAnswer;
import com.maqautocognita.service.MathLessonService;

import java.util.List;

/**
 * Created by sc.chi on 14/5/16.
 */
public class MathMultiplicationScreen extends AbstractMathScreen {

    public MathMultiplicationScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);

    }

    @Override
    protected List<MathLesson> getLessonList() {
        List<MathLesson> mathLessonList = MathLessonService.getInstance().getAllMathLesson(LessonUnitCode.MATH_4.code);
        for (MathLesson mathLesson : mathLessonList) {
            IAutoCognitaSection mathSection = null;
            if ("mtste".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleMultiplicationStorySection(
                        new MathAudioScriptWithElementCode("mtste_hi", "mtste_i1", "mtste_i2"), this, this);
            } else if ("mtstp".equals(mathLesson.getElementCode())) {
                mathSection = new MultiplicationStorySection(
                        new MathAudioScriptWithElementCode("mtstp_hi", "mtstp_i1", "mtstp_i2"), this, this);
            } else if ("mtmxe".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleMultiplicationMatrixSection(
                        new MathAudioScriptWithElementCode("mtmxe_hi", "mtmxe_i1"), this, this);
            } else if ("mtmxp".equals(mathLesson.getElementCode())) {
                mathSection = new MultiplicationMatrixSection(
                        new MathAudioScriptWithElementCode("mtmxp_hi"), this, this);
            } else if ("mtft".equals(mathLesson.getElementCode())) {
                mathSection = new MultiplicationFactSection(
                        new MathAudioScriptWithElementCode("mtft_hi"), this, this);
            } else if ("mtsm".equals(mathLesson.getElementCode())) {
                mathSection = new MultiplicationSummary(new MathAudioScriptWithElementCode("mtsm_hi"), this, this);
            } else if ("mtsmna".equals(mathLesson.getElementCode())) {
                mathSection = new MultiplicationSummaryWithoutAnswer(
                        new MathAudioScriptWithElementCode("mtsmna_hi"), this, this);
            } else if ("mtrv1".equals(mathLesson.getElementCode())) {
                mathSection = new MultiplicationMatrixReview(
                        new MathAudioScriptWithElementCode("mtrv1_hi"), this, this);
            } else if ("mtrv2".equals(mathLesson.getElementCode())) {
                mathSection = new MultiplicationReview(new MathAudioScriptWithElementCode("mtrv2_hi"), this, this);
            }
            mathLesson.setAutoCognitaSection(mathSection);
        }
        return mathLessonList;
    }

    @Override
    protected LessonUnitCode getUnitCode() {
        return LessonUnitCode.MATH_4;
    }
}
