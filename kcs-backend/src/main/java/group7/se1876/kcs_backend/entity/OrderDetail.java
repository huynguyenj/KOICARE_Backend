package group7.se1876.kcs_backend.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;

    @Column
    private int quantity;

    @Column
    private double price;

    @Column
    private boolean isDeleted;

    @Column
    private String userName;

    @Column
    private String address;

    @Column
    private String phone;

    @Column
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopId", referencedColumnName = "shopId")
    private Shop shop;

}
