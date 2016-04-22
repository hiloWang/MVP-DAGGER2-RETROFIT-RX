package com.github.hilo.data.executor;

import com.github.hilo.domain.executor.ThreadExecutor;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Decorated {@link java.util.concurrent.ThreadPoolExecutor}
 */
@Singleton
public class JobExecutor implements ThreadExecutor {

    private static final int INITIAL_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 5;

    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 10;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final BlockingDeque<Runnable> workQueue;
    private final ThreadFactory threadFactory;
    private final ThreadPoolExecutor threadPoolExecutor;

    @Inject
    public JobExecutor() {
        this.workQueue = new LinkedBlockingDeque<>();
        this.threadFactory = new JobThreadFactory();
        this.threadPoolExecutor = new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, workQueue, threadFactory);
    }

    @Override
    public void execute(Runnable runnableCommand) {
        if (runnableCommand == null) {
            throw new IllegalArgumentException("Runnable to execute cannot be null");
        }
        this.threadPoolExecutor.execute(runnableCommand);
    }

    private static class JobThreadFactory implements ThreadFactory {
        private static final String THREAD_NAME = "jobThread_";
        private int counter = 0;

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, THREAD_NAME + counter++);
        }
    }
}
