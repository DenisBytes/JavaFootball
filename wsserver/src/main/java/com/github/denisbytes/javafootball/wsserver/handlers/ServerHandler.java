package com.github.denisbytes.javafootball.wsserver.handlers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.denisbytes.javafootball.wsserver.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class ServerHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Connection established with session ID: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            kafkaProducer.sendMessage(message.getPayload());
            System.out.println("Received JSON data from session ID " + session.getId() + ": " + message.getPayload());
        } catch (Exception e) {
            System.err.println("Error parsing JSON message: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Connection closed with session ID: " + session.getId() + ", Status: " + status);
    }

}