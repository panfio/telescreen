package ru.panfio.telescreen.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class IsoLocalDateTimeAdapter
    extends XmlAdapter<String, LocalDateTime> {

    @Override
    public String marshal(final LocalDateTime localDateTime) throws Exception {
        return localDateTime.toString();
    }

    @Override
    public LocalDateTime unmarshal(final String string) throws Exception {
        return LocalDateTime.parse(string, DateTimeFormatter.ISO_DATE_TIME);
    }

}
