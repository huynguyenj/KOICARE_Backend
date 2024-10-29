package group7.se1876.kcs_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
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
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="userId", referencedColumnName = "userId")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "pond_fish",
            joinColumns = @JoinColumn(name = "pond_id"),
            inverseJoinColumns = @JoinColumn(name = "fish_id")
    )
    private List<Fish> fish;

    @OneToOne(mappedBy = "pond", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private WaterParameter waterParameter;

    @OneToMany(mappedBy = "pond", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PondWaterPramHistory> pondWaterParamsHistory;

}
