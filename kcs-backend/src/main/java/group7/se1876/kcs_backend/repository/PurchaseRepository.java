package group7.se1876.kcs_backend.repository;


import group7.se1876.kcs_backend.entity.Product;
import group7.se1876.kcs_backend.entity.Purchase;
import group7.se1876.kcs_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, String> {
    Optional<Purchase> findByUserAndProduct(User user, Product product);
}