package ru.panfio.telescreen.handler.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.model.Message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class MessageService implements Processing {
    @Value("${server.zoneOffset}")
    private int zoneOffset;
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
        for (String filename : objectStorage.listAllObjects()) {
            if (!filename.startsWith("app/telegram")
                    && !filename.contains("messages")) {
                continue;
            }
            //todo process in parallel
            String content = objectStorage.getContent(filename, "utf-8");
            processFile(content);
        }
        log.info("End processing Telegram messages");
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
            Document doc = Jsoup.parse(html, "utf-8");
            return doc.select("div.message.default");
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private Message parseTelegramMessage(Element message) {
        var date = LocalDateTime.parse(
                message.select("div.date.details").attr("title"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        var msg = new Message();
        // message1234567890
        msg.setLegacyID(message.id().substring("message".length()));
        msg.setCreated(date.toInstant(
                ZoneOffset.ofHours(zoneOffset)));
        msg.setType(Message.Type.TELEGRAM);
        msg.setAuthor(message.select("div.from_name").text());
        msg.setContent(message.select("div.text").text());
        return msg;
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
