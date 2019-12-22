package ru.panfio.telescreen.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.*;
import ru.panfio.telescreen.repository.*;

import java.util.List;

@Service
public class PersistenceServiceImpl implements PersistenceService {

    private static final Logger log = LoggerFactory.getLogger(PersistenceServiceImpl.class);

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    WellbeingRepository wellbeingRepository;

    @Autowired
    ListenRecordRepository listenRecordRepository;

    @Autowired
    AutotimerRepository autotimerRepository;

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    TimeLogRepository timeLogRepository;

    @Autowired
    YouTubeRepository youTubeRepository;

    @Autowired
    CallRecordRepository callRecordRepository;

    @Override
    public void saveMessages(List<Message> messageList) {
        for (Message message : messageList) {
            Message dbMessage = messageRepository.findByLegacyIdAndCreatedTime(
                    message.getLegacyID(), message.getCreated());
            if (dbMessage == null) {
                messageRepository.save(message);
            }
        }
    }

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

    @Override
    public void saveListenRecords(List<ListenRecord> records) {
        listenRecordRepository.saveAll(records);
    }

    @Override
    public void saveAutotimerRecords(List<Autotimer> records) {
        autotimerRepository.saveAll(records);
    }

    @Override
    public void saveMediaRecords(List<Media> records) {
        //todo clear table before saving
        mediaRepository.saveAll(records);
    }

    @Override
    public void saveTimeLogRecords(List<TimeLog> records) {
        timeLogRepository.saveAll(records);
    }

    @Override
    public void saveYouTubeRecords(List<YouTube> records) {
        youTubeRepository.saveAll(records);
    }

    @Override
    public void saveCallRecords(List<CallRecord> records) {
        callRecordRepository.saveAll(records); //todo
    }
}
