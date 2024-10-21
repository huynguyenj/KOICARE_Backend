package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.ProductRequest;
import group7.se1876.kcs_backend.dto.response.ProductResponse;
import group7.se1876.kcs_backend.entity.OrderDetail;
import group7.se1876.kcs_backend.entity.Product;
import group7.se1876.kcs_backend.entity.Shop;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.exception.*;
import group7.se1876.kcs_backend.repository.OrderDetailRepository;
import group7.se1876.kcs_backend.repository.ProductRepository;
import group7.se1876.kcs_backend.repository.ShopRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private ShopRepository shopRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public ProductResponse createProduct(ProductRequest productRequest) throws ProductAlreadyExistsException {

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        Shop shop = shopRepository.findByOwnerShop_UserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA_WITH_USERID));

        if (productRepository.existsByProductName(productRequest.getProductName())) {
            throw new ProductAlreadyExistsException("Product with name already exists.");
        }
        Product product = Product.builder()
                .productName(productRequest.getProductName())
                .price(productRequest.getPrice())
                .category(productRequest.getCategory())
                .quantity(productRequest.getQuantity())
                .image(productRequest.getImage())
                .description(productRequest.getDescription())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .isDeleted(false)
                .shop(shop)
                .build();
        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    public String uploadImage(MultipartFile file, int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ItemNotFoundException("Product with ID " + productId + " not found."));

        String imageUrl = cloudinaryService.uploadFile(file);
        product.setImage(imageUrl);
        productRepository.save(product);
        return imageUrl;
    }


    public Optional<ProductResponse> updateProduct(int id, ProductRequest productRequest) {

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Shop shop = shopRepository.findByOwnerShop_UserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA_WITH_USERID));

        Product productCheck = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_EXISTED));

        if (productCheck.getShop().getShopId() != shop.getShopId()) {
            throw new AppException(ErrorCode.DATA_NOT_EXISTED);
        }

        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            return Optional.empty();
        }
        Product product = productOptional.get();
        product.setProductName(productRequest.getProductName());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setQuantity(productRequest.getQuantity());
        product.setUpdateAt(LocalDateTime.now());
        Product updatedProduct = productRepository.save(product);
        return Optional.of(convertToResponse(updatedProduct));
    }

    //For global
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    //For shop
    public List<ProductResponse> getAllProductsInShop() {

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        Shop shop = shopRepository.findByOwnerShop_UserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA_WITH_USERID));

        List<Product> products = shop.getProducts();

        return products.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public void deleteProduct(int id) {

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Shop shop = shopRepository.findByOwnerShop_UserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA_WITH_USERID));

        Product productCheck = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_EXISTED));

        if (productCheck.getShop().getShopId() != shop.getShopId()) {
            throw new AppException(ErrorCode.DATA_NOT_EXISTED);
        }
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
        productResponse.setImage(product.getImage());
        productResponse.setDescription(product.getDescription());
        productResponse.setShopName(product.getShop().getShopName());
        return productResponse;

    }
}
