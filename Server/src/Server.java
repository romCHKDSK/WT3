import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;


public class Server {

    public static final int PORT = 4004;
    private static List<ServerThread> serverThreads = new LinkedList<>();


    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server Started");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverThreads.add(new ServerThread(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            System.out.println("Server Closed");
            server.close();
        }
    }
}


