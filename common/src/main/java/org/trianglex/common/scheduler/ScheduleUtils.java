package org.trianglex.common.scheduler;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public abstract class ScheduleUtils {

    private static final ConcurrentHashMap<String, Class<? extends Job>> JOB_CLASS_MAP = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ScheduleUtils.class);

    private ScheduleUtils() {

    }

    @SuppressWarnings("unchecked")
    public static void createJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {

            Class<? extends Job> jobClass;
            String key = String.format("%s:%s", scheduleJob.getJobName(), scheduleJob.getJobGroup());
            if ((jobClass = JOB_CLASS_MAP.get(key)) == null) {
                jobClass = (Class<? extends Job>) Class.forName(scheduleJob.getClassName()).newInstance().getClass();
                JOB_CLASS_MAP.put(key, jobClass);
            }

            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                    .withDescription(scheduleJob.getDescription())
                    .build();

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup())
                    .withDescription(scheduleJob.getDescription())
                    .withSchedule(scheduleBuilder)
                    .startNow()
                    .build();

            scheduler.scheduleJob(jobDetail, cronTrigger);

            logger.info("Create schedule job {}-{} success.", scheduleJob.getJobGroup(), scheduleJob.getJobName());

            if (scheduleJob.getPause()) {
                pauseJob(scheduler, scheduleJob);
            }

        } catch (Exception e) {
            logger.error("Execute schedule job failed.");
        }
    }

    public static void updateJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {

            TriggerKey triggerKey = getTriggerKey(scheduleJob);

            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            CronTrigger cronTrigger = getCronTrigger(scheduler, scheduleJob);

            cronTrigger = cronTrigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withDescription(scheduleJob.getDescription())
                    .withSchedule(cronScheduleBuilder).build();

            scheduler.rescheduleJob(triggerKey, cronTrigger);

            logger.info("Update schedule job {}-{} success.", scheduleJob.getJobGroup(), scheduleJob.getJobName());

            if (scheduleJob.getPause()) {
                pauseJob(scheduler, scheduleJob);
            }
        } catch (SchedulerException e) {
            logger.error("Update schedule job failed.");
        }
    }

    public static void runJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            scheduler.triggerJob(getJobKey(scheduleJob));
            logger.info("Run schedule job {}-{} success.", scheduleJob.getJobGroup(), scheduleJob.getJobName());
        } catch (SchedulerException e) {
            logger.error("Run schedule job failed.");
        }
    }

    public static void pauseJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            scheduler.pauseJob(getJobKey(scheduleJob));
            logger.info("Pause schedule job {}-{} success.", scheduleJob.getJobGroup(), scheduleJob.getJobName());
        } catch (SchedulerException e) {
            logger.error("Pause schedule job failed.");
        }
    }

    public static void resumeJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            scheduler.resumeJob(getJobKey(scheduleJob));
            logger.info("Resume schedule job {}-{} success.", scheduleJob.getJobGroup(), scheduleJob.getJobName());
        } catch (SchedulerException e) {
            logger.error("Resume schedule job failed.");
        }
    }

    public static void deleteJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            scheduler.deleteJob(getJobKey(scheduleJob));
            logger.info("Delete schedule job {}-{} success.", scheduleJob.getJobGroup(), scheduleJob.getJobName());
        } catch (SchedulerException e) {
            logger.error("Delete schedule job failed.");
        }
    }

    private static TriggerKey getTriggerKey(ScheduleJob scheduleJob) {
        return TriggerKey.triggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup());
    }

    private static JobKey getJobKey(ScheduleJob scheduleJob) {
        return JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
    }

    private static CronTrigger getCronTrigger(Scheduler scheduler, ScheduleJob scheduleJob) throws SchedulerException {
        return (CronTrigger) scheduler.getTrigger(getTriggerKey(scheduleJob));
    }

}
