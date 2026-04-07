package co.edu.uptc.ms_contracts.messaging;

import co.edu.uptc.shared.messaging.dto.EventDTO;
import co.edu.uptc.shared.messaging.publisher.BasePublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class ContractEventPublisher extends BasePublisher {

    public ContractEventPublisher(KafkaTemplate<String, EventDTO> kafkaTemplate) {
        super(kafkaTemplate);
    }

    public void publishContractCreated(Object contract) {
        EventDTO event = new EventDTO();
        event.setAction("CREATE/CONTRACT");
        event.setServiceOrigin("ms-contracts");
        event.setData(contract);

        try {
            publish("audit.events", event);
        } catch (Exception e) {
            System.err.println("Error sending to kafka: " + e.getMessage());
        }
    }
}