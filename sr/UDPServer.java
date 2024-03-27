package sr;

import java.io.*;
import java.lang.*;
import java.net.*;

public class UDPServer {
    private DatagramSocket socket = null;
    // private ServerSocket serverSocket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String clientInput = "";
    private int sentMemes = 0;

    public UDPServer(int port) {
        try {
            // Setting up connection
            // serverSocket = new ServerSocket(port);
            // System.out.println("Created server on port " + port);

            // socket = serverSocket.accept();
            socket = new DatagramSocket(port);
            // System.out.println("Got connection address from " +
            // socket.getInetAddress().toString() + " on port " + port);

            // Initializing input/output streams
            // in = new DataInputStream(socket.getInputStream());
            // out = new DataOutputStream(socket.getOutputStream());
            // out.writeUTF("Hello!");
            byte[] receiveData = new byte[1024];
            byte[] sendData;
            while (sentMemes < 10) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());

                InetAddress IPAddress = receivePacket.getAddress();
                System.out.println(IPAddress);
                int clientPort = receivePacket.getPort();
                String fileName = "sr/memes/meme" + sentence + ".jpg";
                System.out.println("filename is " + fileName);
                File file = new File(fileName);
                // Print absolute path
                System.out.println("Absolute Path: " + file.getAbsolutePath());

                // Print file name
                System.out.println("File Name: " + file.getName());

                // Print file size in bytes
                System.out.println("File Size: " + file.length() + " bytes");

                // Check if the file exists
                System.out.println("Does the file exist? " + file.exists());

                // Check if the file is readable
                System.out.println("Is the file readable? " + file.canRead());

                // Check if the file is a directory
                System.out.println("Is this a directory? " + file.isDirectory());
                sendData = new byte[(int) (file.length())];
                System.out.println("here");

                try {
                    FileInputStream fileIn = new FileInputStream(file);
                    sendData = fileIn.readAllBytes();
                    fileIn.close(); // Close the FileInputStream
                } catch (FileNotFoundException f) {
                    System.out.println("Cannot find file");
                    System.exit(-1);
                } catch (Exception e) {
                    System.out.println("Failed to read file");
                    System.exit(-1);
                }
                DatagramPacket sendPacket = new DatagramPacket(sendData, (int) file.length(), IPAddress, clientPort);
                socket.send(sendPacket);
                sentMemes++;
            }
           
        } catch (Exception e) {
            System.out.println("Error listening on port " + port);
            System.exit(-1);
        } finally {
            // Ensure the socket is closed
            if (socket != null) {
                socket.close();
            }
        }
    }

    public static void main(String[] args) {
        try {
            int port = Integer.valueOf(args[0]);
            // Call server function with given port argument to initialize server/server
            // sockets
            UDPServer s = new UDPServer(port);
        } catch (Exception e) {
            System.out.println("Failed to capture command line arguments");
            System.exit(-1);
        }
    }
}