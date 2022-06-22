package ua.epan.elearn.selection.committee.spring.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.epan.elearn.selection.committee.spring.mail.EmailServiceImpl;
import ua.epan.elearn.selection.committee.spring.model.dto.ApplicationDto;
import ua.epan.elearn.selection.committee.spring.model.entity.*;
import ua.epan.elearn.selection.committee.spring.model.exception.UserAlreadyAppliedException;
import ua.epan.elearn.selection.committee.spring.model.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ApplicationService {

    @Autowired
    private EmailServiceImpl emailService;

    private final ApplicationRepository applicationRepository;
    private final ApplicationStateRepository applicationStateRepository;
    private final RequiredSubjectRepository requiredSubjectRepository;
    private final SubjectUserGradesRepository subjectUserGradesRepository;

    public List<Application> getAllApplicationsByUserId(User userId) {
        return applicationRepository.findAllByUserId(userId);
    }

    public Application getApplicationsById(Long applicationId) {
        return applicationRepository.getReferenceById(applicationId);
    }

    public List<Application> getAllApplicationsRecruitment(Recruitment recruitmentId) {
        List<Application> list = applicationRepository.findAllByRecruitmentId(recruitmentId);
        if (list.size() > 1)
            list.sort((o1, o2) -> o2.getAverageScore().intValue() - o1.getAverageScore().intValue());
        return list;
    }

    public void addNewApplication(ApplicationDto applicationDto) throws UserAlreadyAppliedException {

        Application application = new Application(applicationDto);
        application.setApplicationStateId(
                applicationRepository.findApplicationStateByStateEnum(
                        ApplicationState.ApplicationStateEnum.REGISTERED
                )
        );
        checkApplicationIsRegister(application.getUserId(), application.getRecruitmentId());
        applicationRepository.save(application);
        log.info("Application ({}) has been created", application.getId());

        List<SubjectUserGrades> subjectUserGradesList = new ArrayList<>();
        List<RequiredSubject> requiredSubjectList = requiredSubjectRepository.findAllByFacultyId(application.getRecruitmentId().getFacultyId());

        SubjectUserGrades subjectUserGrade;
        for (int i = 0; i < applicationDto.getGrades().size(); i++) {

            subjectUserGrade = SubjectUserGrades.builder()
                    .applicationId(application)
                    .subjectId(requiredSubjectList.get(i).getSubjectId())
                    .grade(applicationDto.getGrades().get(i))
                    .build();
            subjectUserGradesList.add(subjectUserGrade);

        }
        subjectUserGradesRepository.saveAll(subjectUserGradesList);

        log.info("Grades ({}) added to application({}) successful", subjectUserGradesList.toString(), application.getId());
    }

    public void finalizeApplications(Recruitment recruitment) {

        List<Application> applicationList = getAllApplicationsRecruitment(recruitment);
        applicationList.stream()
                .limit(recruitment.getBudgetCapacity())
                .forEach(e -> {
                    e.setApplicationStateId(
                            applicationStateRepository.findByApplicationStateEnum(ApplicationState.ApplicationStateEnum.ACCEPTED_FOR_BUDGET)
                    );
                    log.info("Application {} state changed for ACCEPTED_FOR_BUDGET", e);
                    emailService.sendBudgetMessage(e.getUserId().getEmail());
                });

        applicationList.stream()
                .skip(recruitment.getBudgetCapacity())
                .limit(recruitment.getGeneralCapacity() - recruitment.getBudgetCapacity())
                .forEach(e -> {
                    e.setApplicationStateId(
                            applicationStateRepository.findByApplicationStateEnum(ApplicationState.ApplicationStateEnum.ACCEPTED_FOR_CONTRACT)
                    );
                    log.info("Application {} state changed for ACCEPTED_FOR_CONTRACT", e);
                    emailService.sendContractMessage(e.getUserId().getEmail());
                });

        applicationList.stream()
                .skip(recruitment.getGeneralCapacity())
                .forEach(e -> {
                    e.setApplicationStateId(
                            applicationStateRepository.findByApplicationStateEnum(ApplicationState.ApplicationStateEnum.REJECTED)
                    );
                    log.info("Application {} state changed for REJECTED", e);
                    emailService.sendBadMessage(e.getUserId().getEmail());
                });

        applicationRepository.saveAll(applicationList);
        log.info("All applications in Recruitment ({}) was finalized", recruitment);

    }


    public void checkApplicationIsRegister(User user, Recruitment recruitment) throws UserAlreadyAppliedException {
        if (applicationRepository.existsApplicationByUserIdAndRecruitmentId(user, recruitment)) {
            log.warn("User ({}) already register applied application", user);
            throw new UserAlreadyAppliedException();
        }
    }
}
