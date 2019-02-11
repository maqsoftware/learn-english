package com.maqautocognita.utils;

import com.maqautocognita.constant.ActivityCodeEnum;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class SectionUtils {

    public static boolean isWriting(ActivityCodeEnum code) {
        return ActivityCodeEnum.WRITING.equals(code);
    }

    public static boolean isSpeaking(ActivityCodeEnum code) {
        return ActivityCodeEnum.SPEAKING.equals(code);
    }

    public static boolean isReading(ActivityCodeEnum code) {
        return ActivityCodeEnum.READING.equals(code);
    }

    public static boolean isReadingAndListening(ActivityCodeEnum code) {
        return ActivityCodeEnum.READING_AND_LISTENING.equals(code);
    }

    public static boolean isFillInTheBlanks(ActivityCodeEnum code) {
        return ActivityCodeEnum.FILL_IN_THE_BLANKS.equals(code);
    }

    public static boolean isGrouping(ActivityCodeEnum code) {
        return ActivityCodeEnum.GROUPING.equals(code);
    }

    public static boolean isWordBlend(ActivityCodeEnum code) {
        return ActivityCodeEnum.WORD_BLEND.equals(code);
    }

    public static boolean isListenAndType(ActivityCodeEnum code) {
        return ActivityCodeEnum.LISTEN_AND_TYPE.equals(code);
    }

}
