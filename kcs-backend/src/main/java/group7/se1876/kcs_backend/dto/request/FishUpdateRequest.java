package group7.se1876.kcs_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FishUpdateRequest {
    private String fishName;
    private MultipartFile fishImg;
    private double fishSize;
    private String fishShape;
    private int fishAge;
    private double fishWeight;
    private String fishGender;
    private String fishHealth;
    private String fishType;
    private String origin;
    private double price;
}
