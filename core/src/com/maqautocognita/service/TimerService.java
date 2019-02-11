package com.maqautocognita.service;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class TimerService {

    private static final int TIMER_DELAY_SECOND = 2;
    private final ITimerListener timerListener;

    private Timer timer;

    public TimerService(ITimerListener timerListener) {
        this.timerListener = timerListener;
    }

    public void startTimer(Object threadIndicator) {
        startTime(threadIndicator, TIMER_DELAY_SECOND);
    }

    private void startTime(final Object threadIndicator, float second) {
        if (null == timer) {
            timer = new Timer();
        } else {
            timer.cancel();
            timer = new Timer();
        }

        timerListener.beforeStartTimer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerListener.onTimerComplete(threadIndicator);
            }
        }, (long) (second * 1000));
    }

    public void clearTimer() {
        if (null != timer) {
            timer.cancel();
        }
    }

    public void startTimer(Object threadIndicator, float second) {
        startTime(threadIndicator, second);
    }

    public interface ITimerListener {
        void beforeStartTimer();

        void onTimerComplete(Object threadIndicator);
    }


}
