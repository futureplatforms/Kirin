package com.futureplatforms.kirin.android;

import java.util.TimerTask;

import android.os.Handler;

import com.futureplatforms.kirin.dependencies.TimerTask.PlatformTimerTask;
import com.futureplatforms.kirin.dependencies.TimerTask.TimerDelegate;

public class AndroidTimer implements TimerDelegate
{

	@Override 
	public PlatformTimerTask getPlatformTimerTask()
	{
		
		return new PlatformTimerTask()
		{
			private TimerTask getTimerTask() {
                return new TimerTask() {
                    @Override
                    public void run() {
                        self.run();
                    }
                };
            }
			
			private PlatformTimerTask self = this;
			private Handler handler = new Handler();
			private TimerTask scheduledTask = getTimerTask();
			@Override
			public void schedule(int delayMillis)
			{
				handler.postDelayed(scheduledTask, delayMillis);
			}
            @Override
            public void cancel() {
				handler.removeCallbacks(scheduledTask);

            }
		};
	}
	
	
}
