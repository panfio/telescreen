package com.panfio.telescreen.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panfio.telescreen.data.repository.MediaRepository;
import com.panfio.telescreen.model.Media;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MediaService {
    private MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @KafkaListener(topics = "media", groupId = "data")
    public void listen(String msg) {
        Media activity = parseJson(msg);
        if (activity != null) {
            saveMediaRecord(activity);
        }
    }

    private Media parseJson(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(msg, Media.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Saves media record in the database.
     *
     * @param record record
     */
    public void saveMediaRecord(Media record) {
        // TODO find by path
        //only existing files are stored in the database
//        mediaRepository.deleteAll();
        mediaRepository.save(record);
    }
}
