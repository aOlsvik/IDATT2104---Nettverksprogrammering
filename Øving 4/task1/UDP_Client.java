package task1;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class UDP_Client {

    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public UDP_Client() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendEcho(String msg) {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 4445);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buf=new byte[1024];
        packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] responseBytes = Arrays.copyOf(packet.getData(), packet.getLength());
        String received = new String(responseBytes);

        return received;
    }

    public void close() {
        socket.close();
    }
}
