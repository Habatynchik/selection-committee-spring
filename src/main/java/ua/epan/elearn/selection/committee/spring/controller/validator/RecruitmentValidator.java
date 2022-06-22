package ua.epan.elearn.selection.committee.spring.controller.validator;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ua.epan.elearn.selection.committee.spring.model.dto.RecruitmentDto;

import java.time.LocalDate;

public class RecruitmentValidator {

    public static void validateRecruitmentDto(RecruitmentDto recruitmentDto, BindingResult validationResult) {

        if (!validationResult.hasErrors()) {

            //generalCapacityIsMore
            if (recruitmentDto.getGeneralCapacity() > recruitmentDto.getRemainingGeneralCapacity())
                validationResult.addError(
                        new FieldError(RecruitmentDto.class.getName(), "generalCapacity", "generalCapacityIsMore")
                );

            //generalCapacityLessBudgetCapacity
            if (recruitmentDto.getGeneralCapacity() < recruitmentDto.getBudgetCapacity())
                validationResult.addError(
                        new FieldError(RecruitmentDto.class.getName(), "generalCapacity", "generalCapacityLessBudgetCapacity")
                );

            //budgetCapacityIsMore
            if (recruitmentDto.getBudgetCapacity() > recruitmentDto.getRemainingBudgetCapacity())
                validationResult.addError(
                        new FieldError(RecruitmentDto.class.getName(), "budgetCapacity", "budgetCapacityIsMore")
                );

            //startDateOutOfDate
            if (recruitmentDto.getStartDate().isBefore(LocalDate.now()))
                validationResult.addError(
                        new FieldError(RecruitmentDto.class.getName(), "startDate", "startDateOutOfDate")
                );

            //endDateAfterCurrentYear
            if (recruitmentDto.getEndDate().getYear() != LocalDate.now().getYear())
                validationResult.addError(
                        new FieldError(RecruitmentDto.class.getName(), "endDate", "endDateAfterCurrentYear")
                );

            //endDateBeforeStartDate
            if (recruitmentDto.getStartDate().isAfter(recruitmentDto.getEndDate()))
                validationResult.addError(
                        new FieldError(RecruitmentDto.class.getName(), "endDate", "endDateBeforeStartDate")
                );
        }


    }
}
