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
@Getter
@Setter
public class EmailSent {

    @Id
    @GeneratedValue
    private Long id;
    private String customerName;
    private String customeremail;
    private String productDesc="새로운 제품이 등록되었습니다.";
    private ProductModified product;
 
    @PostPersist
    private void publishProductRegistered() {
        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        EmailSentCompleted emailSentCompleted = new EmailSentCompleted();
        try {
            BeanUtils.copyProperties(this, emailSentCompleted);
            json = objectMapper.writeValueAsString(emailSentCompleted);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        if( json != null ){
            ProducerRecord producerRecord = new ProducerRecord<>("eventTopic", json);
            kafkaTemplate.send(producerRecord);
        }
    }
}
