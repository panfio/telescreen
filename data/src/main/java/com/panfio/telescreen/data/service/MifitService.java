package com.panfio.telescreen.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panfio.telescreen.data.repository.MiFitActivityRepository;
import com.panfio.telescreen.model.MiFitActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MifitService {
    private final MiFitActivityRepository miFitActivityRepo;

    public MifitService(MiFitActivityRepository miFitActivityRepository) {
        this.miFitActivityRepo = miFitActivityRepository;
    }

    @KafkaListener(topics = "mifit", groupId = "data")
    public void listen(String msg) {
        MiFitActivity activity = parseJson(msg);
        if (activity != null) {
            saveMiFitActivityRecord(activity);
        }
    }

    private MiFitActivity parseJson(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(msg, MiFitActivity.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Saves activity in the database.
     *
     * @param activity activity
     */
    public void saveMiFitActivityRecord(MiFitActivity activity) {
//        MiFitActivity dbRecord =
//                miFitActivityRepo.findByDate(activity.getDate());
//        if (dbRecord == null) {
//            miFitActivityRepo.save(activity);
//        }
//        activity.setId(dbRecord.getId());
        miFitActivityRepo.save(activity);
    }

}
