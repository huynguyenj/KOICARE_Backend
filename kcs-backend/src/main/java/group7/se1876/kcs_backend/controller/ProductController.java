package group7.se1876.kcs_backend.controller;

import group7.se1876.kcs_backend.dto.request.ProductRequest;
import group7.se1876.kcs_backend.dto.response.ErrorResponse;
import group7.se1876.kcs_backend.dto.response.ProductResponse;
import group7.se1876.kcs_backend.enums.CategoryProduct;
import group7.se1876.kcs_backend.exception.ProductAlreadyExistsException;
import group7.se1876.kcs_backend.service.CloudinaryService;
import group7.se1876.kcs_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CloudinaryService cloudinaryService;

//    @PostMapping(consumes = { "multipart/form-data" })
//    public ResponseEntity<ProductResponse> createProduct(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("productName") String productName,
//            @RequestParam("price") double price,
//            @RequestParam("category") String category,
//            @RequestParam("quantity") int quantity,
//            @RequestParam("description") String description) {
//        try {
//            System.out.println("Product Name: " + productName);
//            System.out.println("Price: " + price);
//            System.out.println("Category: " + category);
//            System.out.println("Quantity: " + quantity);
//            System.out.println("Description: " + description);
//
//            String imageUrl = cloudinaryService.uploadFile(file);
//            ProductRequest productRequest = new ProductRequest();
//            productRequest.setProductName(productName);
//            productRequest.setPrice(price);
//            productRequest.setCategory(CategoryProduct.valueOf(category));
//            productRequest.setQuantity(quantity);
//            productRequest.setDescription(description);
//            productRequest.setImage(imageUrl);
//            ProductResponse productResponse = productService.createProduct(productRequest);
//            return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
//        } catch (ProductAlreadyExistsException e) {
//            // Handle specific case when product already exists
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ProductResponse("Product already exists"));
//        } catch (Exception e) {
//            // Catch any other exceptions
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ProductResponse("Failed to create product"));
//        }
//    }
    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            ProductResponse productResponse = productService.createProduct(productRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
        } catch (ProductAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ProductResponse("Product already exists"));
        }
    }
    private String uploadFile(MultipartFile file) {
        return cloudinaryService.uploadFile(file);
    }


    private String saveFile(MultipartFile file) {
        String uploadDir = "path_to_save_images/";
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        return "http://localhost:8080/images/" + file.getOriginalFilename();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable int id, @RequestBody ProductRequest productRequest) {
        Optional<ProductResponse> updatedProduct = productService.updateProduct(id, productRequest);
        return updatedProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }


    @PostMapping("/order/{productID}")
    public ResponseEntity<String> orderProduct(@PathVariable int productID, @RequestParam int quantity) {
        String message = productService.orderProduct(productID, quantity);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<ProductResponse> searchProduct(@PathVariable int id) {
        Optional<ProductResponse> product = productService.searchProduct(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
