package com.maqautocognita.constant;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public enum LessonUnitCode {
    ALPHABET("A"), PHONIC_UNIT_1("P1"),
    PHONIC_UNIT_2("P2"), PHONIC_UNIT_3("P3"),
    PHONIC_UNIT_4("P4"), MATH_1("M1"), MATH_2("M2"),
    MATH_3("M3"), MATH_4("M4"), MATH_5("M5");

    public final String code;

    LessonUnitCode(String code) {
        this.code = code;
    }

}
