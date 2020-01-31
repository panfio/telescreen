package com.panfio.telescreen.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panfio.telescreen.data.repository.MessageRepository;
import com.panfio.telescreen.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "message", groupId = "data")
    public void listen(String msg) {
        Message activity = parseJson(msg);
        if (activity != null) {
            messageRepository.save(activity);
        }
    }

    private Message parseJson(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(msg, Message.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Saves message records in the database.
     *
     * @param message list of records
     */
    public void saveMessages(Message message) {
        Message dbMessage =
                messageRepository.findByLegacyIDAndCreated(
                        message.getLegacyID(), message.getCreated());
        if (dbMessage == null) {
            messageRepository.save(message);
        }
    }
}
