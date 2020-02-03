package ru.panfio.telescreen.handler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.dao.CallDaoJdbc;
import ru.panfio.telescreen.handler.model.Call;

import java.util.List;

@Slf4j
@Service
public class CallService implements Processing {
    //todo save telegram/skype calls
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
     * Processing Call history from android phone.
     */
    public void processCallHistory() {
        List<Call> callRecords = callDaoJdbc.getPhoneCalls();
        callRecords.forEach(this::sendCall);
    }

    private void sendCall(Call call) {
        messageBus.send("call", call);
    }

    @Override
    public void process() {
        processCallHistory();
    }

    @Override
    public String name() {
        return "call";
    }
}


