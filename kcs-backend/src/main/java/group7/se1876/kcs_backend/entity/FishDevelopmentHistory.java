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
public class FishDevelopmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;
    private Date date;
    private double size;
    private int age;
    private double weight;

    @ManyToOne
    @JoinColumn(name = "fish_id", referencedColumnName = "fishId")
    private Fish fish;
}
