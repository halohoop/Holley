package com.halohoop.holley.http.core.managers;

/**
 * Created by Pooholah on 2017/5/14.
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 饿汉单例模式
 */
public class ThreadPoolManager {
    private static ThreadPoolManager mPoolManager = new ThreadPoolManager();


    /**
     * 阻塞式队列
     */
    private LinkedBlockingQueue<Future<?>> mTaskQueue = new LinkedBlockingQueue<>();
    private ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadPoolManager() {
        this.mThreadPoolExecutor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),//4核
                10,//最大线程数
                10, TimeUnit.SECONDS,//存活时间
                new ArrayBlockingQueue<Runnable>(4),//维持线程池开销
                mRejectHandler);//拒绝策略

        //开始运行
        this.mThreadPoolExecutor.execute(mRunnable);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                FutureTask<?> futureTask = null;
                try {
                    futureTask = (FutureTask<?>) mTaskQueue.take();//阻塞式方法
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (futureTask != null) {
                    mThreadPoolExecutor.execute(futureTask);
                }
            }
        }
    };

    private RejectedExecutionHandler mRejectHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                mTaskQueue.put(new FutureTask<>(r, null));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public <T> void execute(FutureTask<T> futureTask) throws InterruptedException {
        mTaskQueue.put(futureTask);
    }

    public static ThreadPoolManager getInstance() {
        return mPoolManager;
    }
}
