package com.epam.esm.exceptionhandler;

import com.epam.esm.configuration.Translator;
import com.epam.esm.exception.ResourceNotFoundServiceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;


@ControllerAdvice
public class CustomExceptionHandler {

    private final Translator translator;

    public CustomExceptionHandler(Translator translator) {
        this.translator = translator;
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionMessage> handleServiceException(ServiceException e){
        String message = translator.toLocale("error500_message");
        ExceptionMessage mes = new ExceptionMessage(message, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value());
        ResponseEntity<ExceptionMessage> responseEntity = new ResponseEntity<>(mes, HttpStatus.INTERNAL_SERVER_ERROR);
        changeResponseCharset(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(ResourceNotFoundServiceException.class)
    public ResponseEntity<ExceptionMessage> handleResourceNotFoundException(ResourceNotFoundServiceException e){
        String message = translator.toLocale("error404_message");
        ExceptionMessage mes = new ExceptionMessage(message, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value());
        ResponseEntity<ExceptionMessage> responseEntity = new ResponseEntity<>(mes, HttpStatus.NOT_FOUND);
        changeResponseCharset(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionMessage> handleValidationException(ValidationException e) {
        String message = translator.toLocale(e.getMessage());
        ExceptionMessage mes = new ExceptionMessage(message, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        ResponseEntity<ExceptionMessage> responseEntity = new ResponseEntity<>(mes, HttpStatus.BAD_REQUEST);
        changeResponseCharset(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String message = translator.toLocale("message_not_readable");
        ExceptionMessage mes = new ExceptionMessage(message, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        ResponseEntity<ExceptionMessage> responseEntity = new ResponseEntity<>(mes, HttpStatus.BAD_REQUEST);
        changeResponseCharset(responseEntity);
        return responseEntity;
    }

    private void changeResponseCharset(ResponseEntity<ExceptionMessage> responseEntity){
        HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(responseEntity.getHeaders());
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
    }
}
