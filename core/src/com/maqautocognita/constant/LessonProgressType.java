package com.maqautocognita.constant;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public enum LessonProgressType {

    COUNT("CT"),
    COMPARE("CP"),
    ADD("AD"),
    SUBTRACT("SB"),
    MULTIPLY("MT"),
    PLACE_VALUE("PV"),
    NUMBER_PATTERN("NP"),
    SHAPE("SH"),
    CLICK("CL"),
    MATH_TYPE("TY"),
    WRITE("WR"),
    SPEAK("SP"),
    MAKE_PICTURE("MP"),
    MATCH("MA"),
    COLOR_BLOCKS("CB"),
    LONG_FORM("LF");

    public final String type;

    LessonProgressType(String type) {
        this.type = type;
    }
}
