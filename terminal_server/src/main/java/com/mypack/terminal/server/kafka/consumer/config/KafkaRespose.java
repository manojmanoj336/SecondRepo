package com.mypack.terminal.server.kafka.consumer.config;

public class KafkaRespose {

	  private String terminalId;
	  private String desc;

	  public KafkaRespose() {
	    // Required Empty Constructor for (de)serialization
	  }

	  public KafkaRespose(String terminalId, String desc) {
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
