package ru.panfio.telescreen.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Autotimer;
import ru.panfio.telescreen.repository.AutotimerRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class AutotimerService {
    private static final Logger logger = LoggerFactory.getLogger(AutotimerService.class);

    @Autowired
    AutotimerRepository autotimerRepository;

    public Iterable<Autotimer> findAll() {
        return autotimerRepository.findAll();
    }

    public Iterable<Autotimer> getAllBetweenDates(LocalDateTime t1, LocalDateTime t2) {
        return autotimerRepository.getAllBetweenDates(t1, t2).stream().filter(t -> Duration.between(t.getStartTime(), t.getEndTime()).toMillis() > 10000).collect(Collectors.toList());
    }
}
