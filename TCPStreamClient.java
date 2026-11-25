
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Tests TCP with multiple short writes and one large write.
 */
public class TCPStreamClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {

        if (args.length != 1 ||
                (!args[0].equals("small") &&
                 !args[0].equals("large"))) {

            System.out.println("Usage: java TCPStreamClient <small|large>");
            return;
        }

        try (Socket socket = new Socket(HOST, PORT)) {

            OutputStream out = socket.getOutputStream();

            System.out.println("Connected to server.");

            if (args[0].equals("small")) {

                sendSmallMessages(out);

            } else {

                sendLargeBlock(out);
            }

            out.flush();

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    /**
     * Sends several short strings without line breaks.
     */
    private static void sendSmallMessages(OutputStream out)
            throws IOException {

        String[] messages = {"Hello", "World", "Java", "TCP"};

        for (String message : messages) {

            byte[] data = message.getBytes(StandardCharsets.UTF_8);

            out.write(data);

            System.out.println("Write: " + message
                    + " (" + data.length + " bytes)");
        }
    }

    /**
     * Sends 10000 bytes using a single write call.
     */
    private static void sendLargeBlock(OutputStream out)
            throws IOException {

        byte[] data = new byte[10000];

        Arrays.fill(data, (byte) 'A');

        out.write(data);

        System.out.println("Single write: " + data.length + " bytes");
    }
}
