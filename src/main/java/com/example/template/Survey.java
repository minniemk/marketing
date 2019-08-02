package com.example.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostPersist;

@Entity
public class Survey {

    @Id
    @GeneratedValue
    private Long id;
    private String customerName;
    private String surveyMessage;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSurveyMessage() {
        return surveyMessage;
    }

    public void setSurveyMessage(String surveyMessage) {
        this.surveyMessage = surveyMessage;
    }

    @PostPersist //Survery에 insert 일어난후에 호출...
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
