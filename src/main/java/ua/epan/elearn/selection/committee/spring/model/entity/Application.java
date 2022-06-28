package ua.epan.elearn.selection.committee.spring.model.entity;

import lombok.*;
import org.hibernate.annotations.Formula;
import ua.epan.elearn.selection.committee.spring.model.dto.ApplicationDto;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
@Table(name = "application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "application_state_id", nullable = false)
    private ApplicationState applicationStateId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "recruitment_id", nullable = false)
    private Recruitment recruitmentId;

    @Formula("(select avg(sug.grade) from application a " +
            "left join subject_user_grades sug ON sug.application_id = a.id " +
            "where a.id = id " +
            "group by a.id)")
    private Long averageScore;

    public Application(ApplicationDto applicationDto) {
        this.userId = applicationDto.getUser();
        this.recruitmentId = applicationDto.getRecruitment();
    }
}
