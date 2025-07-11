package org.acme.upload;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class UploadService {

    private final Map<String, UploadProgress> uploads = new ConcurrentHashMap<>();

    public void startUpload(String uploadId, long totalBytes) {
        uploads.put(uploadId, new UploadProgress(totalBytes));
    }

    public void updateProgress(String uploadId, long chunkSize) {
        var progress = uploads.get(uploadId);
        if (progress != null) {
            progress.uploadedBytes += chunkSize;
        }
    }

    public UploadProgress getProgress(String uploadId) {
        return uploads.get(uploadId);
    }

    public void finishUpload(String uploadId) {
        uploads.remove(uploadId);
    }

}
