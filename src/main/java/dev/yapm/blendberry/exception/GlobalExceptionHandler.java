package dev.yapm.blendberry.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized exception handler for the BlendBerry API.
 * Handles common application-level exceptions and returns consistent HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation failures for annotated request parameters or bodies.
     *
     * @param ignoredE the exception thrown during validation.
     * @return a 400 BAD REQUEST with a validation error message.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ignoredE) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "ValidationError");
        response.put("message", "Argument Not Valid");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles failures during date parsing from client input.
     *
     * @param ignoredE the exception caused by invalid date format.
     * @return a 400 BAD REQUEST indicating the date format is invalid.
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Map<String, String>> handleDateTimeParseException(DateTimeParseException ignoredE) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "DateTimeParseError");
        response.put("message", "Invalid Date Format");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles attempts to insert duplicated keys into unique-indexed fields.
     *
     * @param e the exception thrown by MongoDB on duplicate keys.
     * @return a 409 CONFLICT with a duplication error message.
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateKeyException(DuplicateKeyException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "DuplicateKeyError");
        response.put("message", "Duplicated Key");
        response.put("details", e.getMostSpecificCause().getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Handles general unexpected exceptions.
     *
     * @param ignoredE the exception not caught by more specific handlers.
     * @return a 500 INTERNAL SERVER ERROR response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ignoredE) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "UnexpectedError");
        response.put("message", "An unexpected error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles database access exceptions.
     *
     * @param e the exception caused by database operations.
     * @return a 500 INTERNAL SERVER ERROR response.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "DatabaseError");
        response.put("message", "Database Access Error");
        response.put("details", e.getMostSpecificCause().getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles cases where a remote configuration is not found.
     *
     * @param e the exception indicating no config matched the lookup.
     * @return a 404 NOT FOUND with a descriptive message.
     */
    @ExceptionHandler(RemoteConfigNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRCNFException(RemoteConfigNotFoundException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "RemoteConfigNotFound");
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}