package uptc.edu.co.shared.messaging.publisher;

import java.time.Instant;
import org.springframework.kafka.core.KafkaTemplate;
import uptc.edu.co.shared.messaging.dto.EventDTO;

public abstract class BasePublisher {
    private final KafkaTemplate<String, EventDTO> kafkaTemplate;

    protected BasePublisher(KafkaTemplate<String, EventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    protected void publish(String topic, EventDTO event) {
        if (event.getTimestamp() == null) {
            event.setTimestamp(Instant.now());
        }
        kafkaTemplate.send(topic, event);
    }
}