package com.maqautocognita.service;

import android.util.Log;

import com.maqautocognita.scene2d.actions.AbstractAdvanceListener;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;

/**
 * Created by sc.chi on 5/6/16.
 */
public class SpeechRecognizeService implements RecognitionListener, com.maqautocognita.adapter.ISpeechRecognizeService {

    private static final String MENU_SEARCH = "menu";
    private final String directoryLocation;
    private AdvanceSpeechRecognizer recognizer;
    private com.maqautocognita.listener.ISpeechRecognizeResultListener speechRecognizeResultListener;
    private SpeechToText speechToText;
    private MicrophoneInputStream microphoneInputSteamCapture;
    private boolean isIBMSpeechToTextListening;

    public SpeechRecognizeService(String directoryLocation) {
        this.directoryLocation = directoryLocation;
    }

    public void changeRecognizeFile(final String fileName) {

        try {
            setupRecognizer(new File(directoryLocation), fileName);
        } catch (IOException e) {
            Log.e(getClass().getName(), "changeRecognizeFile", e);
        }

    }

    @Override
    public void startSpeechListening(com.maqautocognita.listener.ISpeechRecognizeResultListener speechRecognizeResultListener) {
        this.speechRecognizeResultListener = speechRecognizeResultListener;
        try {
            recognizer.startListening(MENU_SEARCH, speechRecognizeResultListener);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "setSearch", e);
        }
    }

    @Override
    public void stopSpeechListening() {
        if (null != recognizer) {
            recognizer.stop();
        }
    }

    @Override
    public void startIBMSpeechToTextListener(final AbstractAdvanceListener<String> advanceActionListener, final Language language) {
        if (!isIBMSpeechToTextListening) {
            isIBMSpeechToTextListening = true;

            microphoneInputSteamCapture = new MicrophoneInputStream(true);
            getIBMSpeechToText().recognizeUsingWebSocket(microphoneInputSteamCapture, getRecognizeOptions(language),
                    new BaseRecognizeCallback() {
                        @Override
                        public void onTranscription(SpeechResults speechResults) {
                            if (speechResults.getResults() != null && !speechResults.getResults().isEmpty()) {
                                advanceActionListener.onComplete(speechResults.getResults().get(0).getAlternatives().get(0).getTranscript());
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            super.onError(e);
                            advanceActionListener.onError();
                        }

                        @Override
                        public void onDisconnected() {
                            advanceActionListener.onError();
                        }
                    });
        }
    }

    private SpeechToText getIBMSpeechToText() {
        if (null == speechToText) {
            speechToText = new SpeechToText();
            speechToText.setUsernameAndPassword("887e593b-3ed6-4d36-93cc-eb068e4bfdf2", "Ig3G7YETCxoS");
        }
        return speechToText;
    }

    private RecognizeOptions getRecognizeOptions(Language language) {
        return new RecognizeOptions.Builder()
                .continuous(true)
                .contentType(ContentType.OPUS.toString())
                .model(Language.ENGLISH.equals(language) ? "en-US_BroadbandModel" : "es-ES_BroadbandModel")
                .interimResults(true)
                .inactivityTimeout(1500)
                .build();
    }

    @Override
    public void stopIBMSpeechToTextListener() {
        try {
            if (null != microphoneInputSteamCapture) {
                microphoneInputSteamCapture.close();
                microphoneInputSteamCapture = null;
            }
        } catch (IOException e) {
            Log.e(getClass().getName(), "", e);
        }
        isIBMSpeechToTextListening = false;

    }

    @Override
    public boolean isSpeechRecognizeSupport() {
        return AdvanceSpeechRecognizerSetup.isLibraryLoaded();
    }

    private void setupRecognizer(File directoryLocation, String searchFilename) throws IOException {

        if (null != recognizer) {
            //make sure the previous recognizer is released
            recognizer.cancel();
            recognizer.shutdown();
        }

        recognizer = AdvanceSpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(directoryLocation, "en-us-ptm"))
                .setDictionary(new File(directoryLocation, "cmudict-en-us.dict"))

                // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .setRawLogDir(directoryLocation)

                // Threshold to tune for keyphrase to balance between false alarms and misses
                .setKeywordThreshold(1e-45f)

                // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)

                .getRecognizer();
        recognizer.addListener(this);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(directoryLocation, searchFilename);
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

    }


    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {

    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        String result = null;
        if (null != hypothesis) {

            try {
                result = hypothesis.getHypstr();
            } catch (Exception ex) {
                Log.e(getClass().getName(), "onResult", ex);
            }
            Log.i(this.getClass().getName(), "result = " + result);

        }

        if (null != speechRecognizeResultListener) {
            speechRecognizeResultListener.onResult(null == result ? null : result.split(" "));
        }
    }

    @Override
    public void onError(Exception exception) {
        Log.e(getClass().getName(), "onError", exception);
        speechRecognizeResultListener.onResult(null);
    }

    @Override
    public void onTimeout() {
        Log.i(getClass().getName(), "onTimeout");
        speechRecognizeResultListener.onResult(null);
    }
}
