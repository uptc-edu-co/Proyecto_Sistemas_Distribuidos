package co.edu.uptc.shared.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Excepción base para errores de negocio controlados.
 * Encapsula el HttpStatus y un código de error para el cliente.
 */
public class BusinessException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String code;

    public BusinessException(String message, HttpStatus httpStatus, String code) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }
}
