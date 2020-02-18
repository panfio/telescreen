package ru.panfio.telescreen.handler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.util.Json;

import java.util.List;

@Slf4j
@Service
public class KafkaService implements MessageBus {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    @Override
    public void send(String topic, Object message) {
        kafkaTemplate.send(topic, Json.toJson(message));
    }

    @Override
    public <T> void sendAll(String topic, List<T> messages) {
        messages.forEach(message -> send(topic, message));
    }
}
