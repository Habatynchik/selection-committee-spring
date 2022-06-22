package ua.epan.elearn.selection.committee.spring.model.dto;

import lombok.*;
import ua.epan.elearn.selection.committee.spring.model.entity.ApplicationState;
import ua.epan.elearn.selection.committee.spring.model.entity.Recruitment;
import ua.epan.elearn.selection.committee.spring.model.entity.User;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Data
public class ApplicationDto {

    @NotNull
    User user;

    @NotNull
    Recruitment recruitment;

    @NotNull
    private List<@NotNull @Min(100) @Max(200) Long> grades;

}
