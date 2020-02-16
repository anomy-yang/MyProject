package task;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class FileScanTask {
    private final ExecutorService pool =
            //new ThreadPoolExecutor();
            Executors.newFixedThreadPool(4);

    //private static volatile int COUNT;//统计现行的线程数
    private final AtomicInteger count = new AtomicInteger();

    private final CountDownLatch latch = new CountDownLatch(1);

    private FileScanCallback callback;
    public FileScanTask(FileScanCallback callback){
        this.callback = callback;
    }
    /**
     * 启动根目录的扫描任务
     * @param root
     */
    public void startScan(File root) {

       /* synchronized (this) {
            COUNT++;
        }*/
       count.incrementAndGet();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                list(root);
            }
        });
    }

    /**
     * 等待所有任务执行完毕
     */
    public void waitFinish() throws InterruptedException {
      /*  synchronized (this){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
      //等待
        try {
            latch.await();
        }finally {

            pool.shutdown();//调用每个线程的interrupt()中断
        }//POOL.shutdownNow();//调用stop()方法直接关闭
    }


    public void list(File dir) {
        if (!Thread.interrupted()) {
            try {
                callback.execute(dir);
                if (dir.isDirectory()) {
                    File[] children = dir.listFiles();
                    if (children != null && children.length > 0) {
                        for (File child : children) {
                            //启动子线程执行子文件夹的扫描
                            if (child.isDirectory()) {
                            /*synchronized (this) {
                                COUNT++;
                            }*/
                                count.incrementAndGet();
                                pool.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        list(child);
                                    }
                                });
                            } else {
                                callback.execute(child);
                            }
                        }
                    }
                }
            } finally {
           /* synchronized (this) {
                COUNT--;
                if (COUNT == 0) {
                    //所有线程执行结束
                    this.notifyAll();
                }
            }*/
                //所有线程执行完毕
                if (count.decrementAndGet() == 0) {
                    //通知
                    latch.countDown();
                }
            }
        }
    }
}
