package com.panfio.telescreen.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panfio.telescreen.model.Wellbeing;
import com.panfio.telescreen.data.repository.WellbeingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WellbeingService {
    private final WellbeingRepository wellbeingRepository;

    public WellbeingService(WellbeingRepository wellbeingRepository) {
        this.wellbeingRepository = wellbeingRepository;
    }

    @KafkaListener(topics = "wellbeing", groupId = "data")
    public void listen(String msg) {
        Wellbeing activity = parseJson(msg);
        if (activity != null ) {
            saveWellbeingRecord(activity);
        }
    }

    private Wellbeing parseJson(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(msg, Wellbeing.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public void saveWellbeingRecord(Wellbeing wellbeing) {
        Wellbeing dbRecord = wellbeingRepository.findByStartTimeAndEndTime(
                wellbeing.getStartTime(), wellbeing.getEndTime());
        if (dbRecord == null) {
            wellbeingRepository.save(wellbeing);
        }
    }

    /**
     * Finds and returns message records for the period.
     * //TODO rename to app activity
     *
     * @param from time
     * @param to   time
     * @return records
     */
//    public List<Wellbeing> getWellbeingBetweenDates(
//            Instant from, Instant to) {
//        return wellbeingRepository.findByStartTimeBetween(from, to).stream()
//                .filter(t -> Duration
//                        .between(t.getStartTime(), t.getEndTime())
//                        .toMillis() > MIN_USAGE_TIME)
//                .collect(Collectors.toList());
//    }
}
