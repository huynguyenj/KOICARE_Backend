package group7.se1876.kcs_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RatingProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;
    @Lob
    @Column(columnDefinition = "text")
    private String content;
    private String image;
    private Date date;
    private int star;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
    private Long userId;
    private String userName;

}
