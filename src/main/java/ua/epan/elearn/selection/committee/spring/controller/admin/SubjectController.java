package ua.epan.elearn.selection.committee.spring.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.epan.elearn.selection.committee.spring.controller.util.HtmlFilePath;
import ua.epan.elearn.selection.committee.spring.controller.util.UriPath;
import ua.epan.elearn.selection.committee.spring.model.dto.FacultyDto;
import ua.epan.elearn.selection.committee.spring.model.dto.SubjectDto;
import ua.epan.elearn.selection.committee.spring.model.entity.Subject;
import ua.epan.elearn.selection.committee.spring.model.service.SubjectService;

import javax.validation.Valid;
import java.util.List;

@Controller
@Log4j2
@AllArgsConstructor
public class SubjectController {
    private final String SUBJECT_LIST = "subjectList";

    private final String PAGE = "page";
    private final String PAGES = "pages";
    private final String ORDER = "order";
    private final String SIZE = "size";
    private final String SORT = "sort";

    private SubjectService subjectService;

    @GetMapping(UriPath.SUBJECTS)
    public String getSubjectsPage(@RequestParam(name = PAGE, required = false, defaultValue = "1") String page,
                                  @RequestParam(name = SORT, required = false, defaultValue = "id") String sort,
                                  @RequestParam(name = ORDER, required = false, defaultValue = "asc") String order,
                                  @RequestParam(name = SIZE, required = false, defaultValue = "5") String size,
                                  Model model) {

        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size), Sort.by(sort).ascending());
        } else
            pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size), Sort.by(sort).descending());

        Page<Subject> subjectPage = subjectService.getSubjectPagination(pageable);
        List<Subject> subjectList = subjectPage.getContent();

        model.addAttribute(SUBJECT_LIST, subjectList);

        model.addAttribute(SIZE, size);
        model.addAttribute(PAGE, page);
        model.addAttribute(PAGES, subjectPage.getTotalPages());

        return HtmlFilePath.SUBJECTS_PAGE;
    }

    @GetMapping(UriPath.ADD_SUBJECT)
    public String getAddSubjectPage(Model model) {

        model.addAttribute("subjectDto", new SubjectDto());

        return HtmlFilePath.ADD_SUBJECT_PAGE;
    }

    @PostMapping(UriPath.ADD_SUBJECT)
    public String postAddSubjectPage(@ModelAttribute(name = "subjectDto") @Valid SubjectDto subjectDto,
                                     BindingResult validationResult) {

        if (!validationResult.hasErrors()) {
            subjectService.addNewSubject(subjectDto);
            return UriPath.REDIRECT + UriPath.SUBJECTS;
        }


        return HtmlFilePath.ADD_SUBJECT_PAGE;
    }
}
