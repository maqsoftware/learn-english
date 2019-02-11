package com.maqautocognita.section;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface ISectionChangeListener {

    /**
     * It is mainly used to reset everything in the section, such as current screen object
     * It is mainly called before the lesson or review changed
     */
    void reset();

}
