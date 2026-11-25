
package fr.ensea.rts.tcp.server;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MessageHistory class.
 */
class MessageHistoryTest {

    /**
     * Checks that a new history is empty.
     */
    @Test
    void testEmptyHistory() {
        MessageHistory history = new MessageHistory();

        assertTrue(history.getMessages().isEmpty());
    }

    /**
     * Checks that a message is correctly stored.
     */
    @Test
    void testAddMessage() {
        MessageHistory history = new MessageHistory();

        history.addMessage("Hello");

        assertEquals(1, history.getMessages().size());
        assertEquals("Hello", history.getMessages().get(0));
    }

    /**
     * Checks that messages are stored in the correct order.
     */
    @Test
    void testMessageOrder() {
        MessageHistory history = new MessageHistory();

        history.addMessage("AAA");
        history.addMessage("BBB");
        history.addMessage("CCC");

        assertEquals(
                List.of("AAA", "BBB", "CCC"),
                history.getMessages()
        );
    }

    /**
     * Checks that only the last ten messages are stored.
     */
    @Test
    void testHistoryLimit() {
        MessageHistory history = new MessageHistory();

        for (int i = 1; i <= 12; i++) {
            history.addMessage("Message " + i);
        }

        assertEquals(10, history.getMessages().size());
        assertEquals("Message 3", history.getMessages().get(0));
        assertEquals("Message 12", history.getMessages().get(9));
    }

    /**
     * Checks that modifying the returned list does not
     * modify the internal history.
     */
    @Test
    void testHistoryEncapsulation() {
        MessageHistory history = new MessageHistory();

        history.addMessage("Hello");

        List<String> messages = history.getMessages();
        messages.clear();

        assertEquals(1, history.getMessages().size());
    }
}
