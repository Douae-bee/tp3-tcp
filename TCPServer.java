
public class TCPServer {

    private final int port;
    private boolean running;

    /**
     * Constructor: creates a TCP server on the specified port.
     */
    public TCPServer(int port) {
        this.port = port;
        this.running = false;
    }

    /**
     * Default constructor: uses port 8080.
     */
    public TCPServer() {
        this(8080);
    }

    /**
     * Launches the TCP server.
     */
    public void launch() {
            }

    /**
     * Returns a description of the server state
     */
    @Override
    public String toString() {
        return "TCPServer [port=" + port + ", running=" + running + "]";
    }

    /**
     * Main method: starts the server.
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
