package com.maqautocognita.listener;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface IMediaRecordListener {

    /**
     * The amplitude given will be in range 0 - 1, for example, 1 will be the loudest sound
     *
     * @param maxAmplitude
     */
    void onRecording(double maxAmplitude);
}
