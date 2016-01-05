package com.futureplatforms.kirin.dependencies;


/**
 * Equivalent of java.util.Timer/java.util.TimerTask -- represents a task that
 * can be scheduled for one-time or repeated execution.
 * 
 * 
 * HOW TO USE:
 *   Extend this class and implement your delayed task in run()
 *   Task must be non-blocking, if you have a long-running task, separate it into 
 *   bite-size chunks that won't delay things!
 * 
 * @author douglashoskins
 *
 */
public abstract class TimerTask {
    /**
     * Implementing platforms should provide an implementation of PlatformTimerTask, which should
     * provide delayed execution functionality on that platform.
     * @author douglashoskins
     *
     */
    public static abstract class PlatformTimerTask {
        private TimerTask mTimerTask;

        private static int TIMER_ID = Integer.MIN_VALUE;

        private int id;

        public PlatformTimerTask() {
            this.id = TIMER_ID;
            TIMER_ID++;
        }

        protected int getTimerId() {
            return id;
        }

        private final void setRealTimerTask(TimerTask task) {
            mTimerTask = task;
        }
        
        /**
         * This method should be invoked by the PlatformTimerTask implementation 
         * after the specified delay or period
         */
        public final void run() {
            mTimerTask.run();
        }
        
        public abstract void cancel();
        
        /**
         * Platform specific implementations of schedule and scheduleRepeating.  These could
         * wrap around setTimeout and setInterval on a Javascript based system for instance.
         * @param delayMillis
         */
        public abstract void schedule(int delayMillis);
    }
    
    /**
     * Implementing platforms should provide an instance of TimerDelegate, which acts as 
     * a factory for PlatformTimerTasks
     * @author douglashoskins
     *
     */
    public interface TimerDelegate {
        PlatformTimerTask getPlatformTimerTask();
    }
    
    private PlatformTimerTask mPtt;
    
    public TimerTask() {
        this(StaticDependencies.getInstance());
    }
    
    public TimerTask(StaticDependencies deps) {
        mPtt = deps.getTimerDelegate().getPlatformTimerTask();
        // Pass this TimerTask into the native dependency, and our run method will be invoked later
        mPtt.setRealTimerTask(this);
    }
    
    public void cancel() {
        mPtt.cancel();
    }

    public int testId() { return mPtt.getTimerId(); }

    /**
     * Override this method to specify the deferred action to be taken.
     */
    public abstract void run();
    
    public void schedule(int delayMillis) {
        mPtt.schedule(delayMillis);
    }
}
