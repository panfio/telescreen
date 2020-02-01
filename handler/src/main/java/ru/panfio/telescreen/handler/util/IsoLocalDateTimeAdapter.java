package ru.panfio.telescreen.handler.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;
import java.time.ZonedDateTime;


public final class IsoLocalDateTimeAdapter
    extends XmlAdapter<String, Instant> {

    @Override
    public String marshal(final Instant instant) {
        return instant.toString();
    }

    @Override
    public Instant unmarshal(final String string) {
        return ZonedDateTime.parse(string).toInstant();
    }

}
