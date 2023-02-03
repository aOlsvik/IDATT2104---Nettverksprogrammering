public class WorkerThread extends Thread{
    private Runnable function;

    public WorkerThread(Runnable function){
        this.function=function;
    }

    @Override
    public void run() {
        function.run();
    }

}
