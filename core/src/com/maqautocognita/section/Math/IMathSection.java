package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.section.IAutoCognitaSection;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface IMathSection extends IAutoCognitaSection {

    void reloadVoScript();

    MathAudioScriptWithElementCode getMathAudioScriptWithElementCode();
}
