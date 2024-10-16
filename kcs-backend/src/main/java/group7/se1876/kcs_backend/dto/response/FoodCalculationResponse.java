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
public class FoodCalculationResponse {
    private Long calculationId;
    private Date feed;
    private double foodAmount;
    private String fishName;
}
