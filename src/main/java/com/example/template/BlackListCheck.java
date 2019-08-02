package com.example.template;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostPersist;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
public class BlackListCheck {

	
	 @Id
	 @GeneratedValue
	 Long id;
	 String customerName;
	 String blackListMessage;	 
	 
	 
	 public void blackListCheck() {
		 Customer customer = new Customer();
		 String customerName= customer.getCustomerName();
		 customer.blacklistcheck=true;
	 }
	 
	 @PostPersist
	    private void publishBlackListCheck() {
	        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);

	        ObjectMapper objectMapper = new ObjectMapper();
	        String json = null;

	        BlackListCheckCompleted blackListCheckCompleted = new BlackListCheckCompleted();
	        try {
	            BeanUtils.copyProperties(this, blackListCheckCompleted);
	            json = objectMapper.writeValueAsString(blackListCheckCompleted);
	        } catch (JsonProcessingException e) {
	            throw new RuntimeException("JSON format exception", e);
	        }

	        if( json != null ){
	            ProducerRecord producerRecord = new ProducerRecord<>("eventTopic", json);
	            kafkaTemplate.send(producerRecord);
	        }
	    }
	 
	 
	 
}
