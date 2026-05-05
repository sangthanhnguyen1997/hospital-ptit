package ptit.hospitalmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.hospitalmanagementsystem.entity.Medicine;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, String> {
}