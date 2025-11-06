package projectCooking.HandleException;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import projectCooking.Exception.DulicateUserException;



@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(DulicateUserException.class)
    public ResponseEntity<?> handleDuplicateUser(DulicateUserException ex) {
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    	        .body(Collections.singletonMap("error", ex.getMessage()));
    }
}
