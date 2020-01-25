package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Call;
import ru.panfio.telescreen.repository.CallRecordRepository;
import ru.panfio.telescreen.dao.CallDaoJdbc;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CallService implements Processing {

    //todo save telegram/skype calls

    private final CallRecordRepository callRecordRepository;
    private final CallDaoJdbc callDaoJdbc;

    /**
     * Constructor.
     *
     * @param callRecordRepository repo
     * @param callDaoJdbc            dbManager
     */
    public CallService(CallRecordRepository callRecordRepository,
                       CallDaoJdbc callDaoJdbc) {
        this.callRecordRepository = callRecordRepository;
        this.callDaoJdbc = callDaoJdbc;
    }

    /**
     * Finds and returns call records for the period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public Iterable<Call> getCallHistoryBetweenDates(
            LocalDateTime from, LocalDateTime to) {
        return callRecordRepository.findByDateBetween(from, to);
    }

    /**
     * Saves call records in the database.
     *
     * @param records list of records
     */
    public void saveCallRecords(List<Call> records) {
        callRecordRepository.saveAll(records); //todo
    }


    /**
     * Processing Call history from android phone.
     */
    public void processCallHistory() {
        List<Call> callRecords = callDaoJdbc.getPhoneCalls();
        saveCallRecords(callRecords);
    }

    @Override
    public void process() {
        processCallHistory();
    }
}


