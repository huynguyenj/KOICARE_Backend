package group7.se1876.kcs_backend.entity;

import group7.se1876.kcs_backend.enums.CategoryProduct;
import group7.se1876.kcs_backend.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "product_name")
    private String productName;

    @Column
    private double price;

    @Column
    private int quantity;

    @Column
    @Enumerated(EnumType.STRING)
    private CategoryProduct category;

    @Column(name = "service_type")
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

    @Column
    private boolean isDeleted;

    @OneToMany(mappedBy = "product")
    @Transient
    private List<OrderDetail> orderDetails;

}
