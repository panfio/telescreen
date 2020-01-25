package ru.panfio.telescreen.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.model.TimeLog;
import ru.panfio.telescreen.model.timesheet.TimesheetExport;
import ru.panfio.telescreen.repository.TimeLogRepository;
import ru.panfio.telescreen.service.util.DateWizard;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static ru.panfio.telescreen.service.TestFiles.toInputStream;

public class TimeLogServiceTest {


    private TimeLogService service;
    private ObjectStorage objectStorage;
    private TimeLogRepository timeLogRepository;
    private DateWizard dateWizard;

    @Before
    public void setUp() {
        timeLogRepository = mock(TimeLogRepository.class);
        objectStorage = mock(ObjectStorage.class);
        dateWizard = mock(DateWizard.class);
        service = new TimeLogService(timeLogRepository, objectStorage, dateWizard);
    }

    @Test
    public void unmarshallilgXmlShouldWorkCorrectly() {
        TimesheetExport out = service.unmarshallXml(TimesheetExport.class, toInputStream(TestFiles.TIMESHEET));
        assertEquals("Coding", out.getTags().getTags().get(0).getName());
        assertEquals("Description", out.getTasks().getTasks().get(0).getDescription());
        assertEquals(
                Instant.parse("2019-12-07T16:28:00Z"),
                out.getTasks().getTasks().get(0).getStartDate());

        TimesheetExport fromBadFile = service.unmarshallXml(TimesheetExport.class, toInputStream("<xml>"));
        assertNull(fromBadFile);
    }

    @Test
    public void processTimesheetRecordsTest() {
        List<String> activityFiles = Arrays.asList("timesheet/TimesheetBackup_2018-11-04_000000.xml");

        when(objectStorage.listAllObjects()).thenReturn(activityFiles);
        when(dateWizard.dateFromPath(activityFiles.get(0))).thenReturn(Instant.parse("2018-11-04T00:00:00Z"));
        when(objectStorage.getInputStream(activityFiles.get(0))).thenReturn(toInputStream(TestFiles.TIMESHEET));

        service.processTimesheetRecords();

        @SuppressWarnings("unchecked") final ArgumentCaptor<List<TimeLog>> argument = ArgumentCaptor.forClass(List.class);
        verify(timeLogRepository).saveAll(argument.capture());
        List<TimeLog> list = argument.getValue();

        assertEquals(1, list.size());
        assertEquals("Coding", list.get(0).getTags().get(0));
        assertEquals("Description", list.get(0).getDescription());
        assertEquals(
                Instant.parse("2019-12-07T16:28:00Z"),
                list.get(0).getStartDate());
    }
}
