package ru.panfio.telescreen.handler.service;

import java.util.List;

public interface MessageBus {
    /**
     * Sends message to the topic.
     *
     * @param topic   topic/queue
     * @param message message
     */
    void send(String topic, String message);

    /**
     * Converts object to json and sends the message to the topic.
     *
     * @param topic   topic/queue
     * @param message message
     */
    void send(String topic, Object message);

    /**
     * Converts objects to json and sends the messages to the topic.
     *
     * @param topic   topic/queue
     * @param messages messages
     */
    <T> void sendAll(String topic, List<T> messages);
}
