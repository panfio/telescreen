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
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
public class ProcessService {

    private static final Logger log = LoggerFactory.getLogger(ProcessService.class);

    @Autowired
    S3Service s3Service;

    @Autowired
    PersistenceService persistenceService;

    private ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

    public ProcessService(S3Service s3Service, PersistenceService persistenceService) {
        this.s3Service = s3Service;
        this.persistenceService = persistenceService;
    }

    public void processAll() {
        executor.submit(this::processWellbeingRecords);
        executor.submit(this::processAutotimerRecords);
        executor.submit(this::processYouTubeHistory);
        executor.submit(this::processMediaRecords);
        executor.submit(this::processTimesheetRecords);
        executor.submit(this::processSoundCloud);
        executor.submit(this::processTelegramHistory);
        executor.submit(this::processCallHistory);
    }

    /**
     * Processing AutoTimer records
     *
     * @return true if success
     */
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
                //todo save processed filename item to db
                persistenceService.saveAutotimerRecords(list);
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
            persistenceService.saveMediaRecords(fileList);
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
        //todo create method that returns file list in folder
        String lastBackup = s3Service.getListOfFileNames(Bucket.APP)
                .stream().filter(s -> s.startsWith("timesheet/TimesheetBackup"))
                .reduce((max, current) -> {
                    //will fall if filename is incorrect
                    LocalDateTime dt = getDateFromPath(current);
                    return ((dt != null) && (dt.isAfter(getDateFromPath(max)))) ? current : max;
                }).orElse("");
        if (lastBackup.equals("")) {
            log.warn("Timesheet backup files not found");
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
        persistenceService.saveTimeLogRecords(timeLogs);
        return true;
    }

    /**
     * Processing YouTube history from Google export
     */
    public void processYouTubeHistory() {
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
                e.setId(e.getTime().toEpochSecond(ZoneOffset.UTC));
                e.setTitle(e.getTitle().substring(7)); //removes "Watched "
                return e;
            }).collect(Collectors.toList());
            persistenceService.saveYouTubeRecords(filtered);
        } catch (Exception e) {
            log.warn("Parse error " + filename);
            e.printStackTrace();
        }
        log.info("End processing YouTube history");
    }

    /**
     * Processing Spotify listen history
     *
     * @param accessToken token from https://developer.spotify.com/console/get-track/
     */
    public void processSpotifyRecentlyPlayed(String accessToken) {
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
            persistenceService.saveListenRecords(listenedTracks);
        } catch (IOException e) {
            e.printStackTrace();
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
     * Processing the SoundCloud listening history
     */
    public void processSoundCloud() {
        log.info("Start processing SoundCloud history");
        String listenSql = "SELECT * FROM playhistory";
        Map<Long, ListenRecord> soundsInfo = getSoundsInfo();
        List<ListenRecord> listenRecords = new ArrayList<>();
        try (Connection conn = this.connectSQLite(Bucket.APP, "soundcloud/collection.db");
             PreparedStatement pstmt = conn.prepareStatement(listenSql)) {
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

                listenRecords.add(lr);
            }
            persistenceService.saveListenRecords(listenRecords);
        } catch (SQLException | FileNotFoundException e) {
            log.info("Failed processing SoundCloud play history");
        }
        log.info("End processing SoundCloud history");
    }

    /**
     * Processing App activity from Wellbeing database
     */
    public void processWellbeingRecords() {
        log.info("Start processing Wellbeing history");
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
                    wr.setType(Wellbeing.Type.ACTIVITY);

//                    Document doc = Jsoup.parse(new URL("https://play.google.com/store/apps/details?id=" + rs.getString("package_name")) , 50000);
//                    String htmlTitle = doc.head().tagName("title").text();
//                    String appTitle = htmlTitle.substring(0, htmlTitle.lastIndexOf("-") - 1);
                    wr.setApp(rs.getString("package_name"));

                    wellbeingActivities.add(wr);
                    tmp.remove(id);
                }
                if (wellbeingActivities.size() > 500) {
                    persistenceService.saveWellbeingRecords(wellbeingActivities);
                    wellbeingActivities.clear();
                }
            }
            persistenceService.saveWellbeingRecords(wellbeingActivities);
        } catch (Exception e) {
            log.info("Failed processing Wellbeing history");
        }
        log.info("End processing Wellbeing history");
    }

    /**
     * Processing Call history from android phone
     */
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
            //todo save telegram/skype calls
            persistenceService.saveCallRecords(callRecords);
        } catch (SQLException | FileNotFoundException e) {
            log.info("Failed processing call history");
        }
    }

    /**
     * Processing Telegram message history from chat history export
     */
    public void processTelegramHistory() {
        log.info("Start processing Telegram messages");
        for (String filename : s3Service.getListOfFileNames(Bucket.APP)) {
            if (!filename.startsWith("app/telegram") && !filename.contains("messages")) {
                continue;
            }
            try (InputStream inputStream = s3Service.getInputStream(Bucket.APP, filename)) {
                InputStreamReader isReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                persistenceService.saveMessages(parseTelegramMessages(sb.toString()));
            } catch (IOException e) {
                log.warn("Error processing " + filename);
                e.printStackTrace();
            }
        }
        log.info("End processing Telegram messages");
    }

    /**
     * Parsing messages from the given html file
     * @param html
     * @return message list
     */
    public List<Message> parseTelegramMessages(String html) {
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
    private Connection connectSQLite(Bucket bucket, String filename) throws FileNotFoundException {
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
