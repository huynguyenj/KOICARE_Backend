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
public class BlogResponse {
    private Long blogId;
    private String image;
    private String title;
    private String content;
    private Date publishedDate;
    private String userName;
}
