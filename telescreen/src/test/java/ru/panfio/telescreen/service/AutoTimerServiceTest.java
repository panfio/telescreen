package ru.panfio.telescreen.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.model.Autotimer;
import ru.panfio.telescreen.repository.AutotimerRepository;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static ru.panfio.telescreen.service.TestFiles.toInputStream;

public class AutoTimerServiceTest {

    private AutoTimerService service;
    private ObjectStorage objectStorage;
    private AutotimerRepository autotimerRepository;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        autotimerRepository = mock(AutotimerRepository.class);
        objectStorage = mock(ObjectStorage.class);
        service = new AutoTimerService(autotimerRepository, objectStorage);
        Field zoneOffset = service.getClass().getDeclaredField("zoneOffset");
        zoneOffset.setAccessible(true);
        zoneOffset.set(service, "3");
    }

    @Test
    public void autotimerRecordsShouldBeCreatedCorrectly() throws FileNotFoundException {
        List<String> activityFiles = Arrays.asList("autotimer/activities-20191127-101619.json");
        when(objectStorage.listAllObjects()).thenReturn(activityFiles);
        when(objectStorage.getInputStream(activityFiles.get(0))).thenReturn(toInputStream(TestFiles.ACTIVITIES));

        service.processAutotimerRecords();

        @SuppressWarnings("unchecked") final ArgumentCaptor<List<Autotimer>> argument = ArgumentCaptor.forClass(List.class);
        verify(autotimerRepository).saveAll(argument.capture());
        List<Autotimer> list = argument.getValue();

        assertEquals(2, list.size());
        assertNull(list.get(0).getId());
        assertEquals("Google Chrome -> Dreams by Ytho.",list.get(0).getName());
        assertEquals(LocalDateTime.of(2019, 11, 26, 23, 52, 1).toInstant(ZoneOffset.ofHours(3)), list.get(0).getStartTime());
        assertEquals(LocalDateTime.of(2019, 11, 26, 23, 52, 10).toInstant(ZoneOffset.ofHours(3)), list.get(0).getEndTime());
        assertEquals(1, list.get(0).getType());
    }

}
