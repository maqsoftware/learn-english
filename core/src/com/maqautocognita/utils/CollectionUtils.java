package com.maqautocognita.utils;

import java.util.Collection;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CollectionUtils {

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.size() == 0;
    }
}
