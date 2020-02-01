package ru.panfio.telescreen.handler.service;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MinioService implements ObjectStorage {

    private final MinioClient mc;
    private final String bucket;

    /**
     * Constructor.
     *
     * @param mc     Minio Client
     * @param bucket bucket name
     */
    public MinioService(
            MinioClient mc,
            @Value("${minio.bucket}") String bucket) {
        this.mc = mc;
        this.bucket = bucket;
    }

    /**
     * Returns list of filenames in bucket.
     *
     * @return list of filenames or empty list
     */
    @Override
    public List<String> listAllObjects() {
        List<String> fileList = new ArrayList<>();
        try {
            if (!mc.bucketExists(bucket)) {
                log.error("{} bucket does not exist", bucket);
            }
            for (Result<Item> obj : mc.listObjects(bucket)) {
                if (obj.get().isDir()) {
                    continue;
                }
                if (obj.get().objectName().contains(".gitignore")) {
                    continue;
                }
                fileList.add(obj.get().objectName());
            }
            return fileList;
        } catch (Exception e) {
            log.error("Minio is not available: {}", e.getMessage());
            return fileList;
        }
    }

    /**
     * Gets file content type.
     *
     * @param filename filename
     * @return contentType or null
     */
    @Override
    public String contentType(String filename) {
        try {
            ObjectStat objectStat = mc.statObject(bucket, filename);
            return objectStat.contentType();
        } catch (Exception e) {
            log.error("File not found or minio is not available: {}",
                    e.getMessage());
            return null;
        }
    }

    /**
     * Returns the byte array of the file.
     *
     * @param filename filename
     * @return byte array or null if file not found
     */
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

    /**
     * Returns InputStream of the file.
     *
     * @param filename filename
     * @return inputStream or null file not found
     */
    @Override
    public InputStream getInputStream(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        try {
            return mc.getObject(bucket, filename);
        } catch (Exception e) {
            log.warn("File not found {} {}", filename, e.getMessage());
            return null;
        }
    }

    /**
     * Downloads file in the temp directory.
     *
     * @param filename filename
     * @return path if success or else null
     */
    @Override
    public String saveInTmpFolder(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
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

    /**
     * Returns file creation time.
     *
     * @param filename path
     * @return creation time
     * @throws IllegalArgumentException when filename is null
     */
    @Override
    public Instant getCreatedTime(String filename)
            throws IllegalArgumentException {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        try {
            Date createdTime = mc.statObject(bucket, filename).createdTime();
            return  createdTime.toInstant();
        } catch (Exception e) {
            log.warn("File not found {} {}", filename, e.getMessage());
            return null;
        }
    }

    /**
     * Gets text content from the file.
     *
     * @param filename filename
     * @param charset  text encoding
     * @return text
     */
    public String getContent(String filename, String charset) {
        try (InputStream inputStream = getInputStream(filename)) {
            return IOUtils.toString(inputStream, charset);
        } catch (IOException e) {
            log.warn("Error processing " + filename);
            return "";
        }
    }


}