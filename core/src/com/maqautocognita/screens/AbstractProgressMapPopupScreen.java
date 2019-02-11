package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.section.ProgressMapPopupSection;
import com.maqautocognita.service.TimerService;
import com.badlogic.gdx.Gdx;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public abstract class AbstractProgressMapPopupScreen extends AbstractAutoCognitaScreen {

    /**
     * Mainly used as a flag to store the current playing lesson code, if the lesson code is different to the next playing lesson code, the progress map will be shown
     */
    protected String previousLessonCode;
    private ProgressMapPopupSection progressMapPopupSection;
    private TimerService timerService;

    public AbstractProgressMapPopupScreen(AbstractGame game, IMenuScreenListener menuScreenListener, String... images) {
        super(game, menuScreenListener, images);
    }

    protected void showProgressMap(String lessonCode) {

        if (null != previousLessonCode && !previousLessonCode.equals(lessonCode)) {
            timerService = new TimerService(new TimerService.ITimerListener() {
                @Override
                public void beforeStartTimer() {

                }

                @Override
                public void onTimerComplete(Object threadIndicator) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            if (null != progressMapPopupSection) {
                                progressMapPopupSection.hide();
                            }
                        }
                    });
                }
            });

            if (null == progressMapPopupSection) {
                progressMapPopupSection = new ProgressMapPopupSection(
                        getLessonService(),
                        getUnitCode(), new IActionListener() {
                    @Override
                    public void onComplete() {
                        if (null != timerService) {
                            timerService.clearTimer();
                        }
                        if (null != progressMapPopupSection) {
                            progressMapPopupSection.hide();
                        }
                    }
                }, menuScreenListener
                );

            }

            progressMapPopupSection.show();

            //timerService.startTimer(null, 5);
        }

        previousLessonCode = lessonCode;

    }

    protected abstract LessonUnitCode getUnitCode();

    protected abstract void onProgressMapPopupElementSelected(String selectedElementCode);

    protected void clearTimer() {
        if (null != timerService) {
            timerService.clearTimer();
        }
    }

    protected void hideProgressMap() {
        if (null != progressMapPopupSection) {
            progressMapPopupSection.hide();
        }
    }

    @Override
    public void render(float delta) {
        if (isProgressMapShowing()) {
            clearScreen();
            progressMapPopupSection.render();
        } else {
            super.render(delta);
        }
    }

    @Override
    public void hide() {
        super.hide();
        previousLessonCode = null;
        if (null != progressMapPopupSection) {
            progressMapPopupSection.hide();
            progressMapPopupSection = null;
        }
    }

    protected boolean isProgressMapShowing() {
        return null != progressMapPopupSection && progressMapPopupSection.isShowing();
    }
}
