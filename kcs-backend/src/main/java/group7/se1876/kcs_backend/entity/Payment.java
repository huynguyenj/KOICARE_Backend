package group7.se1876.kcs_backend.entity;

import group7.se1876.kcs_backend.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userName;
    private String details;
    private Date date;
    private Double amount;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
