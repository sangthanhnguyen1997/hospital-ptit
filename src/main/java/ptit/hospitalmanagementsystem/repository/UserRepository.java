package ptit.hospitalmanagementsystem.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.hospitalmanagementsystem.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);//code tu tao
    Optional<User> findByUsername(String username);
}