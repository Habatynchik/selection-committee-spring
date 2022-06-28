package ua.epan.elearn.selection.committee.spring.mail;

public interface EmailService {

    void sendSimpleMessage(String to,
                           String subject,
                           String text);

}
