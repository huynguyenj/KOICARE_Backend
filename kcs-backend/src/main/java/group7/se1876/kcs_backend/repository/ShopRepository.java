package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByOwnerShop_UserId(Long userId);
}
