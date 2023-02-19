package task1;

public class Main {
    public static void main(String[] args) {
        UDP_Server server = new UDP_Server();
        server.start();
        UDP_Client client = new UDP_Client();
        System.out.println(client.sendEcho("Hello"));
        System.out.println(client.sendEcho("SIU"));
        System.out.println(client.sendEcho("end"));
        client.close();
    }
}
