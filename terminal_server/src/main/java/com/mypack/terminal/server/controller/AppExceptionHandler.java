package com.mypack.terminal.server.controller;




import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
/*import org.springframework.http.HttpStatus;*/
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mypack.terminal.server.config.common.MessageResponse;



@ControllerAdvice
public class AppExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(AppExceptionHandler.class);

  @ExceptionHandler
  public ResponseEntity<MessageResponse> handleExceptions(Exception exception) {
    log.error("Error occurred {}", exception.getMessage());
    final HttpStatus httpStatus = resolveAnnotatedResponseStatus(exception);
    final MessageResponse errorResponse = new MessageResponse(exception.getMessage());
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
