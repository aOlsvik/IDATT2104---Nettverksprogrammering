public class PrimeThread extends Thread {
    private final int start;
    private final int end;
    private final int threadNr;

    public PrimeThread(int start, int end, int threadNr){
        this.start = start;
        this.end = end;
        this.threadNr = threadNr;
    }
    @Override
    public void run() {
        findPrime();
    }

    private void findPrime() {
        for(int i =start+threadNr; i<=end; i+=Prime.totalThreads){
            if(isPrime(i)){
                Prime.primeNumbers.add(i);
            }
        }
    }

    private boolean isPrime(int number){
        if(number==0 || number==1) return false;
        for(int i = 2; i<number/2+1; i++){
            if(number%i == 0) return false;
        }
        return true;
    }
}