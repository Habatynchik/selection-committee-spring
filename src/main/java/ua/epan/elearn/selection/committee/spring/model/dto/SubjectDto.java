package ua.epan.elearn.selection.committee.spring.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Data
public class SubjectDto {

    @NotBlank
    private String nameEn;

    @NotBlank
    private String nameRu;

    @NotBlank
    private String nameUk;

}
