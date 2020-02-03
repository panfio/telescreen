package ru.panfio.telescreen.handler.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import ru.panfio.telescreen.handler.util.IsoInstantDeserializer;
import ru.panfio.telescreen.handler.util.IsoInstantSerializer;

import java.time.Instant;

@Data
@Builder
public class Autotimer {
    private String id;
    private String name;
    private int type;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant startTime;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant endTime;

    /**
     * Extracts Autotimer type based on the activity name.
     *
     * @param name activity name
     * @return Autotimer type
     */
    public static int typeByName(final String name) {
        //TODO find alternative
        if (name.startsWith("Google Chrome")
                || name.startsWith("Chromium")
                || name.startsWith("Mozilla Firefox")) {
            return 1;
        }
        if (name.equals("Visual Studio Code")
                || name.startsWith(".../src/main")) {
            return 2;
        }
        if (name.startsWith("Telegram")) {
            //CHECKSTYLE:OFF
            return 3;
            //CHECKSTYLE:ON
        }
        return 0;
    }
}
