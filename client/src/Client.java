import sun.jvm.hotspot.HelloWorld;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;

    public Client(String adress, int port) throws IOException {
        socket = new Socket(adress, port);
        System.out.println("Connected to Socket " + adress + ":" + port);

    }

    public void start() throws IOException {


        try (BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

            while (!message.equals("0")) {
                System.out.println("Write message: ");
                message = inputStream.readLine();
                outputStream.writeUTF(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Closing connection");


        Thread sendMessageThread = new Thread(() -> {
            try (BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
                 DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

                System.out.println("Write message: ");
                String message = inputStream.readLine();
                outputStream.writeUTF (message);

            } catch (IOException e) {
                e.printStackTrace();
            }
            ;
        };

        Thread receiveMessageThread = new Thread(() -> {

        });
        socket.close();

    }
}
