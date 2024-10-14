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
public class FoodCalculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calculationId;
    private Date feed;
    private double foodAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fishId", referencedColumnName = "fishId",nullable = false)
    private Fish fish;
}

