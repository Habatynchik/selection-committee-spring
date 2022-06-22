package ua.epan.elearn.selection.committee.spring.model.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.epan.elearn.selection.committee.spring.model.dto.FacultyDto;
import ua.epan.elearn.selection.committee.spring.model.dto.SubjectDto;
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;
import ua.epan.elearn.selection.committee.spring.model.entity.Subject;
import ua.epan.elearn.selection.committee.spring.model.exception.FacultyNameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.repository.FacultyRepository;

import java.util.List;

@AllArgsConstructor
@Log4j2
@Service
public class FacultyService {

    private FacultyRepository facultyRepository;

    public Page<Faculty> getFacultyPagination(Pageable pageable) {
        return facultyRepository.findAll(pageable);
    }


    public void addNewFaculty(FacultyDto facultyDto) throws FacultyNameIsReservedException {
        checkFacultyNameIsUnique(facultyDto.getName());

        Faculty faculty = Faculty.builder()
                .name(facultyDto.getName())
                .generalCapacity(facultyDto.getGeneralCapacity())
                .budgetCapacity(facultyDto.getBudgetCapacity())
                .build();

         facultyRepository.save(faculty);
    }

    public Faculty updateFaculty(FacultyDto facultyDto) throws FacultyNameIsReservedException {

        Faculty faculty = new Faculty(facultyDto);

        if (facultyRepository.findFacultyByName(facultyDto.getName()) != null && !facultyRepository.findFacultyByName(facultyDto.getName()).getId().equals(facultyDto.getId())) {
            log.warn("Faculty  name '{}' already reserved", facultyDto.getName());
            throw new FacultyNameIsReservedException();
        }

        log.info("Faculty '{}' has been updated", faculty);

        return facultyRepository.save(faculty);
    }

    public boolean checkExistByFacultyId(Long facultyId) {
        return facultyRepository.existsById(facultyId);
    }

    public void deleteFaculty(Long facultyId) {
        facultyRepository.deleteById(facultyId);
    }

    public Faculty getFacultyById(Long facultyId) {
        return facultyRepository.findById(facultyId).get();
    }

    public Faculty getFacultyByName(String facultyName) {
        return facultyRepository.findFacultyByName(facultyName);
    }

    private void checkFacultyNameIsUnique(String facultyName) throws FacultyNameIsReservedException {
        if (facultyRepository.existsByName(facultyName)) {
            log.warn("Faculty name '{}' is reserved", facultyName);
            throw new FacultyNameIsReservedException();
        }


    }

}
