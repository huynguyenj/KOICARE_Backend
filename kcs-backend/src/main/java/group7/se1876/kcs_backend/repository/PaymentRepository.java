package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
