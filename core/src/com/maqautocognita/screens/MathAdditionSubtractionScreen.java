package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.bo.MathLesson;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.section.Math.AdditionColorBlockToTenSection;
import com.maqautocognita.section.Math.AdditionConjugateSummary;
import com.maqautocognita.section.Math.AdditionConjugateWithinTenSection;
import com.maqautocognita.section.Math.AdditionStorySection;
import com.maqautocognita.section.Math.ExampleAdditionLongFormSection;
import com.maqautocognita.section.Math.ExampleAdditionStorySection;
import com.maqautocognita.section.Math.ExampleAdditionUsingColorBlockSection;
import com.maqautocognita.section.Math.ExampleSubtractionLongFormSection;
import com.maqautocognita.section.Math.ExampleSubtractionStorySection;
import com.maqautocognita.section.Math.ExampleSubtractionUsingColorBlockSection;
import com.maqautocognita.section.Math.InstructionAdditionLongFormSection;
import com.maqautocognita.section.Math.InstructionAdditionUsingColorBlockSection;
import com.maqautocognita.section.Math.InstructionSubtractionLongFormSection;
import com.maqautocognita.section.Math.InstructionSubtractionUsingColorBlockSection;
import com.maqautocognita.section.Math.SubtractionConjugateSummary;
import com.maqautocognita.section.Math.SubtractionConjugateWithinTenSection;
import com.maqautocognita.section.Math.SubtractionStorySection;
import com.maqautocognita.service.MathLessonService;

import java.util.List;

/**
 * Created by sc.chi on 14/5/16.
 */
public class MathAdditionSubtractionScreen extends AbstractMathScreen {

    private List<MathLesson> mathLessonList;

    public MathAdditionSubtractionScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);
    }

    @Override
    protected List<MathLesson> getLessonList() {
        mathLessonList = MathLessonService.getInstance().getAllMathLesson(LessonUnitCode.MATH_2.code);

        for (MathLesson mathLesson : mathLessonList) {
            IAutoCognitaSection mathSection = null;
            if ("adste".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleAdditionStorySection(new MathAudioScriptWithElementCode("adste_hi", "adste_i1"), this, this);
            } else if ("adst".equals(mathLesson.getElementCode())) {
                mathSection = new AdditionStorySection(new MathAudioScriptWithElementCode("adst_hi", "adst_i1"), this, this);
            } else if ("sbste".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleSubtractionStorySection(new MathAudioScriptWithElementCode("sbste_hi", "sbste_i1"), this, this);
            } else if ("sbst".equals(mathLesson.getElementCode())) {
                mathSection = new SubtractionStorySection(new MathAudioScriptWithElementCode("sbst_hi", "sbst_i1"), this, this);
            } else if ("adcb1".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleAdditionUsingColorBlockSection(new MathAudioScriptWithElementCode("adcb1_hi",
                        "adcb1_i1", "adcb1_i2"), this, this);
            } else if ("adcb2".equals(mathLesson.getElementCode())) {
                mathSection = new InstructionAdditionUsingColorBlockSection(new MathAudioScriptWithElementCode("adcb2_hi",
                        "adcb2_i1", "adcb2_i2"), 5, 2, this, this);
            } else if ("adcb3".equals(mathLesson.getElementCode())) {
                mathSection = new InstructionAdditionUsingColorBlockSection(new MathAudioScriptWithElementCode(null,
                        "adcb3_i1", "adcb3_i2"), 3, 5, this, this);
            } else if ("adcb1".equals(mathLesson.getElementCode())) {
                mathSection =
                        new AdditionColorBlockToTenSection(new MathAudioScriptWithElementCode("adcb1_hi",
                                "adcb1_i1", "adcb1_i2"), this, this);
            } else if ("sbcb1".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleSubtractionUsingColorBlockSection(new MathAudioScriptWithElementCode("sbcb1_hi",
                        "sbcb1_i1", "sbcb1_i2"), this, this);
            } else if ("sbcb2".equals(mathLesson.getElementCode())) {
                mathSection = new InstructionSubtractionUsingColorBlockSection(new MathAudioScriptWithElementCode("sbcb2_hi",
                        "sbcb2_i1", "sbcb2_i2"), 10, 3, this, this);
            } else if ("sbcb3".equals(mathLesson.getElementCode())) {
                mathSection = new InstructionSubtractionUsingColorBlockSection(new MathAudioScriptWithElementCode(null,
                        "sbcb3_i1", "sbcb3_i2"), 8, 3, this, this);
            } else if ("adcnj1".equals(mathLesson.getElementCode())) {
                mathSection = new AdditionConjugateWithinTenSection(new MathAudioScriptWithElementCode("adcnj1_hi",
                        "adcnj1_i1"), 1, 5, this, this);
            } else if ("adlf1e".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleAdditionLongFormSection(new MathAudioScriptWithElementCode("adlf1e_hi",
                        "adlf1e_i1"), 1, 5, this, this);
            } else if ("adlf1p".equals(mathLesson.getElementCode())) {
                mathSection = new InstructionAdditionLongFormSection(new MathAudioScriptWithElementCode("adlf1p_hi"), 1, 5, this, this);
            } else if ("adcnj2".equals(mathLesson.getElementCode())) {
                mathSection = new AdditionConjugateWithinTenSection(new MathAudioScriptWithElementCode("adcnj2_hi",
                        "adcnj2_i1"), 6, 10, this, this);
            } else if ("adcnjs2".equals(mathLesson.getElementCode())) {
                mathSection = new AdditionConjugateSummary(new MathAudioScriptWithElementCode("adcnjs2_hi"), this, this);
            } else if ("adlf2p".equals(mathLesson.getElementCode())) {
                mathSection = new InstructionAdditionLongFormSection(
                        new MathAudioScriptWithElementCode("adlf2p_hi"), 6, 10, this, this);
            } else if ("sbcnj1".equals(mathLesson.getElementCode())) {
                mathSection = new SubtractionConjugateWithinTenSection(
                        new MathAudioScriptWithElementCode("sbcnj1_hi", "sbcnj1_i1"), 1, 5, this, this);
            } else if ("sblf1e".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleSubtractionLongFormSection(
                        new MathAudioScriptWithElementCode("sblf1e_hi", "sblf1e_i1"), 1, 5, this, this);
            } else if ("sblf1p".equals(mathLesson.getElementCode())) {
                mathSection = new InstructionSubtractionLongFormSection(new MathAudioScriptWithElementCode(
                        "sblf1p_hi"), 1, 5, this, this);
            } else if ("sbcnj2".equals(mathLesson.getElementCode())) {
                mathSection = new SubtractionConjugateWithinTenSection(
                        new MathAudioScriptWithElementCode("sbcnj2_hi", "sbcnj2_i1"), 6, 10, this, this);
            } else if ("sbcnjs2".equals(mathLesson.getElementCode())) {
                mathSection = new SubtractionConjugateSummary(new MathAudioScriptWithElementCode("sbcnjs2_hi"), this, this);
            } else if ("sblf2p".equals(mathLesson.getElementCode())) {
                mathSection = new InstructionSubtractionLongFormSection(new MathAudioScriptWithElementCode("sblf2p_hi"), 6, 10, this, this);
            }
            mathLesson.setAutoCognitaSection(mathSection);
        }
        return mathLessonList;

    }


    @Override
    protected LessonUnitCode getUnitCode() {
        return LessonUnitCode.MATH_2;
    }
}
