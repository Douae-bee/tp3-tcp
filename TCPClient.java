
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * TCP client that exchanges text messages with a TCP server.
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

            // Read messages from the server
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(),
                            StandardCharsets.UTF_8));

            // Send messages to the server
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream(),
                            StandardCharsets.UTF_8),
                    true);

            // Read user input from the keyboard
            BufferedReader keyboard = new BufferedReader(
                    new InputStreamReader(
                            System.in,
                            StandardCharsets.UTF_8));

            String message;

            System.out.println("Enter a message or /quit to exit.");

            // Exchange messages until the user quits
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
     * Starts the TCP client using command-line arguments.
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
