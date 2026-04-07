package uptc.edu.co.ms_contracts.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupplierFeignConfig {

    @Value("${gateway.secret:shared-super-secret}")
    private String gatewaySecret;

    @Bean
    public RequestInterceptor gatewayHeadersInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-Gateway-Secret", gatewaySecret);
            requestTemplate.header("X-User", "ms-contracts-internal");
        };
    }
}