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
 *
 * 关于排队：
 * 如果运行的线程少于 corePoolSize，则 Executor 始终首选添加新的线程，而不进行排队。
 * 如果运行的线程等于或多于 corePoolSize，则 Executor 始终首选将请求加入workQueue队列，而不添加新的线程。
 * 如果无法将请求加入队列，则创建新的线程，除非创建此线程超出 maximumPoolSize(这里是10个)，在这种情况下，任务将被拒绝。
 */
@Singleton
public class JobExecutor implements ThreadExecutor {

	// 如果设置的 corePoolSize 和 maximumPoolSize相同，则创建了固定大小的线程池
	private static final int CORE_POOL_SIZE = 3;
	// 将 maximumPoolSize 设置为基本的无界值（如 Integer.MAX_VALUE），则允许池适应任意数量的并发任务
	private static final int MAX_POOL_SIZE = 5;

	// 动态的调整池中的线程数, 当线程池中的线程数量大于 corePoolSize时，某线程空闲时间超过10秒，该线程被终止。
	private static final int KEEP_ALIVE_TIME = 10;
	// Sets the Time Unit to seconds
	private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

	// 缓冲队列
	private final BlockingDeque<Runnable> workQueue;
	// 用来创建新的线程
	private final ThreadFactory threadFactory;
	/*如果此时线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量等于maximumPoolSize，
	那么通过 handler所指定的策略来处理此任务。也就是：处理任务的优先级为：核心线程corePoolSize、任务队列workQueue、最大线程maximumPoolSize，
	如果三者都满了，使用handler处理被拒绝的任务。
	由于这里的workQueue是最大的数量，所以并没有使用handler*/
	private final ThreadPoolExecutor threadPoolExecutor;

	@Inject public JobExecutor() {
		// LinkedBlockingDeque 无界队列, 所有 corePoolSize 线程都忙的情况下将新任务加入队列
		this.workQueue = new LinkedBlockingDeque<>();
		this.threadFactory = new JobThreadFactory();
		this.threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,KEEP_ALIVE_TIME,KEEP_ALIVE_TIME_UNIT,
																										 workQueue,threadFactory);
	}

	@Override public void execute(Runnable runnableCommand) {
		if (runnableCommand == null) {
			throw new IllegalArgumentException("Runnable to execute cannot be null");
		}
		this.threadPoolExecutor.execute(runnableCommand);
	}

	private static class JobThreadFactory implements ThreadFactory {
		private static final String THREAD_NAME = "jobThread_";
		private int counter = 0;

		@Override public Thread newThread(Runnable runnable) {
			return new Thread(runnable,THREAD_NAME + counter++);
		}
	}
}
