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
public class SurveyService {

    @Autowired
    SurveyRepository surveyRepository;

    @KafkaListener(topics = "eventTopic")
    public void onListener(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("##### listener : " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        DeliveryCompleted deliveryCompleted = null;
        try {
            deliveryCompleted = objectMapper.readValue(message, DeliveryCompleted.class);

            System.out.println(" #### type = " + deliveryCompleted.getType());

            /**
             * 배송 완료 이벤트시 설문조사 시작함
             */
            if( deliveryCompleted.getType().equals(DeliveryCompleted.class.getSimpleName())){

                Survey survey = new Survey();
                survey.setCustomerName(deliveryCompleted.getCustomerName());
                survey.setSurveyMessage("넉넉하게 수량 챙겨주세요");

                surveyRepository.save(survey);//Survery에 insert한다.

            }

        }catch (Exception e){

        }
    }
}
