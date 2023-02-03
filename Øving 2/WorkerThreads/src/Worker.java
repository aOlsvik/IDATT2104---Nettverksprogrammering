import java.util.ArrayList;
import java.util.List;



public class Worker extends Thread{
    private final List<Runnable> tasks;
    private final List<Thread> threads;
    private final int nrOfThreads;
    private boolean running = true;

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
                        synchronized (tasks){
                            if(!tasks.isEmpty()){
                                task = tasks.get(0); // Copy task for later use
                                tasks.remove(task); // Remove task from list
                            }
                        }
                    }
                    if (task!=null){
                        post_timeout(task, 1000); // Run task outside of mutex lock
                    }

                    else{
                        if(tasks.isEmpty()){
                            stopRunning();
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }));
        }
    }

    void stopRunning(){
        synchronized (this){
            running = false;
            notifyAll();
        }
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
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            synchronized (tasks){
                post(() -> System.out.println("Task " + finalI +
                        " runs in thread " +
                        currentThread().getId()));
            }
        }
    }

    void post(Runnable task){
        synchronized (tasks){
            tasks.add(task);
        }
    }

    void runTasksInWorkerThreads(){
        for (Thread thread : threads) {
            thread.start();
        }
    }


    public static void main(String[] args) {

        Worker workerThreads = new Worker(4);
        workerThreads.start();
        workerThreads.post_tasks();
        workerThreads.runTasksInWorkerThreads();



    }
}