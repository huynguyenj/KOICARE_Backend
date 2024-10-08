package group7.se1876.kcs_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Pond_KoiFish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "pond_id", nullable = false)
    private Pond pond;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "koi_fish_id")
    private Fish fish;

    @Column(name = "date_added")
    private LocalDateTime dateAdded;

    @Column(name ="end_date")
    private LocalDateTime endDate;
}
