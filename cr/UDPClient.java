package cr;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.LongSummaryStatistics;

public class UDPClient {
    private DatagramSocket socket = null;
    //private DataInputStream in = null;
    private BufferedReader in = null;
    //private DataOutputStream out = null;
    private String inFromServer = "";
    private String outToServer = "";
    private BufferedReader reader = null;
    private int[] randArray = new int[10];
    private int receiveMemes = 0;  
    private static long[] totalCompletionTimes = new long[10];
    private static long[] memeTimes = new long[100];
    private static int completionIndex = 0;
    private static int memeIndex = 0;

    public UDPClient(InetAddress IPAddress, int port) {
        try {
            long startTime = System.nanoTime();
            long totalTime = 0;
            // Setting up connection
            System.out.println("Attempting to connect to " + IPAddress + " on port " + port);
            socket = new DatagramSocket(port);
            System.out.println("Successfully connected to " + IPAddress + " on port " + port);

            reader = new BufferedReader(new InputStreamReader(System.in));

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[65000];

            while (receiveMemes < 10) { 
                long memeStart = System.nanoTime();
                //process request
                int memeNum = getRandomNum();
                System.out.print("Request server for: " + memeNum + "\n");
                outToServer = String.valueOf(memeNum); //send number 1-10 to server
                sendData = outToServer.getBytes();
                //create packet to send out
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port); //TODO port
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
                // timing output
                long memeEnd = System.nanoTime();
                System.out.println("Time to resolve meme request " + memeNum + ": " + (memeEnd - memeStart) + " nanoseconds");
                totalTime += (memeEnd - memeStart);
                memeTimes[memeIndex] = (memeEnd - memeStart);
                memeIndex++;
            }

            socket.close();

            // timing 
            long finishTime = System.nanoTime();
            long timeToComplete = finishTime - startTime;
            totalCompletionTimes[completionIndex] = timeToComplete;
            completionIndex++;
            System.out.println("Average time to resolve one meme request: " + (totalTime / 10) + " nanoseconds");
            System.out.println("Total time to complete: " + timeToComplete + " nanoseconds");
           
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

    public static void getTestStats() {
        LongSummaryStatistics memeStats = Arrays.stream(memeTimes).summaryStatistics();
        LongSummaryStatistics totalStats = Arrays.stream(totalCompletionTimes).summaryStatistics();
        double sdMemes = 0;
        double sdTotals = 0;

        for (long num : memeTimes) {
            sdMemes += Math.pow(num - memeStats.getAverage(), 2);
        }
        sdMemes = Math.sqrt(sdMemes / 100);

        for (long num : totalCompletionTimes) {
            sdTotals = Math.pow(num - totalStats.getAverage(), 2);
        }
        sdTotals = Math.sqrt(sdTotals / 10);
        
        System.out.println();
        System.out.println("Summary statistics for resolving an image across 10 trials:");
        System.out.println("Min: " + memeStats.getMin() + " nanoseconds");
        System.out.println("Max: " + memeStats.getMax() + " nanoseconds");
        System.out.println("Average: " + memeStats.getAverage() + " nanoseconds");
        System.out.println("Standard Deviation: " + sdMemes + " nanoseconds");
        System.out.println();
        System.out.println("Summary statistics for total run times across 10 trials");
        System.out.println("Min: " + totalStats.getMin() + " nanoseconds");
        System.out.println("Max: " + totalStats.getMax() + " nanoseconds");
        System.out.println("Average: " + totalStats.getAverage() + " nanoseconds");
        System.out.println("Standard Deviation: " + sdTotals + " nanoseconds");
    }

    public static void main(String[] args) {
        try {
            String hostname = args[0];
            InetAddress IPAddress = InetAddress.getByName(hostname);
            int port = Integer.valueOf(args[1]);
            //Call client function with given port and hostname arguments to initialize client/client socket
            for (int i = 0; i < 10; i++) {
                UDPClient client = new UDPClient(IPAddress, port);
            }
            getTestStats();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }
}
