package ru.panfio.telescreen.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.model.Media;
import ru.panfio.telescreen.repository.MediaRepository;
import ru.panfio.telescreen.service.util.DateWizard;
import ru.panfio.telescreen.service.util.ObjectDateWizard;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class MediaServiceTest {

    private MediaService service;
    private ObjectStorage objectStorage;
    private MediaRepository mediaRepository;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        mediaRepository = mock(MediaRepository.class);
        objectStorage = mock(ObjectStorage.class);

        ObjectDateWizard dw = new ObjectDateWizard(objectStorage);
        Field zoneOffset = dw.getClass().getDeclaredField("zoneOffset");
        zoneOffset.setAccessible(true);
        zoneOffset.set(dw, "3");

        service = new MediaService(mediaRepository, objectStorage, dw);
    }

    @Test
    public void mediaRecordsShouldBeCreatedCorrectly() {
        List<String> mediaFiles = Arrays.asList(
                "media/photo/IMG_20181104_105411.jpg",
                "media/video/VID_20181104_105411.mp4",
                "media/voicenotes/2018-11-04-10-54-11-note.m4a");
        when(objectStorage.listAllObjects()).thenReturn(mediaFiles);
        service.processMediaRecords();

        @SuppressWarnings("unchecked") final ArgumentCaptor<List<Media>> argument = ArgumentCaptor.forClass(List.class);
        verify(mediaRepository).saveAll(argument.capture());
        List<Media> list = argument.getValue();

        assertEquals(3, list.size());
        assertNull(list.get(0).getId());
        assertEquals("media/photo/IMG_20181104_105411.jpg",list.get(0).getPath());
        assertEquals(Instant.parse("2018-11-04T07:54:11Z"), list.get(0).getCreated());
        assertEquals("photo", list.get(0).getType());
    }
}
