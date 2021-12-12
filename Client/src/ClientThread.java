import java.io.*;
import java.net.Socket;

public class ClientThread {

    private Socket socket;
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток чтения в сокет
    private BufferedReader inputUser; // поток чтения с консоли
    private String addr; // ip адрес клиента
    private int port; // порт соединения
    private String login; // имя клиента
    private String password; // пароль клиента

    public ClientThread(String addr, int port) {
        this.addr = addr;
        this.port = port;
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            new ClientThread.ReadMsg().start();
            this.authorization();
        } catch (IOException e) {
            this.downService();
        } catch (NullPointerException e){
            System.out.println("Start server please");
        }
    }

    private void authorization() {
        try {
            System.out.print("Enter your login (or stop to exit): ");
            login = inputUser.readLine();
            if (login.equals("stop")){
                this.downService();
                return;
            }
            System.out.print("Enter your password: ");
            password = inputUser.readLine();
            send("authorization");
            send(login);
            send(password);
        } catch (IOException ignored) {}
    }

    private void menu(){
        String userCommand;
        while (true) {
            System.out.println("Input command (read, readAll, add, edit, exit): ");
            try {
                userCommand = inputUser.readLine();
                String name = "";
                String lastname = "";
                String age = "";
                String specialty = "";
                String group = "";
                switch (userCommand){
                    case("read"):
                        System.out.print("Enter students name : ");
                        name = inputUser.readLine();
                        System.out.print("Enter students lastname : ");
                        lastname = inputUser.readLine();
                        send("read");
                        send(name);
                        send(lastname);
                        return;
                    case("readAll"):
                        send("readAll");
                        return;
                    case("edit"):
                        System.out.print("Enter students name : ");
                        String oldName = inputUser.readLine();
                        System.out.print("Enter students lastname : ");
                        String oldLastname = inputUser.readLine();
                        System.out.print("Enter students age : ");
                        String oldAge = inputUser.readLine();
                        System.out.print("Enter new students name : ");
                        name = inputUser.readLine();
                        System.out.print("Enter new students lastname : ");
                        lastname = inputUser.readLine();
                        System.out.print("Enter new students age : ");
                        age = inputUser.readLine();
                        System.out.print("Enter new students specialty : ");
                        specialty = inputUser.readLine();
                        System.out.print("Enter new number group : ");
                        group = inputUser.readLine();
                        send("edit");
                        send(oldName);
                        send(oldLastname);
                        send(oldAge);
                        send(name);
                        send(lastname);
                        send(age);
                        send(specialty);
                        send(group);
                        return;
                    case("add"):
                        System.out.print("Enter students name : ");
                        name = inputUser.readLine();
                        System.out.print("Enter students lastname : ");
                        lastname = inputUser.readLine();
                        System.out.print("Enter students age : ");
                        age = inputUser.readLine();
                        System.out.print("Enter students specialty : ");
                        specialty = inputUser.readLine();
                        System.out.print("Enter number group : ");
                        group = inputUser.readLine();
                        send("add");
                        send(name);
                        send(lastname);
                        send(age);
                        send(specialty);
                        send(group);
                        return;
                    case("exit"):
                        ClientThread.this.downService();
                        return;

                    default:
                        System.out.println("invalid command");

                }
            } catch (IOException ignored) {}

        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String word;
            try {
                while (true) {
                    word = in.readLine();
                    switch(word) {
                        case ("stop"): {
                            ClientThread.this.downService();
                            break;
                        }
                        case ("authorization: user not present"): {
                            System.out.println(word);
                            ClientThread.this.authorization();
                            break;
                        }
                        case ("authorization: ok"): {
                            System.out.println(word);
                            ClientThread.this.menu();
                            break;
                        }
                        default: {
                            for(String str: word.split(";")){
                                System.out.println(str);
                            }
                            ClientThread.this.menu();
                        }
                    }
                }
            } catch (IOException e) {
                ClientThread.this.downService();
            }
        }
    }

}
