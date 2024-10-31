package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.RatingProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<RatingProduct,Long> {
    List<RatingProduct> findByProductId(Integer productId);
    void deleteByProductIdAndUserId(Integer productId, Long userId);

}
