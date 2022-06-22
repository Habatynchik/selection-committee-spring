package ua.epan.elearn.selection.committee.spring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.epan.elearn.selection.committee.spring.model.entity.Application;
import ua.epan.elearn.selection.committee.spring.model.entity.ApplicationState;
import ua.epan.elearn.selection.committee.spring.model.entity.Recruitment;
import ua.epan.elearn.selection.committee.spring.model.entity.User;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByUserId(User userId);

    List<Application> findAllByRecruitmentId(Recruitment recruitmentId);

    @Query("SELECT apps FROM ApplicationState apps WHERE apps.applicationStateEnum = :state")
    ApplicationState findApplicationStateByStateEnum(@Param(value = "state") ApplicationState.ApplicationStateEnum state);

    boolean existsApplicationByUserIdAndRecruitmentId(User userId, Recruitment recruitmentId);
}
