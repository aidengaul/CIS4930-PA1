import java.io.*;
import java.net.*;

public class server
{
    ServerSocket ss;
    Socket s;
    DataInputStream dis;
    DataOutputStream dos;
    public server(int port) {
        try {
            System.out.println("Server Started");
            ss = new ServerSocket(port);
            s = ss.accept();
            System.out.println(s);
            String clientAddress = s.getInetAddress().getHostAddress();
            System.out.println("Got connection request from "+ clientAddress);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            ServerChat();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public static void main (String as[]) {
        // Check if port number is provided as command-line argument
        if(as.length != 1) {
            System.out.println("Usage: java Server <port>");
            return;
        }
        
        // Parse port number from command-line argument
        int port = Integer.parseInt(as[0]);
        
        // Create server object with the provided port number
        new server(port);
    }

    public void ServerChat() throws IOException
    {
         String str, s1;
         do
         {
             str=dis.readUTF();
             System.out.println("Client Message:"+str);
             BufferedReader br=new BufferedReader(new   InputStreamReader(System.in));
             s1=br.readLine();
             dos.writeUTF(s1);
             dos.flush();
         }
         while(!s1.equals("bye"));
    }
}