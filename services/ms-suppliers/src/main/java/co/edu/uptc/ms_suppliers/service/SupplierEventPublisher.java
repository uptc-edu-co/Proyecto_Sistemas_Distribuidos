package co.edu.uptc.ms_suppliers.service; 

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import co.edu.uptc.shared.messaging.dto.EventDTO;
import co.edu.uptc.shared.messaging.publisher.BasePublisher;

@Component
public class SupplierEventPublisher extends BasePublisher {

    public SupplierEventPublisher(KafkaTemplate<String, EventDTO> kafkaTemplate) {
        super(kafkaTemplate);
    }

    public void sendAuditLog(String action, Object data) {
        EventDTO event = new EventDTO();
        event.setAction(action);
        event.setServiceOrigin("ms-suppliers");
        event.setData(data);

        try {
            publish("audit.events", event);
        } catch (Exception e) {
            System.err.println("Error enviando a Kafka: " + e.getMessage());
        }
    }
}