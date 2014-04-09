package com.futureplatforms.kirin.console;

import com.futureplatforms.kirin.dependencies.TimerTask.PlatformTimerTask;
import com.futureplatforms.kirin.dependencies.TimerTask.TimerDelegate;

public class ConsoleTimer implements TimerDelegate
{

    @Override
    public PlatformTimerTask getPlatformTimerTask() {
        return new PlatformTimerTask() {
            
            @Override
            public void schedule(final int delayMillis) {
                final PlatformTimerTask that = this;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    	try {
							Thread.sleep(delayMillis);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                        that.run();
                    }
                }).start();
            }
            
            @Override
            public void cancel() {
            }
        };
    }

	
}
