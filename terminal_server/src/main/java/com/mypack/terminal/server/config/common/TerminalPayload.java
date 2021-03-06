package com.mypack.terminal.server.config.common;

import javax.validation.constraints.NotBlank;

public class TerminalPayload {

  @NotBlank
  private String terminalId;
  private int sequenceNo;
  private long timestamp;

  public TerminalPayload() {
  }

  public TerminalPayload(String terminalId, int sequenceNo, long timestamp) {
    this.terminalId = terminalId;
    this.sequenceNo = sequenceNo;
    this.timestamp = timestamp;
  }

  public String getTerminalId() {
    return terminalId;
  }

  public int getSequenceNo() {
    return sequenceNo;
  }

  public long getTimestamp() {
    return timestamp;
  }

public void setTerminalId(String terminalId) {
	this.terminalId = terminalId;
}

public void setSequenceNo(int sequenceNo) {
	this.sequenceNo = sequenceNo;
}

public void setTimestamp(long timestamp) {
	this.timestamp = timestamp;
}
  
  
  
  
  
}
