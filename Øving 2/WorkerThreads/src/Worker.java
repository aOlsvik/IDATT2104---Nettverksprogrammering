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
            threads.add(new WorkerThread(()->{
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
                            task = tasks.get(0); // Copy task for later use
                            tasks.remove(task); // Remove task from list
                        }
                        lock.unlock();
                    }
                    if (task!=null){
                        post_timeout(task, 1000); // Run task outside of mutex lock
                    }
                    else if(tasks.isEmpty() && running){
                        stopRunning();
                    }
                }
            }));
        }
        running = true;
        for(Thread t : threads){
            t.start();
        }
    }

    void stopRunning(){
        lock.lock();
        running=false;
        condition.signalAll();
        lock.unlock();
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
        workerThreads.post_tasks();
        try {
            Thread.sleep(3000);
        } catch (Exception e){
            e.printStackTrace();
        }
        workerThreads.post_tasks();
        workerThreads.post_tasks();

        //Worker eventLoop = new Worker(1);
        //eventLoop.start();
        //eventLoop.post_tasks();
        //eventLoop.runTasksInWorkerThreads();


    }
}