package ua.epan.elearn.selection.committee.spring.controller.common;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.epan.elearn.selection.committee.spring.controller.util.HtmlFilePath;
import ua.epan.elearn.selection.committee.spring.controller.util.UriPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;
import ua.epan.elearn.selection.committee.spring.model.repository.FacultyRepository;
import ua.epan.elearn.selection.committee.spring.model.service.FacultyService;

import java.util.List;

@Controller
@Log4j2
@AllArgsConstructor
public class FacultiesController {

    private FacultyService facultyService;

    @GetMapping(UriPath.FACULTIES)
    public String getFacultiesPage(@RequestParam(name = "page", required = false, defaultValue = "0") String page,
                                   @RequestParam(name = "sort", required = false, defaultValue = "name") String sort,
                                   @RequestParam(name = "order", required = false, defaultValue = "asc") String order,
                                   @RequestParam(name = "size", required = false, defaultValue = "9") String size,
                                   Model model) {

        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(sort).ascending());
        } else pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(sort).descending());

        Page<Faculty> facultyPage = facultyService.getFacultyPagination(pageable);
        List<Faculty> facultyList = facultyPage.getContent();


        model.addAttribute("size", size);
        model.addAttribute("page", facultyPage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("facultyList", facultyList);

        return HtmlFilePath.FACULTIES_PAGE;
    }



}
