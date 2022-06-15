package com.virtualmarathon.stravamicroservice.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;


@ControllerAdvice
public class ErrorHandlers {

    @ExceptionHandler
    public ResponseEntity<ErrorObject> handleException(StravaInfoException e){
        ErrorObject error=new ErrorObject(e.getStatus().value(), e.getMessage(), LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
        return new ResponseEntity<>(error,e.getStatus());
    }

//    @ExceptionHandler
//    public ResponseEntity<ErrorObject> handleException(FeignException e){
//        ErrorObject error=new ErrorObject(HttpStatus.FAILED_DEPENDENCY.value(), e.contentUTF8(), LocalDateTime.now());
//        return new ResponseEntity<>(error,HttpStatus.FAILED_DEPENDENCY);
//    }
}
