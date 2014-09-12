import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main
{

    public static final byte[] STATUS_NOT_OK = "HTTP/1.1 404 Not Found\r\n\r\n".getBytes();
    public static final byte[] STATUS_OK = "HTTP/1.1 200 OK\r\n\r\n".getBytes();

    public static void main(String[] arguments) throws Exception {
        ExecutorService workers = Executors.newCachedThreadPool();

        ServerSocket socket = new ServerSocket(8080);
        while (socket.isBound()) {
            Socket client = socket.accept();
            workers.execute(() -> handleConnection(client));
        }
    }

    private static void handleConnection(Socket client) {
        try (OutputStream out = client.getOutputStream()) {
            Scanner in = new Scanner(client.getInputStream());

            String method = in.next();
            String resource = in.next();

            Path file = Paths.get("/Library/WebServer/Documents", resource);
            if (Files.isRegularFile(file)) {
                out.write(STATUS_OK);
                Files.copy(file, out);
            } else {
                out.write(STATUS_NOT_OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
