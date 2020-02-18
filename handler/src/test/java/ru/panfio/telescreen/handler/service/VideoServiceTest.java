package ru.panfio.telescreen.handler.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

class VideoServiceTest {

    private YouTubeVideoService service;
    private List<String> expectedVideoRecords;
    private List<String> forSend;
    private ObjectStorage objectStorage = new ObjectStorage() {
        @Override
        public List<String> listAllObjects() {
            return null;
        }

        @Override
        public List<String> listObjects(Predicate<String> predicate) {
            return null;
        }

        @Override
        public String contentType(String filename) {
            return null;
        }

        @Override
        public byte[] getByteArray(String filename) {
            return new byte[0];
        }

        @Override
        public InputStream getInputStream(String filename) {
            return TestFiles.toInputStream(TestFiles.YOUTUBE);
        }

        @Override
        public String saveInTmpFolder(String filename) {
            return null;
        }

        @Override
        public Instant getCreatedTime(String filename) throws IllegalArgumentException {
            return null;
        }

        @Override
        public String getContent(String filename, String charset) {
            return null;
        }
    };
    private MessageBus messageBus = new MessageBus() {
        @Override
        public void send(String topic, String message) {
            forSend.add(message);
        }

        @Override
        public void send(String topic, Object message) {
        }

        @Override
        public <T> void sendAll(String topic, List<T> messages) {
        }
    };

    @BeforeEach
    public void setup() {
        service = new YouTubeVideoService(objectStorage, messageBus);
        forSend = new ArrayList<>();
    }

    @Test
    void processYouTubeHistory() {
        expectedVideoRecords = Arrays.asList(
                "{\"id\":1579941364,\"title\":\"Unnholy - Become Human\",\"url\":\"https://www.youtube.com/watch?v=MxHpNYTbO_w\",\"time\":\"2020-01-25T08:36:04.679Z\"}",
                "{\"id\":1579867690,\"title\":\"Олег Шелаев — Node.js: так же быстро, выше, сильнее с GraalVM\",\"url\":\"https://www.youtube.com/watch?v=sKS4A9I8xb8\",\"time\":\"2020-01-24T12:08:10.009Z\"}",
                "{\"id\":1579796507,\"title\":\"Шаблоны Java. Bridge (Мост)\",\"url\":\"https://www.youtube.com/watch?v=1KOkb-B2qnM\",\"time\":\"2020-01-23T16:21:47.652Z\"}"
        );
        service.process();
        Assert.assertEquals(expectedVideoRecords.toString(), forSend.toString());
    }

    @Test
    void name() {
        Assert.assertEquals("video", service.name());
    }
}