package com.maqautocognita.service;

import com.maqautocognita.listener.IDemoListener;
import com.maqautocognita.listener.IMenuScreenListener;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class DemoService implements IDemoListener {

    private static DemoService instance = null;
    private final IMenuScreenListener menuScreenListener;

    public DemoService(final IMenuScreenListener menuScreenListener) {
        this.menuScreenListener = menuScreenListener;
    }

    public static DemoService getInstance(IMenuScreenListener menuScreenListener) {
        if (instance == null) {
            instance = new DemoService(menuScreenListener);
        }
        return instance;
    }

    @Override
    public void onHomeClick() {
        if (null != menuScreenListener) {
            menuScreenListener.onHomeSelected();
        }
    }
//
//    @Override
//    public void onTranslateLanguageSelected(String text, Language fromLanguage, Language toLanguage, IAdvanceActionListener translationListener) {
//        if (StringUtils.isNotBlank(text)) {
//            IBMWatonTranslateAndSpeechService.getInstance().translateTextToLanguage(text, fromLanguage, toLanguage, translationListener);
//        }
//    }
//
//    @Override
//    public void onTextToSpeech(String text, ISoundPlayListener soundPlayListener) {
//        if (StringUtils.isNotBlank(text)) {
//            IBMWatonTranslateAndSpeechService.getInstance().textToSpeech(text, soundPlayListener);
//        } else {
//            if (null != soundPlayListener) {
//                soundPlayListener.onComplete();
//            }
//        }
//    }
//
//    @Override
//    public void onSpeechToTextStart(final AbstractAdvanceListener<String> advanceActionListener, final Language language) {
//        AbstractLearningGame.speechRecognizeService.startIBMSpeechToTextListener(advanceActionListener, language);
//    }
//
//    @Override
//    public void onSpeechToTextStop() {
//        AbstractLearningGame.speechRecognizeService.stopIBMSpeechToTextListener();
//    }
//
//    @Override
//    public void onFacebookClick(String shareText) {
//        if (isFacebookInstalled()) {
//            AbstractLearningGame.shareService.shareTextToFacebook(shareText);
//        }
//    }
//
//    @Override
//    public void onWhatsappClick(String shareText) {
//        if (isWhatsappInstalled()) {
//            AbstractLearningGame.shareService.shareToWhatsapp(shareText);
//        }
//    }
//
//    @Override
//    public boolean isFacebookInstalled() {
//        return AbstractLearningGame.shareService.isFacebookInstalled();
//    }
//
//    @Override
//    public boolean isWhatsappInstalled() {
//        return AbstractLearningGame.shareService.isWhatsappInstalled();
//    }

    @Override
    public void onImageBackgroundClick(String screenCode) {
        if (screenCode.equals("DemoScreenAddSubtract")) {
            menuScreenListener.onMathAdditionSubtractionSelected();
        }else if (screenCode.equals("DemoScreenAlphabet")) {
            menuScreenListener.onAlphabetSelected();
        }else if (screenCode.equals("DemoScreenComprehension1")) {
            menuScreenListener.onReadingComprehensionUnit1Selected();
        }else if (screenCode.equals("DemoScreenComprehension2")) {
            menuScreenListener.onReadingComprehensionUnit2Selected();
        }else if (screenCode.equals("DemoScreenComprehension3")) {
            menuScreenListener.onReadingComprehensionUnit3Selected();
        }else if (screenCode.equals("DemoScreenCount")) {
            menuScreenListener.onMathCountSelected();
        }else if (screenCode.equals("DemoScreenMultiply")) {
            menuScreenListener.onMathMultiplySelected();
        }else if (screenCode.equals("DemoScreenPhonics1")) {
            menuScreenListener.onPhonicU1Selected();
        }else if (screenCode.equals("DemoScreenPhonics2")) {
            menuScreenListener.onPhonicU2Selected();
        }else if (screenCode.equals("DemoScreenPhonics3")) {
            menuScreenListener.onPhonicU3Selected();
        }else if (screenCode.equals("DemoScreenPhonics4")) {
            menuScreenListener.onPhonicU4Selected();
        }else if (screenCode.equals("DemoScreenWord1")) {
            menuScreenListener.onSentenceUnit1Selected();
        }else if (screenCode.equals("DemoScreenWord2")) {
            menuScreenListener.onSentenceUnit2Selected();
        }else if (screenCode.equals("DemoScreenWord3")) {
            menuScreenListener.onSentenceUnit3Selected();
        }else if (screenCode.equals("DemoScreenWord4")) {
            menuScreenListener.onSentenceUnit4Selected();
        }

    }


}
