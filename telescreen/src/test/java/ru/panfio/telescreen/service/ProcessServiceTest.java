package ru.panfio.telescreen.service;

import org.junit.*;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.model.Autotimer;
import ru.panfio.telescreen.model.Media;
import ru.panfio.telescreen.model.Message;
import ru.panfio.telescreen.model.timesheet.TimesheetExport;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static ru.panfio.telescreen.service.TestFiles.toInputStream;

public class ProcessServiceTest {

    private ProcessService service;
    private ObjectStorage objectStorage;
    private PersistenceService persistenceServiceMock;

    @Before
    public void setUp() {
        persistenceServiceMock = mock(PersistenceService.class);
        objectStorage = mock(ObjectStorage.class);
        service = new ProcessService(objectStorage, persistenceServiceMock);
    }

    @Test
    public void dateFromFileNameShouldBeParsedCorrectly() {
        LocalDateTime expectedDateTime = LocalDateTime.of(2018, 11, 4, 10, 54, 11);
        assertEquals(expectedDateTime, service.getDateFromPath("media/photo/IMG_20181104_105411.jpg"));
        assertEquals(expectedDateTime, service.getDateFromPath("media/video/VID_20181104_105411.mp4"));
        assertEquals(expectedDateTime, service.getDateFromPath("media/voicenotes/2018-11-04-10-54-11-note.m4a"));
        assertEquals(expectedDateTime, service.getDateFromPath("media/screenshot/Screenshot_20181104-105411.png"));
        assertEquals(expectedDateTime, service.getDateFromPath("media/screenshot/Screenshot from 2018-11-04 10-54-11.png"));
        assertEquals(expectedDateTime, service.getDateFromPath("media/timesheet/TimesheetBackup_2018-11-04_105411.xml"));
        assertNull(service.getDateFromPath("some/strange_2-11-04 10_file.png"));
        assertNull(service.getDateFromPath("media/voicenotes/2019-10-05 19-25-57-note.m4a"));
    }

    @Test
    public void mediaRecordsShouldBeCreatedCorrectly() {
        List<String> mediaFiles = Arrays.asList(
                "media/photo/IMG_20181104_105411.jpg",
                "media/video/VID_20181104_105411.mp4",
                "media/voicenotes/2018-11-04-10-54-11-note.m4a");
        when(objectStorage.getListOfFileNames()).thenReturn(mediaFiles);
        service.processMediaRecords();

        @SuppressWarnings("unchecked") final ArgumentCaptor<List<Media>> argument = ArgumentCaptor.forClass(List.class);
        verify(persistenceServiceMock).saveMediaRecords(argument.capture());
        List<Media> list = argument.getValue();

        assertEquals(3, list.size());
        assertNull(list.get(0).getId());
        assertThat(list.get(0).getPath(), is("media/photo/IMG_20181104_105411.jpg"));
        assertThat(list.get(0).getCreated(), is(LocalDateTime.of(2018, 11, 4, 10, 54, 11)));
        assertThat(list.get(0).getType(), is("photo"));
        assertThat(list.get(0).getUrl(), is("/media/file?filename=media/photo/IMG_20181104_105411.jpg"));
    }

    @Test
    public void autotimerRecordsShouldBeCreatedCorrectly() throws FileNotFoundException {
        List<String> activityFiles = Arrays.asList("autotimer/activities-20191127-101619.json");
        when(objectStorage.getListOfFileNames()).thenReturn(activityFiles);
        when(objectStorage.getInputStream(activityFiles.get(0))).thenReturn(toInputStream(TestFiles.ACTIVITIES));

        service.processAutotimerRecords();

        @SuppressWarnings("unchecked") final ArgumentCaptor<List<Autotimer>> argument = ArgumentCaptor.forClass(List.class);
        verify(persistenceServiceMock).saveAutotimerRecords(argument.capture());
        List<Autotimer> list = argument.getValue();

        assertEquals(2, list.size());
        assertNull(list.get(0).getId());
        assertThat(list.get(0).getName(), is("Google Chrome -> Dreams by Ytho."));
        assertThat(list.get(0).getStartTime(), is(LocalDateTime.of(2019, 11, 26, 23, 52, 1)));
        assertThat(list.get(0).getEndTime(), is(LocalDateTime.of(2019, 11, 26, 23, 52, 10)));
        assertThat(list.get(0).getType(), is(1));
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

    @Test
    public void telegramParsing() {
        List<Message> tml = service.parseTelegramMessages(TestFiles.TELEGRAM);
        assertEquals("https://example.com" , tml.get(0).getContent());
        assertEquals("1234" , tml.get(0).getLegacyID());
        assertEquals(Message.Type.TELEGRAM, tml.get(0).getType());
        assertEquals("Alex" , tml.get(1).getAuthor());
        assertEquals(LocalDateTime.parse("2019-08-11T22:54:49"), tml.get(1).getCreated());
    }
}