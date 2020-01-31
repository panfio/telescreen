package com.panfio.telescreen.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panfio.telescreen.data.repository.CallRecordRepository;
import com.panfio.telescreen.model.Call;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CallService {
    private final CallRecordRepository callRecordRepository;

    public CallService(CallRecordRepository callRecordRepository) {
        this.callRecordRepository = callRecordRepository;
    }

    @KafkaListener(topics = "call", groupId = "data")
    public void listen(String msg) {
        Call activity = parseJson(msg);
        if (activity != null) {
            saveCallRecords(activity);
        }
    }

    private Call parseJson(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(msg, Call.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Saves call record in the database.
     *
     * @param record record
     */
    public void saveCallRecords(Call record) {
        callRecordRepository.save(record); //todo
    }
}
