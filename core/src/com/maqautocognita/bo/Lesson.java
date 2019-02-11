package com.maqautocognita.bo;

import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.utils.IconPosition;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class Lesson {


    private boolean passed;

    private boolean selected;

    /**
     * It is mainly used to store the dot in the navigation menu which is represent the lesson
     */
    private IconPosition iconPosition;

    private IAutoCognitaSection autoCognitaSection;

    private int sequence;

    public Lesson() {
    }

    public Lesson(boolean passed, boolean selected, IAutoCognitaSection autoCognitaSection, int sequence) {
        this.passed = passed;
        this.selected = selected;
        this.autoCognitaSection = autoCognitaSection;
        this.sequence = sequence;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public IconPosition getIconPosition() {
        return iconPosition;
    }

    public void setIconPosition(IconPosition iconPosition) {
        if (null == this.iconPosition) {
            this.iconPosition = iconPosition;
        }
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public IAutoCognitaSection getAutoCognitaSection() {
        return autoCognitaSection;
    }

    public void setAutoCognitaSection(IAutoCognitaSection autoCognitaSection) {
        this.autoCognitaSection = autoCognitaSection;
    }
}
