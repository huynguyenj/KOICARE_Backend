package group7.se1876.kcs_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class SaltCalculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saltCalculationId;

    private Date date;

    private Double saltAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pondId", referencedColumnName = "pondId")
    private Pond pond;
}
