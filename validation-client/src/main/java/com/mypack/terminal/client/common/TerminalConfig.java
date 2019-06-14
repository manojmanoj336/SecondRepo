package com.mypack.terminal.client.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:terminal.properties")
public class TerminalConfig {

  @Value("${sequence.start}")
  private int start;    

  @Value("${sequence.end}")
  private int end;
  
  @Value("${available.terminals}")
  private String[] terminalIds;
  
  

  public String[] getTerminalIds() {
	return terminalIds;
}

public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }
}
