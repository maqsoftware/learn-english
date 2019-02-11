package com.maqautocognita.listener;


import com.maqautocognita.scene2d.actions.AbstractAdvanceListener;
import com.maqautocognita.scene2d.actions.IAdvanceActionListener;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public interface ISaySomethingListener {

    void onHomeClick();

    void onTranslateLanguageSelected(String text, Language fromLanguage, Language toLanguage, IAdvanceActionListener translationListener);

    void onTextToSpeech(String text, ISoundPlayListener soundPlayListener);

    void onSpeechToTextStart(AbstractAdvanceListener<String> advanceActionListener, Language language);

    void onSpeechToTextStop();

    void onFacebookClick(String shareText);

    void onWhatsappClick(String shareText);

    boolean isFacebookInstalled();

    boolean isWhatsappInstalled();
}
