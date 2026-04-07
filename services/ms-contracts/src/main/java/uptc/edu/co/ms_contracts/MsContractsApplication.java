package uptc.edu.co.ms_contracts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.cloud.openfeign.EnableFeignClients;

import uptc.edu.co.ms_contracts.config.KafkaMessagingProperties;
import uptc.edu.co.ms_contracts.config.KafkaProducerConfig;


@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(KafkaMessagingProperties.class)
@Import(KafkaProducerConfig.class)
public class MsContractsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsContractsApplication.class, args);
    }
}