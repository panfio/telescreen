package ru.panfio.telescreen.handler.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.handler.model.Message;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class MeassageServiceTest {

    private MessageService service;
    private ObjectStorage objectStorage;
    private MessageBus messageBus;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        objectStorage = mock(ObjectStorage.class);
        messageBus = mock(MessageBus.class);
        service = new MessageService(objectStorage, messageBus);

        Field zoneOffset = service.getClass().getDeclaredField("zoneOffset");
        zoneOffset.setAccessible(true);
        zoneOffset.set(service, 3);
    }

    @Test
    public void processTelegramHistoryTest() {
        List<String> activityFiles = Arrays.asList("app/telegram/messages1.html");

        when(objectStorage.listAllObjects()).thenReturn(activityFiles);
        when(objectStorage.getContent(activityFiles.get(0), "utf-8")).thenReturn(TestFiles.TELEGRAM);

        service.processTelegramHistory();

        @SuppressWarnings("unchecked") final ArgumentCaptor<Message> argument = ArgumentCaptor.forClass(Message.class);
        verify(messageBus, times(2)).send(anyString(),argument.capture());
        Message tml = argument.getValue();

        assertEquals("TEST text1", tml.getContent());
        assertEquals("4321", tml.getLegacyID());
        assertEquals(Message.Type.TELEGRAM, tml.getType());
        assertEquals("Alex", tml.getAuthor());
        assertEquals(Instant.parse("2019-08-11T19:54:49Z"), tml.getCreated());
    }
}