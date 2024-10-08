package group7.se1876.kcs_backend.dto.request;

import group7.se1876.kcs_backend.enums.CategoryProduct;
import group7.se1876.kcs_backend.enums.ServiceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    private String productName;
    private double price;
    private CategoryProduct category;
    private int quantity;
    private int quantityOrdered;
}
