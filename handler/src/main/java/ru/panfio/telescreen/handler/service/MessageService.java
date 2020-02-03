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
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MessageService implements Processing {
    private final MessageBus messageBus;
    private final ObjectStorage objectStorage;

    /**
     * Constructor.
     *
     * @param messageBus    message bus
     * @param objectStorage storage
     */
    public MessageService(ObjectStorage objectStorage,
                          MessageBus messageBus) {
        this.messageBus = messageBus;
        this.objectStorage = objectStorage;
    }

    /**
     * Processing Telegram message history from chat history export.
     */
    public void processTelegramHistory() {
        log.info("Start processing Telegram messages");
        for (var filename : getExportFiles()) {
            //todo process in parallel
            var fileContent = getExportContent(filename);
            processFile(fileContent);
        }
        log.info("End processing Telegram messages");
    }

    private String getExportContent(String filename) {
        return objectStorage.getContent(filename, "utf-8");
    }

    private List<String> getExportFiles() {
        return objectStorage.listObjects(this::isMessagesFile);
    }

    private boolean isMessagesFile(String name) {
        return !(name.startsWith("app/telegram") || name.contains("messages"));
    }

    /**
     * Collecting messages from the given html file.
     *
     * @param html html
     */
    private void processFile(String html) {
        Elements messages = parseTelegramMessages(html);
        if (messages == null) {
            return;
        }
        var author = "";
        for (Element message : messages) {
            Message tm = parseTelegramMessage(message);
            //in a message group, only the first element has an author
            String currentAuthor = tm.getAuthor();
            if (!tm.getAuthor().isEmpty()) {
                author = currentAuthor;
            }

            tm.setAuthor(author);
            messageBus.send("message", tm);
        }
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
                .created(parseCreationTime(message).orElseThrow())
                .author(message.select("div.from_name").text())
                .content(message.select("div.text").text())
                .type(Message.Type.TELEGRAM)
                .build();
    }

    private Optional<Instant> parseCreationTime(Element message) {
        return Optional.of(DateWizard.toInstant(
                LocalDateTime.parse(
                        message.select("div.date.details").attr("title"),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                )));
    }

    @Override
    public void process() {
        processTelegramHistory();
    }

    @Override
    public String name() {
        return "message";
    }
}
