package co.edu.uptc.shared.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

/**
 * Global error handler centralizado para todos los microservicios Java.
 * Captura excepciones y retorna siempre { status, message, code }.
 * No expone stack traces ni información interna al cliente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // -------------------------------------------------------------------------
    // Excepciones de negocio propias
    // -------------------------------------------------------------------------

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {

        log.warn("[{}] {} {} — {} (code={})",
                ex.getHttpStatus().value(),
                request.getMethod(), request.getRequestURI(),
                ex.getMessage(), ex.getCode());

        ErrorResponse body = new ErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                ex.getCode());

        return ResponseEntity.status(ex.getHttpStatus()).body(body);
    }

    // -------------------------------------------------------------------------
    // Validación de Bean Validation (@Valid / @Validated)
    // -------------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        log.warn("[400] {} {} — Validation failed: {}",
                request.getMethod(), request.getRequestURI(), message);

        ErrorResponse body = new ErrorResponse(400, message, "VALIDATION_ERROR");
        return ResponseEntity.badRequest().body(body);
    }

    // -------------------------------------------------------------------------
    // Cuerpo JSON malformado
    // -------------------------------------------------------------------------

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJson(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.warn("[400] {} {} — Malformed request body",
                request.getMethod(), request.getRequestURI());

        ErrorResponse body = new ErrorResponse(400, "Malformed or unreadable request body", "MALFORMED_REQUEST");
        return ResponseEntity.badRequest().body(body);
    }

    // -------------------------------------------------------------------------
    // ResponseStatusException (interoperabilidad con código legacy/librerías)
    // -------------------------------------------------------------------------

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(
            ResponseStatusException ex, HttpServletRequest request) {

        int statusCode = ex.getStatusCode().value();

        log.warn("[{}] {} {} — {}",
                statusCode,
                request.getMethod(), request.getRequestURI(),
                ex.getReason());

        ErrorResponse body = new ErrorResponse(
                statusCode,
                ex.getReason() != null ? ex.getReason() : ex.getMessage(),
                "HTTP_ERROR");

        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    // -------------------------------------------------------------------------
    // IllegalArgumentException (errores de lógica / validación simple)
    // -------------------------------------------------------------------------

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {

        log.warn("[400] {} {} — {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage());

        ErrorResponse body = new ErrorResponse(400, ex.getMessage(), "VALIDATION_ERROR");
        return ResponseEntity.badRequest().body(body);
    }

    // -------------------------------------------------------------------------
    // Catch-all: errores inesperados (500)
    // -------------------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex, HttpServletRequest request) {

        log.error("[500] {} {} — Unexpected error: {}",
                request.getMethod(), request.getRequestURI(),
                ex.getMessage(), ex);

        ErrorResponse body = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected internal error occurred",
                "INTERNAL_ERROR");

        return ResponseEntity.internalServerError().body(body);
    }
}
