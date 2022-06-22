package ua.epan.elearn.selection.committee.spring.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ua.epan.elearn.selection.committee.spring.controller.util.HtmlFilePath;
import ua.epan.elearn.selection.committee.spring.controller.util.UriPath;
import ua.epan.elearn.selection.committee.spring.model.entity.Application;
import ua.epan.elearn.selection.committee.spring.model.entity.User;
import ua.epan.elearn.selection.committee.spring.model.service.ApplicationService;
import ua.epan.elearn.selection.committee.spring.model.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class UserController {

    private final String USER = "userProfile";
    private final String USER_LIST = "userList";
    private final String USER_ID = "userId";
    private final String APPLICATIONS = "applications";


    private final String PAGE = "page";
    private final String ORDER = "order";
    private final String SIZE = "size";
    private final String SORT = "sort";

    private final UserService userService;
    private final ApplicationService applicationService;


    @GetMapping(UriPath.PROFILE)
    public String getProfilePage(@RequestParam(value = USER_ID) Long id,
                                 Model model) {

        User user = userService.findUserById(id);
        List<Application> applicationList = applicationService.getAllApplicationsByUserId(user);
        model.addAttribute(USER, user);
        model.addAttribute(APPLICATIONS, applicationList);

        return HtmlFilePath.PROFILE_PAGE;
    }

    @GetMapping(UriPath.USERS)
    public String getUsersPage(@RequestParam(name = PAGE, required = false, defaultValue = "0") String page,
                               @RequestParam(name = SORT, required = false, defaultValue = "id") String sort,
                               @RequestParam(name = ORDER, required = false, defaultValue = "asc") String order,
                               @RequestParam(name = SIZE, required = false, defaultValue = "10") String size,
                               Model model) {

        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(sort).ascending());
        } else pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(sort).descending());

        Page<User> userPage = userService.getUserPagination(pageable);
        List<User> userList = userPage.getContent();

        model.addAttribute(SIZE, size);
        model.addAttribute(PAGE, userPage.getTotalPages());
        model.addAttribute(SORT, sort);
        model.addAttribute(ORDER, order);
        model.addAttribute(USER_LIST, userList);

        return HtmlFilePath.USERS_PAGE;
    }

    @PostMapping(UriPath.USERS)
    public String postUsersPage(@RequestParam(name = "userId") Long userId,
                                @RequestParam(name = "userBlocked") Boolean blocked,
                                Model model) {

        if (blocked.equals(false)) {
            userService.blockUserById(userId);
        } else if (blocked.equals(true)) {
            userService.unblockUserById(userId);
        }

        return UriPath.REDIRECT + UriPath.USERS;
    }


}
