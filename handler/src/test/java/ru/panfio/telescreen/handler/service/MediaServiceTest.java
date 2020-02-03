package ru.panfio.telescreen.handler.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.handler.model.Media;
import ru.panfio.telescreen.handler.service.util.ObjectDateWizard;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class MediaServiceTest {

    private MediaService service;
    private ObjectStorage objectStorage;
    private MessageBus messageBus;

    @Before
    public void setUp() {
        objectStorage = mock(ObjectStorage.class);
        messageBus = mock(MessageBus.class);

        ObjectDateWizard dw = new ObjectDateWizard(objectStorage);
        service = new MediaService(objectStorage, dw, messageBus);
    }

    @Test
    public void mediaRecordsShouldBeCreatedCorrectly() {
        List<String> mediaFiles = Arrays.asList(
                "media/photo/IMG_20181104_105411.jpg",
                "media/video/VID_20181104_105411.mp4",
                "media/voicenote/2018-11-04-10-54-11-note.m4a");
        when(objectStorage.listObjects(any())).thenReturn(mediaFiles);
        service.processMediaRecords();

        @SuppressWarnings("unchecked") final ArgumentCaptor<Media> argument = ArgumentCaptor.forClass(Media.class);
        verify(messageBus, times(3)).send(anyString(), argument.capture());
        Media list = argument.getValue();

        assertNull(list.getId());
        assertEquals("media/voicenote/2018-11-04-10-54-11-note.m4a",list.getPath());
        assertEquals(Instant.parse("2018-11-04T07:54:11Z"), list.getCreated());
        assertEquals("voicenote", list.getType());
    }
}
