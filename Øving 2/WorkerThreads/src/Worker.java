import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Worker extends Thread{
    private final List<Runnable> tasks;
    private final List<Thread> threads;
    private final int nrOfThreads;
    private boolean running = false;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public Worker(int threads){
        if (threads != 1) {
            System.out.println("Worker threads");
        } else {
            System.out.println("Event loop");
        }
        this.nrOfThreads = threads;
        this.threads = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    @Override
    public void run() {
        for (int i = 0; i < nrOfThreads; i++) {
            threads.add(new Thread(()->{
                while(running){
                    Runnable task = null;
                    {
                        lock.lock();
                        long startTime = System.currentTimeMillis();
                        while (tasks.isEmpty()) {
                            try {
                                condition.await(1, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            if(System.currentTimeMillis()-startTime > 5_000) break;
                        }
                        if(!tasks.isEmpty()){
                            task = tasks.get(0);
                            tasks.remove(task);
                        }
                        lock.unlock();
                    }
                    if (task!=null){
                        set_Timeout(task,1000);
                        //post_timeout(task, 1000);
                    }
                    else if(tasks.isEmpty()){
                        lock.lock();
                        if(running) stopRunning();
                        lock.unlock();
                    }
                }
            }));
        }
        running = true;
        for(Thread t : threads){
            t.start();
        }
        for (Thread t : threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void stopRunning(){

        running=false;
        condition.signalAll();
        if(this.nrOfThreads > 1) System.out.println("Shutting down worker threads");
        else System.out.println("Shutting down event loop");

    }

    void set_Timeout(Runnable task, long timeout){
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task.run();
    }

    void post_timeout(Runnable task, long timeout) {
        new Thread(() -> {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.run();
        }).start();
    }

    void post_tasks() {
        lock.lock();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            post(() -> System.out.println("Task " + finalI +
                    " runs in thread " +
                    currentThread().getId()));

        }
        running = true;
        condition.signal();
        lock.unlock();
    }

    void post(Runnable task){
        lock.lock();
        tasks.add(task);
        running = true;
        condition.signal();
        lock.unlock();
    }



    public static void main(String[] args) {
        Worker workerThreads = new Worker(4);
        workerThreads.start();
        Worker eventLoop = new Worker(1);
        eventLoop.start();

        //workerThreads.post_tasks();
        //eventLoop.post_tasks();

        workerThreads.post(()-> {
            System.out.println("Task A worker");
        });
        workerThreads.post(()-> {
            System.out.println("Task B worker");
        });
        
        
        eventLoop.post(()-> {
            System.out.println("Task A event");
        });
        eventLoop.post(()-> {
            System.out.println("Task B event");
        });

        try {
            workerThreads.join();
        } catch (Exception e){
            e.printStackTrace();
        }



        try {
            eventLoop.join();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}