package ua.epan.elearn.selection.committee.spring.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.epan.elearn.selection.committee.spring.controller.util.HtmlFilePath;
import ua.epan.elearn.selection.committee.spring.controller.util.UriPath;
import ua.epan.elearn.selection.committee.spring.controller.validator.FacultyValidator;
import ua.epan.elearn.selection.committee.spring.model.dto.FacultyDto;
import ua.epan.elearn.selection.committee.spring.model.dto.RecruitmentDto;
import ua.epan.elearn.selection.committee.spring.model.dto.SubjectDto;
import ua.epan.elearn.selection.committee.spring.model.dto.UserDto;
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;
import ua.epan.elearn.selection.committee.spring.model.entity.Recruitment;
import ua.epan.elearn.selection.committee.spring.model.entity.RequiredSubject;
import ua.epan.elearn.selection.committee.spring.model.entity.Subject;
import ua.epan.elearn.selection.committee.spring.model.exception.FacultyNameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.service.FacultyService;
import ua.epan.elearn.selection.committee.spring.model.service.RecruitmentService;
import ua.epan.elearn.selection.committee.spring.model.service.SubjectService;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@Log4j2
@AllArgsConstructor
public class FacultyController {

    private final String SUBJECT_LIST = "subjectList";
    private final String FACULTY = "faculty";
    private final String REQUIRED_SUBJECTS_LIST = "requiredSubjectList";
    private final String RECRUITMENTS_LIST = "recruitmentsList";

    private FacultyService facultyService;
    private SubjectService subjectService;
    private RecruitmentService recruitmentService;

    @GetMapping(UriPath.ADD_FACULTY)
    public String getCreateFacultiesPage(Model model) {

        List<Subject> subjectList = subjectService.getAllSubjects();
        model.addAttribute(SUBJECT_LIST, subjectList);
        model.addAttribute("facultyDto", new FacultyDto());

        return HtmlFilePath.ADD_FACULTY_PAGE;
    }

    @PostMapping(UriPath.ADD_FACULTY)
    public String postCreateFacultiesPage(@ModelAttribute(name = "facultyDto") @Valid FacultyDto facultyDto,
                                          BindingResult validationResult,
                                          Model model) {
        FacultyValidator.validateFacultyDto(facultyDto, validationResult);

        if (!validationResult.hasErrors()) {

            try {
                facultyService.addNewFaculty(facultyDto);
                subjectService.addRequiredSubjects(
                        facultyService.getFacultyByName(facultyDto.getName()),
                        facultyDto.getRequiredSubjectList()
                );

                return UriPath.REDIRECT + UriPath.FACULTIES;

            } catch (FacultyNameIsReservedException e) {
                model.addAttribute("facultyNameIsReserved", true);
                log.warn("Faculty name '{}' is reserved", facultyDto.getName());
            }
        }

        model.addAttribute(SUBJECT_LIST, subjectService.getAllSubjects());
        return HtmlFilePath.ADD_FACULTY_PAGE;
    }

    @GetMapping(UriPath.FACULTY)
    public String getFacultyPage(@RequestParam(name = "id") Long facultyId,
                                 Model model) {

        Faculty faculty = facultyService.getFacultyById(facultyId);
        List<Subject> requiredSubjectList = subjectService.getRequiredSubjectsByFacultyId(faculty);
        List<Recruitment> recruitmentsList = recruitmentService.getAllNowOpenedRecruitmentsByFacultyId(faculty);

        model.addAttribute(FACULTY, faculty);
        model.addAttribute(REQUIRED_SUBJECTS_LIST, requiredSubjectList);
        model.addAttribute(RECRUITMENTS_LIST, recruitmentsList);

        return HtmlFilePath.FACULTY_PAGE;
    }

    @PostMapping(UriPath.DELETE_FACULTY)
    public String postDeleteFacultyPage(@RequestParam(name = "facultyId") Long facultyId,
                                        Model model) {

        if (!facultyService.checkExistByFacultyId(facultyId)) {
            return UriPath.REDIRECT + UriPath.FACULTIES;
        }

        facultyService.deleteFaculty(facultyId);

        return UriPath.REDIRECT + UriPath.FACULTIES;
    }

    @GetMapping(UriPath.UPDATE_FACULTY)
    public String getUpdateFacultyPage(@RequestParam(name = "facultyId") Long facultyId,
                                       Model model) {

        Faculty faculty = facultyService.getFacultyById(facultyId);
        List<Subject> subjectList = subjectService.getAllSubjects();
        List<Subject> requiredSubjectList = subjectService.getRequiredSubjectsByFacultyId(faculty);

        FacultyDto facultyDto = FacultyDto.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .generalCapacity(faculty.getGeneralCapacity())
                .budgetCapacity(faculty.getBudgetCapacity())
                .requiredSubjectList(requiredSubjectList)
                .build();

        model.addAttribute(SUBJECT_LIST, subjectList);
        model.addAttribute("facultyDto", facultyDto);

        return HtmlFilePath.UPDATE_FACULTY_PAGE;
    }

    @PostMapping(UriPath.UPDATE_FACULTY)
    public String getUpdateFacultyPage(@ModelAttribute(name = "facultyDto") @Valid FacultyDto facultyDto,
                                       BindingResult validationResult,
                                       Model model) {

        if (!validationResult.hasErrors()) {

            try {
                facultyService.updateFaculty(facultyDto);
                subjectService.updateRequiredSubjects(facultyDto);

                return UriPath.REDIRECT + UriPath.FACULTIES;
            } catch (FacultyNameIsReservedException e) {
                model.addAttribute("facultyNameIsReserved", true);
                log.warn("Faculty name '{}' is reserved", facultyDto.getName());
            }
        }

        model.addAttribute(SUBJECT_LIST, subjectService.getAllSubjects());
        return HtmlFilePath.UPDATE_FACULTY_PAGE;
    }

}
