package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.bo.MathLesson;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.section.Math.AdditionConjugate11_19Summary;
import com.maqautocognita.section.Math.AdditionConjugateAcrossTenSection;
import com.maqautocognita.section.Math.CompareNumberSection;
import com.maqautocognita.section.Math.ExampleAddition2DigitLongFormSection;
import com.maqautocognita.section.Math.ExampleAddition2DigitLongFormWithCarryDigitSection;
import com.maqautocognita.section.Math.ExampleLargeNumberBlock2DigitSection;
import com.maqautocognita.section.Math.ExampleLargeNumberBlock3DigitSection;
import com.maqautocognita.section.Math.ExampleSubtraction2DigitLongFormSection;
import com.maqautocognita.section.Math.ExampleSubtraction2DigitLongFormWithBorrowDigitSection;
import com.maqautocognita.section.Math.InstructionAddition2DigitLongFormSection;
import com.maqautocognita.section.Math.InstructionAddition2DigitLongFormWithCarryDigitSection;
import com.maqautocognita.section.Math.InstructionSubtraction2DigitLongFormSection;
import com.maqautocognita.section.Math.InstructionSubtraction2DigitLongFormWithBorrowDigitSection;
import com.maqautocognita.section.Math.ListenAndMakeLargeNumberBlockSection;
import com.maqautocognita.section.Math.MakeLargeNumberBlockSection;
import com.maqautocognita.section.Math.MissingNumberSection;
import com.maqautocognita.section.Math.NumberSummary;
import com.maqautocognita.section.Math.ReadAndMakeLargeNumberBlockSection;
import com.maqautocognita.section.Math.SayNumberInLargeNumberBlockSection;
import com.maqautocognita.section.Math.SayNumberInLargeNumberWithoutBlockSection;
import com.maqautocognita.section.Math.SubtractionConjugate11_19Summary;
import com.maqautocognita.section.Math.SubtractionConjugateAcrossTenSection;
import com.maqautocognita.section.Math.WriteLargeNumberSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.MathLessonService;

import java.util.List;

/**
 * Created by sc.chi on 14/5/16.
 */
public class MathLargeNumberScreen extends AbstractMathScreen {

