package task1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean running = true;
        UDP_Server server = new UDP_Server();
        server.start();
        UDP_Client client = new UDP_Client();
        Scanner sc = new Scanner(System.in);
        System.out.println("Connected to the server");
        System.out.println("Enter a calculation on the form: 1+1");
        System.out.println("Enter 'end' to exit" );
        while(running){
            String calculation = sc.nextLine();
            if(calculation.trim().equals("end")){
                running=false;
            }
            String s = client.sendEcho(calculation);
            System.out.println(s);
            if(!s.equals("ended program")){
                System.out.println("Enter a new calculation");
            }
        }
        client.close();
    }
}
