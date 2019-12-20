package ru.panfio.telescreen.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.*;
import ru.panfio.telescreen.model.autotimer.Activity;
import ru.panfio.telescreen.model.autotimer.AutotimerRecords;
import ru.panfio.telescreen.model.autotimer.TimeEntry;
import ru.panfio.telescreen.model.spotify.RecentlyPlayedProto;
import ru.panfio.telescreen.model.timesheet.Task;
import ru.panfio.telescreen.model.timesheet.TimesheetExport;
import ru.panfio.telescreen.repository.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcessService {

    private static final Logger log = LoggerFactory.getLogger(ProcessService.class);

    @Autowired
    S3Service s3Service;

    @Autowired
    AutotimerRepository autotimerRepository;

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    TimeLogRepository timeLogRepository;

    @Autowired
    YouTubeRepository youTubeRepository;

    @Autowired
    ListenRecordRepository listenRecordRepository;

    @Autowired
    WellbeingRepository wellbeingRepository;

    @Autowired
    CallRecordRepository callRecordRepository;


    public ProcessService(S3Service s3Service, AutotimerRepository autotimerRepository, MediaRepository mediaRepository, TimeLogRepository timeLogRepository) {
        this.s3Service = s3Service;
        this.autotimerRepository = autotimerRepository;
        this.mediaRepository = mediaRepository;
        this.timeLogRepository = timeLogRepository;
    }

    public boolean processAutotimerRecords() {
        log.info("Processsing Autotimer records");
        int count = 0;
        for (String filename : s3Service.getListOfFileNames(Bucket.APP)) {
            if (!filename.contains("activities-")) {
                continue;
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                AutotimerRecords autotimerRecords = mapper.readValue(
                        s3Service.getInputStream(Bucket.APP, filename),
                        AutotimerRecords.class);
                List<Autotimer> list = collectAutotimers(autotimerRecords.getActivities());
                count = count + list.size();
                autotimerRepository.saveAll(list);
                //todo save processed filename item to db
            } catch (IOException e) {
                log.warn("Autotimer parse error " + filename);
                e.printStackTrace();
                return false;
            }
        }
        log.info("Processed Autotimer items = " + count);
        return true;
    }

    private List<Autotimer> collectAutotimers(List<Activity> activities) {
        List<Autotimer> autotimers = new ArrayList<>();
        for (Activity activity : activities) {
            List<TimeEntry> list = activity.getTime_entries();
            for (TimeEntry timeEntry : list) {
                String name = activity.getName();
                if (name.startsWith("Desktop")) {
                    continue;
                }
                Autotimer autotimer = new Autotimer();
                autotimer.setName(name);
                autotimer.setStartTime(timeEntry.getStart_time().toInstant()
                        .atOffset(ZoneOffset.ofHours(0)).toLocalDateTime());
                autotimer.setEndTime(timeEntry.getEnd_time().toInstant()
                        .atOffset(ZoneOffset.ofHours(0)).toLocalDateTime());
                autotimer.setType(0); //default
                //TODO find alternative
                if (name.startsWith("Google Chrome") ||
                        name.startsWith("Chromium") ||
                        name.startsWith("Mozilla Firefox")) {
                    autotimer.setType(1);
                }
                if (name.equals("Visual Studio Code") ||
                        name.startsWith(".../src/main")) {
                    autotimer.setType(2);
                }
                if (name.startsWith("Telegram")) {
                    autotimer.setType(3);
                }
                autotimers.add(autotimer);
            }
        }
        return autotimers;
    }

    /**
     * Save Media records into database
     *
     * @return success
     */
    public boolean processMediaRecords() {
        try {
            List<Media> fileList = new ArrayList<>();
            for (String path : s3Service.getListOfFileNames(Bucket.MEDIA)) {
                String type = path.substring(0, path.indexOf("/"));
                Media record = new Media();
                record.setPath(path);
                record.setType(type);
                record.setUrl("/media/file?filename=" + path);
                record.setCreated(getCreationTime(path));

                fileList.add(record);
            }
            mediaRepository.saveAll(fileList);
            //todo save processed filename item to db or recreate item list
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Processing Timesheet backup file and save them as TimeLog records
     *
     * @return true if operation is successful
     */
    public boolean processTimesheetRecords() {
        //todo bucket name as Enum
        //todo create method that returns file list in folder
        String lastBackup = s3Service.getListOfFileNames(Bucket.APP)
                .stream().filter(s -> s.startsWith("timesheet/TimesheetBackup"))
                .reduce((max, current) -> {
                    //will fall if filename is incorrect
                    LocalDateTime dt = getDateFromPath(current);
                    return ((dt != null) && (dt.isAfter(getDateFromPath(max)))) ? current : max;
                }).orElse("");
        if (lastBackup.equals("")) {
            log.warn("Timesheet backup files not found or already processed");
            return false;
        }
        log.info("Processing " + lastBackup);
        TimesheetExport te = unmarshallXml(
                TimesheetExport.class,
                s3Service.getInputStream(Bucket.APP, lastBackup));

        Map<String, String> tags = new HashMap<>();
        te.getTags().getTags().forEach(tag -> {
            tags.put(tag.getTagId(), tag.getName());
        });
        List<TimeLog> timeLogs = new ArrayList<>();
        //todo filter and insert records after last import
        for (Task task : te.getTasks().getTasks()) {
            TimeLog tl = new TimeLog();
            tl.setId(task.getTaskId());
            tl.setDescription(task.getDescription());
            tl.setStartDate(task.getStartDate());
            tl.setEndDate(task.getEndDate());
            tl.setLocation(task.getLocation());
            tl.setFeeling(task.getFeeling());
            List<String> tlTags = te.getTaskTags().getTaskTags().stream()
                    .filter(taskTag -> taskTag.getTaskId().equals(task.getTaskId()))
                    .map(el -> tags.get(el.getTagId()))
                    .collect(Collectors.toList());
            tl.setTags(tlTags);
            timeLogs.add(tl);
        }
        timeLogRepository.saveAll(timeLogs);
        //todo save processed filename item to db
        return true;
    }

    public void processYouTubeHistory() {
        //todo what if there are many files for different accounts?
        //todo save last processed
        log.info("Start processing YouTube history");
        String filename = "google/YouTube/history/watch-history.json";
        try {
            InputStream stream = s3Service.getInputStream(Bucket.APP, filename);
            if (stream == null) {
                log.warn("File not found. Put watch-history.json in app/google/YouTube/history/");
                return;
            }
            ObjectMapper mapper = new ObjectMapper();
            List<YouTube> yt = mapper.readValue(
                    stream,
                    new TypeReference<List<YouTube>>() {
                    });
            List<YouTube> filtered = yt.stream().map(e -> {
                e.setTitle(e.getTitle().substring(7)); //removes "Watched "
                return e;
            }).collect(Collectors.toList());
            youTubeRepository.saveAll(filtered);
        } catch (Exception e) {
            log.warn("Parse error " + filename);
            e.printStackTrace();
        }
    }

    public void processSpotifyRecentlyPlayed(String accessToken) {
        //https://developer.spotify.com/console/get-track/
        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        //todo process list of files
        String filename = "spotify/recently_played.bnk";
        InputStream stream = s3Service.getInputStream(Bucket.APP, filename);
        try {
            RecentlyPlayedProto.RecentlyPlayed rp =
                    RecentlyPlayedProto.RecentlyPlayed.parseFrom(stream);

//            int i = 0;
            List<ListenRecord> listenedTracks = new ArrayList<>();
            for (RecentlyPlayedProto.RecentTrack item : rp.getTList()) {
                String trackId = item.getTrack().substring(
                        item.getTrack().lastIndexOf(":") + 1);

                ListenRecord lr = new ListenRecord();
                lr.setExternalId(trackId);
                lr.setType(ListenRecord.Type.SPOTIFY);
                lr.setId((long) item.getTimestamp());
                lr.setArtist(trackId);
                lr.setTitle(trackId);
                lr.setListenTime(LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(item.getTimestamp()),
                        TimeZone.getDefault().toZoneId()));
                final GetTrackRequest getTrackRequest = spotifyApi.getTrack(trackId).build();
                try {
                    Track track = getTrackRequest.execute();

                    lr.setArtist(track.getArtists()[0].getName());
                    lr.setTitle(track.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SpotifyWebApiException e) {
                    e.printStackTrace();
                }
                listenedTracks.add(lr);
//                i++; if (i > 10) return;
            }
            listenRecordRepository.saveAll(listenedTracks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processSoundCloud() {
        long lastPrcessedItem = 0;
        Map<Long, ListenRecord> history = getSoundCloudPlayHistory(lastPrcessedItem);
        Set<Long> keyset = history.keySet();
        Collection<ListenRecord> listenedTracks = history.values();
        if (!listenedTracks.isEmpty()) {
            listenRecordRepository.saveAll(listenedTracks);
        }
        if (!keyset.isEmpty()) {
            Long currentLastProcessedItem = Collections.max(keyset);
            System.out.println(currentLastProcessedItem);
        }
    }

    /**
     * Returns map of tracks from SoundCloud db
     *
     * @return map of tracks
     */
    private Map<Long, ListenRecord> getSoundsInfo() {
        String soundsSql = "SELECT s._id AS id, " +
                "u.username AS username, " +
                "s.title AS title, " +
                "s.permalink_url AS permalink_url " +
                "FROM Sounds s LEFT JOIN Users u ON s.user_id == u._id";
        Map<Long, ListenRecord> soundsInfo = new HashMap<>();
        try (Connection conn = this.connectSQLite(Bucket.APP, "soundcloud/SoundCloud");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(soundsSql)) {
            while (rs.next()) {
                ListenRecord soundInfo = new ListenRecord();
                soundInfo.setExternalId(rs.getString("id"));
                soundInfo.setArtist(rs.getString("username"));
                soundInfo.setTitle(rs.getString("title"));
                soundInfo.setUrl(rs.getString("permalink_url"));

                soundsInfo.put(rs.getLong("id"), soundInfo);
            }
        } catch (SQLException | FileNotFoundException e) {
            log.info("Failed processing SoundCloud sounds info");
        }
        return soundsInfo;
    }

    /**
     * Returns the SoundCloud listening history after the specified time
     *
     * @param timestamp
     * @return map of listened tracks
     */
    private Map<Long, ListenRecord> getSoundCloudPlayHistory(long timestamp) {
        String listenSql = "SELECT * FROM playhistory";
        Map<Long, ListenRecord> soundsInfo = getSoundsInfo();
        Map<Long, ListenRecord> listenRecords = new HashMap<>();
        try (Connection conn = this.connectSQLite(Bucket.APP, "soundcloud/collection.db");
             PreparedStatement pstmt = conn.prepareStatement(listenSql)) {
//            pstmt.setLong(1,timestamp);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ListenRecord sound = soundsInfo.get(rs.getLong("track_id"));
                if (sound == null) {
                    continue;
                }
                Long lrId = rs.getLong("timestamp");
                ListenRecord lr = new ListenRecord();
                lr.setId(lrId);
                lr.setExternalId(sound.getExternalId());
                lr.setArtist(sound.getArtist());
                lr.setTitle(sound.getTitle());
                lr.setType(ListenRecord.Type.SOUNDCLOUD);
                lr.setListenTime(rs.getTimestamp("timestamp").toLocalDateTime());
                lr.setUrl(sound.getUrl());

                listenRecords.put(lrId, lr);
            }
        } catch (SQLException | FileNotFoundException e) {
            log.info("Failed processing SoundCloud play history");
        }
        return listenRecords;
    }

    public void processWellbeingRecords() {
        String wellbeingSql = "SELECT\n" +
                "    e._id, e.package_id,\n" +
                "    e.timestamp, e.type,\n" +
                "    e.instance_id, p.package_name\n" +
                "FROM events e LEFT JOIN packages p \n" +
                "ON e.package_id = p._id\n" +
                "ORDER BY e._id ASC";

        List<Wellbeing> wellbeingActivities = new ArrayList<>();
        try (Connection conn = this.connectSQLite(Bucket.APP, "wellbeing/app_usage");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(wellbeingSql)) {
            Map<Long, Wellbeing> tmp = new HashMap<>();
            while (rs.next()) {
                if (rs.getInt("type") == 1) {
                    long id = rs.getLong("instance_id");

                    Wellbeing wr = new Wellbeing();
                    wr.setStartTime(rs.getTimestamp("timestamp").toLocalDateTime());
                    wr.setId(id);

                    tmp.put(id, wr);
                }
                if (rs.getInt("type") == 2) {
                    long id = rs.getLong("instance_id");
                    Wellbeing tempWellbringRecord = tmp.get(id);
                    if (tempWellbringRecord == null) {
                        continue;
                    }
                    Wellbeing wr = new Wellbeing();
                    wr.setStartTime(tempWellbringRecord.getStartTime());
                    wr.setEndTime(rs.getTimestamp("timestamp").toLocalDateTime());
                    wr.setApp(rs.getString("package_name"));
                    wr.setType(Wellbeing.Type.ACTIVITY);

                    wellbeingActivities.add(wr);
                    tmp.remove(id);
                }
            }
            wellbeingRepository.saveAll(wellbeingActivities);
        } catch (SQLException | FileNotFoundException e) {
            log.info("Failed processing Wellbeing history");
        }
    }

    public void processCallHistory() {
        String callSql = "SELECT _id, number, date, duration, name, type FROM calls";
        List<CallRecord> callRecords = new ArrayList<>();
        try (Connection conn = this.connectSQLite(Bucket.APP, "call/calllog.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(callSql)) {
            while (rs.next()) {
                CallRecord cr = new CallRecord();
                cr.setDate(rs.getTimestamp("date").toLocalDateTime());
                cr.setDuration(rs.getInt("duration"));
                cr.setNumber(rs.getString("number"));
                String name = rs.getString("name");
                cr.setName(name.equals("") ? "Unknown" : name);
                cr.setType(rs.getInt("type"));

                callRecords.add(cr);
            }
            //todo save last processed
            //todo save telegram/skype calls
            callRecordRepository.saveAll(callRecords);
        } catch (SQLException | FileNotFoundException e) {
            log.info("Failed processing call history");
        }
    }

    public List<Message> parseTelegramMessage(String html) {
        List<Message> telegramMessages = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(html, "utf-8");
            Elements messages = doc.select("div.message.default");
            String name = "";
            for (Element message : messages) {
                String currentName = message.select("div.from_name").text();
                LocalDateTime date = LocalDateTime.parse(
                        message.select("div.date.details").attr("title"),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
                if (!currentName.isEmpty()) {
                    name = currentName;
                }

                Message tm = new Message();
                tm.setLegacyID(message.id().substring(7));
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
     * Returns creation time
     *
     * @param path
     * @return file's creation time
     */
    private LocalDateTime getCreationTime(String path) {
        if (path == null) {
            throw new IllegalArgumentException();
        }
        LocalDateTime dateFromPath = getDateFromPath(path);
        if (dateFromPath != null) {
            return dateFromPath;
        }
        LocalDateTime dateFromObjectInfo = s3Service.getCreatedTime(Bucket.MEDIA, path);
        if (dateFromObjectInfo != null) {
            return dateFromObjectInfo;
        }
        //todo get date from meta like Iphone photos (may be significantly slower)
        return LocalDateTime.now(); //todo
    }

    /**
     * Finds and return LocalDateTime from filename or null if not found
     *
     * @param path
     * @return localDateTime can be null
     */
    public LocalDateTime getDateFromPath(String path) {
        //TODO find a pretty solution for this ugly code. consider switch/regexp + date validation
        //Cover new cases in test
        String filename = path.substring(path.indexOf("/") + 1);
        String dateInName = "";
        String pattern = "";
        if (filename.startsWith("IMG_") || filename.startsWith("VID_") || filename.startsWith("Timesheet")) {
            if (filename.startsWith("Timesheet")) {
                pattern = "yyyy-MM-dd_HHmmss";
            } else {
                pattern = "yyyyMMdd_HHmmss";
            }
            int start = filename.indexOf("_") + 1;
            dateInName = filename.substring(start, start + pattern.length());
        } else if (filename.endsWith("-note.m4a")) {
            dateInName = filename.substring(0, filename.lastIndexOf("-"));
            pattern = "yyyy-MM-dd-HH-mm-ss";
        } else if (filename.startsWith("Screenshot")) {
            dateInName = filename.substring(filename.indexOf("2"), filename.lastIndexOf("."));
            if (filename.contains(" ")) {
                pattern = "yyyy-MM-dd HH-mm-ss";
            } else {
                pattern = "yyyyMMdd-HHmmss";
            }
        }
        try {
            return LocalDateTime.parse(dateInName, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
//            log.info("Date not found " + filename + " " + dateInName + " " + pattern);
            return null;
        }
    }

    /**
     * Unmarshal given xml from input stream
     *
     * @param clazz  class
     * @param stream
     * @param <T>
     * @return //todo
     */
    @SuppressWarnings("unchecked")
    public <T> T unmarshallXml(Class<T> clazz, InputStream stream) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (T) jaxbUnmarshaller.unmarshal(stream);
        } catch (Exception e) {
            log.error("Parse error " + clazz.getName());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Establishes a database connection
     *
     * @param bucket
     * @param filename
     * @return connection or null if an error occurred
     */
    private Connection connectSQLite(Bucket bucket, String filename)  throws FileNotFoundException{
        Connection conn = null;
        String path = s3Service.saveFileInTempFolder(bucket, filename);
        if (path == null) {
            log.warn("File not found. Put " + filename + " into " + bucket.getName() + " bucket");
            throw new FileNotFoundException();
        }
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return conn;
    }
}
