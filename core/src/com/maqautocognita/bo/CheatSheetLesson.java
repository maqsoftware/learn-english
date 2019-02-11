package com.maqautocognita.bo;

import com.maqautocognita.bo.storyMode.CheatSheetImage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class CheatSheetLesson {

    public double id;

    public int index;

    public List<CheatSheetImage> imageList;

    public CheatSheetImage selectedImage;

    public void addCheatSheetImage(CheatSheetImage cheatSheetImage) {
        if (null == imageList) {
            imageList = new ArrayList<CheatSheetImage>();
        }
        imageList.add(cheatSheetImage);
    }

}
