package ru.panfio.telescreen.service;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.messages.Item;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


@Service
public class S3Service {

    private final static Logger log = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    MinioClient mc;

    public S3Service(MinioClient mc) {
        this.mc = mc;
    }

    /**
     * Returns list of filenames in bucket
     *
     * @param bucket name
     * @return list of filenames or empty list
     */
    public List<String> getListOfFileNames(Bucket bucket) {
        List<String> fileList = new ArrayList<>();
        try {
            if (!mc.bucketExists(bucket.getName())) {
                log.error(bucket + " bucket does not exist");
            }
            for (Result<Item> obj : mc.listObjects(bucket.getName())) {
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
     * Gets file content type
     *
     * @param bucket
     * @param filename
     * @return contentType or null
     */
    public String getContentType(Bucket bucket, String filename) {
        try {
            ObjectStat objectStat = mc.statObject(bucket.getName(), filename);
            return objectStat.contentType();
        } catch (Exception e) {
            log.error("File not found or minio is not available");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the byte array of the file
     *
     * @param bucket
     * @param filename
     * @return byte array or null if file not found
     */
    public byte[] getByteArray(Bucket bucket, String filename) {
        try {
            InputStream in = mc.getObject(bucket.getName(), filename);
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            log.warn("File not found " + bucket + " " + filename);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns InputStream of the file
     *
     * @param bucket
     * @param filename
     * @return inputStream or null file not found
     */
    public InputStream getInputStream(Bucket bucket, String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        try {
            return mc.getObject(bucket.getName(), filename);
        } catch (Exception e) {
            log.warn("File not found " + bucket.getName() + " " + filename);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * Downloads file in the temp directory
     * @param bucket
     * @param filename
     * @return path if success or else null
     */
    public String saveFileInTempFolder(Bucket bucket, String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        try {
//            if (mc.statObject(bucket.getName(), filename).createdTime() == null) {
//                log.warn("Error saving file " + bucket.getName() + " " + filename);
//                return null;
//            }
            String path = "/tmp/" + bucket.getName() + "/" + filename;
            Files.deleteIfExists(Paths.get(path));
            Files.createDirectories(Paths.get(path).getParent());
            mc.getObject(bucket.getName(), filename, path);
            return path;
        } catch (Exception e) {
            log.error("Error saving file " + bucket.getName() + " " + filename);
            return null;
        }
    }

    public LocalDateTime getCreatedTime(Bucket bucket, String filename) throws IllegalArgumentException {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        try {
            Date createdTime = mc.statObject(bucket.getName(), filename).createdTime();
            return LocalDateTime.ofInstant(
                    createdTime.toInstant(),
                    TimeZone.getDefault().toZoneId());
        } catch (Exception e) {
            log.warn("File not found " + bucket + " " + filename);
            return null;
        }
    }


}
