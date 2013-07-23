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
			@Override
			public void schedule(int delayMillis)
			{
				new Handler().postDelayed(getTimerTask(), delayMillis);
			}
            @Override
            public void cancel() {
                // TODO Auto-generated method stub
                
            }
		};
	}
	
	
}
