

package com.mypack.terminal.server.service.impl;


import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mypack.terminal.server.config.common.SequenceGenerator;
import com.mypack.terminal.server.config.common.TerminalConfig;
import com.mypack.terminal.server.config.common.TerminalPayload;
import com.mypack.terminal.server.exception.InvalidRequestException;
import com.mypack.terminal.server.repository.InMemoryTerminalRepository;
import com.mypack.terminal.server.service.TerminalService;

@Service
public class InMemoryTerminalService implements TerminalService {

	private final SequenceGenerator sequenceGenerator;
  private static final Logger log = LoggerFactory.getLogger(InMemoryTerminalService.class);

  private final TerminalConfig terminalConfig;
  private final InMemoryTerminalRepository terminalRepository;

  @Value("${terminal.processing.time.seconds}")
  private long terminalProcessingTime;

  @Autowired
  public InMemoryTerminalService(TerminalConfig terminalConfig, InMemoryTerminalRepository terminalRepository, SequenceGenerator seq) {
    this.terminalConfig = terminalConfig;
    this.terminalRepository = terminalRepository;
    this.sequenceGenerator=seq;
  }

  private static boolean between(int value, int minValueInclusive, int maxValueExclusive) {
    return (value >= minValueInclusive && value < maxValueExclusive);
  }

  @Override
  public String getAvailableTerminalId() {
    return terminalRepository.getTerminalId();
  }

  @Override
  public Map<String, Integer>  getSquence() {
    return terminalRepository.getMap();
  }
  
  
  @Override
  public void processRequest(String terminalId, int sequenceNo, long timestamp) throws Exception {
    log.info("Terminal Payload: terminalId={} sequence number={} timestamp={}", terminalId, sequenceNo, timestamp);
    if (isNotValidRequest(sequenceNo, terminalId)) {
      throw new InvalidRequestException(String.format("sequenceNo(%d) is either not in range or terminal(%s) not locked for use", sequenceNo, terminalId));
    }
    try {
      TimeUnit.SECONDS.sleep(terminalProcessingTime);
    } finally {
      terminalRepository.makeTerminalAvailable(terminalId);
    }
  }
  
  
  //----
  
  
  @Override
  public void processRequests(String terminalId) throws Exception {

	 
	    final int sequenceId = sequenceGenerator.getNext();
	    final long timestamp = System.currentTimeMillis();
	    final TerminalPayload terminalPayload = new TerminalPayload(terminalId, sequenceId, timestamp);

	  /*  return retryTemplate.execute(
	            (RetryCallback<String, RestClientException>) context ->
	                    restTemplate.postForObject(SERVER_TERMINAL_URL, terminalPayload, String.class));*/
  }
  
  
  
  
  
  
  
  
  //----
  

  private boolean isNotValidRequest(int sequenceNo, String terminalId) {
    return !between(sequenceNo, terminalConfig.getStart(), terminalConfig.getEnd()) || !terminalRepository.isTerminalValid(terminalId);
  }
}
