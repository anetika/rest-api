package com.epam.esm.exceptionhandler;

import com.epam.esm.configuration.Translator;
import com.epam.esm.exception.ResourceNotFoundServiceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    private final Translator translator;

    public CustomExceptionHandler(Translator translator) {
        this.translator = translator;
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleServiceException(ServiceException e){
        String message = translator.toLocale("error500_message");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        changeResponseCharset(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(ResourceNotFoundServiceException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundServiceException e){
        String message = translator.toLocale("error404_message");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        changeResponseCharset(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e){
        String message = translator.toLocale(e.getMessage());
        ResponseEntity<String> responseEntity = new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        changeResponseCharset(responseEntity);
        return responseEntity;
    }

    private void changeResponseCharset(ResponseEntity<String> responseEntity){
        HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(responseEntity.getHeaders());
        httpHeaders.add("Content-Type", "text/plain;charset=UTF-8");
    }
}
