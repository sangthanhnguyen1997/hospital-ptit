package ptit.hospitalmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptit.hospitalmanagementsystem.entity.ExaminationRoom;

public interface ExaminationRoomRepository extends JpaRepository<ExaminationRoom, Long> {
    // Tìm phòng khám theo tên cơ sở (so khớp tương đối)
    Page<ExaminationRoom> findAllByHealthcareCenterCenterNameContaining(String centerName, Pageable pageable);
}