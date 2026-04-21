package ptit.hospitalmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptit.hospitalmanagementsystem.entity.HealthcareCenter;

public interface HealthcareCenterRepository extends JpaRepository<HealthcareCenter,Long> {


}
