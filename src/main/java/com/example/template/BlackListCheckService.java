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
public class BlackListCheckService {

    @Autowired
    BlackListCheckRepository blackListCheckRepository;

    @KafkaListener(topics = "eventTopic")
    public void onListener(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("##### listener : " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Customer customer = new Customer();
        try {
            customer = objectMapper.readValue(message, Customer.class);

            System.out.println(" #### type = " + customer.getType());

            /**
             * 배송 완료 이벤트시 설문조사 시작함
             */
            if( customer.getType().equals(Customer.class.getSimpleName())){

                BlackListCheck blackListCheck = new BlackListCheck();
                blackListCheck.setCustomerName(customer.getCustomerName());
                blackListCheck.setBlackListMessage(customer.getCustomerName()+"is added to our blacklist.");

                blackListCheckRepository.save(blackListCheck);

            }

        }catch (Exception e){

        }
    }
}
