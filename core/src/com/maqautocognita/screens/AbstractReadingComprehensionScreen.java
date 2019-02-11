package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.bo.ReadingComprehension;
import com.maqautocognita.bo.SentenceWithActivityCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.section.sentence.ReadingComprehensionSection;

import java.io.File;

/**
 * Created by siu-chun.chi on 2/7/2017.
 */

public abstract class AbstractReadingComprehensionScreen extends AbstractSentenceScreen<ReadingComprehension> {


    private ReadingComprehensionSection readingComprehensionSection;

    public AbstractReadingComprehensionScreen(AbstractGame game, IMenuScreenListener menuScreenListener, String... images) {
        super(game, menuScreenListener, images);
    }

    @Override
    protected String getAudioPath() {
        return "comprehension" + File.separator;
    }

    @Override
    protected void onLessonSelected(SentenceWithActivityCode<ReadingComprehension> selectedSentenceWithActivityCode) {
        if (null == readingComprehensionSection) {
            readingComprehensionSection = new ReadingComprehensionSection(AbstractReadingComprehensionScreen.this);
        } else {
            readingComprehensionSection.hide();
        }
        selectedSentenceSection = readingComprehensionSection;
        readingComprehensionSection.show(selectedSentenceWithActivityCode.sentence, selectedSentenceWithActivityCode.activityCodeEnum);
    }


}
