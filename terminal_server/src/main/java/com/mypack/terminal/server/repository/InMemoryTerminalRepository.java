package com.mypack.terminal.server.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.mypack.terminal.server.exception.TerminalNotAvailableException;

@Repository
public class InMemoryTerminalRepository {

  private final Map<String, Long> terminalIdToAvailability = new HashMap<>();
  private ScheduledExecutorService scheduledExecutorService;
  
public Map<String,Integer> map = new HashMap<String,Integer>(); 
  
  

  public Map<String, Integer> getMap() {
	return map;
}
  
  

  @Value("${available.terminals}")
  private String[] terminalIds;

  @Value("${terminal.available.period.seconds}")
  private long terminalAvailabilityPeriod;

  private boolean isInitialized;

  @PostConstruct
  void loadData() {
    for (String terminalId : terminalIds) {
      terminalIdToAvailability.put(terminalId, Long.MIN_VALUE);
      map.put(terminalId, 0);
    }
    
   
    
    
  }

   
  
  public String getTerminalId() {
	    synchronized (terminalIdToAvailability) {
	      final String availableTerminal = terminalIdToAvailability.entrySet().stream()
	              .filter(entry -> entry.getValue() < 0)
	              .findAny()
	              .map(Map.Entry::getKey)
	              .orElseThrow(() -> new TerminalNotAvailableException("terminal not available"));
	    	      return availableTerminal;
	    }
	  }
  
  
  public  void lockTerminalId(String availableTerminal,int seq)
  {
	
	    synchronized (terminalIdToAvailability) {
	
	    	  
	    	  
	    	  
	    	
	    	  if(map.containsKey(availableTerminal))
	    	  {
	    		Integer count =  map.get(availableTerminal);
	    		if(count==7){
	    			
	    			map.put(availableTerminal, 0);
	    			
	    			
	    		}else{
	    		map.put(availableTerminal, count+1);
	    		}
	    	  }else{
	    		  map.put(availableTerminal, seq);
	    		  
	    	  }
	    	
	    	
	    	
	    	 terminalIdToAvailability.put(availableTerminal, getAvailableTimeInNanoSecs() + System.nanoTime());
	         initializeCleanUp();
	    	
	    }
      
  }

  
  //
  
  
  
  
  
  
  
  private void initializeCleanUp() {
    if (!isInitialized) {
      scheduledExecutorService = Executors.newScheduledThreadPool(1);
      scheduledExecutorService.scheduleAtFixedRate(this::reactivateUnusedTerminalsAfterPeriod, terminalAvailabilityPeriod, terminalAvailabilityPeriod, TimeUnit.SECONDS);
      isInitialized = true;
    }
  }

  private void reactivateUnusedTerminalsAfterPeriod() {
    synchronized (terminalIdToAvailability) {
      terminalIdToAvailability.entrySet().stream()
              .filter(entry -> {
                final long timeElapsed = System.nanoTime() - entry.getValue();
                return timeElapsed > 0;
              })
              .forEach(entry -> terminalIdToAvailability.put(entry.getKey(), Long.MIN_VALUE));
    }
  }

  private long getAvailableTimeInNanoSecs() {
    return TimeUnit.SECONDS.toNanos(terminalAvailabilityPeriod);
  }

  public boolean isTerminalValid(String terminalId) {
    final Long aLong = terminalIdToAvailability.get(terminalId);
    if (aLong != null) {
      return (aLong - System.nanoTime()) < getAvailableTimeInNanoSecs();
    } else {
      return false;
    }
  }

  public void makeTerminalAvailable(String terminalId) {
    terminalIdToAvailability.put(terminalId, Long.MIN_VALUE);
  }

  @PreDestroy
  void preDestroy() {
    terminalIdToAvailability.clear();
    if (scheduledExecutorService != null) {
      scheduledExecutorService.shutdownNow();
    }
  }
}
