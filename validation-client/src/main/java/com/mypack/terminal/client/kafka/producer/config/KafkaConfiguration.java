package com.mypack.terminal.client.kafka.producer.config;



import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.mypack.terminal.client.common.TerminalPayload;

/*import com.fasterxml.jackson.databind.JsonSerializer;*/
//import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
//import org.apache.kafka.connect.json.JsonSerializer;

//import kafka.tools.ConsoleProducer.ProducerConfig;

@Configuration
public class KafkaConfiguration {

	@Bean
	public ProducerFactory getProducerFactory()
	{
		Map<String,Object> config = new HashMap<String,Object>();
		
	
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory(config);
	}
	
	
	@Bean("ram")
	KafkaTemplate<String, TerminalPayload> getKafkaTemplate()
	{
		return new KafkaTemplate<>(getProducerFactory());
	}
	
}
