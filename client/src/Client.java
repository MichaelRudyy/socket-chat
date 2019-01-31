import sun.jvm.hotspot.HelloWorld;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private String name;
    private String id;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));


    public Client(String adress, int port) throws IOException {
        this.socket = new Socket(adress, port);

        System.out.println(socket.isClosed());

        System.out.println("Connected to Socket " + adress + ":" + port + "\n" +
                "Write a name of user : ");

        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());

        this.name = consoleReader.readLine();
        outputStream.writeUTF(name);
        this.id = inputStream.readUTF();


        System.out.println(this);

    }

    public void start() throws IOException {

        Scanner scn = new Scanner(System.in);

        Thread sendMessageThread = new Thread(() -> {
            while (true) {
                System.out.println("Write message: ");
                String message = scn.nextLine();
                try {
                    if (message.equals("all")) {
                        outputStream.writeUTF(message);
                    }else if (message.equals("logout")){
                        outputStream.writeUTF(message);
                        socket.close();
                        break;
                    } else {
                        System.out.println("to id?");
                        String recieverID = scn.nextLine();
                        outputStream.writeUTF("mes#" + id + "#" + recieverID + "#" + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread receiveMessageThread = new Thread(() -> {
            while (true) {
                try {
                    String message = inputStream.readUTF();
                    System.out.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        receiveMessageThread.start();
        sendMessageThread.start();

    }


    @Override
    public String toString() {
        return "Client{" +
                "socket=" + socket +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
