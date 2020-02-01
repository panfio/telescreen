package com.panfio.telescreen.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panfio.telescreen.data.repository.TimeLogRepository;
import com.panfio.telescreen.model.TimeLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TimeLogService {
    private final TimeLogRepository timeLogRepository;

    public TimeLogService(TimeLogRepository timeLogRepository) {
        this.timeLogRepository = timeLogRepository;
    }

    @KafkaListener(topics = "timelog", groupId = "data")
    public void listen(String msg) {
        TimeLog activity = parseJson(msg);
        if (activity != null ) {
            timeLogRepository.save(activity);
        }
    }

    private TimeLog parseJson(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(msg, TimeLog.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
