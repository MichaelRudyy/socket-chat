import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Server application makes a ServerSocket on a specific port which is 5000.
 * This starts our Server listening for client requests coming in for port 5000.
 */

public class Server {

    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void handleClients() {
        Socket socket;
        int count = 0;

        try {
            while (true) {
                System.out.println("Ready for new one");
                socket = serverSocket.accept();
                ClientHandler client = new ClientHandler(socket,
                        "Client " + count);

                Thread clientThread = new Thread(client);
                clientThread.start();
                System.out.println("Client " + count + " is running");
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class ClientHandler implements Runnable {

    private Socket socket;
    private String name;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket socket, String name, DataInputStream inputStream,DataOutputStream outputStream) {
        this.socket = socket;
        this.name = name;
        this.in = inputStream;
        this.out = outputStream;
    }

    public ClientHandler(Socket socket,String name) throws IOException {
        this.socket = socket;
        this.name = name;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {

        String message = "";

        try {
            while (!message.equals("0")) {
                System.out.println(name + " : Waiting for message from " + this.socket);
                message = in.readUTF();
                System.out.println(name + " : Message is " + message);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
