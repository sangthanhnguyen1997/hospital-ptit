package ptit.hospitalmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.hospitalmanagementsystem.entity.Staff;
@Repository
public interface StaffRepository extends JpaRepository<Staff,Long> {
}
