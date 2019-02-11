package com.maqautocognita.section;

import com.maqautocognita.screens.AbstractAutoCognitaScreen;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface IAutoCognitaSection {

    void render(boolean isShowing);

    void whenSingleTap(int screenX, int screenY);

    void whenTouchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition);

    void whenTouchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition);

    void whenTouchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition);

    void setAutoCognitaScreen(AbstractAutoCognitaScreen abstractAutoCognitaScreen, AbstractAutoCognitaSection.IOnHelpListener onHelpListener);

    void dispose();

    void closeHelp();

    void onHelp();

    void onResize();
}
