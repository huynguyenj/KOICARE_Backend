package group7.se1876.kcs_backend.dto.response;

import group7.se1876.kcs_backend.enums.HealthyStatus;
import group7.se1876.kcs_backend.enums.KoiGender;
import group7.se1876.kcs_backend.enums.KoiOrigin;
import group7.se1876.kcs_backend.enums.KoiSpecies;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KoiFishResponse {
    private int id;

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

    private int pondID;

    private LocalDateTime dateAdded;

    private String message;

    public KoiFishResponse(String message) {
        this.message = message;
    }
}
