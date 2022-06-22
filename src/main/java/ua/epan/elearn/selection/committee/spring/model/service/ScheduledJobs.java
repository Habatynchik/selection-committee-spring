package ua.epan.elearn.selection.committee.spring.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.epan.elearn.selection.committee.spring.model.entity.Recruitment;

import java.util.List;

@Component
public class ScheduledJobs {

    @Autowired
    RecruitmentService recruitmentService;

    @Scheduled(cron = "0 0 0/12 * * ?")
    public void cleanTempDir() {
        recruitmentService.scheduleCloseRecruitmentAndFinalizeApplications();

    }

}
