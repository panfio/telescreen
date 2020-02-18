package ru.panfio.telescreen.handler.service;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class MiFitServiceTest {
    @Test
    void name() {
        MiFitService service = new MiFitService();
        assertEquals("mifit", service.name());
    }
}