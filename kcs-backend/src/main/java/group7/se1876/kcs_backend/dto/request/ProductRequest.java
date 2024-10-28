package group7.se1876.kcs_backend.dto.request;

import group7.se1876.kcs_backend.enums.CategoryProduct;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotNull
    private String productName;
    @Min(0)
    private double price;
    @NotNull
    private CategoryProduct category;
    @Min(1)
    private int quantity;
    private MultipartFile image;
    private String description;

}
