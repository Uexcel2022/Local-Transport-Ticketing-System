package uexcel.com.ltts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<String> handleCustomExcept(CustomException ex) {

        if(ex.getCode().equals("400")){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

        if(ex.getCode().equals("404")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }



}


