package ua.epan.elearn.selection.committee.spring.model.dto.regex;

public class RegExp {
    private RegExp() {}

    public static final String PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).+$";
    public static final String EMAIL = "^[A-Za-z0-9._]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    public static final String DATE = "\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])*";
}
