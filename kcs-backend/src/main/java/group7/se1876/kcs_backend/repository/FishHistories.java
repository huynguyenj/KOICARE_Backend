package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.FishDevelopmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FishHistories extends JpaRepository<FishDevelopmentHistory,Long> {
}
