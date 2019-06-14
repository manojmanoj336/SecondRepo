package com.mypack.terminal.server.controller;


import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mypack.terminal.server.config.common.TerminalPayload;
import com.mypack.terminal.server.exception.TerminalNotAvailableException;
import com.mypack.terminal.server.service.TerminalService;

import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "v1/terminal/server", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TerminalController {

  int i=0;
	private final TerminalService terminalService;
	 TerminalPayload payload = null;
	 String info;
	 boolean status=true;
	 boolean stop=false;

  public TerminalController(TerminalService terminalService) {
    this.terminalService = terminalService;
  }

  /*@GetMapping("availableTerminalId")
  public ResponseEntity<TerminalResponse> getAvailableTerminalId() {
    final String terminalId = terminalService.getAvailableTerminalId();
    return ResponseEntity.ok(new TerminalResponse(terminalId));
  }*/
  
  
 /* @GetMapping("/availableTerminalId")
  public String getAvailableTerminalId() {
    final String terminalId = terminalService.getAvailableTerminalId();
    return terminalId;
  }*/ 
  @GetMapping("/availableTerminalId")
  public ResponseEntity<TerminalPayload> getAvailableTerminalId() {
	  
	 synchronized(this){
	  
	  info="";
	   payload = new TerminalPayload();
	   /*final*/ String terminalId=null;
	   try{
		   terminalId= terminalService.getAvailableTerminalId();
		   status=true;
	    
	    
	   }catch(Exception e )
	   {
		   status=false;
		   info=e.getMessage();
		 
	   }
	 
	   if(info.equals("terminal not available"))
	   {
		  // long startTime=0;
		   stop=true;
		   long startTime = System.nanoTime(); 
		  while(stop){		
		   try{
		  			   
		  terminalId= terminalService.getAvailableTerminalId();
		  stop=false; 
		  status=true;
		  break;   
		   }catch(Exception e)
		   {
			   status=false;
			   System.out.println("Exception: =>>"+ e.getMessage());
			  // return new ResponseEntity<TerminalPayload>(payload, HttpStatus.SERVICE_UNAVAILABLE);
			   }
		   long endTime   = System.nanoTime();
		   long totalTime = endTime - startTime;
		   double elapsedTimeInSecond = (double)totalTime/1_000_000_000;
		   if(Math.round(elapsedTimeInSecond)==30)
		   {
			   status=false;
			   return new ResponseEntity<TerminalPayload>(payload, HttpStatus.SERVICE_UNAVAILABLE); 
		   }
		   
		  
		  }  
	   }
	 
	   if(status){
	   
	   try{
		   System.out.println("================2");
		    
			   Map<String,Integer> ok=  terminalService.getSquence();
			   System.out.println("IN CONTROLLER: terminalid: " +terminalId +" seq: "+ ok.get(terminalId) );
			   
			     payload.setSequenceNo(ok.get(terminalId));
				 payload.setTerminalId(terminalId);
				 final long timestamp = System.currentTimeMillis();
				 payload.setTimestamp(timestamp);
				
	   }catch(Exception e){System.out.println("fff: exce: "+ e);}
	  // return new ResponseEntity<TerminalPayload>(new TerminalPayload(), HttpStatus.INTERNAL_SERVER_ERROR);
	   
	   }
	   System.out.println("end------");
	 }
	   return new ResponseEntity<TerminalPayload>(payload, HttpStatus.OK);
		 
		   
		// return ResponseEntity.accepted().headers(headers).body(payload);
	  }
  
  
  
  
  
  
  
  
  public String getAvailableTerminalIds() {
	  TerminalPayload payload;
	    final String terminalId = terminalService.getAvailableTerminalId();
	 	    
	   if(i!=0){
	    
		   Map<String,Integer> ok=  terminalService.getSquence();
		   
	   }
	   i=1;
	    return terminalId;
	  }
  
  
  
  
  
  
  
  
  
  
  
  
  

  @PostMapping
  public ResponseEntity<String> processTerminalRequest(@RequestBody @Valid TerminalPayload payload) throws Exception {
    terminalService.processRequest(payload.getTerminalId(), payload.getSequenceNo(), payload.getTimestamp());
    return ResponseEntity.ok().build();
  }
}
