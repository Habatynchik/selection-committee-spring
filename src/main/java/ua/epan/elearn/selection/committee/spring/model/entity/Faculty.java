package ua.epan.elearn.selection.committee.spring.model.entity;

import lombok.*;
import ua.epan.elearn.selection.committee.spring.model.dto.FacultyDto;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
@Table(name = "faculty")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "general_capacity")
    private Long generalCapacity;

    @Column(name = "budget_capacity")
    private Long budgetCapacity;

    public Faculty(FacultyDto facultyDto) {
        this.id = facultyDto.getId();
        this.name = facultyDto.getName();
        this.generalCapacity = facultyDto.getGeneralCapacity();
        this.budgetCapacity = facultyDto.getBudgetCapacity();
    }
}
