package util;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    //测试代码，无用

    /**
     * 原子性的操作：java.util.concurrent.atomic包下的类
     * boolean is = true;
     * is = !is;
     */
    private static final AtomicInteger COUNT = new AtomicInteger();

    /**
     * 闭锁：调用await()的线程阻塞等待（一个线程），直到计数器=0
     * latch.countDown()计数减一
     * latch.await()阻塞等待直到计数器=0，然后继续往下执行
     */
    private static final CountDownLatch LATCH = new CountDownLatch(3);

    /**
     * 栅栏：所有线程一起等待，直到计数器=0
     * await():代表所有线程都阻塞等待，计数器-1，直到计数器=0，然后
     * 所有线程往下执行
     */
    private static final CyclicBarrier BARRIER
            = new CyclicBarrier(4);
    /**
     * 信号量：
     * 计数器：初始值
     * 计数器增加：
     * semaphore.release();+1
     * semaphore.release(num);+num
     * 申请计数器一定数量的资源，申请不到就阻塞等待
     * semaphore.acquire();申请的资源数-1
     * semaphore.acquire(num);申请的资源数-num
     */
    private static final Semaphore SEMAPHORE
            = new Semaphore(0);
//    private static final CyclicBarrier BARRIER
////            = new CyclicBarrier(0);

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        for(int i=0; i<3; i++){
            final int j = i;
//            new Thread(new Task(LATCH, String.valueOf(j))).start();
//            new Thread(new Task(BARRIER, String.valueOf(j))).start();
            new Thread(new Task(SEMAPHORE, String.valueOf(j))).start();
        }
//        LATCH.await();
//        BARRIER.await();
        SEMAPHORE.acquire(3);
        System.out.println("执行完毕");
    }

    private static class Task implements Runnable{
        private CountDownLatch cdl;
        private CyclicBarrier barrier;
        private Semaphore semaphore;
        private String message;

        public Task(CountDownLatch cdl, String message) {
            this.cdl = cdl;
            this.message = message;
        }

        public Task(CyclicBarrier barrier, String message) {
            this.barrier = barrier;
            this.message = message;
        }

        public Task(Semaphore semaphore, String message) {
            this.semaphore = semaphore;
            this.message = message;
        }

        @Override
        public void run() {
            System.out.println(message);
            // CountDownLatch计数器减一
//            cdl.countDown();
            // CyclicBarrier
//            try {
//                barrier.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (BrokenBarrierException e) {
//                e.printStackTrace();
//            }
            // Semaphore
            semaphore.release();
        }
    }

    public static void main1(String[] args) throws InterruptedException {
        for(int i=0; i<10; i++){
            COUNT.incrementAndGet();
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(j+"====="+COUNT);
                    if(j == 5){
                        throw new RuntimeException();
                    }
                    if(COUNT.decrementAndGet() == 0){
                        //通知
                    }

                }
            }).start();
        }
        // 等待
        System.out.println("执行完毕："+COUNT);
    }

}
