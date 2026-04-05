package uptc.edu.co.ms_suppliers.config;

import co.edu.uptc.shared.security.GatewayAuthFilter;
import co.edu.uptc.shared.security.GatewayAuthProperties;

/**
 * Extensión de GatewayAuthFilter para permitir acceso directo a /suppliers.
 * ⚠️ ESTO ES SOLO PARA DESARROLLO/TESTING.
 * 
 * En producción, todos los accesos deben ir a través del API Gateway.
 */
public class SuppliersGatewayAuthFilter extends GatewayAuthFilter {

    public SuppliersGatewayAuthFilter(GatewayAuthProperties properties) {
        super(properties);
    }

    /**
     * Permite rutas públicas adicionales para testing.
     * En producción, esto debería ser más restrictivo.
     */
    @Override
    protected boolean isPublicPath(String path) {
        return super.isPublicPath(path)
                || path.startsWith("/suppliers");  // Permite acceso directo a /suppliers
    }
}
