package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Message;
import ru.panfio.telescreen.repository.MessageRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MessageService implements Processing {

    private final MessageRepository messageRepository;
    private final ObjectStorage objectStorage;

    /**
     * Constructor.
     *
     * @param messageRepository repo
     * @param objectStorage     storage
     */
    public MessageService(MessageRepository messageRepository,
                          ObjectStorage objectStorage) {
        this.messageRepository = messageRepository;
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
            saveMessages(getTelegramMessages(content));
        }
        log.info("End processing Telegram messages");
    }

    /**
     * Collecting messages from the given html file.
     *
     * @param html html
     * @return message list
     */
    private List<Message> getTelegramMessages(String html) {
        List<Message> telegramMessages = new ArrayList<>();
        Elements messages = parseTelegramMessages(html);
        if (messages == null) {
            return telegramMessages;
        }
        String author = "";
        for (Element message : messages) {
            Message tm = parseTelegramMessage(message);
            //in a message group, only the first element has an author
            String currentAuthor = tm.getAuthor();
            if (!tm.getAuthor().isEmpty()) {
                author = currentAuthor;
            }

            tm.setAuthor(author);
            telegramMessages.add(tm);
        }
        return telegramMessages;
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
        LocalDateTime date = LocalDateTime.parse(
                message.select("div.date.details").attr("title"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        Message tm = new Message();
        // message1234567890
        tm.setLegacyID(message.id().substring("message".length()));
        tm.setCreated(date);
        tm.setType(Message.Type.TELEGRAM);
        tm.setAuthor(message.select("div.from_name").text());
        tm.setContent(message.select("div.text").text());
        return tm;
    }

    /**
     * Saves message records in the database.
     *
     * @param records list of records
     */
    public void saveMessages(List<Message> records) {
        for (Message message : records) {
            Message dbMessage =
                    messageRepository.findByLegacyIDAndCreated(
                            message.getLegacyID(), message.getCreated());
            if (dbMessage == null) {
                messageRepository.save(message);
            }
        }
    }

    /**
     * Finds and returns message records for the period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public Iterable<Message> getMessageHistoryBetweenDates(
            LocalDateTime from, LocalDateTime to) {
        return messageRepository.findByCreatedBetween(from, to);
    }

    @Override
    public void process() {
        processTelegramHistory();
    }
}
