package ru.panfio.telescreen.service;

import ru.panfio.telescreen.model.*;

import java.util.List;

public interface PersistenceService {
    public void saveMessages(List<Message> messageList);
    public void saveWellbeingRecords(List<Wellbeing> records);
    public void saveListenRecords(List<ListenRecord> records);
    public void saveAutotimerRecords(List<Autotimer> records);
    public void saveMediaRecords(List<Media> records);
    public void saveTimeLogRecords(List<TimeLog> records);
    public void saveYouTubeRecords(List<YouTube> records);
    public void saveCallRecords(List<CallRecord> records);
}
