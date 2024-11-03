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
    private String fishHealth;
    private String fishType;
    private String origin;
    private double price;

    @ManyToMany(mappedBy = "fish")
    private List<Pond> ponds;

    // New relationship: Many Fish can belong to One User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User owner;
    @OneToMany(mappedBy = "fish", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FishDevelopmentHistory> fishDevelopmentHistories;
//    @OneToMany(mappedBy = "fish", cascade = CascadeType.ALL,orphanRemoval = true)
//    private List<FoodCalculation> foodCalculations;
}
