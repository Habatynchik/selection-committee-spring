package ua.epan.elearn.selection.committee.spring.model.dto;

import lombok.*;
import ua.epan.elearn.selection.committee.spring.model.entity.Subject;

import javax.validation.constraints.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Data
public class FacultyDto {

    private Long id;

    @NotBlank(message = "emptyField")
    private String name;

    @NotNull(message = "generalCapacityIncorrect")
    @Min(value = 1, message = "generalCapacityIncorrect")
    private Long generalCapacity;

    @NotNull(message = "budgetCapacityIncorrect")
    @Min(value = 0, message = "budgetCapacityIncorrect")
    private Long budgetCapacity;

    @NotNull
    private List<Subject> requiredSubjectList;

}
