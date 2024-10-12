package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.entity.EmailDetails;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender JavaMailSender;

    @Override
    @Async
    public void sendEmail(EmailDetails emailDetails) {
        MimeMessage mimeMessage = JavaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(new InternetAddress(emailDetails.getMailFrom()));
            mimeMessageHelper.setTo(emailDetails.getMailTo());
            mimeMessageHelper.setSubject(emailDetails.getMailSubject());
            mimeMessageHelper.setText(emailDetails.getMailContent());
            JavaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
