package group7.se1876.kcs_backend.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PondWaterPramHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parameterHistoryId;
    private Date measurementTime;
    private double temperature;
    private double salinity;
    private double ph;
    private double o2;
    private double no2;
    private double no3;
    private double po4;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pond_id", referencedColumnName = "pondId")
    private Pond pond;
}
