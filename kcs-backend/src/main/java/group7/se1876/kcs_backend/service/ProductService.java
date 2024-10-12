package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.ProductRequest;
import group7.se1876.kcs_backend.dto.response.ProductResponse;
import group7.se1876.kcs_backend.entity.OrderDetail;
import group7.se1876.kcs_backend.entity.Product;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.exception.*;
import group7.se1876.kcs_backend.repository.OrderDetailRepository;
import group7.se1876.kcs_backend.repository.ProductRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private UserRepository userRepository;


    public ProductResponse createProduct(ProductRequest productRequest) throws ProductAlreadyExistsException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Optional<Product> productOptional = productRepository.findByProductName(productRequest.getProductName());
        if (productOptional.isPresent()) {
            throw new ProductAlreadyExistsException("Product with name " + productRequest.getProductName() + " already exists.");
        }
        final Product product = Product.builder()
                .productName(productRequest.getProductName())
                .price(productRequest.getPrice())
                .category(productRequest.getCategory())
                .quantity(productRequest.getQuantity())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .user(user)
                .build();

        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    public Optional<ProductResponse> updateProduct(int id, ProductRequest productRequest) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setProductName(productRequest.getProductName());
            product.setPrice(productRequest.getPrice());
            product.setCategory(productRequest.getCategory());
            product.setQuantity(productRequest.getQuantity());
            product.setUpdateAt(LocalDateTime.now());
            Product updatedProduct = productRepository.save(product);
            return Optional.of(convertToResponse(updatedProduct));
        }
        return Optional.empty();
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToResponse).collect(Collectors.toList());
    }


    public void deleteProduct(int id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            // Perform hard delete by calling delete method of the repository
            productRepository.delete(product);
        } else {
            throw new ItemNotFoundException("Product with ID " + id + " not found.");
        }
    }

    public Optional<ProductResponse> searchProduct(int id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(this::convertToResponse);
    }

    public String orderProduct(int productId, int quantity) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new ItemNotFoundException("Item with ID " + productId + " not found.");
        }
        Product product = productOptional.get();
        //kiểm tra số lượng đặt hàng có lớn hơn số lượng tồn kho không
        if (product.getQuantity() < quantity) {
            throw new OutOfStockException("Item with ID " + productId + " is out of stock.");
        }
        // Cap nhat so luong ton kho
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        // Tạo Order mới
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(product.getPrice() * quantity);
        // Lưu OrderDetail vào cơ sở dữ liệu
        orderDetailRepository.save(orderDetail);
        return "Order placed  successfully.";
    }

    private ProductResponse convertToResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setProductName(product.getProductName());
        productResponse.setPrice(product.getPrice());
        productResponse.setCategory(product.getCategory());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setCreateAt(product.getCreateAt());
        productResponse.setUpdateAt(product.getUpdateAt());
        productResponse.setDeleted(product.isDeleted());
        return productResponse;

    }
}
