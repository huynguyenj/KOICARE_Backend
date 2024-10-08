package group7.se1876.kcs_backend.controller;

import group7.se1876.kcs_backend.dto.request.ProductRequest;
import group7.se1876.kcs_backend.dto.response.ProductResponse;
import group7.se1876.kcs_backend.service.ProductService;
import jakarta.transaction.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) throws SystemException {
        ProductResponse newProduct = productService.createProduct(productRequest);
        return ResponseEntity.ok(newProduct);
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
        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted) {
            return ResponseEntity.ok("Product deleted successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/order/{productID}")
    public ResponseEntity<String> orderProduct(@PathVariable int productID, @RequestParam int quantity) {
        String message = productService.orderProduct(productID, quantity);
        return ResponseEntity.ok(message);
    }
}
