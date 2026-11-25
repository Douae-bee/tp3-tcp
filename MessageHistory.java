
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

/**
 * Stores the last 10 messages received by the server.
 */
public class MessageHistory {

    private static final int MAX_MESSAGES = 10;
    private final LinkedList<String> messages = new LinkedList<>();

    /**
     * Adds a message and removes the oldest one if necessary.
     *
     * @param message received message
     */
    public void addMessage(String message) {

        messages.addLast(message);

        if (messages.size() > MAX_MESSAGES) {
            messages.removeFirst();
        }
    }

    /**
     * Returns a copy of the message history.
     *
     * @return stored messages
     */
    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }
}
