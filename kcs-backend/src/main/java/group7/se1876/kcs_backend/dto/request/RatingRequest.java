package group7.se1876.kcs_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {

    private Long ratingId;
    private String content;
    private MultipartFile image;
    private int star;
}
