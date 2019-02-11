package com.maqautocognita.listener;

/**
 * @author sc.chi csc19840914@gmail.com
 *         <p/>
 *         It is mainly used for those caller who dont want implement all method for the interface {@link ISoundPlayListener}.
 *         For example, there is a caller want to implement the method {@link #onComplete()} only, and want leave other methods in blank
 */
public abstract class AbstractSoundPlayListener implements ISoundPlayListener {
    @Override
    public void onPlay(int audioListIndex, long millisecond) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onAudioFileMissing() {
        onComplete();
    }

    @Override
    public void onWaveChanged(int level) {

    }

    @Override
    public boolean isWaveChangeListenerRequired() {
        return false;
    }
}
