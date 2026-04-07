package co.edu.uptc.ms_contracts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import co.edu.uptc.ms_contracts.client.SupplierFeignErrorDecoder;

@Configuration
public class AppConfig {

    @Bean
    public SupplierFeignErrorDecoder supplierFeignErrorDecoder() {
        return new SupplierFeignErrorDecoder();
    }
}