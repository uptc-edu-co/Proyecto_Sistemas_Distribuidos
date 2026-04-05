package uptc.edu.co.ms_suppliers.service; 

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SupplierEventPublisher {

    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;

    public SupplierEventPublisher() {
    }

    public void sendAuditLog(String action, Object data) {
        if (kafkaTemplate != null) {
            Map<String, Object> message = new HashMap<>();
            message.put("action", action);
            message.put("payload", data);
            message.put("timestamp", System.currentTimeMillis());
            try {
                kafkaTemplate.send("audit.events", message);
            } catch (Exception e) {
                System.err.println("Error enviando a Kafka: " + e.getMessage());
            }
        } else {
            
            System.out.println("LOG AUDITORÍA (Simulado): " + action + " para " + data);
        }
    }
}
    
