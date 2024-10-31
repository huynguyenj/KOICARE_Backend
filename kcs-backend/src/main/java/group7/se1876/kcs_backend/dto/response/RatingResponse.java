package group7.se1876.kcs_backend.dto.response;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {

    private Long ratingId;
    private String content;
    private String image;
    private Date date;
    private int star;
    private String productName;
    private String userName;
}
