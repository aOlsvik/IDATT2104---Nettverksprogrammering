import java.io.*;
import java.net.*;

public class SimpleWebServer {
    public static void main(String[] args) throws IOException {
        final int PORT_NUMBER = 8080;
        ServerSocket server = new ServerSocket(PORT_NUMBER);
        System.out.println("Server started on port " + PORT_NUMBER);

        while (true) {
            Socket socket = server.accept();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html; charset=utf-8");
            out.println();

            out.println("<HTML><BODY>");
            out.println("<H1>Greetings. You have connected to my simple web server</H1>");
            out.println("Header from client:");
            out.println("<UL>");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null && !line.equals("")) {
                out.println("<LI>" + line + "</LI>");
            }
            out.println("</UL>");
            out.println("</BODY></HTML>");
            socket.close();
        }
    }
}