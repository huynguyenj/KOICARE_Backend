package group7.se1876.kcs_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KoiFishDevelopmentResponse {
    private Long historyId;
    private double size;
    private Date date;
    private int age;
    private double weight;
    private String fishName;
}
