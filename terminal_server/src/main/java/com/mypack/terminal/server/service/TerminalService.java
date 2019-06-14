package com.mypack.terminal.server.service;

import java.util.Map;

public interface TerminalService {

  String getAvailableTerminalId();

  void processRequest(String terminalId, int sequenceNo, long timestamp) throws Exception;
  void processRequests(String terminalId) throws Exception;
  
  Map<String,Integer> getSquence();
}
