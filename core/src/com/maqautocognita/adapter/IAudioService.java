package com.maqautocognita.adapter;

import com.maqautocognita.listener.IMediaRecordListener;
import com.maqautocognita.listener.ISoundPlayListener;

import java.io.InputStream;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface IAudioService {

    void playAudio(String audioPath, com.maqautocognita.listener.ISoundPlayListener musicPlayerListener);

    void stopMusic();

    String getAudioStorePath();

    boolean isMusicPlaying();

    void startRecord(final IMediaRecordListener mediaRecordListener);

    void startRecord(final IMediaRecordListener mediaRecordListener, String fileName);

    String stopRecord();

    void playBackRecord(final com.maqautocognita.listener.ISoundPlayListener musicPlayerListener);

    void playBackRecord(String fileName, final com.maqautocognita.listener.ISoundPlayListener musicPlayerListener);

    void playInputStream(InputStream inputStream, final ISoundPlayListener soundPlayListener);

    void stopPlayInputStream();

    boolean isAudioExists(String audioFileName);
}
