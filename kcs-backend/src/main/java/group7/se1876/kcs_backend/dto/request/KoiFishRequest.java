package group7.se1876.kcs_backend.dto.request;

import group7.se1876.kcs_backend.enums.HealthyStatus;
import group7.se1876.kcs_backend.enums.KoiGender;
import group7.se1876.kcs_backend.enums.KoiOrigin;
import group7.se1876.kcs_backend.enums.KoiSpecies;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KoiFishRequest {
    private String fishName;

    private String imageFish;

    private int age;

    private KoiSpecies species;

    private double size;

    private double weigh;

    private KoiGender gender;

    private KoiOrigin origin;

    private HealthyStatus healthyStatus;

    private String note;
}
