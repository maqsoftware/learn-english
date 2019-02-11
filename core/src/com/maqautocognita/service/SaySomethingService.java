package com.maqautocognita.service;

import com.maqautocognita.AbstractLearningGame;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.listener.ISaySomethingListener;
import com.maqautocognita.listener.ISoundPlayListener;
import com.maqautocognita.scene2d.actions.AbstractAdvanceListener;
import com.maqautocognita.scene2d.actions.IAdvanceActionListener;
import com.maqautocognita.utils.StringUtils;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class SaySomethingService implements ISaySomethingListener {

    private static SaySomethingService instance = null;
    private final IMenuScreenListener menuScreenListener;

    public SaySomethingService(final IMenuScreenListener menuScreenListener) {
        this.menuScreenListener = menuScreenListener;
    }

    public static SaySomethingService getInstance(IMenuScreenListener menuScreenListener) {
        if (instance == null) {
            instance = new SaySomethingService(menuScreenListener);
        }
        return instance;
    }

    @Override
    public void onHomeClick() {
        if (null != menuScreenListener) {
            menuScreenListener.onHomeSelected();
        }
    }

    @Override
    public void onTranslateLanguageSelected(String text, Language fromLanguage, Language toLanguage, IAdvanceActionListener translationListener) {
        if (StringUtils.isNotBlank(text)) {
            IBMWatonTranslateAndSpeechService.getInstance().translateTextToLanguage(text, fromLanguage, toLanguage, translationListener);
        }
    }

    @Override
    public void onTextToSpeech(String text, ISoundPlayListener soundPlayListener) {
        if (StringUtils.isNotBlank(text)) {
            IBMWatonTranslateAndSpeechService.getInstance().textToSpeech(text, soundPlayListener);
        } else {
            if (null != soundPlayListener) {
                soundPlayListener.onComplete();
            }
        }
    }

    @Override
    public void onSpeechToTextStart(final AbstractAdvanceListener<String> advanceActionListener, final Language language) {
        AbstractLearningGame.speechRecognizeService.startIBMSpeechToTextListener(advanceActionListener, language);
    }

    @Override
    public void onSpeechToTextStop() {
        AbstractLearningGame.speechRecognizeService.stopIBMSpeechToTextListener();
    }

    @Override
    public void onFacebookClick(String shareText) {
        if (isFacebookInstalled()) {
            AbstractLearningGame.shareService.shareTextToFacebook(shareText);
        }
    }

    @Override
    public void onWhatsappClick(String shareText) {
        if (isWhatsappInstalled()) {
            AbstractLearningGame.shareService.shareToWhatsapp(shareText);
        }
    }

    @Override
    public boolean isFacebookInstalled() {
        return AbstractLearningGame.shareService.isFacebookInstalled();
    }

    @Override
    public boolean isWhatsappInstalled() {
        return AbstractLearningGame.shareService.isWhatsappInstalled();
    }


}
