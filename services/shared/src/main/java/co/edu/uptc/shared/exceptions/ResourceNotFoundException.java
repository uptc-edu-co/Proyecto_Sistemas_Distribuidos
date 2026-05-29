package co.edu.uptc.shared.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Excepción para recursos no encontrados (404 NOT FOUND).
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND");
    }
}
