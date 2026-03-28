package co.edu.uptc.shared.messaging.config;

import co.edu.uptc.shared.messaging.dto.EventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerConfigTest {

    @InjectMocks
    private KafkaConsumerConfig config;

    @Mock
    private KafkaMessagingProperties properties;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void testConsumerFactoryCreation() {
        when(properties.getBootstrapServers()).thenReturn("localhost:9092");
        when(properties.getGroupId()).thenReturn("test-group");
        when(properties.getAutoOffsetReset()).thenReturn("earliest");

        ConsumerFactory<String, EventDTO> factory = config.consumerFactory(properties, objectMapper);
        assertNotNull(factory);
    }

    @Test
    void testKafkaListenerContainerFactoryCreation() {
        when(properties.getBootstrapServers()).thenReturn("localhost:9092");
        when(properties.getGroupId()).thenReturn("test-group");
        when(properties.getAutoOffsetReset()).thenReturn("earliest");

        ConsumerFactory<String, EventDTO> mockConsumerFactory = config.consumerFactory(properties, objectMapper);
        ConcurrentKafkaListenerContainerFactory<String, EventDTO> factory = config.kafkaListenerContainerFactory(mockConsumerFactory);
        assertNotNull(factory);
    }
}
