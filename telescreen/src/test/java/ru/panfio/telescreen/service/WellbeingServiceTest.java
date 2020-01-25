package ru.panfio.telescreen.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.model.Wellbeing;
import ru.panfio.telescreen.repository.WellbeingRepository;
import ru.panfio.telescreen.dao.WellbeingDao;

import java.time.Instant;
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
        activities.add(new Wellbeing(116145949L, 1, Instant.parse("2020-01-17T21:37:06.247Z"), Instant.parse("2020-01-17T21:37:06.247Z"), "com.soundcloud.android"));
        activities.add(new Wellbeing(230777275L, 23, Instant.parse("2020-01-17T21:37:06.774Z"), Instant.parse("2020-01-17T21:37:06.774Z"), "org.telegram.messenger"));
        activities.add(new Wellbeing(116145949L, 2, Instant.parse("2020-01-17T21:37:53.117Z"), Instant.parse("2020-01-17T21:37:53.117Z"), "com.soundcloud.android"));
        activities.add(new Wellbeing(230777275L, 1, Instant.parse("2020-01-17T21:37:53.135Z"), Instant.parse("2020-01-17T21:37:53.135Z"), "org.telegram.messenger"));
        activities.add(new Wellbeing(212850132L, 1, Instant.parse("2020-01-17T21:37:53.795Z"), Instant.parse("2020-01-17T21:37:53.795Z"), "com.google.android.apps.nexuslauncher"));
        activities.add(new Wellbeing(230777275L, 2, Instant.parse("2020-01-17T21:37:53.803Z"), Instant.parse("2020-01-17T21:37:53.803Z"), "org.telegram.messenger"));
        activities.add(new Wellbeing(116145949L, 23, Instant.parse("2020-01-17T21:37:54.030Z"), Instant.parse("2020-01-17T21:37:54.030Z"), "com.soundcloud.android"));
        activities.add(new Wellbeing(230777275L, 23, Instant.parse("2020-01-17T21:37:54.406Z"), Instant.parse("2020-01-17T21:37:54.406Z"), "org.telegram.messenger"));

        when(wellbeingDao.getActivities()).thenReturn(activities);

        service.processWellbeingRecords();

        @SuppressWarnings("unchecked") final ArgumentCaptor<Wellbeing> argument = ArgumentCaptor.forClass(Wellbeing.class);
        verify(wellbeingRepository, times(2)).save(argument.capture());

        Wellbeing wr = argument.getValue();
        assertEquals(230777275 , wr.getId().longValue());
        assertEquals("org.telegram.messenger" , wr.getApp());
        assertEquals(2, wr.getType());
        assertEquals(Instant.parse("2020-01-17T21:37:53.135Z"), wr.getStartTime());
        assertEquals(Instant.parse("2020-01-17T21:37:53.803Z"), wr.getEndTime());
    }

}
