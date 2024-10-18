package group7.se1876.kcs_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddWaterParameterRequest {
    private Long parameterId;
    private Date measurementTime;
    private double temperature;
    private double salinity;
    private double ph;
    private double o2;
    private double no2;
    private double no3;
    private double po4;
}
