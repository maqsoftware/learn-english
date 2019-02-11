package com.maqautocognita.adapter;

import com.maqautocognita.scene2d.actions.AbstractAdvanceListener;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface ISpeechRecognizeService {

    /**
     * It is used to ask the recognize engine to change the file for speech recognize, the file is contains the word or letter which required for the recognition
     * <p/>
     * It is recommend to call before the screen show, as the change action is a time consuming action, because it is involve IO.
     *
     * @param fileName
     */
    void changeRecognizeFile(String fileName);

    void startSpeechListening(com.maqautocognita.listener.ISpeechRecognizeResultListener speechRecognizeResultListener);

    void stopSpeechListening();


    void startIBMSpeechToTextListener(AbstractAdvanceListener<String> advanceActionListener, Language language);

    void stopIBMSpeechToTextListener();

    boolean isSpeechRecognizeSupport();

}
