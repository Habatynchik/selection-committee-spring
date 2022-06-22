package ua.epan.elearn.selection.committee.spring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;
import ua.epan.elearn.selection.committee.spring.model.entity.RequiredSubject;

import java.util.List;

@Repository
public interface RequiredSubjectRepository extends JpaRepository<RequiredSubject, Long> {

    List<RequiredSubject> findAllByFacultyId(Faculty facultyId);
    void deleteByFacultyId(Faculty facultyId);
}
