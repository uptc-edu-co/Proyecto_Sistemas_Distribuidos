package co.edu.uptc.shared.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Excepción para errores de autenticación (401 UNAUTHORIZED).
 */
public class AuthenticationException extends BusinessException {

    public AuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "AUTHENTICATION_ERROR");
    }
}
