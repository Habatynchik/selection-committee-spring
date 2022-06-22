package ua.epan.elearn.selection.committee.spring.controller.validator;

public class FieldValidator {

    public static boolean fieldIsEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }

    public static boolean fieldIsNotValidLong(String field) {
        try {
            Long.parseLong(field);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    public static boolean fieldIsNotValidInt(String field) {
        try {
            Long.parseLong(field);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }
}
