package com.maqautocognita.listener;

import com.maqautocognita.adapter.ISpeechRecognizeService;

/**
 * Created by sc.chi on 5/6/16.
 * <p/>
 * It is used to listen the the speech recognize result.
 * To init this callback, ths listener should be init in the {@link ISpeechRecognizeService#startSpeechListening(ISpeechRecognizeResultListener)}
 */
public interface ISpeechRecognizeResultListener {

    /**
     * It will return a array of word that the recognize engine has recognized
     *
     * @param results
     */
    void onResult(String[] results);

    /**
     * It will return the amplitude in percentage during the recording, 1 will be the most loudest
     *
     * @param amplitudePercentage
     */
    void onRecording(float amplitudePercentage);
}
