import java.io.IOException;

public class ClientRunner {
    public static void main(String[] args) {
        try {
            Client client = new Client("127.0.0.1",5000);
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
