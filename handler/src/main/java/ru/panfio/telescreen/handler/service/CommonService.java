package ru.panfio.telescreen.handler.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class CommonService {

    private static final int THREAD_COUNT = 8;
    private final ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);

    private final Map<String, Processing> handlers;

    /**
     * Constructor.
     *
     * @param services handler list
     */
    public CommonService(@Autowired List<Processing> services) {
        Map<String, Processing> handlers = new HashMap<>();
        services.forEach((service) -> {
            handlers.put(service.name(), service);
        });
        this.handlers = handlers;
    }

    public void processAll() {
        log.info("Start processing all task");
        handlers.forEach((k, handler) -> {
            log.info("Submit -> {}", k);
            executor.submit(handler::process);
        });
    }

    public void process(@NonNull String serviceName) {
        Processing service = handlers.get(serviceName);
        if (service == null) {
            log.warn("Wrong handler");
            return;
        }
        executor.submit(service::process);
    }
}
