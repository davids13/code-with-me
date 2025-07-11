package org.acme.upload;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class SseService {

    private final Map<String, SseEventSink> sinks = new ConcurrentHashMap<>();

    Sse sse;

    @Inject
    public SseService(Sse sse) {
        this.sse = sse;
    }

    public void register(String clientId, SseEventSink sink) {
        sinks.put(clientId, sink);
    }

    public void unregister(String clientId) {
        sinks.remove(clientId);
    }

    public void sendProgress(String clientId, UploadProgress progress) {
        var sink = sinks.get(clientId);
        if (sink != null && !sink.isClosed()) {
            // Send complete progress information as JSON
            String progressJson = String.format(
                    "{\"percentage\": %d, \"uploadedBytes\": %d, \"totalBytes\": %d}",
                    progress.getPercentage(), progress.uploadedBytes, progress.totalBytes
            );
            sink.send(sse.newEventBuilder()
                    .name("upload-progress")
                    .data(progressJson)
                    .build());
        }
    }

}
