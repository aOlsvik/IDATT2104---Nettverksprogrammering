package task1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class UDP_Server extends Thread{
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public UDP_Server() {
        try {
            socket = new DatagramSocket(4445);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        running = true;

        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String received
                    = new String(packet.getData(), 0, packet.getLength());
            String newMessage;
            if (received.equals("end")) {
                running = false;
                newMessage = "ended program";
            }else{
                newMessage = received +" from server";
            }

            byte[] array =newMessage.getBytes();
            packet.setData(array);
            try {
                socket.send(new DatagramPacket(packet.getData(), packet.getLength(),packet.getAddress(), packet.getPort()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        socket.close();
    }
}
