package co.edu.uptc.ms_contracts.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SupplierFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 404 -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Proveedor no encontrado");
            case 422 -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "Proveedor inhabilitado");
            case 503 -> new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "ms-suppliers no disponible");
            default -> new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Error al consultar ms-suppliers: " + response.status());
        };
    }
}