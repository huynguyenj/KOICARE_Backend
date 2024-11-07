package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.*;
import group7.se1876.kcs_backend.dto.response.OrderDetailResponse;
import group7.se1876.kcs_backend.dto.response.ProductResponse;
import group7.se1876.kcs_backend.dto.response.RatingResponse;
import group7.se1876.kcs_backend.entity.*;
import group7.se1876.kcs_backend.exception.*;
import group7.se1876.kcs_backend.mapper.ShopMapper;
import group7.se1876.kcs_backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private RatingRepository ratingRepository;

    @PreAuthorize("hasAuthority('ROLE_SHOP')")
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

    @PreAuthorize("hasAuthority('ROLE_SHOP')")
    public Optional<ProductResponse> updateProduct(int id, ProductRequest productRequest) {

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Shop shop = shopRepository.findByOwnerShop_UserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA_WITH_USERID));

        Product productCheck = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_EXISTED));

        if (productCheck.getShop().getShopId() != shop.getShopId()) {
            throw new AppException(ErrorCode.DATA_NOT_EXISTED);
        }
        // Upload image to Firebase

        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            return Optional.empty();
        }

        Product product = productOptional.get();
        if (productRequest.getImage() != null && !productRequest.getImage().isEmpty()) {
            try {
                String imageUrl = firebaseStorageService.uploadFile(productRequest.getImage(),"products/");
                product.setImage(imageUrl);
            } catch (IOException e) {
                throw new AppException(ErrorCode.FAIL_UPLOADFILE);
            }
        }
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
    @PreAuthorize("hasAuthority('ROLE_SHOP')")
    public List<ProductResponse> getAllProductsInShop() {

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        Shop shop = shopRepository.findByOwnerShop_UserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA_WITH_USERID));

        List<Product> products = shop.getProducts();

        return products.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ROLE_SHOP')")
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

    public List<OrderDetailResponse> orderProduct(AddOrderDetail request) {

        List<OrderDetailResponse> responses = new ArrayList<>();
        for (OrderRequest item: request.getOrder()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_EXISTED));
            if (product.getQuantity() < item.getQuantity()) {
                throw new OutOfStockException("Item with ID " + item.getProductId()+ " is out of stock.");
            }
            Shop shop = shopRepository.findById(item.getShopId())
                    .orElseThrow(()-> new AppException(ErrorCode.DATA_NOT_EXISTED));

            //Cập nhật hàng tồn kho
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
            // Tạo Order mới
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setPrice(product.getPrice() * item.getQuantity());
            orderDetail.setAddress(request.getAddress());
            orderDetail.setPhone(request.getPhone());
            orderDetail.setUserName(request.getUserName());
            orderDetail.setShop(shop);
            orderDetail.setDate(new Date());
            orderDetail.setProductName(product.getProductName());
            // Lưu OrderDetail vào cơ sở dữ liệu
            orderDetailRepository.save(orderDetail);

            responses.add(shopMapper.mapToOrderDetailResponse(orderDetail));
        }
        return responses;
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
        productResponse.setShopId(product.getShop().getShopId());
        return productResponse;

    }

    public String orderFailProduct(OrderDetailPaymentError request) {


        for (OrderRequest item: request.getOrder()) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_EXISTED));

            Shop shop = shopRepository.findById(item.getShopId())
                    .orElseThrow(()-> new AppException(ErrorCode.DATA_NOT_EXISTED));

            //Cập nhật hàng tồn kho
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);


            for(Integer orderId: request.getOrderId()) {
                orderDetailRepository.deleteById(orderId);
            }

        }
        return "Delete success!";
    }

    public RatingResponse addRating(int productId, RatingRequest request){

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.INVALID_USERID));


        Product productCheck = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_EXISTED));

        RatingProduct ratingProduct = shopMapper.mapToRatingProduct(request);
        ratingProduct.setProduct(productCheck);
        ratingProduct.setUserName(user.getUserName());
        ratingProduct.setUserId(userId);
        ratingProduct.setDate(new Date());

        // Upload image to Firebase
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String imageUrl = firebaseStorageService.uploadFile(request.getImage(),"rating/");  // Corrected
                ratingProduct.setImage(imageUrl);  // Assuming Pond entity has pondImg field
            } catch (IOException e) {
                throw new AppException(ErrorCode.FAIL_UPLOADFILE);
            }
        }

        ratingRepository.save(ratingProduct);

        return shopMapper.mapToRatingProductResponse(ratingProduct);

    }

    public List<RatingResponse> getAllRating(int productId){

        List<RatingProduct> ratingResponseList = ratingRepository.findByProductId(productId);

        return ratingResponseList.stream().map(r->shopMapper.mapToRatingProductResponse(r)).collect(Collectors.toList());
    }

    @Transactional
    public void deleteRating(int productId, Long userId){

        ratingRepository.deleteByProductIdAndUserId(productId,userId);

    }
}
