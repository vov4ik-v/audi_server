package com.volodymyrvasylyshyn.audi_server.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(value = CarNotFoundExeption.class)
    public final ResponseEntity<String> handlerCustomException(CarNotFoundExeption exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = AudiModelNotFoundException.class)
    public final ResponseEntity<String> handlerCustomException(AudiModelNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = UserExistException.class)
    public final ResponseEntity<String> handlerCustomException(UserExistException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }    @ExceptionHandler(value = PostNotFoundException.class)
    public final ResponseEntity<String> handlerCustomException(PostNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }    @ExceptionHandler(value = ImageNotFoundException.class)
    public final ResponseEntity<String> handlerCustomException(ImageNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = NewsNotFoundException.class)
    public final ResponseEntity<String> handlerCustomException(NewsNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public final ResponseEntity<String> handlerCustomException(ResourceNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = EmailNotFoundExeption.class)
    public final ResponseEntity<String> handlerCustomException(EmailNotFoundExeption exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
