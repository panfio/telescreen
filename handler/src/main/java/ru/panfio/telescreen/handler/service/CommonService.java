package ru.panfio.telescreen.handler.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CommonService {
    private final Map<String, Processing> handlers;

    public CommonService(@Autowired List<Processing> services) {
        Map<String, Processing> handlers = new HashMap<>();
        services.forEach(service -> handlers.put(service.name(), service));
        this.handlers = handlers;
    }

    public void processAll() {
        log.info("Start processing all task");
        handlers.values().parallelStream()
                .forEach(Processing::process);
    }

    public void process(@NonNull String serviceName) {
        Processing service = handlers.get(serviceName);
        if (service == null) {
            log.warn("Wrong handler");
            return;
        }
        service.process();
    }
}
