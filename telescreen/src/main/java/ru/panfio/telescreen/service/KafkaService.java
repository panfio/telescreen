package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.util.Json;

@Slf4j
@Service
public class KafkaService implements MessageBus {
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Constructor.
     *
     * @param kafkaTemplate template
     */
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
}
