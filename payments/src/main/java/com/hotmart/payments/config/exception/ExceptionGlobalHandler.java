package com.hotmart.payments.config.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionGlobalHandler {

    private final MessageSource message;

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException exception) {
        var details = new ExceptionDetails(BAD_REQUEST.value(), exception.getMessage());
        log.error("Validation Exception: {}", details.message());
        return ResponseEntity.badRequest().body(details);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException exception) {
        var details = new ExceptionDetails(FORBIDDEN.value(), exception.getMessage());
        log.error("Forbidden Exception: {}", details.message());
        return ResponseEntity.status(FORBIDDEN).body(details);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public List<ExceptionValidationDetails> handler(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ExceptionValidationDetails> erros = new ArrayList<>();

        fieldErrors.forEach(e -> {
            String message = this.message.getMessage(e, LocaleContextHolder.getLocale());
            ExceptionValidationDetails error = new ExceptionValidationDetails(e.getField(), message);
            erros.add(error);
        });

        return erros;
    }

}
