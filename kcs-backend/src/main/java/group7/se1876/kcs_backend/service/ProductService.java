package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.ProductRequest;
import group7.se1876.kcs_backend.dto.response.ProductResponse;
import group7.se1876.kcs_backend.entity.Product;
import group7.se1876.kcs_backend.repository.OrderDetailRepository;
import group7.se1876.kcs_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        try {
            Product product = Product.builder()
                    .productName(productRequest.getProductName())
                    .price(productRequest.getPrice())
                    .category(productRequest.getCategory())
                    .quantity(productRequest.getQuantity())
                    .serviceType(productRequest.getServiceType())
                    .createAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .isDeleted(false)
                    .build();
            Product savedProduct = productRepository.save(product);
            ProductResponse response = convertToResponse(savedProduct);
            response.setMessage("Product created successfully!");
            return response;
        } catch (Exception e) {
            ProductResponse errorResponse = new ProductResponse();
            errorResponse.setMessage("Failed to create product: " + e.getMessage());
            return errorResponse;
        }
    }
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    private ProductResponse convertToResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setProductName(product.getProductName());
        productResponse.setPrice(product.getPrice());
        productResponse.setCategory(product.getCategory());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setServiceType(product.getServiceType());
        productResponse.setCreateAt(product.getCreateAt());
        productResponse.setUpdateAt(product.getUpdateAt());
        productResponse.setDeleted(product.isDeleted());
        return productResponse;

    }
}
