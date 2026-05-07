package co.edu.uptc.shared.exceptions;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuración Spring Boot para registrar el GlobalExceptionHandler
 * automáticamente en todos los microservicios que consuman el jar shared.
 */
@AutoConfiguration
public class GlobalExceptionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
