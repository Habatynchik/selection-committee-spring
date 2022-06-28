package ua.epan.elearn.selection.committee.spring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;

@Repository
public interface FacultyRepository  extends JpaRepository<Faculty, Long> {

    boolean existsByName(String name);

    Faculty findFacultyByName(String name);
}
