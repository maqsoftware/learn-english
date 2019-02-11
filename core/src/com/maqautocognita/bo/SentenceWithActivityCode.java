package com.maqautocognita.bo;

import com.maqautocognita.constant.ActivityCodeEnum;

/**
 * store the sentence with lesson information
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class SentenceWithActivityCode<T extends AbstractSentence> {

    public T sentence;

    public ActivityCodeEnum activityCodeEnum;

}
