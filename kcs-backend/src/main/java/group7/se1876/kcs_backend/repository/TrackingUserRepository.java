package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.TrackingUserLogin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingUserRepository extends JpaRepository<TrackingUserLogin,Long> {
        @Modifying
        @Transactional
        void deleteByUserId(Long userId);
}
