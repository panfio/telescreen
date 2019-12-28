package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.model.Message;
import ru.panfio.telescreen.service.MessageService;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    /**
     * Controller.
     *
     * @param messageService service
     */
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Get massage history by period.
     *
     * @param from time
     * @param to time
     * @return messages
     */
    @ApiOperation(value = "Get massage history by period")
    @GetMapping("/message")
    ResponseEntity<Iterable<Message>> findMessageHistoryByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity<>(
                messageService.getMessageHistoryBetweenDates(t1, t2),
                HttpStatus.OK);
    }

    /**
     * Processing Telegram message History.
     *
     * @return ok
     */
    @ApiOperation(value = "Processing Telegram message History")
    @GetMapping("/message/process/telegram")
    public ResponseEntity processTelegramHistory() {
        messageService.processTelegramHistory();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}