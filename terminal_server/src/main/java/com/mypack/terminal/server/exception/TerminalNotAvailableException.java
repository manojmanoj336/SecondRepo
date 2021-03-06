package com.mypack.terminal.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class TerminalNotAvailableException extends RuntimeException {

  public TerminalNotAvailableException(String message) {
    super(message);
  }
}
