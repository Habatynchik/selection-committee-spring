package ua.epan.elearn.selection.committee.spring.controller.common;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.epan.elearn.selection.committee.spring.controller.util.HtmlFilePath;
import ua.epan.elearn.selection.committee.spring.controller.util.UriPath;
import ua.epan.elearn.selection.committee.spring.controller.validator.FacultyValidator;
import ua.epan.elearn.selection.committee.spring.controller.validator.RecruitmentValidator;
import ua.epan.elearn.selection.committee.spring.model.dto.RecruitmentDto;
import ua.epan.elearn.selection.committee.spring.model.dto.SubjectDto;
import ua.epan.elearn.selection.committee.spring.model.dto.UserDto;
import ua.epan.elearn.selection.committee.spring.model.entity.Application;
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;
import ua.epan.elearn.selection.committee.spring.model.entity.Recruitment;
import ua.epan.elearn.selection.committee.spring.model.entity.Subject;
import ua.epan.elearn.selection.committee.spring.model.exception.RecruitmentNameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.service.ApplicationService;
import ua.epan.elearn.selection.committee.spring.model.service.FacultyService;
import ua.epan.elearn.selection.committee.spring.model.service.RecruitmentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@Log4j2
@AllArgsConstructor
public class RecruitmentController {

    private final String PAGE = "page";
    private final String PAGES = "pages";
    private final String ORDER = "order";
    private final String SIZE = "size";
    private final String SORT = "sort";
    private final String FILTER = "filter";

    private RecruitmentService recruitmentService;
    private FacultyService facultyService;
    private ApplicationService applicationService;

    @GetMapping(UriPath.RECRUITMENTS)
    public String getFacultiesPage(@RequestParam(name = FILTER, required = false, defaultValue = "current") String[] filters,
                                   @RequestParam(name = PAGE, required = false, defaultValue = "1") String page,
                                   @RequestParam(name = SORT, required = false, defaultValue = "id") String sort,
                                   @RequestParam(name = ORDER, required = false, defaultValue = "asc") String order,
                                   @RequestParam(name = SIZE, required = false, defaultValue = "4") String size,
                                   Model model) {

        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size), Sort.by(sort).ascending());
        } else
            pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size), Sort.by(sort).descending());

        Page<Recruitment> recruitmentPage = recruitmentService.getRecruitmentPaginationWithFilter(filters, pageable);
        List<Recruitment> recruitmentList = recruitmentPage.getContent();
        recruitmentList.stream().filter(e -> e.getStartDate().isAfter(LocalDate.now()))
                .forEach(e -> e.setClosed(true));

        model.addAttribute(SIZE, size);
        model.addAttribute(PAGE, page);
        model.addAttribute(PAGES, recruitmentPage.getTotalPages());
        model.addAttribute(SORT, sort);
        model.addAttribute(ORDER, order);

        model.addAttribute("recruitmentList", recruitmentList);

        insertFiltersIntoRequest(filters, model);

        return HtmlFilePath.RECRUITMENTS_PAGE;
    }

    @GetMapping(UriPath.ADD_RECRUITMENT)
    public String getAddRecruitmentPage(@RequestParam(name = "facultyId") Long facultyId,
                                        Model model) {

        Faculty faculty = facultyService.getFacultyById(facultyId);
        RecruitmentDto recruitmentDto = RecruitmentDto.builder()
                .faculty(faculty)
                .remainingGeneralCapacity(recruitmentService.getRemainingGeneralSpotsInThisYearByFacultyId(faculty))
                .remainingBudgetCapacity(recruitmentService.getRemainingBudgetSpotsInThisYearByFacultyId(faculty))
                .build();

        model.addAttribute("recruitmentDto", recruitmentDto);
        model.addAttribute("facultyId", facultyId);

        return HtmlFilePath.ADD_RECRUITMENT_PAGE;
    }

    @PostMapping(UriPath.ADD_RECRUITMENT)
    public String postAddRecruitmentPage(@ModelAttribute(name = "recruitmentDto") @Valid RecruitmentDto recruitmentDto,
                                         BindingResult validationResult,
                                         Model model) {

        RecruitmentValidator.validateRecruitmentDto(recruitmentDto, validationResult);

        if (!validationResult.hasErrors()) {
            try {
                recruitmentService.addNewRecruitment(recruitmentDto);

                return UriPath.REDIRECT + UriPath.RECRUITMENTS;
            } catch (RecruitmentNameIsReservedException e) {
                validationResult.addError(new FieldError(RecruitmentDto.class.getName(), "name", "recruitmentNameIsReserved"));
            }
        }

        return HtmlFilePath.ADD_RECRUITMENT_PAGE;
    }


    @GetMapping(UriPath.RECRUITMENT)
    public String getRecruitmentPage(@RequestParam(name = "recruitmentId") Long recruitmentId,
                                     Model model) {

        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        if (recruitment == null)
            return UriPath.REDIRECT + UriPath.RECRUITMENTS;

        List<Application> applicationList = applicationService.getAllApplicationsRecruitment(recruitment);

        model.addAttribute("applicationList", applicationList);
        model.addAttribute("recruitment", recruitment);

        return HtmlFilePath.RECRUITMENT_PAGE;
    }

    @PostMapping(UriPath.CLOSE_RECRUITMENT)
    public String postAddRecruitmentPage(@ModelAttribute(name = "recruitmentId") @Valid Long recruitmentId) {

        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        recruitmentService.closeRecruitment(recruitment);
        applicationService.finalizeApplications(recruitment);

        return UriPath.REDIRECT + UriPath.RECRUITMENTS;
    }


    private void insertFiltersIntoRequest(String[] filters, Model model) {
        if (filters != null)
            for (String s : filters) {
                model.addAttribute(s, true);
            }
    }
}
