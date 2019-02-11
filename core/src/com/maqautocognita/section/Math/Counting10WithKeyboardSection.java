package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.maqautocognita.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Screen shows a random number 1-10 of an object (e.g. apple).
 * User is asked to count them and press the number button on the screen.
 * Numbers are not shown on the object when counted.  Use a different kind of object image each time.  Repeat {@link #NUMBER_OF_ROUND_TO_PLAY} times.
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class Counting10WithKeyboardSection extends CountingSection {

    private static final int NUMBER_OF_ROUND_TO_PLAY = 5;
    private List<String> imageObjectList;

    public Counting10WithKeyboardSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, 10, abstractAutoCognitaScreen, onHelpListener, true);
        initImageObjectList();
    }

    private void initImageObjectList() {
        if (null == imageObjectList) {
            imageObjectList = new ArrayList<String>(NUMBER_OF_ROUND_TO_PLAY);
            imageObjectList.add(MathImagePathUtils.APPLE);
            imageObjectList.add(MathImagePathUtils.BIRD);
            imageObjectList.add(MathImagePathUtils.ZEBRA);
            imageObjectList.add(MathImagePathUtils.DUCK);
            imageObjectList.add(MathImagePathUtils.BOAT);
            //random the image sequence
            Collections.shuffle(imageObjectList);
        }
    }

    public Counting10WithKeyboardSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener, boolean isKeyboardRequired) {
        super(mathAudioScriptWithElementCode, 10, abstractAutoCognitaScreen, onHelpListener, isKeyboardRequired);
        initImageObjectList();
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        initImageObjectList();
        return ArrayUtils.join(imageObjectList.toArray(new String[]{}), super.getAllRequiredTextureName());
    }

    @Override
    protected String getScreenObjectByRound(int currentRound) {

        if (currentRound >= 0 && currentRound < imageObjectList.size()) {
            return imageObjectList.get(currentRound);
        }
        return null;
    }


}
