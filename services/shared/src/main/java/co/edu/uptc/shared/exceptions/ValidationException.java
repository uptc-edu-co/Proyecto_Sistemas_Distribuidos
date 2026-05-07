package co.edu.uptc.shared.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Excepción para errores de validación de negocio (400 BAD REQUEST).
 */
public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
    }
}
