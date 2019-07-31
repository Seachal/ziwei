package framework.thread;


import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import framework.ioc.FWConst;

public class ThreadWorker {

    private static ExecutorService executorService = Executors
            .newFixedThreadPool(FWConst.net_pool_size);

    /**
     * 线程池里跑runnable
     *
     * @param runnable
     * @return
     */
    public static Future<?> executeRunalle(Runnable runnable) {

        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(FWConst.net_pool_size);
        }
        return executorService.submit(runnable);
    }


    /**
     * 线程池里跑runnable
     */
    public static Future<?> execute(Runnable runnable) {
        return executorService.submit(runnable);
    }

    public static Future<?> execute(final Task task) {
        Future<?> future = execute(new Runnable() {
            @Override
            public void run() {
                try {
                    task.doInBackground();
                } catch (Exception e) {
                    task.transfer(null, Task.TRANSFER_DOERROR);
                    return;
                }
                task.transfer(null, Task.TRANSFER_DOUI);
            }
        });
        return future;
    }

    /**
     * 延时daley秒执行
     *
     * @param task
     * @param daley 毫秒
     */
    public static void execute(final Task task, final long daley) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                execute(task);
            }
        }, daley);
    }


}
