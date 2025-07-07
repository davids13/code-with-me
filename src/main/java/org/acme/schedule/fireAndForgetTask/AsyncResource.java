package org.acme.schedule.fireAndForgetTask;

import io.smallrye.common.annotation.NonBlocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@Path("async")
public class AsyncResource {

    private static final Logger LOG = Logger.getLogger(AsyncResource.class);

    ProcessingService processingService;

    @Inject
    public AsyncResource(ProcessingService processingService) {
        this.processingService = processingService;
    }

    @GET
    @Path("process")
    @Produces(MediaType.TEXT_PLAIN)
    @NonBlocking // Fire-and-Forget Tasks with @NonBlocking
    public String triggerAsyncProcessing() {
        String taskData = "user-data-" + System.currentTimeMillis();
        LOG.infof("Endpoint called. Triggering background task for: %s" + taskData);
        processingService.processData(taskData); // fire-and-forget
        return "Task for '" + taskData + "' has been submitted for processing in the background!";
    }

    /*
        Try it in your browser at http://localhost:8080/async/process.
        You’ll get an instant response while the logs show processing continues in the background.
    */
}
