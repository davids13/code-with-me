package org.acme.schedule.fireAndForgetTask;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class ProcessingService {

    // https://myfear.substack.com/i/167025094/lab-fire-and-forget-tasks-with-nonblocking

    // Fire-and-Forget Tasks with @NonBlocking

    /*
        Imagine your user uploads a file, and you want to start processing it but not block the response.
        This is a common pattern in modern APIs.
    */

    private static final Logger LOG = Logger.getLogger(ProcessingService.class);

    public CompletionStage<String> processData(String data) {
        return CompletableFuture.supplyAsync(() -> {
            LOG.infof("Starting to process '%s' on thread %s", data, Thread.currentThread().getName());

            try {
                // simulate a long-running task
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            LOG.infof("Finished processing '%s'", data);
            return "Processed: " + data;
        });
    }

}
