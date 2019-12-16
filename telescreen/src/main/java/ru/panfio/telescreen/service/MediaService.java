package ru.panfio.telescreen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Media;
import ru.panfio.telescreen.repository.MediaRepository;

import java.time.LocalDateTime;

@Service
public class MediaService {
    @Autowired
    private MediaRepository repo;

    @Autowired
    S3Service s3Service;

    public MediaService(MediaRepository repo, S3Service s3Service) {
        this.repo = repo;
        this.s3Service = s3Service;
    }

    public String getContentType(String filename) {
        return s3Service.getContentType(Bucket.MEDIA, filename);
    }

    public byte[] getFile(String filename) {
        return s3Service.getByteArray(Bucket.MEDIA,filename);
    }

    public Iterable<Media> getAllMediaRecords() {
        return repo.findAll();
    }

    public Iterable<Media> getMediaRecordsByPeriod(LocalDateTime from, LocalDateTime to) {
        return repo.getAllBetweenDates(from, to);
    }
}

