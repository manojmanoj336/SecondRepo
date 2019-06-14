package com.mypack.terminal.client.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mypack.terminal.client.common.TerminalPayload;
import com.mypack.terminal.client.service.TerminalService;
import com.mypack.terminal.client.util.ValidateResponse;

@RestController
@RequestMapping(value = "/client", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TerminalController {

	@Autowired
	@Resource(name = "ram")
	KafkaTemplate<String, TerminalPayload> kafkaTemplates; // for json mesage

	
	private static final String TOPIC = "finalTopic";

	private final TerminalService terminalService;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private LoadBalancerClient loadBalancer;

	public TerminalController(TerminalService terminalService) {
		this.terminalService = terminalService;
	}

	@GetMapping("/getTerminalId")
	public ResponseEntity<TerminalPayload> getAvailableTerminalId() {

		String statuscode = null;

		String errorcode = null;
		String info = null;
		boolean status = true;
		String result = null;
		String desc = null;
		TerminalPayload payload = null;
		ResponseEntity<TerminalPayload> resulttemp = null;
		ResponseEntity<String> resultt = null;
		ServiceInstance serviceInstance=null;

		String serviceId = "terminal-server".toLowerCase();

		List<ServiceInstance> instances = this.discoveryClient.getInstances(serviceId);

		// Create a RestTemplate.
		RestTemplate restTemplate = new RestTemplate();

		try {

			 serviceInstance = this.loadBalancer.choose(serviceId);

			String url = "http://" + /* serviceInstance.getHost() */"localhost" + ":" + serviceInstance.getPort()
					+ "/v1/terminal/server/availableTerminalId";

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

			try {
				resulttemp = restTemplate.exchange(url, HttpMethod.GET, entity, TerminalPayload.class);
				statuscode = resulttemp.getStatusCode().toString();
				System.out.println("statuscodedddd- " + statuscode);
			} catch (Exception e) {

				info = e.getMessage();
				if (info.contains("503")) {
					info = "SERVICE NOT AVAILABLE";
				} else {
					info = "INTERNAL SERIVER ERROR";
				}

				TerminalPayload t = new TerminalPayload();
				t.setTerminalId(info);
				status = false;
				return new ResponseEntity<TerminalPayload>(
						/* new TerminalPayload(info,0,0) */t, HttpStatus.SERVICE_UNAVAILABLE);
			}

		} catch (Exception e) {

			TerminalPayload t = new TerminalPayload();
			t.setTerminalId("INTERNAL SERIVER ERROR");

			System.out.println(e.getMessage());

			status = false;
			return new ResponseEntity<TerminalPayload>(t, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		if (status) {

			payload = resulttemp.getBody();

			statuscode = resulttemp.getStatusCode().toString();
			System.out.println("statuscode- 3" + statuscode);

			if (statuscode.contains("200")) {
				if (!payload.getTerminalId().equalsIgnoreCase("null") || payload.getTerminalId() != null)
					
					System.out.println("TERMINAL ID: "+payload.getTerminalId() +" | SEQUENCENo: " + payload.getSequenceNo()+" IS NOT CURRENTY BEING LOCKED");
					/*the terminal returns by the server is not currently being locked*/
				
			//------VALIDATE SEQUENCE HERE
				if(ValidateResponse.validateSequence(payload.getSequenceNo()))
				{
					System.out.println("SEQUENCE IS VALID");
				}else{
					System.out.println("SEQUENCE IS NOT VALID");
				}
				
				//-------TERMINAL GOING TO BE LOCK
			
				System.out.println("TERMINAL ID: "+payload.getTerminalId() +" | SEQUENCENo: " + payload.getSequenceNo()+" IS GOING TO BE LOCKED");
				kafkaTemplates.send(TOPIC, payload);
				//TERMINAL IS LOCKED
				System.out.println("TERMINAL ID: "+payload.getTerminalId() +" | SEQUENCENo: " + payload.getSequenceNo()+" IS LOCKED");
				
				/*synchronized(this){*/
				
				try {
					Thread.sleep(10000); //sleep for 10 seconds
					payload.setTerminalId(payload.getTerminalId().concat("mk"));
	/*HERE TERMINAL GOING TO BE UNLOCKED AFTER 10 SECOND 
	 * =>	as PUT method not work here, because we can have N number of terminal server
	 * =>	so, making N number of PUT request on N of server should not work, here CONSUME AND producer should work!!!*/					
					kafkaTemplates.send(TOPIC, payload); //
					System.out.println("client: terminal unlocked: "+ payload.getTerminalId().replace("mk", ""));
					//terminal unlocked
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			//	}
				
				
			}
		}
		payload.setTerminalId(payload.getTerminalId().replace("mk", "| port:"+ String.valueOf(serviceInstance.getPort())));

		return new ResponseEntity<TerminalPayload>(payload, HttpStatus.OK);

		
		
	}


}
