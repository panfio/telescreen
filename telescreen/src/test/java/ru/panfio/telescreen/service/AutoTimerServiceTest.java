package ru.panfio.telescreen.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.model.Autotimer;

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
    private MessageBus messageBus;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        objectStorage = mock(ObjectStorage.class);
        messageBus = mock(MessageBus.class);

        service = new AutoTimerService(objectStorage, messageBus);
        Field zoneOffset = service.getClass().getDeclaredField("zoneOffset");
        zoneOffset.setAccessible(true);
        zoneOffset.set(service, 3);
    }

    @Test
    public void autotimerRecordsShouldBeCreatedCorrectly() throws FileNotFoundException {
        List<String> activityFiles = Arrays.asList("autotimer/activities-20191127-101619.json");
        when(objectStorage.listAllObjects()).thenReturn(activityFiles);
        when(objectStorage.getInputStream(activityFiles.get(0))).thenReturn(toInputStream(TestFiles.ACTIVITIES));

        service.processAutotimerRecords();

        @SuppressWarnings("unchecked") final ArgumentCaptor<Autotimer> argument = ArgumentCaptor.forClass(Autotimer.class);
        verify(messageBus, times(2)).send(anyString(), argument.capture());
        Autotimer list = argument.getValue();

        assertNull(list.getId());
        assertEquals("Google Chrome -> Dreams by Ytho.",list.getName());
        assertEquals(LocalDateTime.of(2019, 11, 26, 23, 52, 30).toInstant(ZoneOffset.ofHours(3)), list.getStartTime());
        assertEquals(LocalDateTime.of(2019, 11, 26, 23, 55, 0).toInstant(ZoneOffset.ofHours(3)), list.getEndTime());
        assertEquals(1, list.getType());
    }

}
