package ua.epan.elearn.selection.committee.spring.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ua.epan.elearn.selection.committee.spring.controller.util.HtmlFilePath;
import ua.epan.elearn.selection.committee.spring.controller.util.UriPath;

@Controller
public class LoginController {

    @GetMapping(UriPath.LOGIN)
    public String getLoginPage() {
        return HtmlFilePath.GUEST_LOGIN_PAGE;
    }

}
