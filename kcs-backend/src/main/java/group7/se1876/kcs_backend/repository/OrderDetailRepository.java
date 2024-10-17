package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByIsDeletedFalse();
}
