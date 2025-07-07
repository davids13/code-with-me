package org.acme.schedule.quartz;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

@ApplicationScoped
public class MyQuartzJob {

    // https://myfear.substack.com/i/167025094/define-a-quartz-job

    // When you need control over job timing or want your scheduled jobs to survive restarts, Quartz is your friend.

    @Inject
    org.quartz.Scheduler quartz;

    void onStart(@Observes StartupEvent event) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob", "myGroup")
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "myGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever())
                .build();
        quartz.scheduleJob(job, trigger);
    }

    void performTask(String taskName) {
        Log.infof("Executing Quartz Job: %s on thread %s", taskName, Thread.currentThread().getName());
    }

    public static class MyJob implements Job {
        @Inject
        MyQuartzJob jobBean;

        public void execute(JobExecutionContext context) throws JobExecutionException {
            jobBean.performTask(context.getJobDetail().getKey().getName());
        }
    }

    /*
        This Java class, MyQuartzJob, demonstrates how to programmatically schedule a recurring task using the Quartz extension in Quarkus:
            - Observing Application Startup: The onStart(@Observes StartupEvent event) method is a CDI observer that triggers when the Quarkus application starts. This is the ideal place to define and schedule our job.
            - Injecting the Scheduler: We inject the main Quartz Scheduler instance (@Inject org.quartz.Scheduler quartz;). This is the central component for managing jobs.
            - Defining the Job and Trigger:
                - A JobDetail is created to define the task itself. It's identified as "myJob" and linked to the MyJob.class, which contains the execution logic.
                - A Trigger is built to define the schedule. In this example, it's configured to start immediately (startNow()) and repeat every 10 seconds indefinitely (withIntervalInSeconds(10).repeatForever()).
            - Scheduling the Job: The core of the setup is the call to quartz.scheduleJob(job, trigger). This tells the Quartz scheduler to execute the defined job based on the trigger's schedule.
            - The Job Logic:
                - The MyJob static inner class implements the org.quartz.Job interface. Its execute() method contains the code that runs on schedule.
                - To leverage CDI benefits like dependency injection and transactions, MyJob injects its parent class (MyQuartzJob) and calls the performTask() method. This separates the Quartz-managed job from the CDI-managed business logic.
                - This approach provides a powerful, programmatic way to manage scheduled tasks directly within your Quarkus application, offering full control over job and trigger definitions at runtime.
            - Watch your terminal. The job runs every 20 seconds.
    */
}
