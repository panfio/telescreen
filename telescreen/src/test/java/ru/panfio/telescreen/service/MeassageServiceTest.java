package ru.panfio.telescreen.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.model.Message;
import ru.panfio.telescreen.model.TimeLog;
import ru.panfio.telescreen.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static ru.panfio.telescreen.service.TestFiles.toInputStream;

public class MeassageServiceTest {

    private MessageService service;
    private ObjectStorage objectStorage;
    private MessageRepository messageRepository;

    @Before
    public void setUp() {
        messageRepository = mock(MessageRepository.class);
        objectStorage = mock(ObjectStorage.class);
        service = new MessageService(messageRepository, objectStorage);
    }

    @Test
    public void processTelegramHistoryTest() {
        List<String> activityFiles = Arrays.asList("app/telegram/messages1.html");

        when(objectStorage.listAllObjects()).thenReturn(activityFiles);
        when(objectStorage.getContent(activityFiles.get(0), "utf-8")).thenReturn(TestFiles.TELEGRAM);
        when(messageRepository.findByLegacyIDAndCreated(any(),any())).thenReturn(null);

        service.processTelegramHistory();

        @SuppressWarnings("unchecked") final ArgumentCaptor<Message> argument = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository, times(2)).save(argument.capture());
        Message tml = argument.getValue();

        assertEquals("TEST text1" , tml.getContent());
        assertEquals("4321" , tml.getLegacyID());
        assertEquals(Message.Type.TELEGRAM, tml.getType());
        assertEquals("Alex" , tml.getAuthor());
        assertEquals(LocalDateTime.parse("2019-08-11T22:54:49"), tml.getCreated());
    }
}
