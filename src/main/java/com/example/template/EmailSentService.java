package com.example.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class EmailSentService {

    @Autowired
    EmailSentRepository emailSentRepository;

    @KafkaListener(topics = "eventTopic")
    public void onListener(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("##### listener : " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ProductModified productModified = null;
        try {
            productModified = objectMapper.readValue(message, ProductModified.class);

            System.out.println(" #### type = " + productModified.getType());

            /**
             * 수량 변경 시 알림 메일 발송
             */
            if( productModified.getType().equals(ProductModified.class.getSimpleName())){

                EmailSent email = new EmailSent();
                
                email.setCustomerName("Song Jun");
                email.setCustomeremail("songjun@naver.com");
                email.setProductDesc("새로운 제품이 등록되었습니다.");
                
       
                
                emailSentRepository.save(email);

            }

        }catch (Exception e){

        }
    }
}
