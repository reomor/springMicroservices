package com.github.reomor.appws.exception;

import com.github.reomor.appws.model.response.CustomErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * handles all exceptions
     * @param exception
     * @param webRequest
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception exception, WebRequest webRequest) {
        CustomErrorMessage errorMessage = new CustomErrorMessage(exception.getLocalizedMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * handle specific exceptions
     * @param exception
     * @param webRequest
     * @return
     */
    @ExceptionHandler(value = {NullPointerException.class, OneMoreCustomException.class})
    public ResponseEntity<Object> handleNPE(Exception exception, WebRequest webRequest) {
        CustomErrorMessage errorMessage = new CustomErrorMessage("NPE - use Kotlin", LocalDateTime.now());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * handle my custom exception
     * @param exception
     * @param webRequest
     * @return
     */
    @ExceptionHandler(value = {MyCustomException.class})
    public ResponseEntity<Object> handleMyCustomException(MyCustomException exception, WebRequest webRequest) {
        CustomErrorMessage errorMessage = new CustomErrorMessage(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
