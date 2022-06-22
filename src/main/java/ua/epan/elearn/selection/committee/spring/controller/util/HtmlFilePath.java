package ua.epan.elearn.selection.committee.spring.controller.util;

public class HtmlFilePath {

    private HtmlFilePath() {
    }

    public static final String COMMON_PREFIX = "common";
    public static final String GUEST_PREFIX = "guest";
    public static final String USER_PREFIX = "user";
    public static final String ADMIN_PREFIX = "admin";

    public static final String GUEST_LOGIN_PAGE = GUEST_PREFIX + "/loginPage";
    public static final String GUEST_REGISTRATION_PAGE = GUEST_PREFIX + "/registrationPage";

    public static final String FACULTIES_PAGE = COMMON_PREFIX + "/faculties";
    public static final String FACULTY_PAGE = COMMON_PREFIX + "/faculty";
    public static final String RECRUITMENTS_PAGE = COMMON_PREFIX + "/recruitments";
    public static final String RECRUITMENT_PAGE = COMMON_PREFIX + "/recruitment";
    public static final String APPLICATION_PAGE = COMMON_PREFIX + "/application";

    public static final String ADD_FACULTY_PAGE = ADMIN_PREFIX + "/addFaculty";
    public static final String ADD_RECRUITMENT_PAGE = ADMIN_PREFIX + "/addRecruitment";
    public static final String ADD_SUBJECT_PAGE = ADMIN_PREFIX + "/addSubject";
    public static final String UPDATE_FACULTY_PAGE = ADMIN_PREFIX + "/updateFaculty";

    public static final String USERS_PAGE = ADMIN_PREFIX + "/users";
    public static final String SUBJECTS_PAGE = ADMIN_PREFIX + "/subjects";


    public static final String PROFILE_PAGE = USER_PREFIX + "/profile";
    public static final String CREATE_APPLICATION_PAGE = USER_PREFIX + "/createApplication";
}
