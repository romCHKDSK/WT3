import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    private Socket socket;
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток завписи в сокет
    private User user;
    private static UserDAO userDAO = new UserDAO();
    private static StudentDAO studentDAO = new StudentDAO();

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }
    @Override
    public void run() {
        try {
            String word = "";
            while (true) {
                word = in.readLine();
                if(word.equals("stop")) {
                    this.downService();
                    break;
                }
                String name = "";
                String lastname = "";
                int age = 0;
                String specialty = "";
                int group = 0;
                switch (word) {
                    case "authorization":
                        String login = in.readLine();
                        String password = in.readLine();
                        user = userDAO.getUser(login, password);
                        if (user == null){
                            send("authorization: user not present");
                        } else {
                            send("authorization: ok");
                        }
                        break;
                    case "read":
                        name = in.readLine();
                        lastname = in.readLine();
                        if((user != null) && user.isCanRead()) {
                            String message = studentDAO.readStudentCase(name, lastname);
                            if (message.length() == 0){
                                send("student not found");
                            } else{
                                send(message);
                            }
                        } else {
                            send("no access rights");
                        }
                        break;

                    case "readAll":
                        if((user != null) && user.isCanRead()) {
                            send(studentDAO.readAllStudentCases());
                        } else {
                            send("no access rights");
                        }
                        break;

                    case "edit":
                        if((user != null) && user.isCanEdit()) {
                            String oldName = in.readLine();
                            String oldLastname = in.readLine();
                            int oldAge = 0;
                            try {
                                oldAge = Integer.parseInt(in.readLine());
                            } catch (Exception ignore) {
                            }
                            Student oldStudent = new Student(oldName, oldLastname, oldAge, "",0);
                            name = in.readLine();
                            lastname = in.readLine();
                            try {
                                age = Integer.parseInt(in.readLine());
                            } catch (Exception ignore) {
                            }
                            specialty = in.readLine();
                            try {
                                group = Integer.parseInt(in.readLine());
                            } catch (Exception ignore) {
                            }
                            Student newStudent = new Student(name, lastname, age, specialty, group);
                            try {
                                boolean isStudentFind = studentDAO.editStudent(oldStudent, newStudent);
                                if (isStudentFind) {
                                    send("student edited");
                                } else {
                                    send("student not found");
                                }
                            } catch (Exception e) {
                                send("Edit error");
                            }
                        } else {
                            send("no access rights");
                        }
                        break;
                    case "add":
                        if((user != null) && user.isCanAdd()) {
                            name = in.readLine();
                            lastname = in.readLine();
                            try {
                                age = Integer.parseInt(in.readLine());
                            } catch (Exception ignore) {
                            }
                            specialty = in.readLine();
                            try {
                                group = Integer.parseInt(in.readLine());
                            } catch (Exception ignore) {
                            }
                            Student student = new Student(name, lastname, age, specialty, group);
                            try {
                                studentDAO.addNewStudent(student);
                                send("student added");
                            } catch (Exception e) {
                                send("Add error");
                            }
                        } else {
                            send("no access rights");
                        }
                        break;
                    default:
                        continue;
                }
            }

        } catch (IOException e) {
            this.downService();
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
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();

            }
        } catch (IOException ignored) {}
    }
}
