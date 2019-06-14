package com.mypack.terminal.client.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mypack.terminal.client.common.TerminalConfig;

@Component
public class ValidateResponse {

	@Autowired
	TerminalConfig config;
	private static Map<String, String> terminalIdToAvailability = new HashMap<>();
	private static  int minValueInclusive;
	private  static int maxValueExclusive;
	

	
	@PostConstruct
	  void loadData() {
	    for (String terminalId : config.getTerminalIds()) {
	      terminalIdToAvailability.put(terminalId, terminalId);     
	    }
	    minValueInclusive=config.getStart();
	    maxValueExclusive=config.getEnd();
	    
	}
	
	/* public boolean isTerminalValid(String terminalId) {
		    final Long aLong = terminalIdToAvailability.get(terminalId);
		    if (aLong != null) {
		      return (aLong - System.nanoTime()) < getAvailableTimeInNanoSecs();
		    } else {
		      return false;
		    }
		  }*/
	
	
	
	 public static boolean validateSequence(int value) {
	    return (value >= minValueInclusive && value < maxValueExclusive);
	  }
	
	 public  static boolean validateTerminalId(String terminalIdd) {
		 
		 return terminalIdToAvailability.containsKey(terminalIdd);
		 
		 
		 
		 //return true;
	  //  return (value >= minValueInclusive && value < maxValueExclusive);
	  }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
