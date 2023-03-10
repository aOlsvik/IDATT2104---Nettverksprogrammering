import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread{

    private final Socket connection;
    public ClientHandler(Socket socket){
        this.connection=socket;

    }

    @Override
    public void run() {
        try {
            InputStreamReader readConnection
                    = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(readConnection);
            PrintWriter printer = new PrintWriter(connection.getOutputStream(), true);


            /* Confirm communication to the client */
            System.out.println("Contact was made with the client!");
            printer.println("Contact was made with the server!");
            printer.println("Do you wanna add or subtract?");
            printer.println("1. Add");
            printer.println("2. Subtract");
            printer.println("3. Exit");

            /* Receive data from client */
            String operation = reader.readLine();
            int selection;
            int firstNumber;
            int secondNumber;

            while (true) {
                selection = Integer.parseInt(operation.trim());
                if(selection == 3){
                    break;
                }

                printer.println("First number:");
                String first = reader.readLine();
                firstNumber = Integer.parseInt(first);

                printer.println("Second number:");
                String second = reader.readLine();
                secondNumber = Integer.parseInt(second);

                if(selection == 1) printer.println(firstNumber + " + "+ secondNumber + " = " + (firstNumber+secondNumber));
                else printer.println(firstNumber + " - "+ secondNumber + " = " + (firstNumber-secondNumber));

                System.out.println("Finished calculation");
                printer.println("Do you want to add or subtract?");
                printer.println("1. Add");
                printer.println("2. Subtract");
                printer.println("3. Exit");
                operation = reader.readLine();
            }

            /* Close connection */
            reader.close();
            printer.close();
            connection.close();
        } catch (IOException ioE){
            ioE.printStackTrace();
        }
    }
}
