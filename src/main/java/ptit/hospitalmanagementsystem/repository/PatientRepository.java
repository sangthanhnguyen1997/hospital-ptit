package ptit.hospitalmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.hospitalmanagementsystem.entity.User;
@Repository
public interface PatientRepository extends JpaRepository<User, String> {
}
