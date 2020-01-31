package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Call;
import ru.panfio.telescreen.repository.CallRecordRepository;
import ru.panfio.telescreen.dao.CallDaoJdbc;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class CallService implements Processing {

    //todo save telegram/skype calls

    @Autowired //todo remove
    private CallRecordRepository callRecordRepository;
    private final MessageBus messageBus;
    private final CallDaoJdbc callDaoJdbc;

    /**
     * Constructor.
     *
     * @param messageBus    message bus
     * @param callDaoJdbc callDaoJdbc
     */
    public CallService(CallDaoJdbc callDaoJdbc,
                       MessageBus messageBus) {
        this.messageBus = messageBus;
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
            Instant from, Instant to) {
        return callRecordRepository.findByDateBetween(from, to);
    }

    /**
     * Processing Call history from android phone.
     */
    public void processCallHistory() {
        List<Call> callRecords = callDaoJdbc.getPhoneCalls();
        callRecords.forEach((call) -> {
            messageBus.send("call", call);
        });
    }

    @Override
    public void process() {
        processCallHistory();
    }
}