    public MathLargeNumberScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);
    }

    @Override
    protected List<MathLesson> getLessonList() {
        List<MathLesson> mathLessonList = MathLessonService.getInstance().getAllMathLesson(LessonUnitCode.MATH_3.code);
        for (MathLesson mathLesson : mathLessonList) {
            IAutoCognitaSection mathSection = null;
            if ("base1".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleLargeNumberBlock2DigitSection(
                        new MathAudioScriptWithElementCode("base1_hi"), this, this);
            } else if ("lnum1".equals(mathLesson.getElementCode())) {
                mathSection = new MakeLargeNumberBlockSection(
                        new MathAudioScriptWithElementCode("lnum1_hi"), 99, this, this);
            } else if ("nums1".equals(mathLesson.getElementCode())) {
                mathSection = new NumberSummary(new MathAudioScriptWithElementCode("nums1_hi"), this, this);
            } else if ("lnmr1".equals(mathLesson.getElementCode())) {
                mathSection = new ReadAndMakeLargeNumberBlockSection(
                        new MathAudioScriptWithElementCode("lnmr1_hi"), 10, 99, this, this);
            } else if ("lnml1".equals(mathLesson.getElementCode())) {
                mathSection = new ListenAndMakeLargeNumberBlockSection(
                        new MathAudioScriptWithElementCode("lnml1_hi"), 10, 99, this, this);
            } else if ("lnms1".equals(mathLesson.getElementCode())) {
                mathSection = new SayNumberInLargeNumberBlockSection(
                        new MathAudioScriptWithElementCode("lnms1_hi"), 10, 99, this, this);
            } else if ("lnmw1".equals(mathLesson.getElementCode())) {
                mathSection = new WriteLargeNumberSection(
                        new MathAudioScriptWithElementCode("lnmw1_hi"), 10, 99, this, this);
            } else if ("msnm1a".equals(mathLesson.getElementCode())) {
                mathSection = new MissingNumberSection(
                        new MathAudioScriptWithElementCode("msnm1a_hi"), 10, 99, 1, 5, this, this);
            } else if ("cpnm1a".equals(mathLesson.getElementCode())) {
                mathSection = new CompareNumberSection(
                        new MathAudioScriptWithElementCode("cpnm1a_hi"), 10, 99, this, this);
            } else if ("base2".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleLargeNumberBlock3DigitSection(
                        new MathAudioScriptWithElementCode("base2_hi"), this, this);
            } else if ("lnmr2".equals(mathLesson.getElementCode())) {
                mathSection = new ReadAndMakeLargeNumberBlockSection(
                        new MathAudioScriptWithElementCode("lnmr2_hi"), 100, 999, this, this);
            } else if ("lnml2".equals(mathLesson.getElementCode())) {
                mathSection = new ListenAndMakeLargeNumberBlockSection(
                        new MathAudioScriptWithElementCode("lnml2_hi"), 100, 990, this, this);
            } else if ("lnms2".equals(mathLesson.getElementCode())) {
                mathSection = new SayNumberInLargeNumberBlockSection(
                        new MathAudioScriptWithElementCode("lnms2_hi"), 100, 999, this, this);
            } else if ("lnmw2".equals(mathLesson.getElementCode())) {
                mathSection = new WriteLargeNumberSection(
                        new MathAudioScriptWithElementCode("lnmw2_hi"), 100, 999, this, this);
            } else if ("lnmsx".equals(mathLesson.getElementCode())) {
                mathSection = new SayNumberInLargeNumberWithoutBlockSection(
                        new MathAudioScriptWithElementCode("lnmsx_hi"), this, this);
            } else if ("msnm1b".equals(mathLesson.getElementCode())) {
                mathSection = new MissingNumberSection(
                        new MathAudioScriptWithElementCode("msnm1b_hi"), 100, 999, 1, 5, this, this);
            } else if ("cpnm1b".equals(mathLesson.getElementCode())) {
                mathSection = new CompareNumberSection(new MathAudioScriptWithElementCode(
                        "cpnm1b_hi"), 100, 999, this, this);
            } else if ("adcnj3".equals(mathLesson.getElementCode())) {
                mathSection = new AdditionConjugateAcrossTenSection(
                        new MathAudioScriptWithElementCode("adcnj3_hi", "adcnj3_i1"), this, this);
            } else if ("adcns3".equals(mathLesson.getElementCode())) {
                mathSection = new AdditionConjugate11_19Summary(new MathAudioScriptWithElementCode("adcns3_hi"), this, this);
            } else if ("sbcnj3".equals(mathLesson.getElementCode())) {
                mathSection = new SubtractionConjugateAcrossTenSection(
                        new MathAudioScriptWithElementCode("sbcnj3_hi", "sbcnj3_i1"), this, this);
            } else if ("sbcns2".equals(mathLesson.getElementCode())) {
                mathSection = new SubtractionConjugate11_19Summary(
                        new MathAudioScriptWithElementCode("sbcns2_hi"), this, this);
            } else if ("adlne".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleAddition2DigitLongFormSection(
                        new MathAudioScriptWithElementCode("adlne_hi"), this, this);
            } else if ("adlnp".equals(mathLesson.getElementCode())) {
                mathSection = new InstructionAddition2DigitLongFormSection(
                        new MathAudioScriptWithElementCode("adlnp_hi"), this, this);
            } else if ("adlce".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleAddition2DigitLongFormWithCarryDigitSection(
                        new MathAudioScriptWithElementCode("adlce_hi"), this, this);
            } else if ("adlcp".equals(mathLesson.getElementCode())) {
                mathSection = new InstructionAddition2DigitLongFormWithCarryDigitSection(
                        new MathAudioScriptWithElementCode("adlcp_hi"), this, this);
            } else if ("sblne".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleSubtraction2DigitLongFormSection(
                        new MathAudioScriptWithElementCode("sblne_hi"), this, this);
            } else if ("sblnp".equals(mathLesson.getElementCode())) {
                mathSection = new InstructionSubtraction2DigitLongFormSection(
                        new MathAudioScriptWithElementCode("sblnp_hi"), this, this);
            } else if ("sblbe".equals(mathLesson.getElementCode())) {
                mathSection = new ExampleSubtraction2DigitLongFormWithBorrowDigitSection(
                        new MathAudioScriptWithElementCode("sblbe_hi"), this, this);
            } else if ("sblbp".equals(mathLesson.getElementCode())) {
                mathSection = new InstructionSubtraction2DigitLongFormWithBorrowDigitSection(
                        new MathAudioScriptWithElementCode("sblbp_hi"), this, this);
            }
            mathLesson.setAutoCognitaSection(mathSection);
        }

        return mathLessonList;
    }

    @Override
    public AbstractLessonService getLessonService() {
        return null;
    }

    @Override
    protected LessonUnitCode getUnitCode() {
        return LessonUnitCode.MATH_3;
    }
}
