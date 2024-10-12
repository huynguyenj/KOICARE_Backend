package group7.se1876.kcs_backend.dto.response;

import group7.se1876.kcs_backend.enums.CategoryProduct;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductResponse {

    private int id;
    private String productName;
    private double price;
    private CategoryProduct category;
    private int quantity;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private boolean isDeleted;
    private String image;
    private String description;
}
