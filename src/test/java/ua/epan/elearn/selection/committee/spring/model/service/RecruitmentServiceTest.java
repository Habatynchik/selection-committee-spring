package ua.epan.elearn.selection.committee.spring.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.epan.elearn.selection.committee.spring.model.dto.RecruitmentDto;
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;
import ua.epan.elearn.selection.committee.spring.model.entity.Recruitment;
import ua.epan.elearn.selection.committee.spring.model.exception.FacultyNameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.exception.RecruitmentNameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.repository.FacultyRepository;
import ua.epan.elearn.selection.committee.spring.model.repository.RecruitmentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RecruitmentServiceTest {

    @Autowired
    private RecruitmentService recruitmentService;

    @MockBean
    private RecruitmentRepository recruitmentRepository;
    @MockBean
    private FacultyRepository facultyRepository;

    private Recruitment RECRUITMENT;
    private RecruitmentDto RECRUITMENT_DTO;
    private Faculty FACULTY;

    @BeforeEach
    void setUp() {
        FACULTY = Faculty.builder()
                .id(1L)
                .name("FACULTY")
                .generalCapacity(50L)
                .budgetCapacity(25L)
                .build();

        RECRUITMENT = Recruitment.builder()
                // .id(1l)
                .facultyId(FACULTY)
                .name("RECRUITMENT")
                .generalCapacity(10L)
                .budgetCapacity(5L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .closed(false)
                .build();

        RECRUITMENT_DTO = RecruitmentDto.builder()
                .name("RECRUITMENT")
                .faculty(FACULTY)
                .generalCapacity(10L)
                .budgetCapacity(5L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .build();

    }

    @Test
    void addNewRecruitmentThrowsRecruitmentNameIsReservedException() {
        when(recruitmentRepository.existsByNameAndFacultyId(RECRUITMENT_DTO.getName(), RECRUITMENT_DTO.getFaculty())).thenReturn(true);

        assertThrows(RecruitmentNameIsReservedException.class, () -> recruitmentService.addNewRecruitment(RECRUITMENT_DTO));

    }

    @Test
    void addNewRecruitmentNothingThrows() throws RecruitmentNameIsReservedException {
        when(recruitmentRepository.existsByNameAndFacultyId(RECRUITMENT_DTO.getName(), RECRUITMENT_DTO.getFaculty())).thenReturn(false);
        when(recruitmentRepository.save(RECRUITMENT)).thenReturn(RECRUITMENT);

        recruitmentService.addNewRecruitment(RECRUITMENT_DTO);

        verify(recruitmentRepository, times(1)).save(RECRUITMENT);


    }

    @Test
    void closeRecruitment() {
        Recruitment closedRecruitment = RECRUITMENT;
        closedRecruitment.setClosed(true);

        when(recruitmentRepository.save(closedRecruitment)).thenReturn(closedRecruitment);

        recruitmentService.closeRecruitment(RECRUITMENT);
        verify(recruitmentRepository, times(1)).save(RECRUITMENT);

    }

    @Test
    void getAllNowOpenedRecruitmentsByFacultyId() {
        when(recruitmentRepository.findOpenedRecruitmentByFacultyId(FACULTY)).thenReturn(List.of(RECRUITMENT));

        assertEquals(recruitmentService.getAllNowOpenedRecruitmentsByFacultyId(FACULTY), List.of(RECRUITMENT));
    }

    @Test
    void getRemainingBudgetSpotsInThisYearByFacultyId() {
        when(recruitmentRepository.findAllRecruitmentWhereCurrentYearByFacultyId(FACULTY)).thenReturn(List.of(RECRUITMENT));
        when(facultyRepository.findById(FACULTY.getId())).thenReturn(Optional.of(FACULTY));

        assertEquals(recruitmentService.getRemainingBudgetSpotsInThisYearByFacultyId(FACULTY), Long.valueOf(20));
    }

    @Test
    void getRemainingGeneralSpotsInThisYearByFacultyId() {
        when(recruitmentRepository.findAllRecruitmentWhereCurrentYearByFacultyId(FACULTY)).thenReturn(List.of(RECRUITMENT));
        when(facultyRepository.findById(FACULTY.getId())).thenReturn(Optional.of(FACULTY));

        assertEquals(recruitmentService.getRemainingGeneralSpotsInThisYearByFacultyId(FACULTY), Long.valueOf(40));

    }

    @Test
    void getRecruitmentById() {
        when(recruitmentRepository.getReferenceById(1L)).thenReturn(RECRUITMENT);
        assertEquals(recruitmentService.getRecruitmentById(1L), RECRUITMENT);

    }

    @Test
    void getRecruitmentByApplicationId() {
        when(recruitmentRepository.findByApplicationId(1L)).thenReturn(RECRUITMENT);
        assertEquals(recruitmentService.getRecruitmentByApplicationId(1L), RECRUITMENT);
    }


}
