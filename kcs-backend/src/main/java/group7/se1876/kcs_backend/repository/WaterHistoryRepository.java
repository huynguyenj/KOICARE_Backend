package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.PondWaterPramHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterHistoryRepository extends JpaRepository<PondWaterPramHistory,Long> {
}
