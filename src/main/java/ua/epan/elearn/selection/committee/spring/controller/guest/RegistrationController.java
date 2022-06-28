package ua.epan.elearn.selection.committee.spring.controller.guest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.epan.elearn.selection.committee.spring.controller.util.HtmlFilePath;
import ua.epan.elearn.selection.committee.spring.controller.util.UriPath;
import ua.epan.elearn.selection.committee.spring.model.dto.UserDto;
import ua.epan.elearn.selection.committee.spring.model.exception.EmailIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.exception.UsernameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.service.UserService;

import javax.validation.Valid;

@Controller
@Log4j2
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping(UriPath.REGISTRATION)
    public String getRegistrationPage(Model model) {
        model.addAttribute("userDto", new UserDto());

        return HtmlFilePath.GUEST_REGISTRATION_PAGE;
    }

    @PostMapping(UriPath.REGISTRATION)
    public String createNewAccount(@ModelAttribute(name = "userDto") @Valid UserDto userDto,
                                   BindingResult validationResult,
                                   Model model) {

        if (!validationResult.hasFieldErrors("password") && !userDto.getPassword().equals(userDto.getPasswordCopy())) {
            validationResult.addError(
                    new FieldError(UserDto.class.getName(), "passwordCopy", "passwordsNotSame")
            );
        }

        if (!validationResult.hasErrors()) {
            try {
                log.info("User wants to create new account");
                userService.registerNewAccount(userDto);

                return UriPath.REDIRECT + UriPath.LOGIN;
            } catch (UsernameIsReservedException e) {
                model.addAttribute("usernameIsReserved", true);
            } catch (EmailIsReservedException e) {
                model.addAttribute("emailIsReserved", true);
            }
        }

        log.warn("User print some incorrect data during registration");
        return HtmlFilePath.GUEST_REGISTRATION_PAGE;
    }

}
