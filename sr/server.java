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
            System.out.println("Got connection address from " + socket.getInetAddress().toString() + " on port " + port);

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
                        case "1":
                            fileName = "sr/memes/meme1.jpg";
                            System.out.println("returning: \"sr/memes/meme1.jpg\" file");
                            requestedJoke = true;
                            break;
                        case "2":
                            fileName = "sr/memes/meme2.jpg";
                            System.out.println("returning: \"sr/memes/meme2.jpg\" file");
                            requestedJoke = true;
                            break;
                        case "3":
                            fileName = "sr/memes/meme3.jpg";
                            System.out.println("returning: \"sr/memes/meme3.jpg\" file");
                            requestedJoke = true;
                            break;
                        case "4":
                            fileName = "sr/memes/meme4.jpg";
                            System.out.println("returning: \"sr/memes/meme4.jpg\" file");
                            requestedJoke = true;
                            break;
                        case "5":
                            fileName = "sr/memes/meme5.jpg";
                            System.out.println("returning: \"sr/memes/meme5.jpg\" file");
                            requestedJoke = true;
                            break;
                        case "6":
                            fileName = "sr/memes/meme6.jpg";
                            System.out.println("returning: \"sr/memes/meme6.jpg\" file");
                            requestedJoke = true;
                            break;
                        case "7":
                            fileName = "sr/memes/meme7.jpg";
                            System.out.println("returning: \"sr/memes/meme7.jpg\" file");
                            requestedJoke = true;
                            break;
                        case "8":
                            fileName = "sr/memes/meme8.jpg";
                            System.out.println("returning: \"sr/memes/meme8.jpg\" file");
                            requestedJoke = true;
                            break;
                        case "9":
                            fileName = "sr/memes/meme9.jpg";
                            System.out.println("returning: \"sr/memes/meme9.jpg\" file");
                            requestedJoke = true;
                            break;
                        case "10":
                            fileName = "sr/memes/meme10.jpg";
                            System.out.println("returning: \"sr/memes/meme10.jpg\" file");
                            requestedJoke = true;
                            break;
                        case "bye":
                            continue;
                        default:
                            out.writeUTF("Error: Please enter Joke + a number 1-3 to receive a joke");
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
            //Call server function with given port argument to initialize server/server sockets
            server s = new server(port);
        } catch (Exception e) {
            System.out.println("Failed to capture command line arguments");
            System.exit(-1);
        }
    }
}