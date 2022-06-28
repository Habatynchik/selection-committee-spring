package ua.epan.elearn.selection.committee.spring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.epan.elearn.selection.committee.spring.model.entity.ApplicationState;

public interface ApplicationStateRepository  extends JpaRepository<ApplicationState, Long> {

    ApplicationState findByApplicationStateEnum(ApplicationState.ApplicationStateEnum applicationStateEnum);

}
