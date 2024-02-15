import java.io.*;
import java.net.*;

public class client
{
    Socket s;
    DataInputStream din;
    DataOutputStream dout;
    public client(String serverAddress, int port) {
        try {
            s = new Socket(serverAddress, port);
            System.out.println(s);
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            clientChat();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void clientChat() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s1;
        do {
            s1 = br.readLine();
            dout.writeUTF(s1);
            dout.flush();
            System.out.println("Server Message:" + din.readUTF());
        } while (!s1.equals("stop"));
    }

    public static void main(String as[]) {
        // Check if both server IP address and port number are provided as command-line arguments
        if(as.length != 2) {
            System.out.println("Usage: java Client <serverAddress> <port>");
            return;
        }

        // Parse server IP address and port number from command-line arguments
        String serverAddress = as[0];
        int port = Integer.parseInt(as[1]);

        // Create client object with the provided server IP address and port number
        new client(serverAddress, port);
    }
}
    
