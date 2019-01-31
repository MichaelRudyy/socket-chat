import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * Server application makes a ServerSocket on a specific port which is 5000.
 * This starts our Server listening for client requests coming inputStream for port 5000.
 */

public class Server {

    private final String ID_STRING = "id%d";

    private ServerSocket serverSocket;
    private static HashMap<String, ClientHandler> clientMap = new HashMap<>();

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void handleClients() {
        Socket socket;
        int count = 0;

        try {
            while (true) {
                System.out.println("Waiting for new client");
                socket = serverSocket.accept();

                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                registerClient(socket, inputStream, outputStream, count);


                System.out.println("Client " + clientMap.get(String.format(ID_STRING, count)) + " is running");
                Thread thread = new Thread(clientMap.get(String.format(ID_STRING, count)));
                thread.start();
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerClient(Socket socket,
                                DataInputStream inputStream,
                                DataOutputStream outputStream,
                                int count) throws IOException {
        String name = inputStream.readUTF();
        ClientHandler client = new ClientHandler(socket,
                name,
                String.format(ID_STRING, count));
        outputStream.writeUTF(String.format(ID_STRING, count));
        clientMap.put(String.format(ID_STRING, count), client);
    }

    public static ClientHandler getClientHandlerByID(String id) {
        return clientMap.get(id);
    }

    public static String getAllClients() {
        String values = "";
        for (Map.Entry<String, ClientHandler> entry : clientMap.entrySet()) {
            values += "id = " + entry.getKey() + " name : " + entry.getValue().getName() + "\n";
        }
        return values;
    }
}


class ClientHandler implements Runnable {

    private Socket socket;
    private String name;
    private String id;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public ClientHandler(Socket socket, String name, String id) throws IOException {
        this.socket = socket;
        this.name = name;
        this.id = id;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public String getName(){
        return this.name;
    }

    @Override
    public void run() {

        String request = "";

        try {
            while (true) {
                request = inputStream.readUTF();
                if (request.equals("logout")) {
                    break;
                }
                //System.out.println(request);
                StringTokenizer tokenizer = new StringTokenizer(request, "#");
                switch (tokenizer.nextToken()) {
                    case "mes": {
                        String senderID = tokenizer.nextToken();
                        String receiverID = tokenizer.nextToken();
                        String message = tokenizer.nextToken();
                        DataOutputStream recieverSocket = Server.getClientHandlerByID(receiverID).outputStream;
                        recieverSocket.writeUTF(Server.getClientHandlerByID(senderID).name + " : " + message);
                        break;
                    }
                    case "all": {
                        outputStream.writeUTF(Server.getAllClients());
                        break;
                    }
                    case "logout": {

                    }
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
