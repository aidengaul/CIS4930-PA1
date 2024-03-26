package sr;
import java.io.*;
import java.lang.*;
import java.net.*;

public class server {
    private DatagramSocket socket = null;
    //private ServerSocket serverSocket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String clientInput = "";

    public server(int port) {
        try {
            // Setting up connection
            //serverSocket = new ServerSocket(port);
            //System.out.println("Created server on port " + port);

            //socket = serverSocket.accept();
            socket = new DatagramSocket(port);
            //System.out.println("Got connection address from " + socket.getInetAddress().toString() + " on port " + port);

            // Initializing input/output streams
            //in = new DataInputStream(socket.getInputStream());
            //out = new DataOutputStream(socket.getOutputStream());
            //out.writeUTF("Hello!");
            byte[] receiveData = new byte[1024];
            byte[] sendData;

            int i = 3;
            while(true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String sentence = new String(receivePacket.getData());
                InetAddress IPAddress = receivePacket.getAddress();
                System.out.println(IPAddress);
                int clientPort = receivePacket.getPort();
                String fileName = "sr/memes/meme" + sentence + ".jpg"; 
                System.out.println(fileName);
                File file = new File(fileName);
                sendData = new byte[(int) (file.length())];

                try {
                    FileInputStream fileIn = new FileInputStream(file);
                    sendData = fileIn.readAllBytes();
                }
                catch (FileNotFoundException f) {
                    System.out.println("Cannot find file");
                    System.exit(-1);
                }
                catch (Exception e) {
                    System.out.println("Failed to read file");
                    System.exit(-1);
                }

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, clientPort);
                socket.send(sendPacket);
                i--;
            }
            //socket.close();
            /*
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
            */
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