package com.pequla.ticket.error;

import com.pequla.ticket.model.ErrorModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorAdvice {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorModel handleRuntimeException(RuntimeException exception, HttpServletRequest request) {
        String message = exception.getMessage();
        if (exception instanceof UserRejectedException rje) {
            message = rje.getType().name();
        }
        return ErrorModel.builder()
                .name(exception.getClass().getName())
                .message(message)
                .path(request.getServletPath())
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
