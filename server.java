import java.io.*;
import java.net.*;

public class server {
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String clientInput = "";


    public server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Created server on port" + port);

            socket = serverSocket.accept();
            System.out.println("Client accepted on port" + port);

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Hello!");

            while (!clientInput.equals("bye")) {
                try {
                    clientInput = in.readUTF();
                } catch (IOException e) {
                    System.out.println("Error reading input from client on port" 
                        + port + ": " + e);
                }
            }

            System.out.println("Received kill signal from client -- closing connection on port " + port);
            socket.close();
            in.close();
            out.close();
        } 
        catch (IOException e) {
            System.out.println("Error listening on port " + port);
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        try {
            int port = Integer.valueOf(args[0]);
            server s = new server(port);
        } catch (Error e) {
            System.out.println("Failed to capture command line arguments");
            System.exit(-1);
        }
    }
}