package com.pragma.powerup.trazabilidad.infrastructure.exceptionhandler;

import com.pragma.powerup.trazabilidad.domain.exception.AccessDeniedException;
import com.pragma.powerup.trazabilidad.domain.exception.BusinessRuleException;
import com.pragma.powerup.trazabilidad.domain.exception.DomainException;
import com.pragma.powerup.trazabilidad.domain.exception.InternalProcessException;
import com.pragma.powerup.trazabilidad.domain.exception.ResourceNotFoundException;
import com.pragma.powerup.trazabilidad.domain.utils.DomainErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";
    private static final String ERRORS = "errors";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> fe.getDefaultMessage() == null ? "valor invalido" : fe.getDefaultMessage(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(MESSAGE, DomainErrorMessage.VALIDATION_FAILED.getMessage());
        body.put(ERRORS, fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        v -> v.getMessage(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(MESSAGE, DomainErrorMessage.VALIDATION_FAILED.getMessage());
        body.put(ERRORS, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleNotReadable(HttpMessageNotReadableException ignored) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(MESSAGE, DomainErrorMessage.INVALID_REQUEST_BODY.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrity(DataIntegrityViolationException ignored) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(MESSAGE, DomainErrorMessage.DATA_INTEGRITY_CONFLICT.getMessage()));
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Map<String, String>> handleBusinessRule(BusinessRuleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(MESSAGE, ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(MESSAGE, ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(MESSAGE, ex.getMessage()));
    }

    @ExceptionHandler(InternalProcessException.class)
    public ResponseEntity<Map<String, String>> handleInternalProcess(InternalProcessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(MESSAGE, ex.getMessage()));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(DomainException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(MESSAGE, ex.getMessage()));
    }
}
