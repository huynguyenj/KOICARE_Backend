package group7.se1876.kcs_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WaterParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parameterId;
    private Date measurementTime;
    private double temperature;
    private double salinity;
    private double ph;
    private double o2;
    private double no2;
    private double no3;
    private double po4;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pondId", referencedColumnName = "pondId")
    private Pond pond;
}
