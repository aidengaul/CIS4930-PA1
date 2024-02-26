package sr;
import java.io.*;
import java.lang.*;
import java.net.*;

public class server {
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String clientInput = "";

    public server(int port) {
        try {
            // Setting up connection
            serverSocket = new ServerSocket(port);
            System.out.println("Created server on port " + port);

            socket = serverSocket.accept();
            System.out.println("Client accepted with address " + socket.getInetAddress().toString() + " on port " + port);

            // Initializing input/output streams
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Hello!");
            
            boolean requestedJoke = false;
            String fileName = "";

            while (!clientInput.equals("bye")) {
                try {
                    clientInput = in.readUTF();
                    System.out.println("Client requested: " + clientInput);

                    // Switch statement on input string
                    switch (clientInput) {
                        case "Joke 1":
                            fileName = "sr/joke1.txt";
                            System.out.println("returning: \"joke1.txt\" file");
                            requestedJoke = true;
                            break;
                        case "Joke 2":
                            fileName = "sr/joke2.txt";
                            System.out.println("returning: \"joke2.txt\" file");
                            requestedJoke = true;
                            break;
                        case "Joke 3":
                            fileName = "sr/joke3.txt";
                            System.out.println("returning: \"joke3.txt\" file");
                            requestedJoke = true;
                            break;
                        case "bye":
                            continue;
                        default:
                            out.writeUTF("You are an idiot. Ask for a joke.");
                            break;
                    }

                    // Send desired file to client
                    if (requestedJoke == true) {
                        out.writeUTF("Sending file " + fileName.substring(3, fileName.length()));
                        File jokeFile = new File(fileName);
                        byte[] fileBytes = new byte[(int) jokeFile.length()];
                        FileInputStream fileIn = new FileInputStream(jokeFile);
                        BufferedInputStream fileReader = new BufferedInputStream(fileIn);
                        fileReader.read(fileBytes, 0, fileBytes.length);

                        out.write(fileBytes, 0, fileBytes.length);
                        fileIn.close();
                        fileReader.close();

                        fileName = "";
                        requestedJoke = false;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            // Close connection
            System.out.println("Received disconnect signal from client address " + socket.getInetAddress().toString() + " on port " + port);
            System.out.println("Exiting");
            out.writeUTF("Disconnected");
            in.close();
            out.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("Error listening on port " + port);
            System.exit(-1);
        }
    }
    public static void main(String[] args) {
        try {
            int port = Integer.valueOf(args[0]);
            server s = new server(port);
        } catch (Exception e) {
            System.out.println("Failed to capture command line arguments");
            System.exit(-1);
        }
    }
}