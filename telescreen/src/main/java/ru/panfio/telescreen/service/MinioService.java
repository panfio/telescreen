package ru.panfio.telescreen.service;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@Service
public class MinioService implements ObjectStorage {

    private final MinioClient mc;

    private final String bucket;

    //CHECKSTYLE:OFF
    @Autowired
    public MinioService(
            MinioClient mc, @Value("${minio.bucket}")
            String bucket) {
        this.mc = mc;
        this.bucket = bucket;
    }
    //CHECKSTYLE:ON

    /**
     * Returns list of filenames in bucket.
     *
     * @return list of filenames or empty list
     */
    @Override
    public List<String> getListOfFileNames() {
        List<String> fileList = new ArrayList<>();
        try {
            if (!mc.bucketExists(bucket)) {
                log.error(bucket + " bucket does not exist");
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
        } catch (Exception ignore) {
            ignore.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Gets file content type.
     *
     * @param filename filename
     * @return contentType or null
     */
    @Override
    public String getContentType(String filename) {
        try {
            ObjectStat objectStat = mc.statObject(bucket, filename);
            return objectStat.contentType();
        } catch (Exception e) {
            log.error("File not found or minio is not available");
            e.printStackTrace();
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
            log.warn("File not found " + filename);
            e.printStackTrace();
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
            log.warn("File not found " + filename);
//            e.printStackTrace();
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
    public String saveFileInTempFolder(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        try {
//            if (mc.statObject(bucket, filename).createdTime() == null) {
//                log.warn("Error saving file " + bucket + " " + filename);
//                return null;
//            }
            String path = "/tmp/" + bucket + "/" + filename;
            Files.deleteIfExists(Paths.get(path));
            Files.createDirectories(Paths.get(path).getParent());
            mc.getObject(bucket, filename, path);
            return path;
        } catch (Exception e) {
            log.error("Error saving file " + filename);
            return null;
        }
    }

    /**
     * Returns file creation time.
     * @param filename path
     * @return creation time
     * @throws IllegalArgumentException when filename is null
     */
    @Override
    public LocalDateTime getCreatedTime(String filename)
            throws IllegalArgumentException {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        try {
            Date createdTime = mc.statObject(bucket, filename).createdTime();
            return LocalDateTime.ofInstant(
                    createdTime.toInstant(),
                    TimeZone.getDefault().toZoneId());
        } catch (Exception e) {
            log.warn("File not found " + bucket + " " + filename);
            return null;
        }
    }

}
