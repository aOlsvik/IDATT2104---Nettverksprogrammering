import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Prime {
    static List<Integer> primeNumbers;
    static int totalThreads;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Enter the interval where you want to check for prime numbers:");
            System.out.println("Starting number:");
            String start = sc.nextLine();
            System.out.println("Last number:");
            String end = sc.nextLine();
            System.out.println("How many threads should be used to find the prime numbers?");
            String threads = sc.nextLine();
            try{
                Integer.parseInt(start);
                Integer.parseInt(end);
                Integer.parseInt(threads);
            } catch (NumberFormatException nfe){
                System.out.println("Please enter numbers as values");
                System.out.println("Type anything to continue, press enter to quit");
                String continues = sc.nextLine();
                if(continues.isBlank()){
                    break;
                }
            }
            int startNumber = Integer.parseInt(start);
            int endNumber = Integer.parseInt(end);
            int numberOfThreads = Integer.parseInt(threads);
            totalThreads = numberOfThreads;
            if(startNumber<0 || endNumber<0 || numberOfThreads<=0) {
                System.out.println("Please enter positive numbers. Number of threads must be greater than 0");
                System.out.println("Press enter to continue, press anything else to quit");
                String continues = sc.nextLine();
                if (!continues.isBlank()) {
                    break;
                }
                continue;
            }
            if(endNumber<startNumber){
                System.out.println("The start of the interval must be lower than the end of it");
                System.out.println("Press enter to continue, press anything else to quit");
                String continues = sc.nextLine();
                if (!continues.isBlank()) {
                    break;
                }
                continue;
            }
            if((endNumber-startNumber) < numberOfThreads) {
                System.out.println("Number of threads must be fewer than the length of the interval");
                System.out.println("Press enter to continue, press anything else to quit");
                String continues = sc.nextLine();
                if (!continues.isBlank()) {
                    break;
                }
                continue;
            }
            PrimeThread[] primeThreads = new PrimeThread[numberOfThreads];
            primeNumbers  = Collections.synchronizedList(new ArrayList<>());
            for (int i = 0; i < numberOfThreads; i++) {
                PrimeThread p = new PrimeThread(startNumber, endNumber, i);
                primeThreads[i] = p;
                p.start();
            }

            for (PrimeThread primeThread : primeThreads) {
                try {
                    primeThread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Collections.sort(primeNumbers);
            System.out.println("The prime numbers between " + startNumber + " and " + endNumber + " are:");
            System.out.println(primeNumbers);
            System.out.println("Total of " + primeNumbers.size() + " prime numbers");
            System.out.println("Press enter to continue, press anything else to quit");
            String continues = sc.nextLine();
            if (!continues.isBlank()) {
                break;
            }
        }
    }

}