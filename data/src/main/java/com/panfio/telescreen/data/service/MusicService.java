package com.panfio.telescreen.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panfio.telescreen.data.repository.MusicRecordRepository;
import com.panfio.telescreen.model.Music;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MusicService {
    private final MusicRecordRepository musicRecordRepository;

    public MusicService(MusicRecordRepository musicRecordRepository) {
        this.musicRecordRepository = musicRecordRepository;
    }

    @KafkaListener(topics = "music", groupId = "data")
    public void listen(String msg) {
        Music activity = parseJson(msg);
        if (activity != null ) {
            musicRecordRepository.save(activity);
        }
    }

    private Music parseJson(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(msg, Music.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
