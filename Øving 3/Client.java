/*
 * SocketKlient.java  - "Programmering i Java", 4.utgave - 2009-07-01
 *
 * Programmet kontakter et tjenerprogram som allerede kjører på port 1250.
 * Linjer med tekst sendes til tjenerprogrammet. Det er laget slik at
 * det sender disse tekstene tilbake.
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {
    public static void main(String[] args) throws IOException {
        final int PORTNUMBER = 1250;

        /* Scanner to read from commandline */
        Scanner readFromCommandLine = new Scanner(System.in);
        System.out.print("Please provide the name of the machine where the Server is running: ");
        String serverMachine = readFromCommandLine.nextLine();

        /* Sets up the connection to the server */
        Socket connection = new Socket(serverMachine, PORTNUMBER);
        System.out.println("Connection was made");

        /* Opens a connection for communication with the server */
        InputStreamReader readConnection
                = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

        /* Reads the communication confirmation and selecting operator */
        String confirmation = reader.readLine();
        System.out.println(confirmation);

        String selection1 = reader.readLine() +"\n";
        String selection2 = reader.readLine() +"\n";
        String selection3 = reader.readLine()+"\n";
        String selection4 = reader.readLine();
        System.out.println(selection1+selection2+selection3+ selection4);

        /* Reads from used / command line */
        String selection = readFromCommandLine.nextLine();
        String num1, num2;
        writer.println(selection);
        while (!selection.equals("3")) {
            String number1 = reader.readLine();
            System.out.println(number1);
            num1 = readFromCommandLine.nextLine();
            writer.println(num1);

            String number2 = reader.readLine();
            System.out.println(number2);
            num2 = readFromCommandLine.nextLine();
            writer.println(num2);

            String result = reader.readLine();
            System.out.println(result);

            selection1 = reader.readLine()+"\n";
            selection2 = reader.readLine()+"\n";
            selection3 = reader.readLine() + "\n";
            selection4 = reader.readLine();
            System.out.println(selection1+selection2+selection3+ selection4);
            selection = readFromCommandLine.nextLine();
            writer.println(selection);
        }

        /* Closes the connection */
        reader.close();
        writer.close();
        connection.close();
    }
}
