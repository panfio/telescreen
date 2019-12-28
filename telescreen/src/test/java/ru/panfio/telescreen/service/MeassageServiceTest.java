package ru.panfio.telescreen.service;

import org.junit.Before;
import org.junit.Test;
import ru.panfio.telescreen.model.Message;
import ru.panfio.telescreen.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
    public void telegramParsing() {
        List<Message> tml = service.parseTelegramMessages(TestFiles.TELEGRAM);
        assertEquals("https://example.com" , tml.get(0).getContent());
        assertEquals("1234" , tml.get(0).getLegacyID());
        assertEquals(Message.Type.TELEGRAM, tml.get(0).getType());
        assertEquals("Alex" , tml.get(1).getAuthor());
        assertEquals(LocalDateTime.parse("2019-08-11T22:54:49"), tml.get(1).getCreated());
    }
}
