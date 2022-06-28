package ua.epan.elearn.selection.committee.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import ua.epan.elearn.selection.committee.spring.model.service.ScheduledJobs;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Profile("prod")
    @Bean
    public ScheduledJobs scheduledJobs()
    {
        return new ScheduledJobs();
    }

}
