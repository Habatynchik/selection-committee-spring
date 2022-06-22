package ua.epan.elearn.selection.committee.spring.model.dto;

import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import ua.epan.elearn.selection.committee.spring.model.dto.regex.RegExp;
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Data
public class RecruitmentDto {

    @NotBlank(message = "emptyField")
    private String name;

    @NotNull(message = "generalCapacityIncorrect")
    @Min(value = 1, message = "generalCapacityIncorrect")
    private Long generalCapacity;

    @NotNull(message = "budgetCapacityIncorrect")
    @Min(value = 0, message = "budgetCapacityIncorrect")
    private Long budgetCapacity;

    @NotNull(message = "emptyFieldException")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "emptyFieldException")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Faculty faculty;

    private Long remainingGeneralCapacity;

    private Long remainingBudgetCapacity;

}
