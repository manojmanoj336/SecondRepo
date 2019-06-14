package com.mypack.terminal.client.controller;


import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientResponseException;

import com.mypack.terminal.client.common.MessageResponse;



@ControllerAdvice
public class ClientExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(ClientExceptionHandler.class);

  @ExceptionHandler
  public ResponseEntity<?> handleExceptions(Exception exception) {
    log.error("Error occurred {}", exception.getMessage());
    if (exception instanceof RestClientResponseException) {
      final String responseBody = ((RestClientResponseException) exception).getResponseBodyAsString();
      return new ResponseEntity<>(responseBody, HttpStatus.SERVICE_UNAVAILABLE);
    }
    final MessageResponse errorResponse = new MessageResponse(exception.getMessage());
    final HttpStatus httpStatus = resolveAnnotatedResponseStatus(exception);
    return new ResponseEntity<>(errorResponse, httpStatus);
  }

private HttpStatus resolveAnnotatedResponseStatus(Exception exception) {
	// TODO Auto-generated method stub
	final ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);
    if (annotation != null) {
      return annotation.value();
    } else {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }

}
