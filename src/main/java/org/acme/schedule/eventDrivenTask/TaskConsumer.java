package org.acme.schedule.eventDrivenTask;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class TaskConsumer {

    // create consumer
    @Incoming("task-requests") // Marks a method that consumes messages from a channel.
    public CompletionStage<Void> process(String task) {
        return CompletableFuture.runAsync(() -> {
            Log.infof("Reactive consumer received task: 's'. Processing on thread %s.", task, Thread.currentThread().getName());
            // simulate processing
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Log.infof("Finished processing reactive task: '%s'", task);
        });
    }

    /*
        Check your application console. Every 15 seconds, the TaskProducer will generate a message.
        Shortly after, you will see the TaskConsumer log that it has received and processed the task.
        This demonstrates a decoupled, event-driven flow. This is a no-broker-needed setup thanks to the
        in-memory connector, ideal for testing or learning experiments like this one.
    */

    // Channels: These are logical names that connect producers and consumers.
}
