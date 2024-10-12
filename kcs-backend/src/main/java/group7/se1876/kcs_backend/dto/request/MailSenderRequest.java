package group7.se1876.kcs_backend.dto.request;

import lombok.Data;
import lombok.Getter;

@Data
public class MailSenderRequest {

    private String mailTo;
    private String mailSubject;
    private String mailContent;
}
