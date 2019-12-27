package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.*;
import ru.panfio.telescreen.repository.*;

import java.util.List;

@Slf4j
@Service
public class PersistenceServiceImpl implements PersistenceService {

    private final MessageRepository messageRepository;

    private final WellbeingRepository wellbeingRepository;

    private final ListenRecordRepository listenRecordRepository;

    private final AutotimerRepository autotimerRepository;

    private final MediaRepository mediaRepository;

    private final TimeLogRepository timeLogRepository;

    private final YouTubeRepository youTubeRepository;

    private final CallRecordRepository callRecordRepository;

    //CHECKSTYLE:OFF
    @Autowired
    public PersistenceServiceImpl(
            MessageRepository messageRepository,
            WellbeingRepository wellbeingRepository,
            ListenRecordRepository listenRecordRepository,
            AutotimerRepository autotimerRepository,
            MediaRepository mediaRepository,
            TimeLogRepository timeLogRepository,
            YouTubeRepository youTubeRepository,
            CallRecordRepository callRecordRepository) {
        this.messageRepository = messageRepository;
        this.wellbeingRepository = wellbeingRepository;
        this.listenRecordRepository = listenRecordRepository;
        this.autotimerRepository = autotimerRepository;
        this.mediaRepository = mediaRepository;
        this.timeLogRepository = timeLogRepository;
        this.youTubeRepository = youTubeRepository;
        this.callRecordRepository = callRecordRepository;
    }
    //CHECKSTYLE:ON

    /**
     * Saves message records in the database.
     *
     * @param records list of records
     */
    @Override
    public void saveMessages(List<Message> records) {
        for (Message message : records) {
            Message dbMessage =
                    messageRepository.findByLegacyIdAndCreatedTime(
                            message.getLegacyID(), message.getCreated());
            if (dbMessage == null) {
                messageRepository.save(message);
            }
        }
    }

    /**
     * Saves message records in the database.
     *
     * @param records list of records
     */
    @Override
    public void saveWellbeingRecords(List<Wellbeing> records) {
        for (Wellbeing wellbeing : records) {
            Wellbeing dbRecord = wellbeingRepository.findWellbeingRecord(
                    wellbeing.getStartTime(), wellbeing.getEndTime());
            if (dbRecord == null) {
                wellbeingRepository.save(wellbeing);
            }
        }
    }

    /**
     * Saves listened records in the database.
     *
     * @param records list of records
     */
    @Override
    public void saveListenRecords(List<ListenRecord> records) {
        listenRecordRepository.saveAll(records);
    }

    /**
     * Saves AutoTimer records in the database.
     *
     * @param records list of records
     */
    @Override
    public void saveAutotimerRecords(List<Autotimer> records) {
        autotimerRepository.saveAll(records);
    }

    /**
     * Saves media records in the database.
     *
     * @param records list of records
     */
    @Override
    public void saveMediaRecords(List<Media> records) {
        //todo clear table before saving
        mediaRepository.saveAll(records);
    }

    /**
     * Saves timelog records in the database.
     *
     * @param records list of records
     */
    @Override
    public void saveTimeLogRecords(List<TimeLog> records) {
        timeLogRepository.saveAll(records);
    }

    /**
     * Saves view records in the database.
     *
     * @param records list of records
     */
    @Override
    public void saveYouTubeRecords(List<YouTube> records) {
        youTubeRepository.saveAll(records);
    }

    /**
     * Saves call records in the database.
     *
     * @param records list of records
     */
    @Override
    public void saveCallRecords(List<CallRecord> records) {
        callRecordRepository.saveAll(records); //todo
    }
}
