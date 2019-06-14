package com.mypack.terminal.server.config.common;

public class MessageResponse {

  private String message;

  public MessageResponse() {
    //Required Empty Constructor for (de)serialization
  }

  public MessageResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
