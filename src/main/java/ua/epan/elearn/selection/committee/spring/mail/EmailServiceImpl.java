package ua.epan.elearn.selection.committee.spring.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service("EmailService")
public class EmailServiceImpl implements EmailService  {

    private static final String NOREPLY_ADDRESS = "habatynchik@gmail.com";

    private final String BUDGET_MESSAGE = "Dear enrollee, \n" +
            "Congratulations! With great pleasure we offer you a budget place in our educational institution. \n" +
            "Sincerely, \n" +
            "Gamaiunov Nikita, \n" +
            "Director of Admission";

    private final String CONTRACT_MESSAGE = "Dear enrollee, \n" +
            "Congratulations! With great pleasure we offer you a contract place in our educational institution. \n" +
            "Sincerely, \n" +
            "Gamaiunov Nikita, \n" +
            "Director of Admission";

    private final String BAD_MESSAGE = "Dear enrollee, \n" +
            "We are very sorry to tell you this, but you did not pass the competition. We hope to see you in the next recruitment.\n" +
            "Sincerely, \n" +
            "Gamaiunov Nikita, \n" +
            "Director of Admission";

    @Autowired
    private JavaMailSender emailSender;

    public void sendBudgetMessage(String recipientMail) {
        sendSimpleMessage(recipientMail, "Dear enrollee", BUDGET_MESSAGE);
    }

    public void sendContractMessage(String recipientMail) {
        sendSimpleMessage(recipientMail, "Dear enrollee", CONTRACT_MESSAGE);
    }

    public void sendBadMessage(String recipientMail) {
        sendSimpleMessage(recipientMail, "Dear enrollee", BAD_MESSAGE);
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NOREPLY_ADDRESS);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

}
