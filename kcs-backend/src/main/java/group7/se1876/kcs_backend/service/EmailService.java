package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.entity.EmailDetails;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    public void sendEmail(EmailDetails emailDetails);

}
