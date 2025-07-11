package org.acme.upload;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@Path("upload")
public class FileUploadResource {

    // https://myfear.substack.com/i/167082337/backend-logic-reactive-file-chunking-with-progress

    private static final Logger LOG = Logger.getLogger(FileUploadResource.class);
    Vertx vertx;
    UploadService uploadService;
    SseService sseService;

    @Inject
    public FileUploadResource(Vertx vertx, UploadService uploadService, SseService sseService) {
        this.vertx = vertx;
        this.uploadService = uploadService;
        this.sseService = sseService;
    }

    public FileUploadResource() throws IOException {
        java.nio.file.Path tempDir = null;
        // Create uploads directory in Maven target folder
        java.nio.file.Path targetDir = java.nio.file.Path.of("target");
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        targetDir.resolve("uploads");
        if (!Files.exists(tempDir)) {
            Files.createDirectories(tempDir);
        }
        LOG.infof("Upload temp directory: %s", tempDir.toAbsolutePath());
    }

    @POST
    @Path("/chunk")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Blocking
    public Uni<Response> uploadChunk(byte[] body,
                                     @HeaderParam("X-Upload-Id") String uploadId,
                                     @HeaderParam("X-Chunk-Number") int chunkNumber,
                                     @HeaderParam("X-Total-Bytes") long totalBytes,
                                     @HeaderParam("X-Client-Id") String clientId) {

        java.nio.file.Path tempDir = null;

        LOG.infof("Received chunk %d for upload %s (size: %d bytes, total: %d bytes, client: %s)",
                chunkNumber, uploadId, body.length, totalBytes, clientId);

        if (uploadId == null || uploadId.isEmpty())
            return Uni.createFrom().item(Response.status(400).entity("Missing X-Upload-Id").build());

        if (chunkNumber == 1) {
            LOG.infof("Starting new upload %s with total size %d bytes", uploadId, totalBytes);
            uploadService.startUpload(uploadId, totalBytes);
        }

        var chunkPath = tempDir.resolve(uploadId + ".part" + chunkNumber);
        byte[] chunkData = body;

        LOG.infof("Writing chunk %d to file: %s", chunkNumber, chunkPath);

        return vertx.fileSystem()
                .writeFile(chunkPath.toString(), Buffer.buffer(chunkData))
                .onItem().transform(v -> {
                    uploadService.updateProgress(uploadId, chunkData.length);
                    var progress = uploadService.getProgress(uploadId);
                    LOG.infof("Chunk %d uploaded successfully. Progress: %d%% (%d/%d bytes)",
                            chunkNumber, progress.getPercentage(), progress.uploadedBytes, progress.totalBytes);
                    sseService.sendProgress(clientId, progress);
                    return Response.ok("Chunk " + chunkNumber + " uploaded").build();
                });
    }

    @POST
    @Path("/complete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Blocking
    public Uni<Response> completeUpload(UploadCompletionRequest request) {
        java.nio.file.Path tempDir = null;
        LOG.infof("Completing upload %s: %s (%d chunks)", request.uploadId, request.fileName, request.totalChunks);

        java.nio.file.Path finalPath = tempDir.resolve(request.fileName);
        try {
            Files.createFile(finalPath);
            LOG.infof("Created final file: %s", finalPath);

            for (int i = 1; i <= request.totalChunks; i++) {
                java.nio.file.Path chunk = tempDir.resolve(request.uploadId + ".part" + i);
                LOG.debugf("Processing chunk %d: %s", i, chunk);
                Files.write(finalPath, Files.readAllBytes(chunk), StandardOpenOption.APPEND);
                Files.delete(chunk);
                LOG.debugf("Merged and deleted chunk %d", i);
            }
            uploadService.finishUpload(request.uploadId);
            LOG.infof("Upload %s completed successfully. Final file: %s", request.uploadId, finalPath);
            return Uni.createFrom().item(Response.ok("Upload complete").build());
        } catch (IOException e) {
            LOG.errorf(e, "Failed to complete upload %s", request.uploadId);
            return Uni.createFrom().item(Response.serverError().entity(e.getMessage()).build());
        }
    }

    public record UploadCompletionRequest(String uploadId, String fileName, int totalChunks) {
    }

}
