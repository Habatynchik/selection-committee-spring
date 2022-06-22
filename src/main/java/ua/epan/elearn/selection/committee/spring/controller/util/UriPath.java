package ua.epan.elearn.selection.committee.spring.controller.util;

public class UriPath {
    private UriPath() {
    }


    public static final String REDIRECT = "redirect:";

    public static final String USER = "/user";

    public static final String ADMIN = "/admin";

    public static final String LOGIN = "/login";

    public static final String REGISTRATION = "/registration";

    public static final String PROFILE = USER +  "/profile";

    public static final String FACULTIES = "/faculties";

    public static final String ADD_FACULTY = ADMIN + "/add-faculty";

    public static final String ADD_SUBJECT = ADMIN + "/add-subject";

    public static final String SUBJECTS = ADMIN + "/subjects";

    public static final String USERS = ADMIN + "/users";

    public static final String RECRUITMENTS = "/recruitments";

    public static final String RECRUITMENT = "/recruitment";

    public static final String ADD_RECRUITMENT = ADMIN + "/add-recruitment";

    public static final String CLOSE_RECRUITMENT = ADMIN + "/close-recruitment";

    public static final String APPLICATION = "/application";

    public static final String SUBMIT_APPLICATION =USER + "/submit-application";

    public static final String CREATE_APPLICATION = USER + "/create-application";

    public static final String LOGOUT = "/logout";

    public static final String HOME = "/";

    public static final String FACULTY = "/view-faculty";

    public static final String DELETE_FACULTY = ADMIN + FACULTY + "/delete";

    public static final String UPDATE_FACULTY = ADMIN + "/change-faculty";

}
