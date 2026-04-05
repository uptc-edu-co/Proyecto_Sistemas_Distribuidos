package uptc.edu.co.ms_suppliers.config;

import co.edu.uptc.shared.security.GatewayAuthFilter;
import co.edu.uptc.shared.security.GatewayAuthProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Configuración personalizada de seguridad para ms-suppliers.
 * Extiende el GatewayAuthFilter para permitir acceso a /suppliers en modo desarrollo.
 */
@Configuration
public class SecurityConfig {

    /**
     * Filtro personalizado que permite rutas públicas adicionales.
     * En producción, esto no debería permitir /suppliers.
     */
    @Bean
    public SuppliersGatewayAuthFilter suppliersGatewayAuthFilter(GatewayAuthProperties properties) {
        return new SuppliersGatewayAuthFilter(properties);
    }

    @Bean
    public FilterRegistrationBean<SuppliersGatewayAuthFilter> suppliersGatewayAuthFilterRegistration(
            SuppliersGatewayAuthFilter filter) {
        FilterRegistrationBean<SuppliersGatewayAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setName("suppliersGatewayAuthFilter");
        return registration;
    }
}
