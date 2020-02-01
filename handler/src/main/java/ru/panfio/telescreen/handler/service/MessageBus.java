package ru.panfio.telescreen.handler.service;

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
}
