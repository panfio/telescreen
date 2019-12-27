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

    //CHECKSTYLE:OFF
    private final TimeLogRepository timeLogRepository;

    private final YouTubeRepository youTubeRepository;

    private final ListenRecordRepository listenRecordRepository;

    private final WellbeingRepository wellbeingRepository;

    private final CallRecordRepository callRecordRepository;

    private final AutotimerRepository autotimerRepository;

    private final MessageRepository messageRepository;

    @Autowired
    public AppService(TimeLogRepository timeLogRepository,
                      YouTubeRepository youTubeRepository,
                      ListenRecordRepository listenRecordRepository,
                      WellbeingRepository wellbeingRepository,
                      CallRecordRepository callRecordRepository,
                      AutotimerRepository autotimerRepository,
                      MessageRepository messageRepository) {
        this.timeLogRepository = timeLogRepository;
        this.youTubeRepository = youTubeRepository;
        this.listenRecordRepository = listenRecordRepository;
        this.wellbeingRepository = wellbeingRepository;
        this.callRecordRepository = callRecordRepository;
        this.autotimerRepository = autotimerRepository;
        this.messageRepository = messageRepository;
    }

    public Iterable<YouTube> getYouTubeRecordsBetweenDates(
            LocalDateTime t1, LocalDateTime t2) {
        return youTubeRepository.getAllBetweenDates(t1, t2);
    }

    public Iterable<TimeLog> getTimeLogRecordsBetweenDates(
            LocalDateTime t1, LocalDateTime t2) {
        return timeLogRepository.getAllBetweenDates(t1, t2);
    }

    public Iterable<ListenRecord> getListenRecordsBetweenDates(
            LocalDateTime t1, LocalDateTime t2) {
        return listenRecordRepository.getAllBetweenDates(t1, t2);
    }

    public Iterable<Wellbeing> getWellbeingBetweenDates(
            LocalDateTime t1, LocalDateTime t2) {
        return wellbeingRepository.getAllBetweenDates(t1, t2).stream()
                .filter(t -> Duration
                        .between(t.getStartTime(), t.getEndTime())
                        .toMillis() > 5000)
                .collect(Collectors.toList());
    }

    public Iterable<CallRecord> getCallHistoryBetweenDates(
            LocalDateTime t1, LocalDateTime t2) {
        return callRecordRepository.getAllBetweenDates(t1, t2);
    }

    public Iterable<Autotimer> getAutotimerRecordsBetweenDates(
            LocalDateTime t1, LocalDateTime t2) {
        return autotimerRepository.getAllBetweenDates(t1, t2)
                .stream().filter(t -> {
                    long duration = Duration.between(
                            t.getStartTime(), t.getEndTime()).toMillis();

                    // 10s < duration < 5h
                    return duration > 10000 && duration < 18000000;
                }).collect(Collectors.toList());
    }

    public Iterable<Message> getMessageHistoryBetweenDates(
            LocalDateTime t1, LocalDateTime t2) {
        return messageRepository.getAllBetweenDates(t1, t2);
    }

    //CHECKSTYLE:ON
}
