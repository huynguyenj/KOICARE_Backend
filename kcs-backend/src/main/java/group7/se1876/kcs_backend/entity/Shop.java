package group7.se1876.kcs_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;
    private String shopName;
    private String address;
    private String phone;
    private String email;
    private String contactInfo;
    private boolean status;

//    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
//    private List<Product> products;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User ownerShop;
}
