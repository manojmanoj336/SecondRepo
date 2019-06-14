package com.mypack.terminal.server.kafka.consumer.listner;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypack.terminal.server.config.common.TerminalPayload;
import com.mypack.terminal.server.kafka.consumer.config.KafkaRespose;
import com.mypack.terminal.server.repository.InMemoryTerminalRepository;

@Service
public class KafkaConsumerListner {

	@Autowired
	InMemoryTerminalRepository repo;
	@Autowired
	InMemoryTerminalRepository repo1;
	ObjectMapper mapper=null;
	@KafkaListener(topics ="finalTopic", groupId = "group_id")
	public void consume(String message)
	{
		TerminalPayload obj=null;
		String terminalid=null;
		int seq;
		
		System.out.println("consumer message=======>" + message);
		 /*JSONObject obj = new JSONObject(message);	
		  * */
		
		mapper = new ObjectMapper();
		try{
		 obj = mapper.readValue(message, TerminalPayload.class);
		 terminalid = obj.getTerminalId();
		
		 
		// desc=obj.getDesc();
		 System.out.println("terminalId: "+ terminalid  /*"and seq: "+ seq*/) ;
		 System.out.println("-------------------------");
		 System.out.println("time: "+ obj.getTimestamp());
		 if( !terminalid.equalsIgnoreCase("null") || terminalid!=null )
		 {
			if(terminalid.contains("mk")){
				System.out.println("contains mk-----");
				repo1.makeTerminalAvailable(terminalid.replace("mk", ""));
				System.out.println("===> " +terminalid.replace("mk", "") +" is now availabl");
				
			}	else{	
				 seq= obj.getSequenceNo();
				 System.out.println("terminalId: "+ terminalid + "and seq: "+ seq) ;
			 repo.lockTerminalId(terminalid,seq);
			}
		 }
		 
		 obj=null;
		}catch(Exception e ){
			System.out.println("excpetion while parsing kafkajson string: "+ e.getMessage());
			}
		
		
		
		
	}
	
}
