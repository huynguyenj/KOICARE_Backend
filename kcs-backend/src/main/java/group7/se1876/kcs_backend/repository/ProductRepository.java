package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.Product;
import group7.se1876.kcs_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByProductName(String productName);
    boolean existsByProductName(String productName);

}
