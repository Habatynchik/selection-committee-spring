package ua.epan.elearn.selection.committee.spring.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.epan.elearn.selection.committee.spring.controller.util.HtmlFilePath;
import ua.epan.elearn.selection.committee.spring.controller.util.UriPath;
import ua.epan.elearn.selection.committee.spring.controller.validator.ApplicationValidator;
import ua.epan.elearn.selection.committee.spring.model.dto.ApplicationDto;
import ua.epan.elearn.selection.committee.spring.model.dto.RecruitmentDto;
import ua.epan.elearn.selection.committee.spring.model.entity.*;
import ua.epan.elearn.selection.committee.spring.model.exception.FacultyNameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.exception.UserAlreadyAppliedException;
import ua.epan.elearn.selection.committee.spring.model.service.ApplicationService;
import ua.epan.elearn.selection.committee.spring.model.service.RecruitmentService;
import ua.epan.elearn.selection.committee.spring.model.service.SubjectService;
import ua.epan.elearn.selection.committee.spring.model.service.UserService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.security.Principal;

@Controller
@Log4j2
@RequiredArgsConstructor
public class ApplicationController {

    private static final String RECRUITMENT_ID = "recruitmentId";
    private static final String REQUIRED_SUBJECTS_LIST = "requiredSubjectList";

    private final SubjectService subjectService;
    private final ApplicationService applicationService;
    private final UserService userService;
    private final RecruitmentService recruitmentService;

    @GetMapping(UriPath.CREATE_APPLICATION)
    public String getCreateApplicationPage(@RequestParam(name = "recruitmentId") Long recruitmentId,
                                           Model model,
                                           Authentication authentication) {

        User user = userService.getByUsername(authentication.getName());
        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);

        try {
            applicationService.checkApplicationIsRegister(user, recruitment);
        } catch (UserAlreadyAppliedException e) {
            return UriPath.REDIRECT + UriPath.RECRUITMENT + "?recruitmentId=" + recruitment.getId();
        }

        ApplicationDto applicationDto = ApplicationDto.builder()
                .user(user)
                .recruitment(recruitment)
                .build();

        List<Subject> requiredSubjectList = subjectService.getRequiredSubjectsByRecruitmentId(recruitmentId);
        model.addAttribute("applicationDto", applicationDto);
        model.addAttribute(REQUIRED_SUBJECTS_LIST, requiredSubjectList);
        return HtmlFilePath.CREATE_APPLICATION_PAGE;
    }

    @PostMapping(UriPath.CREATE_APPLICATION)
    public String postCreateApplicationPage(@ModelAttribute(name = "applicationDto") @Valid ApplicationDto applicationDto,
                                            BindingResult validationResult,
                                            Model model) {


        if (!validationResult.hasErrors()) {

            try {
                applicationService.addNewApplication(applicationDto);

                return UriPath.REDIRECT + UriPath.RECRUITMENTS;
            } catch (UserAlreadyAppliedException e) {
                model.addAttribute("userAlreadyApplied", true);
            }
        }

        if (ApplicationValidator.validateApplicationDto(applicationDto))
            model.addAttribute("invalidGrade", true);

        model.addAttribute(REQUIRED_SUBJECTS_LIST, subjectService.getRequiredSubjectsByRecruitmentId(applicationDto.getRecruitment().getId()));
        return HtmlFilePath.CREATE_APPLICATION_PAGE;
    }

    @GetMapping(UriPath.APPLICATION)
    public String getApplicationPage(@RequestParam(name = "applicationId") Long applicationId,
                                     Model model) {

        Application application = applicationService.getApplicationsById(applicationId);

        if (application == null)
            return UriPath.REDIRECT + UriPath.RECRUITMENTS;

        Recruitment recruitment = recruitmentService.getRecruitmentByApplicationId(application.getId());
        List<SubjectUserGrades> subjectUserGradesList = subjectService.getSubjectUserGradesByApplicationId(application);

        model.addAttribute("app", application);
        model.addAttribute("recruitment", recruitment);
        model.addAttribute("subjectUserGradesList", subjectUserGradesList);

        return HtmlFilePath.APPLICATION_PAGE;
    }


}
