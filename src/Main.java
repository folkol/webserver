import java.io.OutputStream;
import java.net.ServerSocket;

public class Main
{
    public static void main(String[] arguments) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        try (OutputStream out = serverSocket.accept().getOutputStream()) {
            out.write("Hello, world!\n".getBytes());
        }
    }
}
