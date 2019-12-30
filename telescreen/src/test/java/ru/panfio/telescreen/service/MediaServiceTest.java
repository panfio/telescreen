package ru.panfio.telescreen.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.model.Media;
import ru.panfio.telescreen.repository.MediaRepository;
import ru.panfio.telescreen.service.util.ObjectDateWizard;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class MediaServiceTest {

    private MediaService service;
    private ObjectStorage objectStorage;
    private MediaRepository mediaRepository;

    @Before
    public void setUp() {
        mediaRepository = mock(MediaRepository.class);
        objectStorage = mock(ObjectStorage.class);
        service = new MediaService(mediaRepository, objectStorage, new ObjectDateWizard(objectStorage));
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
        assertThat(list.get(0).getPath(), is("media/photo/IMG_20181104_105411.jpg"));
        assertThat(list.get(0).getCreated(), is(LocalDateTime.of(2018, 11, 4, 10, 54, 11)));
        assertThat(list.get(0).getType(), is("photo"));
        assertThat(list.get(0).getUrl(), is("/api/media/file?filename=media/photo/IMG_20181104_105411.jpg"));
    }
}
