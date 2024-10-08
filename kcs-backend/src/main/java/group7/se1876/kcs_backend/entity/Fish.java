package group7.se1876.kcs_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

public class Fish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fishId;
    private String fishName;
    private String fishImg;
    private double fishSize;
    private String fishShape;
    private int fishAge;
    private double fishWeight;
    private String fishGender;
    private String fishType;
    private String origin;
    private double price;

    @ManyToMany(mappedBy = "fish")
    private List<Pond> ponds;

}
