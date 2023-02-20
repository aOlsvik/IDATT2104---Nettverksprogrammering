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
                newMessage = calculate(received);
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
    private String calculate(String calculation){
        String[] elements = new String[3];
        StringBuilder s=new StringBuilder();
        for(char c : calculation.trim().toCharArray()){
            if(Character.isDigit(c)){
                s.append(c);
            } else if (c == '/' || c == '*' || c == '+' || c == '-'){
                elements[0] = s.toString();
                s = new StringBuilder();
                elements[1] = String.valueOf(c);
            }
            else{
                return "Invalid calculation. " +
                        "Please enter a calculation on the form:" +
                        "1+1";
            }
        }
        elements[2] = s.toString();
        int answer = 0;
        String operator="";
        if(elements[1].equals("+")){
            operator="+";
            answer = Integer.parseInt(elements[0]) + Integer.parseInt(elements[2]);
        }else if(elements[1].equals("-")){
            operator="-";
            answer = Integer.parseInt(elements[0]) - Integer.parseInt(elements[2]);
        }else if(elements[1].equals("*")){
            operator="*";
            answer = Integer.parseInt(elements[0]) * Integer.parseInt(elements[2]);
        }else if(elements[1].equals("/")){
            operator="/";
            if(elements[2].equals("0")) return "Cannot divide by zero";
            answer = Integer.parseInt(elements[0]) / Integer.parseInt(elements[2]);
        }
        return elements[0] + operator + elements[2] + "=" + answer;
    }
}
