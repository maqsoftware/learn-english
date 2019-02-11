package com.maqautocognita.constant;

import com.badlogic.gdx.Gdx;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public enum ActivityCodeEnum {
    SPEAKING("SP"), WRITING("LW"), READING("AL"),
    READING_AND_LISTENING("AL"),
    GROUPING("GR"),
    WORD_BLEND("WB"),
    LISTEN_AND_TYPE("LT"),
    FILL_IN_THE_BLANKS("FB"),

    READ_AND_LISTEN("RL"),
    MULTIPLE_CHOICE("T"),

    READ_ARRANGE("RA"),
    LISTEN_ARRANGE("LA"),
    JUST_ARRANGE("JA"),

    READ_BUILD("RB"),
    LISTEN_BUILD("LB"),

    READ_AND_WRITE("RW"),

    READ_AND_TYPE("RT"),

    POSITIVE_VERB_CONJUGATION("PVC"),

    NEGATIVE_VERB_CONJUGATION("NVC"),

    NOUN_CONJUGATION("NC"),

    SENTENCE_BUILD("SB"),

    COMPREHENSION_READ("R"),

    COMPREHENSION_LISTEN("L");

    private final String code;

    ActivityCodeEnum(String code) {
        this.code = code;
    }

    public static ActivityCodeEnum getActivityCodeEnumByCode(String code) {
        for (ActivityCodeEnum activityCodeEnum : ActivityCodeEnum.values()) {
            if (activityCodeEnum.code.equalsIgnoreCase(code.trim())) {
                return activityCodeEnum;
            }
        }

        Gdx.app.log(ActivityCodeEnum.class.getName(), "no activityCode found for " + code);

        return null;
    }

    public String getCode() {
        return code;
    }
}
