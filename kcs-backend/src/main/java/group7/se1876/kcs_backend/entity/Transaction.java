package group7.se1876.kcs_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String details;
    private Date date;
    private String bankCode;
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
