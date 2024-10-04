package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
