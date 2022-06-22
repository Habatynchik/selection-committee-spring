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
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;
import ua.epan.elearn.selection.committee.spring.model.entity.RequiredSubject;
import ua.epan.elearn.selection.committee.spring.model.entity.Subject;
import ua.epan.elearn.selection.committee.spring.model.exception.FacultyNameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.exception.UsernameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.repository.FacultyRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static org.postgresql.shaded.com.ongres.scram.common.ScramAttributes.USERNAME;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FacultyServiceTest {

    private final Long FACULTY_ID = 1L;
    private final String FACULTY_NAME = "FACULTY";

    @Autowired
    private FacultyService facultyService;

    @MockBean
    private FacultyRepository facultyRepository;

    private Subject SUBJECT;
    private Faculty FACULTY ;
    private Faculty FACULTY_1 ;
    private FacultyDto FACULTY_DTO;

    @BeforeEach
    void setUp() {
        SUBJECT = Subject.builder()
                .nameEn("English")
                .nameRu("Английский")
                .nameUk("Английский")
                .build();

        FACULTY = Faculty.builder()
                .id(FACULTY_ID)
                .name(FACULTY_NAME)
                .generalCapacity(50L)
                .budgetCapacity(25L)
                .build();

        FACULTY_1 = Faculty.builder()
                .name(FACULTY_NAME)
                .generalCapacity(50L)
                .budgetCapacity(25L)
                .build();

        FACULTY_DTO = FacultyDto.builder()
               .id(FACULTY_ID)
               .name(FACULTY_NAME)
               .generalCapacity(50L)
               .budgetCapacity(25L)
               .requiredSubjectList(List.of(SUBJECT))
               .build();
    }


    @Test
    void addNewFacultyNothingThrows() throws FacultyNameIsReservedException {

        when(facultyRepository.existsByName(FACULTY_DTO.getName())).thenReturn(false);
        when(facultyRepository.save(FACULTY_1)).thenReturn(FACULTY_1);

       facultyService.addNewFaculty(FACULTY_DTO);
     //   assertEquals(facultyService.addNewFaculty(FACULTY_DTO), FACULTY_1);

        verify(facultyRepository, times(1)).save(FACULTY_1);
    }

    @Test
    void addNewFacultyThrowsFacultyNameIsReservedException(){
        when(facultyRepository.existsByName(FACULTY_NAME)).thenReturn(true);
        assertThrows(FacultyNameIsReservedException.class, () -> facultyService.addNewFaculty(FACULTY_DTO));

    }



    @Test
    void updateFacultyThrowsFacultyNameIsReservedException(){
        Faculty newFaculty = Faculty.builder().id(2L).name(FACULTY_NAME).build();
        when(facultyRepository.findFacultyByName(FACULTY_NAME)).thenReturn(newFaculty);

        assertThrows(FacultyNameIsReservedException.class, () -> facultyService.updateFaculty(FACULTY_DTO));

    }

    @Test
    void updateFacultyNothingThrows() throws FacultyNameIsReservedException {
        when(facultyRepository.findFacultyByName(FACULTY_NAME)).thenReturn(FACULTY);
        when(facultyRepository.save(FACULTY)).thenReturn(FACULTY);

        assertEquals(facultyService.updateFaculty(FACULTY_DTO), FACULTY);

        verify(facultyRepository, times(1)).save(FACULTY);
    }

    @Test
    void checkExistByFacultyId(){

        when(facultyRepository.existsById(FACULTY_ID)).thenReturn(true);

        assertEquals(facultyService.checkExistByFacultyId(FACULTY_ID), true);

        verify(facultyRepository, times(0)).save(FACULTY);
    }


    @Test
    void getFacultyById(){

        when(facultyRepository.findById(FACULTY_ID)).thenReturn(Optional.of(FACULTY));

        assertEquals(facultyService.getFacultyById(FACULTY_ID), FACULTY);

        verify(facultyRepository, times(0)).save(FACULTY);
    }

    @Test
    void getFacultyByName(){

        when(facultyRepository.findFacultyByName(FACULTY_NAME)).thenReturn(FACULTY);

        assertEquals(facultyService.getFacultyByName(FACULTY_NAME), FACULTY);

        verify(facultyRepository, times(0)).save(FACULTY);
    }
}
