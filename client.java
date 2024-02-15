import java.io.*;
import java.net.*;

public class client {
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String inFromServer = "";
    private String outToServer = "";
    private BufferedReader reader = null;

    public client(String hostname, int port) {
        try {
            socket = new Socket(hostname, port);
            System.out.println("Connected to " + hostname + " on port " + port);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(System.in));

            while (!outToServer.equals("bye")) {
                try {
                    inFromServer = in.readUTF();
                    System.out.println(inFromServer);
                    outToServer = reader.readLine();
                    out.writeUTF(outToServer);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }

            socket.close();
            out.close();
            in.close();
        } 
        catch (UnknownHostException e) {
            System.out.println(e);
            System.exit(-1);
        }
        catch (IOException e) {
            System.out.println(e);
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        try {
            String hostname = args[0];
            int port = Integer.valueOf(args[1]);
            client client = new client(hostname, port);
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }
}
