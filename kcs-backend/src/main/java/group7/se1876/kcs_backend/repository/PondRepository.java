package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.Pond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PondRepository extends JpaRepository<Pond,Long> {
    Optional<Pond> findByUser_UserId(Long userID);
}
