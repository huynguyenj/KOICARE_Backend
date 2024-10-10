package group7.se1876.kcs_backend.dto.response;

import group7.se1876.kcs_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PondWithFishResponse {
    private Long pondId;
    private String pondName;
    private Date date;
    private List<FishResponseWithPond> fishResponses;


}
