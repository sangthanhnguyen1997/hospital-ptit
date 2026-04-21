package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import ptit.hospitalmanagementsystem.enums.ResultStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "ket_qua_xet_nghiem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ket_qua_xet_nghiem")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_chi_dinh_xet_nghiem", nullable = false)
    private LabTestOrder labTestOrder;

    @Column(name = "gia_tri_ket_qua", length = 500)
    private String resultValue;

    @Column(name = "ket_luan", length = 1000)
    private String conclusion;

    @Column(name = "ngay_thuc_hien")
    private LocalDateTime executionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nhan_vien_thuc_hien")
    private Staff performerStaff;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_ket_qua", nullable = false, length = 50)
    private ResultStatus status;
}