package ru.panfio.telescreen.handler.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.model.Message;
import ru.panfio.telescreen.handler.service.util.DateWizard;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TelegramMessageService implements Processing {
    private final MessageBus messageBus;
    private final ObjectStorage objectStorage;

    public TelegramMessageService(ObjectStorage objectStorage,
                                  MessageBus messageBus) {
        this.messageBus = messageBus;
        this.objectStorage = objectStorage;
    }

    @Override
    public void process() {
        log.info("Start processing Telegram messages");
        getExportFiles().parallelStream().forEach(this::processFile);
        log.info("End processing Telegram messages");
    }

    private void processFile(String filename) {
        log.info("processing {}", filename);
        var fileContent = getExportContent(filename);
        List<Message> messages = getMessages(fileContent);
        messages.forEach(this::sendMessage);
    }

    private String getExportContent(String filename) {
        return objectStorage.getContent(filename, "utf-8");
    }

    private List<String> getExportFiles() {
        return objectStorage.listObjects(this::isMessagesFile);
    }

    private boolean isMessagesFile(String name) {
        return name.startsWith("telegram/") && name.contains("messages");
    }

    private void sendMessage(Message tm) {
        messageBus.send("message", tm);
    }

    /**
     * Collecting messages from the given html file.
     *
     * @param html html
     */
    private List<Message> getMessages(String html) {
        Elements messages = parseTelegramMessages(html);
        if (messages == null) {
            return new ArrayList<>();
        }
        var author = "";
        List<Message> messageList = new ArrayList<>();
        for (Element message : messages) {
            Message tm = parseTelegramMessage(message);
            //in a message group, only the first element has an author
            String currentAuthor = tm.getAuthor();
            if (!tm.getAuthor().isEmpty()) {
                author = currentAuthor;
            }

            messageList.add(constructMessage(author, tm));
        }
        return messageList;
    }

    private Message constructMessage(String author, Message tm) {
        return Message.builder()
                .legacyID(tm.getLegacyID())
                .created(tm.getCreated())
                .author(author)
                .content(tm.getContent())
                .type(Message.Type.TELEGRAM)
                .build();
    }

    private Elements parseTelegramMessages(String html) {
        try {
            var doc = Jsoup.parse(html, "utf-8");
            return doc.select("div.message.default");
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private Message parseTelegramMessage(Element message) {
        return Message.builder()
                .legacyID(message.id().substring("message".length()))
                .created(parseSendTime(message).orElseThrow())
                .author(message.select("div.from_name").text())
                .content(message.select("div.text").text())
                .build();
    }

    private Optional<Instant> parseSendTime(Element message) {
        return Optional.of(DateWizard.toInstant(
                LocalDateTime.parse(
                        message.select("div.date.details").attr("title"),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                )));
    }

    @Override
    public String name() {
        return "message";
    }
}
