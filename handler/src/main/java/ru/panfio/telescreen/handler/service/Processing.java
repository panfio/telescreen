package ru.panfio.telescreen.handler.service;

public interface Processing {
    /**
     * Performs files processing.
     */
    void process();

    /**
     * Handler name.
     * @return name
     */
    String name();
}
