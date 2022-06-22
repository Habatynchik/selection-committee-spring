package ua.epan.elearn.selection.committee.spring.model.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "application_state")
public class ApplicationState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ApplicationStateEnum applicationStateEnum;

    public enum ApplicationStateEnum {
        REGISTERED,
        CANCELED,
        REJECTED,
        ACCEPTED_FOR_CONTRACT,
        ACCEPTED_FOR_BUDGET;

    }



}
