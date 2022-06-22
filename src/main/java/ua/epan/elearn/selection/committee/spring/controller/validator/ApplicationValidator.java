package ua.epan.elearn.selection.committee.spring.controller.validator;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ua.epan.elearn.selection.committee.spring.model.dto.ApplicationDto;
import ua.epan.elearn.selection.committee.spring.model.dto.FacultyDto;
import ua.epan.elearn.selection.committee.spring.model.dto.RecruitmentDto;

import java.util.function.Predicate;

public class ApplicationValidator {

    public static boolean validateApplicationDto(ApplicationDto applicationDto) {


        return applicationDto.getGrades().stream().anyMatch(e -> e == null || e < 100 || e > 200);


    }
}
