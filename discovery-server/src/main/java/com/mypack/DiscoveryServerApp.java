package com.mypack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApp {
	public void grtDa()
	{
		System.out.println("hello");
	}
	
  public static void main(String[] args) {
    SpringApplication.run(DiscoveryServerApp.class, args);
  }
}
