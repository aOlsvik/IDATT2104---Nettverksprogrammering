import java.io.*;
import java.net.*;

class Server {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;
        ServerSocket server = new ServerSocket(PORTNR);
        System.out.println("Waiting...");

        while(true){
            Socket connection = server.accept();
            ClientThread clientThread = new ClientThread(connection);
            clientThread.start();
            System.out.println("Accepted a client");
        }
    }
}
