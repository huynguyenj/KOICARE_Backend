package group7.se1876.kcs_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FishResponse {
    private Long fishId;
    private String fishName;
    private String fishImg;
    private double fishSize;
    private String fishShape;
    private int fishAge;
    private double fishWeight;
    private String fishGender;
    private String fishType;
    private String origin;
    private double price;
}
