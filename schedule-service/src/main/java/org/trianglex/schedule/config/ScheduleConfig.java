package org.trianglex.schedule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduleConfig {

    @Bean
    public ScheduleServiceListener scheduleServiceListener() {
        return new ScheduleServiceListener();
    }

}
