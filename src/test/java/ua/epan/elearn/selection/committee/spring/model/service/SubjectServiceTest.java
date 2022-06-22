package ua.epan.elearn.selection.committee.spring.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.epan.elearn.selection.committee.spring.model.dto.FacultyDto;
import ua.epan.elearn.selection.committee.spring.model.dto.SubjectDto;
import ua.epan.elearn.selection.committee.spring.model.dto.UserDto;
import ua.epan.elearn.selection.committee.spring.model.entity.*;
import ua.epan.elearn.selection.committee.spring.model.exception.UsernameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.repository.RequiredSubjectRepository;
import ua.epan.elearn.selection.committee.spring.model.repository.SubjectRepository;
import ua.epan.elearn.selection.committee.spring.model.repository.SubjectUserGradesRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SubjectServiceTest {

    @Autowired
    private SubjectService subjectService;

    @MockBean
    private SubjectRepository subjectRepository;
    @MockBean
    private SubjectUserGradesRepository subjectUserGradesRepository;
    @MockBean
    private RequiredSubjectRepository requiredSubjectRepository;


    private Subject SUBJECT;
    private Faculty FACULTY;
    private FacultyDto FACULTY_DTO;
    private RequiredSubject REQUIRED_SUBJECT;
    private SubjectDto SUBJECT_DTO;

    @BeforeEach
    void setUp() {
        FACULTY = Faculty.builder()
                .id(1L)
                .name("FACULTY")
                .generalCapacity(50L)
                .generalCapacity(25L)
                .build();

        SUBJECT = Subject.builder()
                .nameEn("English")
                .nameRu("Английский")
                .nameUk("Английский")
                .build();


        FACULTY_DTO = FacultyDto.builder()
                .id(1L)
                .name("FACULTY")
                .generalCapacity(50L)
                .budgetCapacity(25L)
                .requiredSubjectList(List.of(SUBJECT))
                .build();


        REQUIRED_SUBJECT = RequiredSubject.builder()
                .subjectId(SUBJECT)
                .facultyId(FACULTY)
                .build();

        SUBJECT_DTO = new SubjectDto(
                "English",
                "Английский",
                "Английский"
        );
    }

    @Test
    void addNewSubject() {
        when(subjectRepository.save(SUBJECT)).thenReturn(SUBJECT);

        assertEquals(subjectService.addNewSubject(SUBJECT_DTO), SUBJECT);
        verify(subjectRepository, times(1)).save(SUBJECT);
    }

    @Test
    void updateRequiredSubjects() {
        when(requiredSubjectRepository.findAllByFacultyId(FACULTY)).thenReturn(List.of(REQUIRED_SUBJECT));
        when(requiredSubjectRepository.save(REQUIRED_SUBJECT)).thenReturn(REQUIRED_SUBJECT);

        subjectService.updateRequiredSubjects(FACULTY_DTO);

        verify(subjectRepository, times(0)).save(SUBJECT);
    }

    @Test
    void getAllSubjects() {
        when(subjectRepository.findAll()).thenReturn(List.of(SUBJECT));

        assertEquals(subjectService.getAllSubjects(), List.of(SUBJECT));
        verify(subjectRepository, times(0)).save(SUBJECT);
    }

    @Test
    void getRequiredSubjectsByFacultyId() {
        when(subjectRepository.findAllByFacultyId(FACULTY)).thenReturn(List.of(SUBJECT));

        assertEquals(subjectService.getRequiredSubjectsByFacultyId(FACULTY), List.of(SUBJECT));
        verify(subjectRepository, times(0)).save(SUBJECT);
    }

    @Test
    void getSubjectUserGradesByApplicationId() {
        Application application = new Application();
        SubjectUserGrades subjectUserGrades = new SubjectUserGrades();
        when(subjectUserGradesRepository.findAllByApplicationId(application)).thenReturn(List.of(subjectUserGrades));

        assertEquals(subjectService.getSubjectUserGradesByApplicationId(application), List.of(subjectUserGrades));
        verify(subjectRepository, times(0)).save(SUBJECT);
    }

    @Test
    void getRequiredSubjectsByRecruitmentId() {
        when(subjectRepository.findAllByRecruitmentId(1L)).thenReturn(List.of(SUBJECT));

        assertEquals(subjectService.getRequiredSubjectsByRecruitmentId(1L), List.of(SUBJECT));
        verify(subjectRepository, times(0)).save(SUBJECT);
    }

    @Test
    void addRequiredSubjects() {
        when(requiredSubjectRepository.save(REQUIRED_SUBJECT)).thenReturn(REQUIRED_SUBJECT);

        subjectService.addRequiredSubjects(FACULTY, List.of(SUBJECT));
        verify(requiredSubjectRepository, times(1)).save(REQUIRED_SUBJECT);
    }

}
