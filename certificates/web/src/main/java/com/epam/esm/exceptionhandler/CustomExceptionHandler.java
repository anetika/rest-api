package com.epam.esm.exceptionhandler;

import com.epam.esm.exception.PaginationException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.translator.Translator;
import com.epam.esm.util.CharsetUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class CustomExceptionHandler {
    private final Translator translator;
    private final CharsetUtil charsetUtil;

    public CustomExceptionHandler(Translator translator, CharsetUtil charsetUtil) {
        this.translator = translator;
        this.charsetUtil = charsetUtil;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleResourceNotFoundException(){
        String message = translator.toLocale("error404_message");
        ExceptionMessage mes = new ExceptionMessage(message, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value());
        ResponseEntity<ExceptionMessage> responseEntity = new ResponseEntity<>(mes, HttpStatus.NOT_FOUND);
        charsetUtil.changeExceptionResponseCharset(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionMessage> handleHttpMessageNotReadableException() {
        String message = translator.toLocale("message_not_readable");
        ExceptionMessage mes = new ExceptionMessage(message, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        ResponseEntity<ExceptionMessage> responseEntity = new ResponseEntity<>(mes, HttpStatus.BAD_REQUEST);
        charsetUtil.changeExceptionResponseCharset(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        String message = translator.toLocale("validation_exception_message");
        ExceptionMessage mes = new ExceptionMessage(String.format(message, Objects.requireNonNull(e.getFieldError()).getField()), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        ResponseEntity<ExceptionMessage> responseEntity = new ResponseEntity<>(mes, HttpStatus.BAD_REQUEST);
        charsetUtil.changeExceptionResponseCharset(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionMessage> handleIllegalArgumentException(){
        String message = translator.toLocale("argument_exception_message");
        ExceptionMessage mes = new ExceptionMessage(message, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        ResponseEntity<ExceptionMessage> responseEntity = new ResponseEntity<>(mes, HttpStatus.BAD_REQUEST);
        charsetUtil.changeExceptionResponseCharset(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(PaginationException.class)
    public ResponseEntity<ExceptionMessage> handlePaginationException() {
        String message = translator.toLocale("pagination_exception_message");
        ExceptionMessage mes = new ExceptionMessage(message, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        ResponseEntity<ExceptionMessage> responseEntity = new ResponseEntity<>(mes, HttpStatus.BAD_REQUEST);
        charsetUtil.changeExceptionResponseCharset(responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionMessage> handleRuntimeException(){
        String message = translator.toLocale("error500_message");
        ExceptionMessage mes = new ExceptionMessage(message, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value());
        ResponseEntity<ExceptionMessage> responseEntity = new ResponseEntity<>(mes, HttpStatus.INTERNAL_SERVER_ERROR);
        charsetUtil.changeExceptionResponseCharset(responseEntity);
        return responseEntity;
    }


}
