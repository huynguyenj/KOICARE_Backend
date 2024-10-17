package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
