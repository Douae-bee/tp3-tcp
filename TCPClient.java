
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * TCP client with message history reception.
 */
public class TCPClient {

    private final String host;
    private final int port;

    /**
     * Creates a TCP client.
     *
     * @param host server address
     * @param port server port
     */
    public TCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Connects to the server and exchanges messages.
     */
    public void start() {

        try (Socket socket = new Socket(host, port)) {

            System.out.println("Connected to " + host + ":" + port);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(),
                            StandardCharsets.UTF_8));

            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream(),
                            StandardCharsets.UTF_8),
                    true);

            BufferedReader keyboard = new BufferedReader(
                    new InputStreamReader(
                            System.in,
                            StandardCharsets.UTF_8));

            // Receive the message history
            receiveHistory(in);

            String message;

            System.out.println("Enter a message or /quit to exit.");

            while ((message = keyboard.readLine()) != null) {

                if (message.equals("/quit")) {
                    break;
                }

                out.println(message);

                String response = in.readLine();

                if (response == null) {
                    System.out.println("Server closed the connection.");
                    break;
                }

                System.out.println(response);
            }

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }

        System.out.println("Client disconnected.");
    }

    /**
     * Receives the history sent by the server.
     *
     * @param in server input stream
     */
    private void receiveHistory(BufferedReader in)
            throws IOException {

        String header = in.readLine();

        if (header == null || !header.matches("HISTORY (10|[0-9])")) {
            throw new IOException("Invalid or missing history header.");
        }

        int count = Integer.parseInt(header.substring(8));

        System.out.println("--- Message history ---");

        if (count == 0) {
            System.out.println("No previous messages.");
        }

        for (int i = 0; i < count; i++) {

            String message = in.readLine();

            if (message == null) {
                throw new IOException("Incomplete message history.");
            }

            System.out.println(message);
        }

        System.out.println("--- End of history ---");
    }

    /**
     * Starts the TCP client.
     *
     * @param args server host and port
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: java TCPClient <host> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        TCPClient client = new TCPClient(host, port);

        client.start();
    }
}
