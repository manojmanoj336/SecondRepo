package com.mypack.terminal.client.service;

import java.util.Map;

public interface TerminalService {

  String processRequest(Map<String, Object> payload);
}
