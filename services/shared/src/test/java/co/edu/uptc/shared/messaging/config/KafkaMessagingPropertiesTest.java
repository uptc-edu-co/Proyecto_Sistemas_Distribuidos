package co.edu.uptc.shared.messaging.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KafkaMessagingPropertiesTest {

    @Test
    void testDefaultValues() {
        KafkaMessagingProperties props = new KafkaMessagingProperties();
        assertEquals("localhost:9092", props.getBootstrapServers());
        assertEquals("shared-messaging-group", props.getGroupId());
        assertEquals("earliest", props.getAutoOffsetReset());
    }

    @Test
    void testSetters() {
        KafkaMessagingProperties props = new KafkaMessagingProperties();
        props.setBootstrapServers("kafka:9092");
        props.setGroupId("custom-group");
        props.setAutoOffsetReset("latest");

        assertEquals("kafka:9092", props.getBootstrapServers());
        assertEquals("custom-group", props.getGroupId());
        assertEquals("latest", props.getAutoOffsetReset());
    }
}
