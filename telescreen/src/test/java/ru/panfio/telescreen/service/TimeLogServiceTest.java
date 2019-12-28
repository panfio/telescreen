package ru.panfio.telescreen.service;

import org.junit.Before;
import org.junit.Test;
import ru.panfio.telescreen.model.timesheet.TimesheetExport;
import ru.panfio.telescreen.repository.TimeLogRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static ru.panfio.telescreen.service.TestFiles.toInputStream;

public class TimeLogServiceTest {


    private TimeLogService service;
    private ObjectStorage objectStorage;
    private TimeLogRepository timeLogRepository;

    @Before
    public void setUp() {
        timeLogRepository = mock(TimeLogRepository.class);
        objectStorage = mock(ObjectStorage.class);
        service = new TimeLogService(timeLogRepository, objectStorage, new ObjectDateWizard(objectStorage));
    }

    @Test
    public void unmarshallilgXmlShouldWorkCorrectly() {
        TimesheetExport out = service.unmarshallXml(TimesheetExport.class, toInputStream(TestFiles.TIMESHEET));
        assertEquals("Coding", out.getTags().getTags().get(0).getName());
        assertEquals("Description", out.getTasks().getTasks().get(0).getDescription());
        assertEquals(
                LocalDateTime.parse("2019-12-07T19:28:00+03:00", DateTimeFormatter.ISO_DATE_TIME),
                out.getTasks().getTasks().get(0).getStartDate());

        TimesheetExport fromBadFile = service.unmarshallXml(TimesheetExport.class, toInputStream("<xml>"));
        assertNull(fromBadFile);
    }

}
