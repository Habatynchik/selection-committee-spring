package ua.epan.elearn.selection.committee.spring.model.entity;

import lombok.*;
import ua.epan.elearn.selection.committee.spring.model.dto.RecruitmentDto;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
@Table(name = "recruitment")
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "general_capacity")
    private Long generalCapacity;

    @Column(name = "budget_capacity")
    private Long budgetCapacity;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "closed")
    private Boolean closed;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty facultyId;

    public Recruitment(RecruitmentDto recruitmentDto) {
        this.name = recruitmentDto.getName();
        this.generalCapacity = recruitmentDto.getGeneralCapacity();
        this.budgetCapacity = recruitmentDto.getBudgetCapacity();
        this.startDate = recruitmentDto.getStartDate();
        this.endDate = recruitmentDto.getEndDate();
        this.closed = false;
        this.facultyId = recruitmentDto.getFaculty();
    }
}
