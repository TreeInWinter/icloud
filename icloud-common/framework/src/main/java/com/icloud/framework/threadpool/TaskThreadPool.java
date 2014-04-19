package com.icloud.framework.threadpool;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @project LocationGateway
 * @author sunnylocus
 * @verson 1.0.0
 * @date Aug 2, 2008
 * @jdk 1.4.2
 */
public class TaskThreadPool extends ThreadGroup {

    private static final Log log = LogFactory.getLog(TaskThreadPool.class);

    private boolean isClosed = false; // 线程池是否关�?
    private LinkedList<Runnable> workQueue; // 工作队列
    private static int threadPoolID = 1; // 线程池的id

    public TaskThreadPool(int poolSize) { // poolSize 表示线程池中的工作线程的数量

	super(threadPoolID + ""); // 指定 ThreadGroup的名�?
	setDaemon(true); // 继承到的方法，设置是否守护线程池
	workQueue = new LinkedList<Runnable>(); // 创建工作队列
	for (int i = 0; i < poolSize; i++) {
	    new WorkThread(i).start(); // 创建并启动工作线�?线程池数量是多少就创建多少个工作线程
	}
    }

    /** 向工作队列中加入�?��新任�?由工作线程去执行该任�? */
    public synchronized void execute(Runnable task) {
	if (isClosed) {
	    throw new IllegalStateException();
	}
	if (task != null) {
	    workQueue.add(task);// 向队列中加入�?��任务
	    notify(); // 唤醒�?��正在getTask()方法中待任务的工作线�?
	}
    }

    /** 从工作队列中取出�?��任务,工作线程会调用此方法 */
    private synchronized Runnable getTask(int threadid) throws InterruptedException {
	while (workQueue.size() == 0) {
	    if (isClosed)
		return null;
	    log.info("工作线程" + threadid + " 等待任务...");
	    wait(); // 如果工作队列中没有任�?就等待任�?
	}
	log.info("工作线程" + threadid + " 执行任务...");
	return (Runnable) workQueue.removeFirst(); // 反回队列中第�?��元素,并从队列中删�?
    }

    /** 关闭线程�? */
    public synchronized void closePool() {
	if (!isClosed) {
	    waitFinish(); // 等待工作线程执行完毕
	    isClosed = true;
	    workQueue.clear(); // 清空工作队列
	    interrupt(); // 中断线程池中的所有的工作线程,此方法继承自ThreadGroup�?
	}
    }

    /** 等待工作线程把所有任务执行完�? */
    public void waitFinish() {
	synchronized (this) {
	    isClosed = true;
	    notifyAll(); // 唤醒�?��还在getTask()方法中等待任务的工作线程
	}
	Thread[] threads = new Thread[activeCount()]; // activeCount()
						      // 返回该线程组中活动线程的估计值�?
	int count = enumerate(threads); // enumerate()方法继承自ThreadGroup类，根据活动线程的估计�?获得线程组中当前�?��活动的工作线�?
	for (int i = 0; i < count; i++) { // 等待�?��工作线程结束
	    try {
		threads[i].join(); // 等待工作线程结束
	    } catch (InterruptedException ex) {
		ex.printStackTrace();
	    }
	}
    }

    /**
     * 内部�?工作线程,负责从工作队列中取出任务,并执�?
     * 
     * @author sunnylocus
     */
    private class WorkThread extends Thread {
	private int id;

	public WorkThread(int id) {
	    // 父类构�?方法,将线程加入到当前ThreadPool线程组中
	    super(TaskThreadPool.this, id + "");
	    this.id = id;
	}

	public void run() {
	    while (!isInterrupted()) { // isInterrupted()方法继承自Thread类，判断线程是否被中�?
		Runnable task = null;
		try {
		    task = getTask(id); // 取出任务
		} catch (InterruptedException ex) {
		    ex.printStackTrace();
		}
		// 如果getTask()返回null或�?线程执行getTask()时被中断，则结束此线�?
		if (task == null)
		    return;

		try {
		    task.run(); // 运行任务
		} catch (Throwable t) {
		    t.printStackTrace();
		}
	    }// end while
	}// end run
    }// end workThread
}