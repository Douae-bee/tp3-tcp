
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPServer {

    private final int port;
    private boolean running;

    // Constructor with a specified port
    public TCPServer(int port) {
        this.port = port;
        this.running = false;
    }

    // Default constructor
    public TCPServer() {
        this(8080);
    }

    // Launch the TCP server
    public void launch() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            running = true;

            System.out.println("TCP Server started on port " + port);

            // Wait for clients continuously
            while (true) {

                Socket clientSocket = serverSocket.accept();

                try (Socket socket = clientSocket) {

                    System.out.println("Client connected: "
                            + socket.getInetAddress());

                    // Get input and output streams
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream(),
                                    StandardCharsets.UTF_8));

                    PrintWriter out = new PrintWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream(),
                                    StandardCharsets.UTF_8),
                            true);

                    String message;

                    // Read messages until the client disconnects
                    while ((message = in.readLine()) != null) {

                        System.out.println("Received: " + message);

                        String response = socket.getInetAddress()
                                + ": " + message;

                        out.println(response);
                        out.flush();
                    }

                    System.out.println("Client disconnected: "
                            + socket.getInetAddress());

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
