package com.maqautocognita.service;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.listener.ISoundPlayListener;
import com.maqautocognita.scene2d.actions.IAdvanceActionListener;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class IBMWatonTranslateAndSpeechService {

    private static IBMWatonTranslateAndSpeechService instance = null;
    private LanguageTranslator service;
    private TextToSpeech textToSpeech;

    public static IBMWatonTranslateAndSpeechService getInstance() {
        if (instance == null) {
            instance = new IBMWatonTranslateAndSpeechService();

        }
        return instance;
    }

    public void textToSpeech(String pText, final ISoundPlayListener soundPlayListener) {
        stopPlayTextToSpeech();
        getTextToSpeechService().synthesize(pText, Voice.EN_ALLISON,
                com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat.WAV).enqueue(new ServiceCallback<InputStream>() {

            @Override
            public void onResponse(InputStream inputStream) {
                AbstractGame.audioService.playInputStream(inputStream, soundPlayListener);
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //nothing can do in here
                }
            }

            @Override
            public void onFailure(Exception e) {
                soundPlayListener.onStop();
            }
        });

    }

    public void stopPlayTextToSpeech() {
        AbstractGame.audioService.stopPlayInputStream();
    }

    private TextToSpeech getTextToSpeechService() {
        if (null == textToSpeech) {
            textToSpeech = new TextToSpeech();
            textToSpeech.setApiKey("3IYf_R0KJHaAgAVD-kOHzDzP1LZKfF-JKj6nyuJ4zA1e");
            //textToSpeech.setUsernameAndPassword("90c3bea2-8620-476e-bfd4-fc6fc4b410f5", "N1XCyg1uMFSm");
            textToSpeech.setUsernameAndPassword("6b3aca8c-e3ef-44ca-8616-628c378c7671", "5blJqZTVIkDg");
        }
        return textToSpeech;
    }

    public void translateTextToLanguage(String vText, Language fromLanguage, Language toLanguage, final IAdvanceActionListener<String> advanceActionListener) {
        getLanguageTranslatorService().translate(
                vText, fromLanguage, toLanguage)
                .enqueue(new ServiceCallback<TranslationResult>() {
                    @Override
                    public void onResponse(TranslationResult response) {
                        if (null != advanceActionListener) {
                            advanceActionListener.onComplete(response.getFirstTranslation());
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

    }

    private LanguageTranslator getLanguageTranslatorService() {
        if (null == service) {
            service = new LanguageTranslator();
            //service.setUsernameAndPassword("2e1b9c9c-689d-40bb-bd47-df008fd0f5e4", "poSHe6xVQTsO");
            service.setUsernameAndPassword("ffb3587b-f4b1-4dc7-afa6-f9a70a40cbff", "KqG1Igzy4yeL");
        }
        return service;
    }


}
