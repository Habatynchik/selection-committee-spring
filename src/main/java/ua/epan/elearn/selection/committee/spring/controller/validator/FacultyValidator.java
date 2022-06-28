package ua.epan.elearn.selection.committee.spring.controller.validator;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ua.epan.elearn.selection.committee.spring.model.dto.FacultyDto;
import ua.epan.elearn.selection.committee.spring.model.dto.RecruitmentDto;

public class FacultyValidator {

    public static void validateFacultyDto(FacultyDto facultyDto, BindingResult validationResult) {

        if (!validationResult.hasErrors()) {

            if (facultyDto.getGeneralCapacity() < facultyDto.getBudgetCapacity())
                validationResult.addError(
                        new FieldError(RecruitmentDto.class.getName(), "generalCapacity", "generalCapacityLessBudgetCapacity")
                );

            if (facultyDto.getRequiredSubjectList().size() < 3)
                validationResult.addError(
                        new FieldError(RecruitmentDto.class.getName(), "requiredSubjectList", "fewRequiredSubjects")
                );
        }
    }

}
