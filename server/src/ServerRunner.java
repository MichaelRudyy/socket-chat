import java.io.IOException;

public class ServerRunner {
    public static void main(String[] args) {
        try {
            Server server = new Server(5000);
            server.handleClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
