package com.plugin.br.cordova.face;

public class ThreadPoolUtil {
    /**
     * 在非UI线程中执行
     *
     * @param task
     */
    public static void runTaskInThread(Runnable task) {
        ThreadPoolFactory.getCommonThreadPool().excute(task);
    }

    /**
     * 移除线程池中线程
     *
     * @param task
     */
    public static boolean removeTask(Runnable task) {
        return ThreadPoolFactory.getCommonThreadPool().remove(task);
    }
}