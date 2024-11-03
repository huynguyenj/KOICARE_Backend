package group7.se1876.kcs_backend.entity;

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

    @Column(nullable = false)
    private double price;

    @Column
    private int quantity;

    @Column
    private String category;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

    @Column(name = "image")
    private String image;

    @Lob
    @Column(nullable = false,columnDefinition = "text")
    private String description;

    @Column
    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatingProduct> ratingProducts;


}
