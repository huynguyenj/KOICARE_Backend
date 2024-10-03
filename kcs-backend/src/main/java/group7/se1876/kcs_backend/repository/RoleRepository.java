package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.RoleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleDetail,Long> {
    Optional<RoleDetail> findByRoleType(String roleType);
}
