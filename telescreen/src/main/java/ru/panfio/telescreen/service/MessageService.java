package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Message;
import ru.panfio.telescreen.repository.MessageRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MessageService {

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
            try (InputStream inputStream =
                         objectStorage.getInputStream(filename)) {
                InputStreamReader isReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                saveMessages(parseTelegramMessages(sb.toString()));
            } catch (IOException e) {
                log.warn("Error processing " + filename);
                e.printStackTrace();
            }
        }
        log.info("End processing Telegram messages");
    }

    /**
     * Parsing messages from the given html file.
     *
     * @param html html
     * @return message list
     */
    public List<Message> parseTelegramMessages(String html) {
        List<Message> telegramMessages = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(html, "utf-8");
            Elements messages = doc.select("div.message.default");
            String name = "";
            for (Element message : messages) {
                String currentName =
                        message.select("div.from_name").text();
                LocalDateTime date = LocalDateTime.parse(
                        message.select("div.date.details").attr("title"),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
                if (!currentName.isEmpty()) {
                    name = currentName;
                }

                Message tm = new Message();
                // message1234567890
                tm.setLegacyID(message.id().substring("message".length()));
                tm.setCreated(date);
                tm.setType(Message.Type.TELEGRAM);
                tm.setAuthor(name);
                tm.setContent(message.select("div.text").text());

                telegramMessages.add(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return telegramMessages;
    }

    /**
     * Saves message records in the database.
     *
     * @param records list of records
     */
    public void saveMessages(List<Message> records) {
        for (Message message : records) {
            Message dbMessage =
                    messageRepository.findByLegacyIdAndCreatedTime(
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
        return messageRepository.getAllBetweenDates(from, to);
    }

}
