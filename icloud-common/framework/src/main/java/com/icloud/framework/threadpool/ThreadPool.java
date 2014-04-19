package com.icloud.framework.threadpool;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool<V> {
    private ThreadPoolExecutor tpe;
    private CompletionService<V> cs;

    public ThreadPool() {
	this(30, 30, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000));
    }

    public ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
	this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadPoolExecutor.AbortPolicy());
    }

    public ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
	tpe = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	cs = new ExecutorCompletionService<V>(tpe);
    }

    public void shutdown() {
	tpe.shutdown();
    }


	public Future<V> sumbit(Callable<V> task) {
		return cs.submit(task);
	}
	
	public Future<V> sumbit(Runnable task, V result) {
		return cs.submit(task, result);
	}

    public Future<V> sumbit(Runnable task) {
	return cs.submit(task, null);
    }

    public List<Runnable> shutdownNow() {
	return tpe.shutdownNow();
    }

    public static void main(String args[]) {
	ThreadPool<Object> pool = new ThreadPool<Object>();
	pool.sumbit(new Callable<Object>() {

	    @Override
	    public Object call() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("daf");
		return null;
	    }

	});
	pool.shutdown();
    }

	public void join() {
		// TODO Auto-generated method stub
		
	}
}
