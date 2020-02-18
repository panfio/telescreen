package ru.panfio.telescreen.handler.service;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MinioService implements ObjectStorage {

    private final MinioClient mc;
    private final String bucket;

    public MinioService(MinioClient mc,
                        @Value("${minio.bucket}") String bucket) {
        this.mc = mc;
        this.bucket = bucket;
    }

    public void createFolderStructure() {
        try {
            if (!mc.bucketExists(bucket)) {
                mc.makeBucket(bucket);
            }
            InputStream file = new ByteArrayInputStream(
                    "*\n!.gitignore".getBytes(StandardCharsets.UTF_8));
            List<String> folders = Arrays.asList(
                    "autotimer", "call", "dayone", "google",
                    "media/call", "media/photo",
                    "media/screenshot", "media/video",
                    "media/voicenote", "mifit", "sms",
                    "soundcloud", "timesheet", "wellbeing", "whatsapp"
            );
            for (String path : folders) {
                String filename = path + "/.gitignore";
                if (getObjectStats(bucket, filename) == null) {
                    mc.putObject(bucket, filename, file, "text/plain");
                }
            }
        } catch (Exception e) {
            log.error("Error creating folder structure {}", e.getMessage());
        }
    }

    private ObjectStat getObjectStats(String bucket, String objectName) {
        try {
            return mc.statObject(bucket, objectName);
        } catch (Exception e) {
            log.warn("File not found {} {}", objectName, e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> listAllObjects() {
        List<String> fileList = new ArrayList<>();
        try {
            for (Result<Item> obj : mc.listObjects(bucket)) {
                if (obj.get().isDir()) {
                    continue;
                }
                if (obj.get().objectName().contains(".gitignore")) {
                    continue;
                }
                fileList.add(obj.get().objectName());
            }
        } catch (Exception e) {
            log.error("Minio is not available: {}", e.getMessage());
        }
        return fileList;
    }

    @Override
    public List<String> listObjects(Predicate<String> criteria) {
        return listAllObjects()
                .stream()
                .filter(criteria)
                .collect(Collectors.toList());
    }

    @Override
    public String contentType(String filename) {
        ObjectStat objectStat = getObjectStats(bucket, filename);
        if (objectStat == null) {
            return null;
        }
        return objectStat.contentType();
    }

    @Override
    public byte[] getByteArray(String filename) {
        try {
            InputStream in = mc.getObject(bucket, filename);
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            log.warn("File not found {} {}", filename, e.getMessage());
            return null;
        }
    }

    @Override
    public InputStream getInputStream(String filename) {
        try {
            return mc.getObject(bucket, filename);
        } catch (Exception e) {
            log.warn("File not found {} {}", filename, e.getMessage());
            return null;
        }
    }

    @Override
    public String saveInTmpFolder(@NonNull String filename) {
        try {
            String path = "/tmp/" + bucket + "/" + filename;
            Files.deleteIfExists(Paths.get(path));
            Files.createDirectories(Paths.get(path).getParent());
            mc.getObject(bucket, filename, path);
            return path;
        } catch (Exception e) {
            log.error("Error saving file {} {}", filename, e.getMessage());
            return null;
        }
    }

    @Override
    public Instant getCreatedTime(String filename)
            throws IllegalArgumentException {
        ObjectStat objectStat = getObjectStats(bucket, filename);
        if (objectStat == null) {
            return null;
        }
        return objectStat.createdTime().toInstant();
    }

    public String getContent(String filename, String charset) {
        try (InputStream inputStream = getInputStream(filename)) {
            return IOUtils.toString(inputStream, charset);
        } catch (IOException e) {
            log.warn("Error processing " + filename);
            return "";
        }
    }
}
