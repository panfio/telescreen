package ru.panfio.telescreen.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.model.Wellbeing;
import ru.panfio.telescreen.repository.WellbeingRepository;
import ru.panfio.telescreen.dao.WellbeingDao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class WellbeingServiceTest {
    private WellbeingService service;
    private WellbeingDao wellbeingDao;
    private WellbeingRepository wellbeingRepository;

    @Before
    public void setUp() {
        wellbeingRepository = mock(WellbeingRepository.class);
        wellbeingDao = mock(WellbeingDao.class);
        service = new WellbeingService(wellbeingRepository, wellbeingDao);
    }

    @Test
    public void processWellbeingRecordsTest() {
        List<Wellbeing> activities = new ArrayList<>();
        activities.add(new Wellbeing(116145949L, 1, LocalDateTime.parse("2020-01-17T21:37:06.247"), LocalDateTime.parse("2020-01-17T21:37:06.247"), "com.soundcloud.android"));
        activities.add(new Wellbeing(230777275L, 23, LocalDateTime.parse("2020-01-17T21:37:06.774"), LocalDateTime.parse("2020-01-17T21:37:06.774"), "org.telegram.messenger"));
        activities.add(new Wellbeing(116145949L, 2, LocalDateTime.parse("2020-01-17T21:37:53.117"), LocalDateTime.parse("2020-01-17T21:37:53.117"), "com.soundcloud.android"));
        activities.add(new Wellbeing(230777275L, 1, LocalDateTime.parse("2020-01-17T21:37:53.135"), LocalDateTime.parse("2020-01-17T21:37:53.135"), "org.telegram.messenger"));
        activities.add(new Wellbeing(212850132L, 1, LocalDateTime.parse("2020-01-17T21:37:53.795"), LocalDateTime.parse("2020-01-17T21:37:53.795"), "com.google.android.apps.nexuslauncher"));
        activities.add(new Wellbeing(230777275L, 2, LocalDateTime.parse("2020-01-17T21:37:53.803"), LocalDateTime.parse("2020-01-17T21:37:53.803"), "org.telegram.messenger"));
        activities.add(new Wellbeing(116145949L, 23, LocalDateTime.parse("2020-01-17T21:37:54.030"), LocalDateTime.parse("2020-01-17T21:37:54.030"), "com.soundcloud.android"));
        activities.add(new Wellbeing(230777275L, 23, LocalDateTime.parse("2020-01-17T21:37:54.406"), LocalDateTime.parse("2020-01-17T21:37:54.406"), "org.telegram.messenger"));

        when(wellbeingDao.getActivities()).thenReturn(activities);

        service.processWellbeingRecords();

        @SuppressWarnings("unchecked") final ArgumentCaptor<Wellbeing> argument = ArgumentCaptor.forClass(Wellbeing.class);
        verify(wellbeingRepository, times(2)).save(argument.capture());

        Wellbeing wr = argument.getValue();
        assertEquals(230777275 , wr.getId().longValue());
        assertEquals("org.telegram.messenger" , wr.getApp());
        assertEquals(2, wr.getType());
        assertEquals(LocalDateTime.parse("2020-01-17T21:37:53.135"), wr.getStartTime());
        assertEquals(LocalDateTime.parse("2020-01-17T21:37:53.803"), wr.getEndTime());
    }

}
