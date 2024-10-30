package group7.se1876.kcs_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private int id;
    private String productName;
    private double price;
    private String category;
    private int quantity;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private boolean isDeleted;
    private String image;
    private String description;
    private String shopName;
    private Long shopId;

    public ProductResponse(String productAlreadyExists) {
    }
}
