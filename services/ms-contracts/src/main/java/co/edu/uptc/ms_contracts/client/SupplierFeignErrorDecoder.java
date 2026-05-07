package co.edu.uptc.ms_contracts.client;

import co.edu.uptc.shared.exceptions.BusinessException;
import co.edu.uptc.shared.exceptions.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class SupplierFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 404 -> new ResourceNotFoundException("Supplier not found");
            case 422 -> new BusinessException("Supplier is disabled", HttpStatus.UNPROCESSABLE_ENTITY, "SUPPLIER_DISABLED");
            case 503 -> new BusinessException("ms-suppliers is unavailable", HttpStatus.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE");
            default  -> new BusinessException(
                    "Error while calling ms-suppliers: " + response.status(),
                    HttpStatus.BAD_GATEWAY,
                    "BAD_GATEWAY");
        };
    }
}