package de.griesser.outfit.web.impl;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final ErrorAttributes errorAttributes;

    ControllerExceptionHandler(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({ValidationException.class, ConstraintViolationException.class})
    Map<String, Object> exceptionHandler(ValidationException e, WebRequest request) {
        return errorAttributes.getErrorAttributes(request, false);
    }

}
