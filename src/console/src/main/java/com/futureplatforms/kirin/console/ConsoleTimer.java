package com.futureplatforms.kirin.console;

import com.futureplatforms.kirin.controllers.TimerTask.PlatformTimerTask;
import com.futureplatforms.kirin.controllers.TimerTask.TimerDelegate;

public class ConsoleTimer implements TimerDelegate
{

    @Override
    public PlatformTimerTask getPlatformTimerTask() {
        return new PlatformTimerTask() {
            
            @Override
            public void schedule(int delayMillis) {
                final PlatformTimerTask that = this;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        that.run();
                    }
                });
            }
            
            @Override
            public void cancel() {
            }
        };
    }

	
}
