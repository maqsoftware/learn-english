package com.maqautocognita.section;

import com.maqautocognita.bo.Activity;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface IActivityChangeListener extends ISectionChangeListener {

    /**
     * It will be call when a new activity is selected, whatever it is normal lesson ,review or master test.
     *
     * @param selectedActivity
     * @param isMasterTest     indicate if it is a master test
     */
    void setSelectedActivity(Activity selectedActivity, boolean isMasterTest);

}
