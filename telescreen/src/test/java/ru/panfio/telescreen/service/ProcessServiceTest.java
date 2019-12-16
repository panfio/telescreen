package ru.panfio.telescreen.service;

import org.junit.*;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.model.Autotimer;
import ru.panfio.telescreen.model.Media;
import ru.panfio.telescreen.model.timesheet.TimesheetExport;
import ru.panfio.telescreen.repository.AutotimerRepository;
import ru.panfio.telescreen.repository.MediaRepository;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Ignore
public class ProcessServiceTest {

    private ProcessService service;
    private MediaRepository mediaRepositoryMock;
    private AutotimerRepository autotimerRepositoryMock;
    private S3Service s3Mock;

    @Before
    public void setUp() {
        mediaRepositoryMock = mock(MediaRepository.class);
        autotimerRepositoryMock = mock(AutotimerRepository.class);
        s3Mock = mock(S3Service.class);

        //service = new ProcessService(s3Mock, autotimerRepositoryMock, mediaRepositoryMock);
    }

    @Test
    public void dateFromFileNameShouldBeParsedCorrectly() {
        LocalDateTime expectedDateTime = LocalDateTime.of(2018, 11, 4, 10, 54, 11);
        assertEquals(expectedDateTime, service.getDateFromPath("photo/IMG_20181104_105411.jpg"));
        assertEquals(expectedDateTime, service.getDateFromPath("video/VID_20181104_105411.mp4"));
        assertEquals(expectedDateTime, service.getDateFromPath("voicenotes/2018-11-04-10-54-11-note.m4a"));
        assertEquals(expectedDateTime, service.getDateFromPath("screenshot/Screenshot_20181104-105411.png"));
        assertEquals(expectedDateTime, service.getDateFromPath("screenshot/Screenshot from 2018-11-04 10-54-11.png"));
        assertEquals(expectedDateTime, service.getDateFromPath("timesheet/TimesheetBackup_2018-11-04_105411.xml"));
        assertNull(service.getDateFromPath("some/strange_2-11-04 10_file.png"));
        assertNull(service.getDateFromPath("voicenotes/2019-10-05 19-25-57-note.m4a"));
    }

    @Test
    public void mediaRecordsShouldBeCreatedCorrectly() {
        List<String> mediaFiles = Arrays.asList(
                "photo/IMG_20181104_105411.jpg",
                "video/VID_20181104_105411.mp4",
                "voicenotes/2018-11-04-10-54-11-note.m4a");
        when(s3Mock.getListOfFileNames(Bucket.MEDIA)).thenReturn(mediaFiles);
        service.processMediaRecords();

        @SuppressWarnings("unchecked") final ArgumentCaptor<List<Media>> argument = ArgumentCaptor.forClass(List.class);
        verify(mediaRepositoryMock).saveAll(argument.capture());
        List<Media> list = argument.getValue();

        assertEquals(3, list.size());
        assertNull(list.get(0).getId());
        assertThat(list.get(0).getPath(), is("photo/IMG_20181104_105411.jpg"));
        assertThat(list.get(0).getCreated(), is(LocalDateTime.of(2018, 11, 4, 10, 54, 11)));
        assertThat(list.get(0).getType(), is("photo"));
        assertThat(list.get(0).getUrl(), is("/media/file?filename=photo/IMG_20181104_105411.jpg"));
    }

    @Test
    public void autotimerRecordsShouldBeCreatedCorrectly() throws FileNotFoundException {
        List<String> activityFiles = Arrays.asList("activities-20191127-101619.json");
        InputStream stream = new ByteArrayInputStream(TestFiles.activities.getBytes(Charset.forName("UTF-8")));
        when(s3Mock.getListOfFileNames(Bucket.AUTOTIMER)).thenReturn(activityFiles);
        when(s3Mock.getInputStream(Bucket.AUTOTIMER, activityFiles.get(0))).thenReturn(stream);

        service.processAutotimerRecords();

        @SuppressWarnings("unchecked") final ArgumentCaptor<List<Autotimer>> argument = ArgumentCaptor.forClass(List.class);
        verify(autotimerRepositoryMock).saveAll(argument.capture());
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
        InputStream stream = new ByteArrayInputStream(TestFiles.timesheet.getBytes(Charset.forName("UTF-8")));
        TimesheetExport out = service.unmarshallXml(TimesheetExport.class, stream);
        assertEquals("Coding", out.getTags().getTags().get(0).getName());
        assertEquals("Description", out.getTasks().getTasks().get(0).getDescription());
        assertEquals(
                LocalDateTime.parse("2019-12-07T19:28:00+03:00",  DateTimeFormatter.ISO_DATE_TIME),
                out.getTasks().getTasks().get(0).getStartDate());

        TimesheetExport fromBadFile = service.unmarshallXml(TimesheetExport.class, new ByteArrayInputStream("<xml>".getBytes(Charset.forName("UTF-8"))));
        assertNull(fromBadFile);
    }

//    public void shouldUnmarshalCorrectly() { //todo meaningful name
//        List<String> files = Arrays.asList(
//                "timesheet/TimesheetBackup_2019-12-05_000000.xml",
//                "timesheet/TimesheetBackup_2019-12-08_000000.xml",
//                "timesheet/TimesheetBackup_2019-12-07_000000.xml");
//        when(s3Mock.getListOfFileNames("autotimer")).thenReturn(files);
//        InputStream inputStream = new ByteArrayInputStream(activities.getBytes(Charset.forName("UTF-8")));
//        when(s3Mock.getInputStream("app", files.get(1))).thenReturn(inputStream);
//
//        //assertEquals();
//        //todo assert process last file
//        //todo store records
//        //todo refactor tests
//        //List<Tag> list = service.processTimesheetRecords();
//        //assertEquals("Coding", list.get(0).getName());
//    }

}