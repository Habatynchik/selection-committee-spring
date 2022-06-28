package ua.epan.elearn.selection.committee.spring.controller.validator;

import ua.epan.elearn.selection.committee.spring.model.dto.ApplicationDto;

public class ApplicationValidator {

    public static boolean validateApplicationDto(ApplicationDto applicationDto) {
        return applicationDto.getGrades().stream().anyMatch(e -> e == null || e < 100 || e > 200);
    }

}
