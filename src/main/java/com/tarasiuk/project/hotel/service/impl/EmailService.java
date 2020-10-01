package com.tarasiuk.project.hotel.service.impl;

import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.Booking;
import com.tarasiuk.project.hotel.model.Email;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
public class EmailService {

    private static final String EMAIL_TEMPLATE = "email-template.ftl";
    private static final String BOOKING_SUBJECT = "Complete booking";
    private static final String SOURCE_EMAIL = "renaissance-team@gmail.com";
    private static final String MAP_KEY_USERNAME = "username";
    private static final String MAP_KEY_CONTENT = "content";
    private static final String ACCEPT_CONTENT = "Your booking from %s by %s has been confirmed. To pay %d $. " +
            "Thank you for choosing us.";
    private static final String CANCEL_CONTENT = "Your booking from %s by %s has been denied. " +
            "You can phone us +375 17 309-90-99. Thank you for choosing us.";

    private final JavaMailSender javaMailSender;
    private final FreeMarkerConfig freeMarkerConfig;

    public EmailService(JavaMailSender javaMailSender, FreeMarkerConfig freeMarkerConfig) {
        this.javaMailSender = javaMailSender;
        this.freeMarkerConfig = freeMarkerConfig;
    }

    @Async
    public void sendEmail(Email email) throws MessagingException, IOException, TemplateException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_RELATED,
                StandardCharsets.UTF_8.name());

        Template t1 = freeMarkerConfig.getConfiguration().getTemplate(EMAIL_TEMPLATE);
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t1, email.getModel());

        helper.setTo(email.getTo());
        helper.setText(html, true);
        helper.setSubject(email.getSubject());
        helper.setFrom(email.getFrom());

        javaMailSender.send(message);
    }

    public void sendAcceptBooking(Booking booking) {
        String content = String.format(ACCEPT_CONTENT, booking.getFirstDay(), booking.getLastDay(), booking.getCost());

        Email mailMessage = createMail(booking, content);

        try {
            sendEmail(mailMessage);
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    public void sendCancelBooking(Booking booking) {
        String content = String.format(CANCEL_CONTENT, booking.getFirstDay(), booking.getLastDay());

        Email mailMessage = createMail(booking, content);

        try {
            sendEmail(mailMessage);
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private Email createMail(Booking booking, String content) {
        Account account = booking.getAccount();

        Email mailMessage = new Email();
        mailMessage.setTo(account.getUsername());
        mailMessage.setSubject(BOOKING_SUBJECT);
        mailMessage.setFrom(SOURCE_EMAIL);

        HashMap<String, String> model = new HashMap<>();
        model.put(MAP_KEY_USERNAME, account.getClientPassport().getName() + " " +
                account.getClientPassport().getLastName());
        model.put(MAP_KEY_CONTENT, content);

        mailMessage.setModel(model);
        return mailMessage;
    }
}
