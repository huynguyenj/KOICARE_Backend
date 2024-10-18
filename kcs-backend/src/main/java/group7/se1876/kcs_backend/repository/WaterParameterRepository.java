package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.WaterParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WaterParameterRepository extends JpaRepository<WaterParameter,Long> {
    Optional<WaterParameter> findByPond_PondId(Long pondId);
    boolean existsByPond_PondId(Long pondId);

}
