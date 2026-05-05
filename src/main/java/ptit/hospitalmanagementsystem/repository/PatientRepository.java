package ptit.hospitalmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.hospitalmanagementsystem.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
