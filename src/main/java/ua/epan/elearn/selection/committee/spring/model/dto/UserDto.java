package ua.epan.elearn.selection.committee.spring.model.dto;

import lombok.*;
import ua.epan.elearn.selection.committee.spring.model.dto.regex.RegExp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Data
public class UserDto {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 4, max = 64, message = "sizeOutOfBounds")
    @Pattern(regexp = RegExp.EMAIL, message = "emailNotMatchTemplate")
    private String email;

    @NotBlank
    @Size(min = 8, max = 64, message = "sizeOutOfBounds")
    @Pattern(regexp = RegExp.PASSWORD, message = "passwordNotMatchTemplate")
    private String password;

    @NotBlank
    private String passwordCopy;

    @NotBlank
    @Size(max = 32)
    private String firstName;

    @NotBlank
    @Size(max = 32)
    private String secondName;

    @Size(max = 32)
    private String patronymic;

    @NotBlank
    @Size(max = 32)
    private String city;

    @NotBlank
    @Size(max = 32)
    private String region;

    @NotBlank
    @Size(max = 32)
    private String institution;


}