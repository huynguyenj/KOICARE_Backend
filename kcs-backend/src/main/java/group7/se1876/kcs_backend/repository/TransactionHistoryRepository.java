package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.TransactionHistory;
import group7.se1876.kcs_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findByUser(User user);
}
