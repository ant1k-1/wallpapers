package com.example.wallpapers.exception;

import org.hibernate.query.sqm.PathElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public ErrorMessage notFoundException(NotFoundException ex) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage requestHandlingNoHandlerFound(NoHandlerFoundException ex) {
        return new ErrorMessage(
                ex.getStatusCode().value(),
                ex.getStatusCode().toString(),
                ex.getRequestURL(),
                ex.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadArgumentException.class)
    public ErrorMessage badArgumentException(BadArgumentException ex) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage()
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PathElementException.class)
    public ErrorMessage pathElementException(PathElementException ex) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage()
        );
    }

}