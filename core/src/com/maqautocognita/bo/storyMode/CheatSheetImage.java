package com.maqautocognita.bo.storyMode;

import com.maqautocognita.bo.CheatSheetBox;
import com.maqautocognita.bo.CheatSheetLesson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chun on 27/2/2017.
 */

public class CheatSheetImage {

    public CheatSheetLesson lesson;

    public String imageName;

    public List<CheatSheetBox> cheatSheetBoxList;

    public int index;

    public void addCheatSheetBox(CheatSheetBox cheatSheetBox) {
        if (null == cheatSheetBoxList) {
            cheatSheetBoxList = new ArrayList<CheatSheetBox>();
        }
        cheatSheetBoxList.add(cheatSheetBox);
    }
}
