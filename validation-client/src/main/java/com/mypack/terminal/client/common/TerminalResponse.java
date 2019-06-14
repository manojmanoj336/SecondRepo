package com.mypack.terminal.client.common;

public class TerminalResponse {

  private String terminalId;
  private String desc;

  public TerminalResponse() {
    // Required Empty Constructor for (de)serialization
  }

  public TerminalResponse(String terminalId, String desc) {
    this.terminalId = terminalId;
    this.desc=desc;
  }

  public String getTerminalId() {
    return terminalId;
  }

public String getDesc() {
	return desc;
}

public void setDesc(String desc) {
	this.desc = desc;
}

public void setTerminalId(String terminalId) {
	this.terminalId = terminalId;
}
  
  
}
