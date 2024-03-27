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
    private int[] randArray = new int[10];
    private int receiveMemes = 0;  

    public client(InetAddress IPAddress, int port) {
        try {
            // Setting up connection
            System.out.println("Attempting to connect to " + IPAddress + " on port " + port);
            socket = new DatagramSocket(port);
            System.out.println("Successfully connected to " + IPAddress + " on port " + port);

            reader = new BufferedReader(new InputStreamReader(System.in));

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[65000];

            while (receiveMemes < 10) { 
                //process request
                int memeNum = getRandomNum();
                System.out.print("Request server for: " + memeNum + "\n");
                outToServer = String.valueOf(memeNum); //send number 1-10 to server
                sendData = outToServer.getBytes();
                //create packet to send out
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5566); //TODO port
                socket.send(sendPacket);
                //receive packet
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                System.out.println("Received file from server");
                //process file from server
                byte[] responseFromServer = receivePacket.getData();
                // byte[] fileBytes = new byte[1024];
                FileOutputStream fileOut = new FileOutputStream("./cr/memes/meme" + memeNum + ".jpg");
                BufferedOutputStream fileWriter = new BufferedOutputStream(fileOut);
                //int totalBytes = response.read(fileBytes, 0, responseFromServer.length);
                fileWriter.write(responseFromServer, 0, responseFromServer.length);
                fileWriter.close();
                fileOut.close();
            }

            socket.close();
           
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }finally {
            // Ensure the socket is closed
            if (socket != null) {
                socket.close();
            }
        }
    }

    public int getRandomNum() {
        int min = 0;
        int max = 10;
        int num = min + (int)(Math.random() * (max - min)+1);

        while (randArray[num - 1] == 1) {
            num = min + (int)(Math.random() * (max - min)+1);
        }
        randArray[num - 1] = 1;

        receiveMemes++;
        return num;
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
