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

@Setter
@Getter
@Entity
public class blackListCheck {

	
	 @Id
	 @GeneratedValue
	 private Long id;
	 private String blackListMessage;
	 private Customer customer;
	 
	 @PostPersist
	    private void publishDeliveryStart() {
	        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);

	        ObjectMapper objectMapper = new ObjectMapper();
	        String json = null;

	        SurveyCompleted surveyCompleted = new SurveyCompleted();
	        try {
	            BeanUtils.copyProperties(this, surveyCompleted);
	            json = objectMapper.writeValueAsString(surveyCompleted);
	        } catch (JsonProcessingException e) {
	            throw new RuntimeException("JSON format exception", e);
	        }

	        if( json != null ){
	            ProducerRecord producerRecord = new ProducerRecord<>("eventTopic", json);
	            kafkaTemplate.send(producerRecord);
	        }
	    }
	 
	 
	 
}
