package co.edu.uptc.shared.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Excepción para errores de autorización / permisos insuficientes (403 FORBIDDEN).
 */
public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN, "FORBIDDEN");
    }
}
