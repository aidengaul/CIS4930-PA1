package cr;
import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.Instant;

public class TCPClient {
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String inFromServer = "";
    private String outToServer = "";
    private BufferedReader reader = null;
    private int[] randArray = new int[10];
    private int receiveMemes = 0;
    private long[] retreiveTime1Array = new long[100];
    private int retreiveTime1ArrayIndex = 0;
    private long[] setupTime2Array = new long[10];
    private int setupTime2ArrayIndex = 0;

    public TCPClient(String hostname, int port) {
        try {
            // Setting up connection
            System.out.println("Attempting to connect to " + hostname + " on port " + port);
            Instant startSetup = Instant.now();
            socket = new Socket(hostname, port);
            Instant endSetup = Instant.now();
            Duration timeElapsedSetup = Duration.between(startSetup, endSetup);
            long timeElapsedNanosSetup = timeElapsedSetup.toNanos();
            setupTime2Array[setupTime2ArrayIndex] = timeElapsedNanosSetup;
            setupTime2ArrayIndex++;
            System.out.println(timeElapsedNanosSetup + " x 10^(-6) milliseconds"); 
            System.out.println("Successfully connected to " + hostname + " on port " + port);

            // Initializing input/output streams
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(System.in));

            //Server sends "Hello!" to ensure connection is established before client can send a message
            inFromServer = in.readUTF();
            System.out.println("Received from server: " + inFromServer);
            /* 
            Instant start = Instant.now();
            for (int i = 0; i < 5; i++) {
                System.out.println("sleeping"); 
            }  
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            long timeElapsedMillis = timeElapsed.toNanos();
            System.out.println(timeElapsedMillis + " x 10^(-6) milliseconds"); 
            */
            while(receiveMemes < 10) {
                try {
                    Instant start = Instant.now();
                    int memeNum = getRandomNum();
                    System.out.print("Request server for: " + memeNum + "\n");
                    //System.out.print("i: " + i + "\n");
                    outToServer = String.valueOf(memeNum); //send number 1-10 to server
                    out.writeUTF(outToServer);
                    out.flush();

                    inFromServer = in.readUTF();
                    System.out.println("Received server response: " + inFromServer);
                    
                    //Write joke file received from server to cr directory
                    if (inFromServer.contains("Sending file")) {
                        byte[] fileBytes = new byte[65000];
                        String fileName = "./cr/meme" + String.valueOf(memeNum) + ".jpg";
                        //FileOutputStream fileOut = new FileOutputStream(fileName, fileName.length());
                        //FileOutputStream fileOut = new FileOutputStream("./cr/meme" + inFromServer.substring(inFromServer.length() - 9 , inFromServer.length()));
                        FileOutputStream fileOut = new FileOutputStream(fileName);
                        BufferedOutputStream fileWriter = new BufferedOutputStream(fileOut);
                        int totalBytes = in.read(fileBytes, 0, fileBytes.length);
                        fileWriter.write(fileBytes, 0, totalBytes);
                        fileWriter.close();
                        fileOut.close();
                        
                        Instant end = Instant.now();
                        Duration timeElapsed = Duration.between(start, end);
                        long timeElapsedNanos = timeElapsed.toNanos();
                        System.out.println(timeElapsedNanos + " x 10^(-6) milliseconds \n");
                        retreiveTime1Array[retreiveTime1ArrayIndex] = timeElapsedNanos;
                        retreiveTime1ArrayIndex++;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            out.writeUTF("bye");
            out.flush();
            for (int i = 0; i < retreiveTime1Array.length; i++) {
                System.out.println((i + 1) + " " + retreiveTime1Array[i]);
            }

            // Close connection
            System.out.println("Exiting");
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
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
            int port = Integer.valueOf(args[1]);
            //Call client function with given port and hostname arguments to initialize client/client socket
            TCPClient client = new TCPClient(hostname, port);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }
}
