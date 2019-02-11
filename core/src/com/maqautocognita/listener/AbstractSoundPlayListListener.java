package com.maqautocognita.listener;

/**
 * @author sc.chi csc19840914@gmail.com
 *         <p/>
 *         It is mainly used for those caller who dont want implement all method for the interface {@link ISoundPlayListener}.
 *         For example, there is a caller want to implement the method {@link #onComplete()} only, and want leave other methods in blank
 */
public abstract class AbstractSoundPlayListListener extends com.maqautocognita.listener.AbstractSoundPlayListener implements ISoundPlayListListener {

    public void beforePlaySound(int index) {

    }
}
