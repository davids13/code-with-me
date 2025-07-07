package org.acme.schedule.scheduler;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;

@ApplicationScoped
public class ScheduleTasks {

    // https://myfear.substack.com/i/167025094/lab-scheduled-tasks-with-scheduled

    // Scheduled Tasks with @Scheduled

    private static final Logger LOG = Logger.getLogger(ScheduleTasks.class);

    private int counter = 0;

    @Scheduled(every = "10s", identity = "heartbeat-task")
    void heartbeat() {
        counter++;
        LOG.infof("[%s] Heartbeat check #%d running on thread: %s", LocalDateTime.now(), counter, Thread.currentThread().getName());
    }

    @Scheduled(cron = "0 15 10 * * ?")
    void dailyReport() {
        LOG.infof("[%s] Generating daily report...", LocalDateTime.now());
    }
}
