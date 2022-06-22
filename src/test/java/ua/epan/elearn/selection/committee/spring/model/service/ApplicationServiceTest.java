package ua.epan.elearn.selection.committee.spring.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.epan.elearn.selection.committee.spring.mail.EmailServiceImpl;
import ua.epan.elearn.selection.committee.spring.model.dto.ApplicationDto;
import ua.epan.elearn.selection.committee.spring.model.dto.FacultyDto;
import ua.epan.elearn.selection.committee.spring.model.dto.SubjectDto;
import ua.epan.elearn.selection.committee.spring.model.entity.*;
import ua.epan.elearn.selection.committee.spring.model.exception.UserAlreadyAppliedException;
import ua.epan.elearn.selection.committee.spring.model.repository.ApplicationRepository;
import ua.epan.elearn.selection.committee.spring.model.repository.ApplicationStateRepository;
import ua.epan.elearn.selection.committee.spring.model.repository.RequiredSubjectRepository;
import ua.epan.elearn.selection.committee.spring.model.repository.SubjectUserGradesRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationServiceTest {

    @Autowired
    private ApplicationService applicationService;

    @MockBean
    private EmailServiceImpl emailService;
    @MockBean
    private ApplicationRepository applicationRepository;
    @MockBean
    private ApplicationStateRepository applicationStateRepository;
    @MockBean
    private RequiredSubjectRepository requiredSubjectRepository;
    @MockBean
    private SubjectUserGradesRepository subjectUserGradesRepository;

    private Application APPLICATION;
    private ApplicationDto APPLICATION_DTO;
    private User USER;
    private Recruitment RECRUITMENT;

    @BeforeEach
    void setUp() {
        RECRUITMENT = Recruitment.builder().build();
        USER = User.builder().build();
        APPLICATION = Application.builder()
                .applicationStateId(ApplicationState.builder()
                        .id(1L)
                        .applicationStateEnum(ApplicationState.ApplicationStateEnum.REGISTERED)
                        .build())
                .averageScore(200L)
                .recruitmentId(RECRUITMENT)
                .userId(USER)
                .build();

        APPLICATION_DTO = ApplicationDto.builder()
                .recruitment(RECRUITMENT)
                .user(USER)
                .grades(List.of(new Long[]{200L, 200L, 200L}))
                .build();
    }


    @Test
    void getAllApplicationsByUserId() {
        when(applicationRepository.findAllByUserId(USER)).thenReturn(List.of(APPLICATION));

        assertEquals(applicationService.getAllApplicationsByUserId(USER), List.of(APPLICATION));

    }

    @Test
    void getApplicationsById() {
        when(applicationRepository.getReferenceById(1L)).thenReturn(APPLICATION);

        assertEquals(applicationService.getApplicationsById(1L), APPLICATION);

    }

    @Test
    void getAllApplicationsRecruitment() {
        when(applicationRepository.findAllByRecruitmentId(RECRUITMENT)).thenReturn( List.of(APPLICATION));

        assertEquals(applicationService.getAllApplicationsRecruitment(RECRUITMENT), List.of(APPLICATION));
    }

    @Test
    void addNewApplicationThrowsUserAlreadyAppliedException() {
        when(applicationRepository.existsApplicationByUserIdAndRecruitmentId(USER, RECRUITMENT)).thenReturn( true);

        assertThrows(UserAlreadyAppliedException.class, () -> applicationService.addNewApplication(APPLICATION_DTO));
    }
}
