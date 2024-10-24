package group7.se1876.kcs_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddBlogRequest {
    private Long blogId;
    private MultipartFile image;
    private String title;
    private String content;
    private LocalDate publishedDate;
}
