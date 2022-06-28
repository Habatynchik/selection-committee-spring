package ua.epan.elearn.selection.committee.spring.model.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epan.elearn.selection.committee.spring.model.dto.FacultyDto;
import ua.epan.elearn.selection.committee.spring.model.dto.SubjectDto;
import ua.epan.elearn.selection.committee.spring.model.entity.*;
import ua.epan.elearn.selection.committee.spring.model.repository.RequiredSubjectRepository;
import ua.epan.elearn.selection.committee.spring.model.repository.SubjectRepository;
import ua.epan.elearn.selection.committee.spring.model.repository.SubjectUserGradesRepository;

import java.util.List;

/**
 * Service for business logic related with Subject.
 *
 * @author Nikita Gamaiunov
 */
@AllArgsConstructor
@Log4j2
@Service
public class SubjectService {

    private SubjectRepository subjectRepository;
    private SubjectUserGradesRepository subjectUserGradesRepository;
    private RequiredSubjectRepository requiredSubjectRepository;

    /**
     * Adds RequiredSubject to Database by Faculty.
     *
     * @param facultyId Faculty instance.
     * @param subjectList List of Subjects.
     */
    @Transactional
    public void addRequiredSubjects(Faculty facultyId, List<Subject> subjectList) {
        RequiredSubject requiredSubject;

        for (Subject subject : subjectList) {
            requiredSubject = RequiredSubject.builder()
                    .facultyId(facultyId)
                    .subjectId(subject)
                    .build();
            log.info("Required subject '{}' has been added to faculty '{}'", requiredSubject, facultyId);
            requiredSubjectRepository.save(requiredSubject);
        }
    }

    /**
     * Creates Faculty from FacultyDto.
     *
     * @param facultyDto FacultyDto instance.
     */
    @Transactional
    public void updateRequiredSubjects(FacultyDto facultyDto) {
        Faculty faculty = new Faculty(facultyDto);

        List<RequiredSubject> requiredSubjectList = requiredSubjectRepository.findAllByFacultyId(faculty);

        for (Subject subject : facultyDto.getRequiredSubjectList()) {

            if (requiredSubjectList.contains(subject)) {
                requiredSubjectList.remove(subject);

            } else {
                RequiredSubject requiredSubject = RequiredSubject.builder()
                        .subjectId(subject)
                        .facultyId(faculty)
                        .build();
                requiredSubjectRepository.save(requiredSubject);
                log.info("Required subject '{}' has been added to faculty '{}'", requiredSubject, faculty.getId());
            }
        }

        requiredSubjectRepository.deleteAll(requiredSubjectList);
        log.info("Required subjects '{}' has been deleted from faculty '{}'", requiredSubjectList.toString(), faculty.getId());
    }

    /**
     * Create new subject from SubjectDto.
     *
     * @param subjectDto SubjectDto instance.
     * @return saved Subject in database.
     */
    @Transactional
    public Subject addNewSubject(SubjectDto subjectDto) {
        Subject subject = Subject.builder()
                .nameEn(subjectDto.getNameEn())
                .nameRu(subjectDto.getNameRu())
                .nameUk(subjectDto.getNameUk())
                .build();

        log.info("New subject '{}' has been created", subject);

        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Page<Subject> getSubjectPagination(Pageable pageable) {
        return subjectRepository.findAll(pageable);
    }

    public List<Subject> getRequiredSubjectsByFacultyId(Faculty facultyId) {
        return subjectRepository.findAllByFacultyId(facultyId);
    }

    public List<SubjectUserGrades> getSubjectUserGradesByApplicationId(Application applicationId) {
        return subjectUserGradesRepository.findAllByApplicationId(applicationId);
    }

    public List<Subject> getRequiredSubjectsByRecruitmentId(Long recruitment) {
        return subjectRepository.findAllByRecruitmentId(recruitment);
    }

}
