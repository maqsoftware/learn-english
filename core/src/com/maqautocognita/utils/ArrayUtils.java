package com.maqautocognita.utils;

import com.badlogic.gdx.utils.StringBuilder;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ArrayUtils {

    public static String printArray(String arrays[]) {
        StringBuilder sb = new StringBuilder();
        if (isNotEmpty(arrays)) {
            for (String text : arrays) {
                sb.append(text + ",");
            }
            sb.deleteCharAt(sb.length - 1);
        }
        return sb.toString();
    }

    public static boolean isNotEmpty(Object arrays[]) {
        return !isEmpty(arrays);
    }

    public static boolean isEmpty(Object arrays[]) {
        return null == arrays || arrays.length == 0;

    }

    public static int[] toArray(List<Integer> integerList) {
        if (CollectionUtils.isNotEmpty(integerList)) {
            int[] integers = new int[integerList.size()];
            for (int i = 0; i < integers.length; i++) {
                integers[i] = integerList.get(i);
            }
            return integers;
        }
        return null;
    }

    public static boolean isNotContainIgnoreCase(String arrays[], String word) {
        return !isContainIgnoreCase(arrays, word);
    }

    public static boolean isContainIgnoreCase(String arrays[], String word) {
        if (null == arrays) {
            return false;
        }

        for (String element : arrays) {
            if (element.trim().equalsIgnoreCase(word.trim())) {
                return true;
            }
        }

        return false;
    }

    public static String toString(String[] values) {
        StringBuilder textBuilder = new StringBuilder();
        if (isNotEmpty(values)) {
            for (String value : values) {
                textBuilder.append(value);
            }
        }

        return textBuilder.toString();
    }

    public static String[] join(String[] array1, String[] array2) {
        int totalLength = 0;
        if (isNotEmpty(array1)) {
            totalLength += array1.length;
        }

        if (isNotEmpty(array2)) {
            totalLength += array2.length;
        }

        if (totalLength > 0) {
            String[] joinedArray = new String[totalLength];
            int startIndex = 0;
            if (isNotEmpty(array1)) {
                System.arraycopy(array1, 0, joinedArray, startIndex, array1.length);
                startIndex += array1.length;
            }

            if (isNotEmpty(array2)) {
                System.arraycopy(array2, 0, joinedArray, startIndex, array2.length);
            }

            return joinedArray;
        }

        return null;

    }
}
