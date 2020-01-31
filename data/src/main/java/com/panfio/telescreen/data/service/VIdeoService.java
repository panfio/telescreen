package com.panfio.telescreen.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panfio.telescreen.data.repository.YouTubeRepository;
import com.panfio.telescreen.model.YouTube;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VIdeoService {
    private final YouTubeRepository youTubeRepository;

    public VIdeoService(YouTubeRepository youTubeRepository) {
        this.youTubeRepository = youTubeRepository;
    }

    @KafkaListener(topics = "video", groupId = "data")
    public void listen(String msg) {
        YouTube activity = parseJson(msg);
        if (activity != null ) {
            youTubeRepository.save(activity);
        }
    }

    private YouTube parseJson(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(msg, YouTube.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
