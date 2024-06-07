package uexcel.com.ltts.exception;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import org.hibernate.HibernateException;
import org.hibernate.PersistentObjectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class CustomExceptHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { CustomException.class })
    public ResponseEntity<String> handleCustomExcept(CustomException ex) {

        return switch (ex.getCode()) {
            case "400" -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
            case "404" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            case "401" -> ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(ex.getMessage());
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        };

    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<String> handExceptions(Exception ex) {

        if(ex instanceof IllegalStateException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

        if(ex instanceof IllegalArgumentException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        if(ex instanceof NullPointerException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        if(ex instanceof PersistentObjectException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        if(ex instanceof HibernateException){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        if(ex instanceof ExpiredJwtException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        if(ex instanceof ServletException){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }



}


