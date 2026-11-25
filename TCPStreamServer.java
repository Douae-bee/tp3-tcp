
import java.io.InputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Demonstrates how TCP receives data as a byte stream.
 */
public class TCPStreamServer {

    private static final int PORT = 8080;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("TCP Stream Server started on port " + PORT);

            while (true) {

                try (Socket socket = serverSocket.accept()) {

                    System.out.println("Client connected.");

                    // Wait before reading the received data
                    Thread.sleep(1000);

                    InputStream in = socket.getInputStream();

                    byte[] buffer = new byte[BUFFER_SIZE];

                    int bytesRead;
                    int totalBytes = 0;
                    int readCount = 0;

                    while ((bytesRead = in.read(buffer)) != -1) {

                        readCount++;
                        totalBytes += bytesRead;

                        System.out.println("Read " + readCount
                                + ": " + bytesRead + " bytes");

                        // Display short messages
                        if (bytesRead <= 100) {
                            System.out.println("Data: "
                                    + new String(buffer, 0, bytesRead,
                                    StandardCharsets.UTF_8));
                        }
                    }

                    System.out.println("Total reads: " + readCount);
                    System.out.println("Total bytes: " + totalBytes);
                    System.out.println("Client disconnected.");

                } catch (IOException e) {
                    System.out.println("Client error: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
