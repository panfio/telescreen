package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class CommonService {

    private static final int THREAD_COUNT = 8;
    private final ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);

    private final List<Processing> handlers;

    /**
     * Constructor.
     *
     * @param handlers handler list
     */
    public CommonService(@Autowired  List<Processing> handlers) {
        this.handlers = handlers;
    }

    /**
     * {@inheritDoc}
     */
    public void processAll() {
        handlers.forEach(handler -> executor.submit(handler::process));
    }
}
