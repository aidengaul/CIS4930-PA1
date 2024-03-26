package cr;
import java.io.*;
import java.net.*;

public class client {
    private DatagramSocket socket = null;
    //private DataInputStream in = null;
    private BufferedReader in = null;
    //private DataOutputStream out = null;
    private String inFromServer = "";
    private String outToServer = "";
    private BufferedReader reader = null;

    public client(InetAddress IPAddress, int port) {
        try {
            // Setting up connection
            System.out.println("Attempting to connect to " + IPAddress + " on port " + port);
            socket = new DatagramSocket(port);
            System.out.println("Successfully connected to " + IPAddress + " on port " + port);

            // Initializing input/output streams
            //in = new DataInputStream(socket.getInputStream());
            //out = new DataOutputStream(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(System.in));

            //Server sends "Hello!" to ensure connection is established before client can send a message
            //inFromServer = in.readUTF();
            //System.out.println("Received from server: " + inFromServer);

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

            String sentence = reader.readLine();
            sendData = sentence.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            socket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String responseFromServer = new String(receivePacket.getData());

            System.out.println("from server " + responseFromServer);
            socket.close();
            /* 
            while (!outToServer.equals("bye")) {
                try {
                    System.out.print("Request server for: ");
                    outToServer = reader.readLine();
                    out.writeUTF(outToServer);
                    out.flush();

                    inFromServer = in.readUTF();
                    System.out.println("Received server response: " + inFromServer);
                    
                    //Write joke file received from server to cr directory
                    if (inFromServer.contains("Sending file")) {
                        byte[] fileBytes = new byte[1024];
                        FileOutputStream fileOut = new FileOutputStream("./cr/" + inFromServer.substring(inFromServer.length() - 9 , inFromServer.length()));
                        BufferedOutputStream fileWriter = new BufferedOutputStream(fileOut);
                        int totalBytes = in.read(fileBytes, 0, fileBytes.length);
                        fileWriter.write(fileBytes, 0, totalBytes);
                        fileWriter.close();
                        fileOut.close();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            // Close connection
            System.out.println("Exiting");
            out.close();
            in.close();
            socket.close();
            */
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        try {
            String hostname = args[0];
            InetAddress IPAddress = InetAddress.getByName(hostname);
            int port = Integer.valueOf(args[1]);
            //Call client function with given port and hostname arguments to initialize client/client socket
            client client = new client(IPAddress, port);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }
}
