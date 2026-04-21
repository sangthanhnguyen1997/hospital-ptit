package ptit.hospitalmanagementsystem.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.hospitalmanagementsystem.entity.Doctor;
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
