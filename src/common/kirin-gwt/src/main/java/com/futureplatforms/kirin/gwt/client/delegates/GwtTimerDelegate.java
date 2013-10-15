package com.futureplatforms.kirin.gwt.client.delegates;

import com.futureplatforms.kirin.controllers.TimerTask.PlatformTimerTask;
import com.futureplatforms.kirin.controllers.TimerTask.TimerDelegate;
import com.google.gwt.user.client.Timer;

/**
 * GWT implementation of a TimerDelegate and PlatformTimerTask
 * @author douglashoskins
 *
 */
public class GwtTimerDelegate implements TimerDelegate {
    @Override
    public PlatformTimerTask getPlatformTimerTask() {
        return new PlatformTimerTask() {
            private Timer mTimer; 
            
            private Timer createTimer() {
                final PlatformTimerTask self = this;
                return new Timer() {
                    public void run() {
                        self.run();
                    }
                };
            }
            
            @Override
            public void schedule(int delayMillis) {
                (mTimer = createTimer()).schedule(delayMillis);
            }
            
            @Override
            public void cancel() {
                if (mTimer != null) { mTimer.cancel(); }
            }
        };
    }
}
