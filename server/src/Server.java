import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;


/**
 * Server application makes a ServerSocket on a specific port which is 5000.
 * This starts our Server listening for client requests coming in for port 5000.
 */

public class Server {

    private ServerSocket serverSocket;
    private static HashMap<String, ClientHandler> clientMap;

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
                clientMap.put(String.valueOf(count), client);

                Thread clientThread = new Thread(client);
                clientThread.start();
                System.out.println("Client " + count + " is running");
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ClientHandler getClientHandlerByID(String id) {
        return clientMap.get(id);
    }
}


class ClientHandler implements Runnable {

    private Socket socket;
    private String name;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket socket, String name, DataInputStream inputStream, DataOutputStream outputStream) {
        this.socket = socket;
        this.name = name;
        this.in = inputStream;
        this.out = outputStream;
    }

    public ClientHandler(Socket socket, String name) throws IOException {
        this.socket = socket;
        this.name = name;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {

        String request = "";

        try {
            while (!request.equals("logout")) {
                StringTokenizer tokenizer = new StringTokenizer(request, "#");
                String receiverID = tokenizer.nextToken();
                String senderID = tokenizer.nextToken();
                String message = tokenizer.nextToken();
                DataOutputStream out = Server.getClientHandlerByID(receiverID).out;
                out.writeUTF(senderID + " : " + message);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void connectToUser(int id) {

    }
}
