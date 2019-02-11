package com.maqautocognita.listener;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface ISoundPlayListener {

    /**
     * @param millisecond the current music playing position in millisecond
     */
    void onPlay(int audioListIndex, long millisecond);

    void onComplete();

    void onStop();

    void onAudioFileMissing();

    /**
     * Method called when a new waveform capture is available.
     * It is mainly used when want to capture the wave level for the playing audio
     *
     * @param level 0 - 255
     */
    void onWaveChanged(int level);

    /**
     * which is used to indicate if the visualization engine is required to enable. The engine is used to listen the wave changes of the playing audio.
     * If false returned, the wave listener will be off, and the callback of the {@link #onWaveChanged(int)} will be stop.
     */
    boolean isWaveChangeListenerRequired();

}
