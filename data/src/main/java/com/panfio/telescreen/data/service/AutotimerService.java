package com.panfio.telescreen.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panfio.telescreen.data.repository.AutotimerRepository;
import com.panfio.telescreen.model.Autotimer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AutotimerService {
    private final AutotimerRepository autotimerRepository;

    public AutotimerService(AutotimerRepository autotimerRepository) {
        this.autotimerRepository = autotimerRepository;
    }

    @KafkaListener(topics = "autotimer", groupId = "data")
    public void listen(String msg) {
        Autotimer activity = parseJson(msg);
        if (activity != null) {
            save(activity);
        }
    }

    private Autotimer parseJson(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(msg, Autotimer.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Saves AutoTimer record in the database.
     *
     * @param record list of record
     */
    public void save(Autotimer record) {
        //todo
        autotimerRepository.save(record);
    }

//    /**
//     * Finds and returns the "AutoTimer" records for the period.
//     *
//     * @param from time
//     * @param to   time
//     * @return records
//     */
//    public Iterable<Autotimer> getAutotimerRecordsBetweenDates(
//            Instant from, Instant to) {
//        return autotimerRepository.findByStartTimeBetween(from, to)
//                .stream().filter(t -> {
//                    long duration = Duration.between(
//                            t.getStartTime(), t.getEndTime()).toMillis();
//                    // 10s < duration < 5h
//                    //CHECKSTYLE:OFF
//                    return duration > 10000 && duration < 18000000;
//                    //CHECKSTYLE:ON
//                }).collect(Collectors.toList());
//    }
}
