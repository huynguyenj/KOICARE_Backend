package group7.se1876.kcs_backend.repository;

import group7.se1876.kcs_backend.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> {
    List<Blog> findByUser_UserId(Long userId);
}
