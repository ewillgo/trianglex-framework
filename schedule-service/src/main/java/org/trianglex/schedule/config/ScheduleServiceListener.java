package org.trianglex.schedule.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ScheduleServiceListener implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
//        List<ScheduleJob> scheduleJobList = jobService.getAllEnableJob();
//        for (ScheduleJob scheduleJob : scheduleJobList) {
//            try {
//                CronTrigger cronTrigger = ScheduleUtil.getCronTrigger(scheduler, scheduleJob);
//                if (cronTrigger == null) {
//                    ScheduleUtil.createScheduleJob(scheduler, scheduleJob);
//                } else {
//                    ScheduleUtil.updateScheduleJob(scheduler, scheduleJob);
//                }
//                logger.info("Startup {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
//            } catch (ServiceException e) {
//                e.printStackTrace();
//            }
//        }
    }

}
