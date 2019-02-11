package com.maqautocognita.constant;

/**
 * Created by siu-chun.chi on 7/1/2017.
 */

public enum LessonType {

    READING(LessonUnitCode.ALPHABET, LessonUnitCode.PHONIC_UNIT_1, LessonUnitCode.PHONIC_UNIT_2, LessonUnitCode.PHONIC_UNIT_3,
            LessonUnitCode.PHONIC_UNIT_4),
    MATH(LessonUnitCode.MATH_1, LessonUnitCode.MATH_2, LessonUnitCode.MATH_3, LessonUnitCode.MATH_4, LessonUnitCode.MATH_5);

    public final LessonUnitCode[] lessonUnitCodes;

    LessonType(LessonUnitCode... lessonUnitCodes) {
        this.lessonUnitCodes = lessonUnitCodes;
    }

    public static LessonType getLessonType(LessonUnitCode givenLessonUnitCode) {
        for (LessonType lessonType : LessonType.values()) {
            for (LessonUnitCode lessonUnitCode : lessonType.lessonUnitCodes) {
                if (lessonUnitCode.equals(givenLessonUnitCode)) {
                    return lessonType;
                }
            }
        }

        return null;
    }
}
