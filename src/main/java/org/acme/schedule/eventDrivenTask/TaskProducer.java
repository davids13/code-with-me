package org.acme.schedule.eventDrivenTask;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.time.Duration;

@ApplicationScoped
public class TaskProducer {

    // https://myfear.substack.com/i/167025094/lab-event-driven-background-tasks-with-reactive-messaging

    // Event-Driven Background Tasks with Reactive Messaging

    // create producer
    @Outgoing("task-requests") // Marks a method that produces messages for a specific channel
    public Multi<String> generate() {
        // every 15 seconds, send a new request
        return Multi.createFrom().ticks().every(Duration.ofSeconds(15)).map(tick -> "Task payload " + tick);
    }

    // Channels: These are logical names that connect producers and consumers.
}
