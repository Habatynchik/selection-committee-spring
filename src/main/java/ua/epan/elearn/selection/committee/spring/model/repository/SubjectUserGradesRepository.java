package ua.epan.elearn.selection.committee.spring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.epan.elearn.selection.committee.spring.model.entity.Application;
import ua.epan.elearn.selection.committee.spring.model.entity.SubjectUserGrades;

import java.util.List;

public interface SubjectUserGradesRepository extends JpaRepository<SubjectUserGrades, Long> {

    List<SubjectUserGrades> findAllByApplicationId(Application applicationId);
}
