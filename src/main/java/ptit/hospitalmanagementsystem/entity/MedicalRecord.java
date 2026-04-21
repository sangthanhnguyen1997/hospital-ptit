package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "ho_so_kham",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_ho_so_kham_ma_phieu_kham", columnNames = "ma_phieu_kham")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ho_so_kham")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_phieu_kham", nullable = false, unique = true)
    private ExaminationTicket examinationTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_bac_si", nullable = false)
    private Doctor doctor;

    @Column(name = "ly_do_kham", length = 500)
    private String reasonForVisit;

    @Column(name = "chan_doan_so_bo", length = 1000)
    private String preliminaryDiagnosis;

    @Column(name = "chan_doan_cuoi_cung", length = 1000)
    private String finalDiagnosis;

    @Column(name = "ket_luan", length = 1000)
    private String conclusion;

    @Column(name = "huong_dieu_tri", length = 1000)
    private String treatmentPlan;

    @Column(name = "ngay_lap", nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "medicalRecord")
    @Builder.Default
    private List<LabTestOrder> labTestOrders = new ArrayList<>();

    @OneToOne(mappedBy = "medicalRecord", cascade = CascadeType.ALL)
    private PrescriptionHeader prescriptionHeader;

    @PrePersist
    public void prePersist() {
        if (createdDate == null) createdDate = LocalDateTime.now();
    }
}