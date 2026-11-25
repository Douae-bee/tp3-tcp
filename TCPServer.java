
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * TCP echo server with message history.
 */
public class TCPServer {

    private final int port;
    private boolean running;
    private final MessageHistory history;

    /**
     * Creates a server on the specified port.
     *
     * @param port listening port
     */
    public TCPServer(int port) {
        this.port = port;
        this.running = false;
        this.history = new MessageHistory();
    }

    /**
     * Creates a server on port 8080.
     */
    public TCPServer() {
        this(8080);
    }

    /**
     * Starts the TCP server.
     */
    public void launch() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            running = true;

            System.out.println("TCP Server started on port " + port);

            while (true) {

                Socket clientSocket = serverSocket.accept();

                try (Socket socket = clientSocket) {

                    handleClient(socket);

                } catch (IOException e) {

                    System.out.println("Error handling client: "
                            + e.getMessage());
                }
            }

        } catch (IOException e) {

            System.out.println("Server error: " + e.getMessage());

        } finally {
            running = false;
        }
    }

    /**
     * Handles communication with one client.
     *
     * @param socket client connection
     */
    private void handleClient(Socket socket) throws IOException {

        System.out.println("Client connected: "
                + socket.getInetAddress());

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream(),
                        StandardCharsets.UTF_8));

        PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        socket.getOutputStream(),
                        StandardCharsets.UTF_8),
                true);

        // Send the history to the new client
        sendHistory(out);

        String message;

        while ((message = in.readLine()) != null) {

            System.out.println("Received: " + message);

            String response = socket.getInetAddress()
                    + ": " + message;

            // Store the received message
            history.addMessage(response);

            // Send the echo
            out.println(response);
        }

        System.out.println("Client disconnected: "
                + socket.getInetAddress());
    }

    /**
     * Sends the stored messages to a new client.
     *
     * @param out client output stream
     */
    private void sendHistory(PrintWriter out) {

        List<String> messages = history.getMessages();

        out.println("HISTORY " + messages.size());

        for (String message : messages) {
            out.println(message);
        }
    }

    @Override
    public String toString() {
        return "TCPServer [port=" + port
                + ", running=" + running + "]";
    }

    public static void main(String[] args) {

        int port = 8080;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        TCPServer server = new TCPServer(port);

        System.out.println(server);

        server.launch();
    }
}
