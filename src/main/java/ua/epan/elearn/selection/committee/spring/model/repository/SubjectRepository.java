package ua.epan.elearn.selection.committee.spring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;
import ua.epan.elearn.selection.committee.spring.model.entity.Subject;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {


    @Query("SELECT s FROM Subject s JOIN RequiredSubject rs ON rs.subjectId = s WHERE rs.facultyId = :facultyId")
    List<Subject> findAllByFacultyId(@Param(value = "facultyId") Faculty facultyId);

    @Query("SELECT s FROM Subject s JOIN RequiredSubject  rs ON rs.subjectId = s JOIN Faculty f ON rs.facultyId = f JOIN Recruitment r ON r.facultyId = f WHERE r.id = :recruitmentId")
    List<Subject> findAllByRecruitmentId(@Param(value = "recruitmentId") Long recruitmentId);

}
