package ua.epan.elearn.selection.committee.spring.model.repository;

import org.hibernate.criterion.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;
import ua.epan.elearn.selection.committee.spring.model.entity.Recruitment;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    @Query("SELECT r FROM Recruitment r WHERE r.closed=false and r.facultyId=:facultyId AND current_date BETWEEN r.startDate AND r.endDate")
    List<Recruitment> findOpenedRecruitmentByFacultyId(@Param(value = "facultyId") Faculty facultyId);

    @Query("SELECT r FROM Recruitment r WHERE r.closed=false AND current_date > r.endDate")
    List<Recruitment> findAllOverdueOpenedRecruitment();

    List<Recruitment> findAllRecruitmentWhereCurrentYearByFacultyId(Faculty facultyId);

    @Query("SELECT r FROM Recruitment r JOIN Application app ON app.recruitmentId = r WHERE app.id = :applicationId")
    Recruitment findByApplicationId(@Param(value = "applicationId") Long applicationId);

    @Query("SELECT r FROM Recruitment r WHERE " +
            " r.startDate > current_date  AND 1 = :bool1 OR" +
            " current_date BETWEEN r.startDate AND r.endDate AND 1 = :bool2 OR " +
            " r.endDate < current_date AND 1 = :bool3 ")
    Page<Recruitment> findAllWithFilterAndPagination(@Param(value = "bool1") Integer bool1,
                                                     @Param(value = "bool2") Integer bool2,
                                                     @Param(value = "bool3") Integer bool3, Pageable pageable);


    boolean existsByNameAndFacultyId(String name, Faculty facultyId);
}
