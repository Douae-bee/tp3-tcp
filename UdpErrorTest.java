import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;


public class UdpErrorTest {

    /** Maximum waiting time for an answer, in milliseconds*/
    private static final int TIMEOUT_MS = 3000;

    /** Size of the reception buffer in bytes */
    private static final int BUFFER_SIZE = 1024;

    
    private final InetAddress address;

    /** Port of the target server. */
    private final int port;

    /**
     * Creates the test.
     *
     * @param host the target host
     * @param port the target port
     * @throws IOException if the host cannot be resolved
     */
    public UdpErrorTest(String host, int port) throws IOException {
        this.address = InetAddress.getByName(host);
        this.port = port;
    }

    /**
     * Sends a datagram with an unconnected socket, then waits for an answer.
     *
     * @throws IOException if the socket cannot be used
     */
    public void testUnconnected() throws IOException {
        System.out.println("--- UDP without connect() ---");
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT_MS);
            sendAndReceive(socket);
        }
    }

    /**
     * Sends a datagram with a socket on which connect() was called,
     * then waits for an answer.
     *
     * @throws IOException if the socket cannot be used
     */
    public void testConnected() throws IOException {
        System.out.println("--- UDP with connect() ---");
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(address, port);
            socket.setSoTimeout(TIMEOUT_MS);
            sendAndReceive(socket);
        }
    }

    /**
     * Sends one datagram and waits for the answer, reporting what happens.
     *
     * @param socket the UDP socket to use
     * @throws IOException for unexpected I/O errors
     */
    private void sendAndReceive(DatagramSocket socket) throws IOException {
        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        try {
            socket.send(new DatagramPacket(data, data.length, address, port));
            System.out.println("send(): no error");

            DatagramPacket answer = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            socket.receive(answer);
            System.out.println("Answer received: "
                    + new String(answer.getData(), 0, answer.getLength(), StandardCharsets.UTF_8));
        } catch (PortUnreachableException e) {
            System.out.println("receive()/send(): PortUnreachableException (ICMP received)");
        } catch (SocketTimeoutException e) {
            System.out.println("receive(): timeout after " + TIMEOUT_MS
                    + " ms, no error reported");
        }
    }

    /**
     * Entry point: java UdpErrorTest host port
     *
     * @param args the host and the port of a port where nothing listens
     * @throws IOException if the test fails
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java UdpErrorTest <host> <port>");
            return;
        }
        UdpErrorTest test = new UdpErrorTest(args[0], Integer.parseInt(args[1]));
        test.testUnconnected();
        test.testConnected();
    }
}