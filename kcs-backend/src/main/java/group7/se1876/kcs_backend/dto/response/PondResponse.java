package group7.se1876.kcs_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PondResponse {

        private Long pondId;
        private String pondName;
        private String pondImg;
        private double size;
        private double depth;
        private double volume;
        private int drainCount;
        private double pumpCapacity;
        private Date date;
        private List<FishResponseWithPond> fishResponses;
        private Long userId;
}
