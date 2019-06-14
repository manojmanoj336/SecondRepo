package com.mypack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;


@RibbonClient(name = "ping-a-server", configuration = RibbonConfiguration.class)
@SpringBootApplication
@EnableEurekaClient
public class TerminalClientApp {

  public static void main(String[] args) {
    SpringApplication.run(TerminalClientApp.class, args);
  }

 /* @PostConstruct
  void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }*/
}
