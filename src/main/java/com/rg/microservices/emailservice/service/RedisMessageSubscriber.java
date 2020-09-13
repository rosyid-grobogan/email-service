package com.rg.microservices.emailservice.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rg.microservices.emailservice.dto.EmailDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RedisMessageSubscriber implements MessageListener {
    private final EmailService emailService;
    private ObjectMapper objectMapper;

    @Autowired
    public RedisMessageSubscriber(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        log.debug("{}", message);
        String msg = message.toString();
        try {
            EmailDto emailDto = objectMapper.readValue(msg, EmailDto.class);
            emailService.sendEmail(emailDto.getTo(), emailDto.getSubject(), emailDto.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
