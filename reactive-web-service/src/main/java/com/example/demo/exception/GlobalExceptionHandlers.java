package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandlers {

    @ExceptionHandler(ClientException.class)
    public ProblemDetail handleClientException(ClientException exception) {
        if(exception.getStatus() == HttpStatus.FORBIDDEN){
            ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
            return problemDetail;
        }
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Error message from downstream:" + exception.getMessage());
        problemDetail.setProperty("timestamp", new Date());//custom property
        problemDetail.setProperty("errorCode", "1000");//custom property
        //in the response, type and instance are set by Spring to comply with RFC7807. Spring doesn't provide a way to remove them
        return problemDetail;
    }

    @ExceptionHandler(ServerException.class)
    public ProblemDetail handleServerException(ServerException exception) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail("Error message from downstream:" + exception.getMessage());
        problemDetail.setProperty("timestamp", new Date());//custom property
        problemDetail.setProperty("errorCode", "2000");//custom property
        //in the response, type and instance are set by Spring to comply with RFC7807. Spring doesn't provide a way to remove them
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAll(Exception exception) {
        exception.printStackTrace();
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail("Error message from downstream:" + exception.getMessage());
        problemDetail.setProperty("timestamp", new Date());//custom property
        problemDetail.setProperty("errorCode", "3000");//custom property
        //in the response, type and instance are set by Spring to comply with RFC7807. Spring doesn't provide a way to remove them
        return problemDetail;
    }

}
