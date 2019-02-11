package com.maqautocognita.listener;

import com.maqautocognita.utils.UserPreferenceUtils;

/**
 * It is mainly used to listen the value changes in {@link UserPreferenceUtils}
 *
 * @author sc.chi csc19840914@gmail.com
 */
public interface IUserPreferenceValueChangeListener {

    void onValueChange(String key, Object value);

    /**
     * it will be call when {@link UserPreferenceUtils#removeByKey(String)}
     *
     * @param key
     */
    void onRemove(String key);
}
