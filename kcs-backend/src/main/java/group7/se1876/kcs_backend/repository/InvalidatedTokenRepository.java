package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken,String> {

    @Modifying
    @Transactional
    @Query("DELETE FROM InvalidatedToken t WHERE t.timeExpired <:currentTime")
    void deleteExpiredTokens(Date currentTime);
}
