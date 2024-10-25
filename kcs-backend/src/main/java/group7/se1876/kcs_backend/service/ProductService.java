package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.AddOrderDetail;
import group7.se1876.kcs_backend.dto.request.ProductRequest;
import group7.se1876.kcs_backend.dto.response.OrderDetailResponse;
import group7.se1876.kcs_backend.dto.response.ProductResponse;
import group7.se1876.kcs_backend.entity.OrderDetail;
import group7.se1876.kcs_backend.entity.Product;
import group7.se1876.kcs_backend.entity.Shop;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.exception.*;
import group7.se1876.kcs_backend.mapper.ShopMapper;
import group7.se1876.kcs_backend.repository.OrderDetailRepository;
import group7.se1876.kcs_backend.repository.ProductRepository;
import group7.se1876.kcs_backend.repository.ShopRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private ShopMapper shopMapper;
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
                .description(productRequest.getDescription())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .isDeleted(false)
                .shop(shop)
                .build();

        // Upload image to Firebase
        if (productRequest.getImage() != null && !productRequest.getImage().isEmpty()) {
            try {
                String imageUrl = firebaseStorageService.uploadFile(productRequest.getImage(),"products/");  // Corrected
                product.setImage(imageUrl);  // Assuming Pond entity has pondImg field
            } catch (IOException e) {
                throw new AppException(ErrorCode.FAIL_UPLOADFILE);
            }
        }

        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
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

    public OrderDetailResponse orderProduct(int productId, Long shopId, AddOrderDetail request) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(()-> new AppException(ErrorCode.DATA_NOT_EXISTED));

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new ItemNotFoundException("Item with ID " + productId + " not found.");
        }
        Product product = productOptional.get();
        //kiểm tra số lượng đặt hàng có lớn hơn số lượng tồn kho không
        if (product.getQuantity() < request.getQuantity()) {
            throw new OutOfStockException("Item with ID " + productId + " is out of stock.");
        }
        // Cap nhat so luong ton kho
        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);
        // Tạo Order mới
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setQuantity(request.getQuantity());
        orderDetail.setPrice(product.getPrice() * request.getQuantity());
        orderDetail.setAddress(request.getAddress());
        orderDetail.setPhone(request.getPhone());
        orderDetail.setUserName(request.getUserName());
        orderDetail.setShop(shop);
        orderDetail.setDate(request.getDate());
        // Lưu OrderDetail vào cơ sở dữ liệu
        orderDetailRepository.save(orderDetail);
        return shopMapper.mapToOrderDetailResponse(orderDetail);
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
