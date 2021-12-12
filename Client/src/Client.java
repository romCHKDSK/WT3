public class Client {

    public static String ipAddr = "localhost";
    public static int port = 4004;

    public static void main(String[] args) {
        new ClientThread(ipAddr, port);
    }
}
