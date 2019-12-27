package ru.panfio.telescreen.service;

import ru.panfio.telescreen.model.*;

import java.util.List;

public interface PersistenceService {
    //CHECKSTYLE:OFF
    void saveMessages(List<Message> messageList);
    void saveWellbeingRecords(List<Wellbeing> records);
    void saveListenRecords(List<ListenRecord> records);
    void saveAutotimerRecords(List<Autotimer> records);
    void saveMediaRecords(List<Media> records);
    void saveTimeLogRecords(List<TimeLog> records);
    void saveYouTubeRecords(List<YouTube> records);
    void saveCallRecords(List<CallRecord> records);
    //CHECKSTYLE:ON
}
