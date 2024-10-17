package group7.se1876.kcs_backend.entity;


import group7.se1876.kcs_backend.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "Orders")

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column
    private String note;

    @Column
    private boolean isDeleted;


    @OneToMany(mappedBy = "order")
    @Transient
    private List<OrderDetail> orderDetails;


}
