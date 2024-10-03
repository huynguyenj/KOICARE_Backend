package group7.se1876.kcs_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Pond {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pondId;
    private String pondName;
    private String pondImg;
    private double size;
    private double depth;
    private double volume;
    private int drainCount;
    private double pumpCapacity;

    @OneToMany(mappedBy = "pond",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    // SaltCalculation_ID is a foreign key in the Pond table
    private Set<SaltCalculation> saltCalculation;


}
