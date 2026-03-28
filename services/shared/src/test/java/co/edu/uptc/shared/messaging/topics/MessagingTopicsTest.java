package co.edu.uptc.shared.messaging.topics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MessagingTopicsTest {

    @Test
    void testConstants() {
        assertEquals("audit.events", MessagingTopics.AUDIT_EVENTS);
    }
}
