package co.edu.uptc.shared.messaging.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KafkaMessagingProperties.class)
public class MessagingConfiguration {
}