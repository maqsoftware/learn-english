package com.maqautocognita.listener;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface ISoundPlayListListener extends ISoundPlayListener {

    /**
     * This will be call before the sound file start to play
     *
     * @param index it is belongs to the sound file index in the sound file list which going to play
     */
    void beforePlaySound(int index);

}
