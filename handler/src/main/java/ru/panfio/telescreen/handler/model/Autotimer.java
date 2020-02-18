package ru.panfio.telescreen.handler.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import ru.panfio.telescreen.handler.util.IsoInstantDeserializer;
import ru.panfio.telescreen.handler.util.IsoInstantSerializer;

import java.time.Instant;

@Getter
@Builder
public class Autotimer {
    private final String id;
    private final String name;
    private final int type;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private final Instant startTime;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private final Instant endTime;

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
