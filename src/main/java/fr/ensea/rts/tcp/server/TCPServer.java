package fr.ensea.rts.tcp.server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

/**
 * TCP echo server with message history and client identification.
 */
public class TCPServer {

    private final int port;
    private boolean running;
    private final MessageHistory history;

    // Step 14: Client identification
    private int clientCounter = 0;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
     * Creates a server on the default port.
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

                // Assign a unique ID to each client
                int clientId = ++clientCounter;

                try (Socket socket = clientSocket) {

                    handleClient(socket, clientId);

                } catch (IOException e) {

                    System.out.println("Error handling Client #"
                            + clientId + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {

            System.out.println("Server error: " + e.getMessage());

        } finally {
            running = false;
        }
    }

    /**
     * Handles communication with a connected client.
     *
     * @param socket client socket
     * @param clientId unique client identifier
     * @throws IOException if a communication error occurs
     */
    private void handleClient(Socket socket, int clientId)
            throws IOException {

        // Step 14: Record the connection time
        LocalDateTime connectionTime = LocalDateTime.now();
        long startTime = System.nanoTime();

        System.out.println("[" + connectionTime.format(DATE_FORMAT)
                + "] Client #" + clientId + " connected: "
                + socket.getInetAddress());

        try {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(),
                            StandardCharsets.UTF_8));

            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream(),
                            StandardCharsets.UTF_8),
                    true);

            // Step 13: Send previous messages
            sendHistory(out);

            String message;

            while ((message = in.readLine()) != null) {

                System.out.println("Received from Client #"
                        + clientId + ": " + message);

                String response = socket.getInetAddress()
                        + ": " + message;

                history.addMessage(response);

                out.println(response);
            }

        } finally {

            // Step 14: Record the disconnection time
            LocalDateTime disconnectionTime = LocalDateTime.now();

            Duration sessionDuration = Duration.ofNanos(
                    System.nanoTime() - startTime);

            System.out.println("["
                    + disconnectionTime.format(DATE_FORMAT)
                    + "] Client #" + clientId + " disconnected.");

            System.out.printf(
                    "Client #%d session duration: %.2f seconds%n",
                    clientId,
                    sessionDuration.toNanos() / 1_000_000_000.0);
        }
    }

    /**
     * Sends the message history to a new client.
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

    /**
     * Starts the server using command-line arguments.
     *
     * @param args optional server port
     */
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
