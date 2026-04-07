package uptc.edu.co.ms_contracts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uptc.edu.co.ms_contracts.client.SupplierFeignErrorDecoder;

@Configuration
public class AppConfig {

    @Bean
    public SupplierFeignErrorDecoder supplierFeignErrorDecoder() {
        return new SupplierFeignErrorDecoder();
    }
}