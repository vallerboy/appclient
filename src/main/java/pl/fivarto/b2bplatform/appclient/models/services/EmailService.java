package pl.fivarto.b2bplatform.appclient.models.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    public enum  EmailType {
        ACCOUNT_ACTIVATED, ACCOUNT_DEACTIVATED, REGISTRED, ORDER_DONE_USER, ORDER_DONE_ADMIN;
    }

    final private JavaMailSender javaMailSender;
    final private TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String email, EmailType emailType,  String ... data){
        StringBuilder content = new StringBuilder();
        String title = null;
        switch (emailType){
            case ACCOUNT_ACTIVATED: {
                content = new StringBuilder("Z przyjemnością informujemy, że Państwa konto zostało aktywowane. Od teraz można składać już zamówienia. ");
                title = "Aktywacja konta w serwsie B2B";
                break;
            }
            case ACCOUNT_DEACTIVATED: {
                content = new StringBuilder("Państwa konto zostało dezaktywowane. Od teraz nie można już składać zamówień");
                title = "Dezaktywacja konta w serwsie B2B";
                break;
            }

            case REGISTRED: {
                content = new StringBuilder("Informujemy, że konto zostało założone pomyślnie. Prosimy oczekiwać na jego akceptację, wtedy będą mogli się Państwo zalogować.");
                title = "Rejestracja w serwisie Bratek B2B";
                break;
            }
            case ORDER_DONE_USER: {
                content = new StringBuilder("<b>Dziękujemy za założenie zamówienia.</b> <br/>");
                title = "Twoje zamówienie zostało złożone pomyślnie";
                break;
            }
            case ORDER_DONE_ADMIN: {
                content = new StringBuilder("OTRZYMAŁES NOWE ZAMÓWIENIE <br>");
                title = "Ktoś złożył zamówienie - juuuupi";
                break;
            }
        }


        for (String datum : data) {
            content.append(datum).append("\r\n");
        }

        Context context = new Context();
        context.setVariable("message", content.toString());

        String body  = templateEngine.process("email/mainTemplate", context);
        sendEmail(email, title, body);
    }

    void sendEmail(String email, String title, String content){
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(email);
            helper.setReplyTo("zamowienia@bratekbb.pl");
            helper.setFrom("zamowienia@bratekbb.pl");
            helper.setSubject(title);
            helper.setText(content, true);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        javaMailSender.send(mail);
    }
}
