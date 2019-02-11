
package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.bo.MathLesson;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.section.Math.CompareNumberSection;
import com.maqautocognita.section.Math.Counting10WithKeyboardSection;
import com.maqautocognita.section.Math.CountingAdd1NumberBlockSection;
import com.maqautocognita.section.Math.CountingAddAppleSection;
import com.maqautocognita.section.Math.CountingCanvasColorBlockSameSection;
import com.maqautocognita.section.Math.CountingCanvasColorBlockSection;
import com.maqautocognita.section.Math.CountingCompareSection;
import com.maqautocognita.section.Math.CountingMatchingSection;
import com.maqautocognita.section.Math.CountingNumberWritingSection;
import com.maqautocognita.section.Math.CountingSection;
import com.maqautocognita.section.Math.CountingStoryForPlayingBackgroundSection;
import com.maqautocognita.section.Math.CountingStorySection;
import com.maqautocognita.section.Math.MissingNumberSection;
import com.maqautocognita.section.Math.NumberWritingSection;
import com.maqautocognita.service.MathLessonService;
import com.maqautocognita.utils.AssetManagerUtils;

import java.util.List;

public class MathCountingScreen extends AbstractMathScreen {

    public MathCountingScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener, AssetManagerUtils.GENERAL_ICONS_FOR_NINE_PATCH, AssetManagerUtils.NUMBER_TRAY, AssetManagerUtils.SMALL_BLOCK,
                AssetManagerUtils.NUMBER_BLOCKS_HORIZONTAL,
                AssetManagerUtils.GREY_48_UC_LETTER, AssetManagerUtils.GREY_48_LC_LETTER, AssetManagerUtils.RED_48_UC_LETTER, AssetManagerUtils.RED_48_LC_LETTER,
                AssetManagerUtils.RED_48_NUMBER,
                AssetManagerUtils.CORRECT_FRAME,
                AssetManagerUtils.WRONG_FRAME);
    }

    @Override
    protected List<MathLesson> getLessonList() {

        List<MathLesson> mathLessonList = MathLessonService.getInstance().getAllMathLesson(LessonUnitCode.MATH_1.code);
        for (MathLesson mathLesson : mathLessonList) {
            IAutoCognitaSection mathSection = null;
            if ("ctin1".equals(mathLesson.getElementCode())) {
                mathSection = new CountingAddAppleSection(new MathAudioScriptWithElementCode("ctin1_hi"), 5, this, this);
            } else if ("ctgu1".equals(mathLesson.getElementCode())) {
                mathSection = new CountingSection(new MathAudioScriptWithElementCode("ctgu1_hi"), 5, this, this, false);
            } else if ("ctun1".equals(mathLesson.getElementCode())) {
                mathSection = new CountingSection(new MathAudioScriptWithElementCode("ctun1_hi"), 5, this, this, true);
            } else if ("ctst1".equals(mathLesson.getElementCode())) {
                mathSection = new CountingStorySection(new MathAudioScriptWithElementCode("ctst1_hi", "ctst1_i1"), 1, 5, this, this);
            } else if ("ctre1".equals(mathLesson.getElementCode())) {
                mathSection = new CountingMatchingSection(new MathAudioScriptWithElementCode("ctre1_hi"), 5, this, this);
            } else if ("ctin2".equals(mathLesson.getElementCode())) {
                mathSection = new CountingAddAppleSection(new MathAudioScriptWithElementCode("ctin2_hi"), 10, this, this);
            } else if ("ctgu2".equals(mathLesson.getElementCode())) {
                mathSection = new CountingSection(new MathAudioScriptWithElementCode("ctgu2_hi"), 10, this, this, false);
            } else if ("ctun2".equals(mathLesson.getElementCode())) {
                mathSection = new CountingSection(new MathAudioScriptWithElementCode("ctun2_hi"), 10, this, this, true);
            } else if ("ctst2".equals(mathLesson.getElementCode())) {
                mathSection = new CountingStoryForPlayingBackgroundSection(
                        new MathAudioScriptWithElementCode("ctst2_hi", "ctst2_i1"), 6, 10, this, this);
            } else if ("ctre2".equals(mathLesson.getElementCode())) {
                mathSection = new CountingMatchingSection(new MathAudioScriptWithElementCode("ctre2_hi"), 10, this, this);
            } else if ("ctwbl".equals(mathLesson.getElementCode())) {
                mathSection = new CountingAdd1NumberBlockSection(new MathAudioScriptWithElementCode("ctwbl_hi"), false,
                        this, this);
            } else if ("ctwg".equals(mathLesson.getElementCode())) {
                mathSection = new NumberWritingSection(new MathAudioScriptWithElementCode("ctwg_hi"), true, this, this);
            } else if ("ctwu".equals(mathLesson.getElementCode())) {
                mathSection = new NumberWritingSection(new MathAudioScriptWithElementCode("ctwu_hi"), false, this, this);
            }
            //review section
            else if ("ctren".equals(mathLesson.getElementCode())) {
                mathSection = new Counting10WithKeyboardSection(new MathAudioScriptWithElementCode("ctren_hi"), this, this);
            } else if ("ctrew".equals(mathLesson.getElementCode())) {
                mathSection = new CountingNumberWritingSection(new MathAudioScriptWithElementCode("ctrew_hi"), this, this);
            } else if ("ctcpm".equals(mathLesson.getElementCode())) {
                mathSection = new CountingCompareSection(
                        new MathAudioScriptWithElementCode("ctcpm_hi", "ctcpm_i1", "ctcpm_i2"), this, this, true);
            } else if ("ctcpl".equals(mathLesson.getElementCode())) {
                mathSection = new CountingCompareSection(
                        new MathAudioScriptWithElementCode("ctcpl_hi", "ctcpl_i1", "ctcpl_i2"), this, this, false);
            } else if ("ctcbl".equals(mathLesson.getElementCode())) {
                mathSection = new CountingAdd1NumberBlockSection(new MathAudioScriptWithElementCode("ctcbl_hi"), true, this, this);
            } else if ("cttrm".equals(mathLesson.getElementCode())) {
                mathSection = new CountingCanvasColorBlockSection(new MathAudioScriptWithElementCode(null,
                        "cttrm_i1", "cttrm_i2"), true, this, this);
            } else if ("cttrl".equals(mathLesson.getElementCode())) {
                mathSection = new CountingCanvasColorBlockSection(new MathAudioScriptWithElementCode(null,
                        "cttrl_i1", "cttrl_i2"), false, this, this);
            } else if ("cttrs".equals(mathLesson.getElementCode())) {
                mathSection = new CountingCanvasColorBlockSameSection(new MathAudioScriptWithElementCode(null,
                        "cttrs_i1", "cttrs_i2"), this, this);
            } else if ("ctmsn".equals(mathLesson.getElementCode())) {
                mathSection = new MissingNumberSection(new MathAudioScriptWithElementCode("ctmsn_i1"), 0, 9, 1, 5, this, this);
            } else if ("ctcpr".equals(mathLesson.getElementCode())) {
                mathSection = new CompareNumberSection(new MathAudioScriptWithElementCode("ctcpr_i1"), 0, 9, this, this);
            }

            mathLesson.setAutoCognitaSection(mathSection);
        }
        return mathLessonList;
    }


    @Override
    protected LessonUnitCode getUnitCode() {
        return LessonUnitCode.MATH_1;
    }
}
