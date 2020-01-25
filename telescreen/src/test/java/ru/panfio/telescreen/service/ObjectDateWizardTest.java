package ru.panfio.telescreen.service;

import org.junit.Before;
import org.junit.Test;
import ru.panfio.telescreen.service.util.DateWizard;
import ru.panfio.telescreen.service.util.ObjectDateWizard;

import java.lang.reflect.Field;
import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class ObjectDateWizardTest {

    private DateWizard service;
    private ObjectStorage objectStorage;

    @Before
    public void setUp() throws IllegalAccessException, NoSuchFieldException {
        objectStorage = mock(ObjectStorage.class);
        service = new ObjectDateWizard(objectStorage);

        Field zoneOffset = service.getClass().getDeclaredField("zoneOffset");
        zoneOffset.setAccessible(true);
        zoneOffset.set(service, "3");
    }

    @Test
    public void dateFromFileNameShouldBeParsedCorrectly() {
        Instant expectedDateTime = Instant.parse("2018-11-04T07:54:11Z");
        assertEquals(expectedDateTime, service.dateFromPath("media/photo/IMG_20181104_105411.jpg"));
        assertEquals(expectedDateTime, service.dateFromPath("media/video/VID_20181104_105411.mp4"));
        assertEquals(expectedDateTime, service.dateFromPath("media/voicenotes/2018-11-04-10-54-11-note.m4a"));
        assertEquals(expectedDateTime, service.dateFromPath("media/screenshot/Screenshot_20181104-105411.png"));
        assertEquals(expectedDateTime, service.dateFromPath("media/screenshot/Screenshot from 2018-11-04 10-54-11.png"));
        assertEquals(expectedDateTime, service.dateFromPath("media/timesheet/TimesheetBackup_2018-11-04_105411.xml"));
        assertNull(service.dateFromPath("some/strange_2-11-04 10_file.png"));
        assertNull(service.dateFromPath("media/voicenotes/2019-22-05 19-25-57-note.m4a"));
    }

}
