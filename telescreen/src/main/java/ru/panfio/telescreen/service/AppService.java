package ru.panfio.telescreen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.*;
import ru.panfio.telescreen.repository.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class AppService {
    @Autowired
    TimeLogRepository timeLogRepository;

    @Autowired
    YouTubeRepository youTubeRepository;

    @Autowired
    ListenRecordRepository listenRecordRepository;

    @Autowired
    WellbeingRepository wellbeingRepository;

    @Autowired
    CallRecordRepository callRecordRepository;

    public Iterable<TimeLog> findAllTimelogs() {
        return timeLogRepository.findAll();
    }

    public Iterable<YouTube> findAllYouTube() {
        return youTubeRepository.findAll();
    }

    public Iterable<YouTube> getYouTubeRecordsBetweenDates(LocalDateTime t1, LocalDateTime t2) {
        return youTubeRepository.getAllBetweenDates(t1, t2);
    }

    public Iterable<TimeLog> getTimeLogRecordsBetweenDates(LocalDateTime t1, LocalDateTime t2) {
        return timeLogRepository.getAllBetweenDates(t1, t2);
    }

    public Iterable<ListenRecord> getListenRecordsBetweenDates(LocalDateTime t1, LocalDateTime t2) {
        return listenRecordRepository.getAllBetweenDates(t1, t2);
    }

    public Iterable<Wellbeing> getWellbeingBetweenDates(LocalDateTime t1, LocalDateTime t2) {
        return wellbeingRepository.getAllBetweenDates(t1, t2).stream()
                .filter(t -> Duration
                        .between(t.getStartTime(), t.getEndTime())
                        .toMillis() > 5000)
                .collect(Collectors.toList());
    }

    public Iterable<CallRecord> getCallHistoryBetweenDates(LocalDateTime t1, LocalDateTime t2) {
        return callRecordRepository.getAllBetweenDates(t1, t2);
    }
}
